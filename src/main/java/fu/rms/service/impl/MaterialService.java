package fu.rms.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.ImportAndExportDto;
import fu.rms.dto.MaterialDto;
import fu.rms.entity.Dish;
import fu.rms.entity.GroupMaterial;
import fu.rms.entity.Import;
import fu.rms.entity.ImportMaterial;
import fu.rms.entity.Material;
import fu.rms.entity.Option;
import fu.rms.entity.Quantifier;
import fu.rms.entity.QuantifierOption;
import fu.rms.entity.Status;
import fu.rms.entity.Supplier;
import fu.rms.entity.Warehouse;
import fu.rms.exception.NotFoundException;
import fu.rms.exception.UpdateException;
import fu.rms.mapper.MaterialMapper;
import fu.rms.repository.DishRepository;
import fu.rms.repository.GroupMaterialRepository;
import fu.rms.repository.ImportRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.repository.OptionRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.repository.SupplierRepository;
import fu.rms.repository.WarehouseRepository;
import fu.rms.request.ImportMaterialRequest;
import fu.rms.request.ImportRequest;
import fu.rms.request.MaterialRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IMaterialService;
import fu.rms.utils.DateUtils;
import fu.rms.utils.Utils;

@Service
public class MaterialService implements IMaterialService {

	@Autowired
	private MaterialRepository materialRepo;

	@Autowired
	private GroupMaterialRepository groupMaterialRepo;

	@Autowired
	private StatusRepository statusRepo;
	
	@Autowired
	private ImportRepository importRepo;
	
	@Autowired
	private SupplierRepository supplierRepo;
	
	@Autowired
	private WarehouseRepository warehouseRepo;

	@Autowired
	private DishRepository dishRepo;

	@Autowired
	private OptionRepository optionRepo;

	@Autowired
	private MaterialMapper materialMapper;

	@Override
	public List<MaterialDto> getAll() {
		List<Material> materials = materialRepo.findByStatusId(StatusConstant.STATUS_MATERIAL_AVAILABLE);
		List<MaterialDto> materialDtos = materials.stream().map(materialMapper::entityToDto)
				.collect(Collectors.toList());
		return materialDtos;
	}

	@Override
	public MaterialDto getById(Long id) {
		Material material = materialRepo.findById(id)
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));
		return materialMapper.entityToDto(material);
	}

	@Transactional
	@Override
	public MaterialDto update(MaterialRequest materialRequest, Long id) {

		Material saveMaterial = materialRepo.findById(id).map(material -> {
			material.setMaterialName(materialRequest.getMaterialName());
			material.setUnit(materialRequest.getUnit());
			material.setRemainNotification(materialRequest.getRemainNotification());
			// set Group Material
			if (materialRequest.getGroupMaterialId() != null) {
				Long groupMaterialId = materialRequest.getGroupMaterialId();
				GroupMaterial groupMaterial = groupMaterialRepo.findById(groupMaterialId)
						.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_GROUP_MATERIAL));
				material.setGroupMaterial(groupMaterial);
			} else {
				material.setGroupMaterial(null);
			}
			
			
			if (!material.getUnitPrice().equals(materialRequest.getUnitPrice()) ) {
				
				material.setUnitPrice(materialRequest.getUnitPrice());
				material.setTotalPrice(
						Utils.multiBigDecimalToDouble(material.getRemain(), materialRequest.getUnitPrice()));
				// change unit price and totalPrice of dish
				List<Dish> dishes = dishRepo.findByMaterialId(material.getMaterialId());
				for (Dish dish : dishes) {
					Double newCost = 0D;
					if (dish.getQuantifiers() != null) {
						for (Quantifier quantifier : dish.getQuantifiers()) {
							if (quantifier.getMaterial().getMaterialId() == material.getMaterialId()) {
								Double cost = Math.ceil(material.getUnitPrice() * quantifier.getQuantity());
								quantifier.setCost(cost);
								newCost = Utils.sumBigDecimalToDouble(newCost, cost);
							} else {
								newCost = Utils.sumBigDecimalToDouble(newCost, quantifier.getCost());
							}
						}
						newCost = Utils.roundUpDecimal(newCost);
						Double different = Utils.subtractBigDecimalToDouble(dish.getCost(), newCost);
						dish.setCost(newCost);
						dish.setDishCost(Utils.subtractBigDecimalToDouble(dish.getDishCost(), different)); // sửa giá
																											// thành
						Dish newDish = dishRepo.save(dish);
						if (newDish == null) {
							throw new UpdateException(MessageErrorConsant.ERROR_UPDATE_DISH);
						}
					}
				}

				List<Option> options = optionRepo.findByMaterialId(material.getMaterialId());
				for (Option option : options) {
					Double newCost = 0D;
					if (option.getQuantifierOptions() != null) {
						for (QuantifierOption quantifierOption : option.getQuantifierOptions()) {
							if (quantifierOption.getMaterial().getMaterialId() == material.getMaterialId()) {
								Double cost = Math.ceil(material.getUnitPrice() * quantifierOption.getQuantity());
								quantifierOption.setCost(cost);
								newCost = Utils.sumBigDecimalToDouble(newCost, cost);
							} else {
								newCost = Utils.sumBigDecimalToDouble(newCost, quantifierOption.getCost());
							}
						}
						newCost = Utils.roundUpDecimal(newCost);
						Double different = Utils.subtractBigDecimalToDouble(option.getCost(), newCost);
						option.setCost(newCost);
						option.setOptionCost(Utils.subtractBigDecimalToDouble(option.getOptionCost(), different));
						optionRepo.save(option);
					}
				}

			}
		

			return material;

		}).orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));

		saveMaterial = materialRepo.save(saveMaterial);
		
		// Update cost's dish and option if unit change		
		
		return materialMapper.entityToDto(saveMaterial);
	}


	
	
	@Override
	public MaterialDto create(ImportRequest request) {
		
		ImportMaterialRequest importMaterialRequest = request.getImportMaterial();
		MaterialRequest materialRequest = importMaterialRequest.getMaterial();

		// check material Code
		String materialCode = materialRequest.getMaterialCode();
		while (true) {
			if (materialRepo.findByMaterialCode(materialCode) != null) {
				materialCode = Utils.generateDuplicateCode(materialCode);
			} else {
				break;
			}
		}
		// create material
		Material material = new Material();
		// set basic information for material
		material.setMaterialCode(materialCode);
		material.setMaterialName(materialRequest.getMaterialName());
		material.setUnit(materialRequest.getUnit());
		material.setUnitPrice(materialRequest.getUnitPrice());
		material.setTotalPrice(request.getTotalAmount());
		material.setTotalImport(importMaterialRequest.getQuantityImport());
		material.setTotalExport(0D);
		material.setRemain(importMaterialRequest.getQuantityImport());
		material.setRemainNotification(materialRequest.getRemainNotification());
		// set status for material
		Status status = statusRepo.findById(StatusConstant.STATUS_MATERIAL_AVAILABLE)
				.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_STATUS));
		material.setStatus(status);
		// set group material for material
		if (materialRequest.getGroupMaterialId() != null) {
			GroupMaterial groupMaterial = groupMaterialRepo.findById(materialRequest.getGroupMaterialId())
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_GROUP_MATERIAL));
			material.setGroupMaterial(groupMaterial);
		}
		// save material to database
		material = materialRepo.save(material);

		// create Import
		Import importEntity = new Import();
		// set information basic for import

		// check importCode
		String importCode = request.getImportCode();
		while (true) {
			if (importRepo.findByImportCode(importCode) != null) {
				importCode = Utils.generateDuplicateCode(importCode);
			} else {
				break;
			}
		}

		importEntity.setImportCode(importCode);
		importEntity.setTotalAmount(request.getTotalAmount());
		importEntity.setComment(request.getComment());
		// set supplier for import
		if (request.getSupplierId() != null) {
			Supplier supplier = supplierRepo.findById(request.getSupplierId())
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_SUPPLIER));
			importEntity.setSupplier(supplier);
		}

		// create ImportMaterial
		ImportMaterial importMaterial = new ImportMaterial();

		// save basic information for importMaterial
		importMaterial.setQuantityImport(importMaterialRequest.getQuantityImport());
		importMaterial.setUnitPrice(materialRequest.getUnitPrice());
		importMaterial.setSumPrice(importMaterialRequest.getSumPrice());
		importMaterial.setExpireDate(DateUtils.localDateTimeAddDay(importMaterialRequest.getExpireDate()));
		// set warehouse for importMaterial
		if (importMaterialRequest.getWarehouseId() != null) {
			Long warehouseId = importMaterialRequest.getWarehouseId();
			Warehouse warehouse = warehouseRepo.findById(warehouseId)
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_WAREHOUSE));
			importMaterial.setWarehouse(warehouse);
		}
		// set material for importMaterial
		importMaterial.setMaterial(material);
		// set import for importMaterial
		importMaterial.setImports(importEntity);

		// set importMaterial for import
		importEntity.setImportMaterials(Arrays.asList(importMaterial));
		// save import to database
		importEntity = importRepo.save(importEntity);
		
		return materialMapper.entityToDto(material);
	}

	@Override
	public List<ImportAndExportDto> getImportAndExportById(Long id) {
		return materialRepo.findImportAndExportById(id);
	}

	
	@Override
	public SearchRespone<MaterialDto> search(String materialCode, Long groupId, Integer page) {

		// check page
		if (page == null || page <= 0) {// check page is null or = 0 => set = 1
			page = 1;
		}
		// Pageable with 10 item for every page
		Pageable pageable = PageRequest.of(page - 1, 10);

		// search
		Page<Material> pageMaterial = null;
		if(StringUtils.isBlank(materialCode) && groupId==null) {
			pageMaterial = materialRepo.findByStatusId(StatusConstant.STATUS_MATERIAL_AVAILABLE, pageable);
		}else {
			pageMaterial = materialRepo.findByCriteria(materialCode, groupId,
					StatusConstant.STATUS_MATERIAL_AVAILABLE, pageable);
		}

		// create new searchRespone
		SearchRespone<MaterialDto> searchRespone = new SearchRespone<MaterialDto>();
		// set current page
		searchRespone.setPage(page);
		// set total page
		searchRespone.setTotalPages(pageMaterial.getTotalPages());
		// set list result material
		List<Material> materials = pageMaterial.getContent();
		List<MaterialDto> materialDtos = materials.stream().map(materialMapper::entityToDto)
				.collect(Collectors.toList());
		searchRespone.setResult(materialDtos);

		return searchRespone;
	}


}
