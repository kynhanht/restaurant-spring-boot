package fu.rms.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.dto.ImportDto;
import fu.rms.dto.ImportMaterialDetailDto;
import fu.rms.entity.Dish;
import fu.rms.entity.Import;
import fu.rms.entity.ImportMaterial;
import fu.rms.entity.Material;
import fu.rms.entity.Option;
import fu.rms.entity.Quantifier;
import fu.rms.entity.QuantifierOption;
import fu.rms.entity.Supplier;
import fu.rms.entity.Warehouse;
import fu.rms.exception.NotFoundException;
import fu.rms.exception.NullPointerException;
import fu.rms.mapper.ImportMapper;
import fu.rms.repository.DishRepository;
import fu.rms.repository.ImportRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.repository.OptionRepository;
import fu.rms.repository.SupplierRepository;
import fu.rms.repository.WarehouseRepository;
import fu.rms.request.ImportExistMaterialRequest;
import fu.rms.request.ImportExistRequest;
import fu.rms.respone.SearchRespone;
import fu.rms.service.IImportService;
import fu.rms.utils.DateUtils;
import fu.rms.utils.Utils;

@Service
public class ImportService implements IImportService {

	@Autowired
	private ImportRepository importRepo;

	@Autowired
	private MaterialRepository materialRepo;

	@Autowired
	private WarehouseRepository warehouseRepo;

	@Autowired
	private SupplierRepository supplierRepo;

	@Autowired
	private DishRepository dishRepo;

	@Autowired
	private OptionRepository optionRepo;

	@Autowired
	private ImportMapper importMapper;

	@Override
	@Transactional
	public ImportDto importExistInventory(ImportExistRequest request) {
		// create Import
		Import importEntity = new Import();
		// check importCode
		String importCode = request.getImportCode();
		while (true) {
			if (importRepo.findByImportCode(importCode) != null) {
				importCode = Utils.generateDuplicateCode(importCode);
			} else {
				break;
			}
		}
		// set basic information for import
		importEntity.setImportCode(importCode);
		importEntity.setTotalAmount(request.getTotalAmount());
		importEntity.setComment(request.getComment());
		// set supplier for import
		if (request.getSupplierId() != null) {
			Supplier supplier = supplierRepo.findById(request.getSupplierId())
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_SUPPLIER));
			importEntity.setSupplier(supplier);
		}

		List<ImportMaterial> importMaterials = new ArrayList<>();
		for (ImportExistMaterialRequest importExistMaterialRequest : request.getImportMaterials()) {
			// create ImportMaterial
			ImportMaterial importMaterial = new ImportMaterial();
			// save basic information for importMaterial
			importMaterial.setQuantityImport(importExistMaterialRequest.getQuantityImport());
			importMaterial.setUnitPrice(importExistMaterialRequest.getUnitPrice());
			importMaterial.setSumPrice(importExistMaterialRequest.getSumPrice());
			importMaterial.setExpireDate(DateUtils.localDateTimeAddDay(importExistMaterialRequest.getExpireDate()));
			// set warehouse for importMaterial
			if (importExistMaterialRequest.getWarehouseId() != null) {
				Long warehouseId = importExistMaterialRequest.getWarehouseId();
				Warehouse warehouse = warehouseRepo.findById(warehouseId)
						.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_WAREHOUSE));
				importMaterial.setWarehouse(warehouse);
			}
			// set material for import
			Long materialId = importExistMaterialRequest.getMaterialId();
			Material material = materialRepo.findById(materialId)
					.orElseThrow(() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));

			Double oldTotalPrice = Utils.multiBigDecimalToDouble(material.getRemain(), material.getUnitPrice());
			Double newTotalPrice = Utils.multiBigDecimalToDouble(importExistMaterialRequest.getQuantityImport(),
					importExistMaterialRequest.getUnitPrice());

			Double totalPrice = Utils.sumBigDecimalToDouble(oldTotalPrice, newTotalPrice);
			Double remain = Utils.sumBigDecimalToDouble(material.getRemain(),
					importExistMaterialRequest.getQuantityImport());
			Double unitPrice = Utils.divideBigDecimalToDouble(totalPrice, remain);
			Double totalImport = Utils.sumBigDecimalToDouble(material.getTotalImport(),
					importExistMaterialRequest.getQuantityImport());

			material.setTotalPrice(Utils.roundUpDecimal(totalPrice));
			material.setRemain(remain);
			material.setUnitPrice(Utils.roundUpDecimal(unitPrice));
			material.setTotalImport(totalImport);

			// set material for ImportMaterial
			importMaterial.setMaterial(material);
			// set import for ImportMaterial
			importMaterial.setImports(importEntity);
			// add to list
			importMaterials.add(importMaterial);
		}

		// set importMaterial for import
		importEntity.setImportMaterials(importMaterials);
		// save import to database
		importEntity = importRepo.save(importEntity);
		// update cost for dish and option relation with material
		try {
			for (ImportMaterial importMaterial : importEntity.getImportMaterials()) {
				Material material = importMaterial.getMaterial();
				List<Dish> dishes = dishRepo.findByMaterialId(material.getMaterialId());
				for (Dish dish : dishes) {
					Double dishCost = 0D;
					if (dish.getQuantifiers() != null) {
						for (Quantifier quantifier : dish.getQuantifiers()) {
							if (quantifier.getMaterial().getMaterialId() == material.getMaterialId()) {
								Double cost = Math
										.ceil(quantifier.getMaterial().getUnitPrice() * quantifier.getQuantity());
								quantifier.setCost(cost);
								dishCost = Utils.sumBigDecimalToDouble(dishCost, cost);
							} else {
								dishCost = Utils.sumBigDecimalToDouble(dishCost, quantifier.getCost());
							}
						}
						dishCost = Utils.roundUpDecimal(dishCost);
						Double different = Utils.subtractBigDecimalToDouble(dish.getCost(), dishCost);
						dish.setCost(dishCost);
						dish.setDishCost(Utils.subtractBigDecimalToDouble(dish.getDishCost(), different));
						dishRepo.save(dish);
					}
				}
				List<Option> options = optionRepo.findByMaterialId(material.getMaterialId());
				for (Option option : options) {
					Double optionCost = 0D;
					if (option.getQuantifierOptions() != null) {
						for (QuantifierOption quantifierOption : option.getQuantifierOptions()) {
							if (quantifierOption.getMaterial().getMaterialId() == material.getMaterialId()) {
								Double cost = Math.ceil(
										quantifierOption.getMaterial().getUnitPrice() * quantifierOption.getQuantity());
								quantifierOption.setCost(cost);
								optionCost = Utils.sumBigDecimalToDouble(optionCost, cost);
							} else {
								optionCost = Utils.sumBigDecimalToDouble(optionCost, quantifierOption.getCost());
							}
						}
						optionCost = Utils.roundUpDecimal(optionCost);
						Double different = Utils.subtractBigDecimalToDouble(option.getCost(), optionCost);
						option.setCost(optionCost);
						option.setOptionCost(Utils.subtractBigDecimalToDouble(option.getOptionCost(), different));
						optionRepo.save(option);
					}
				}
			}
		} catch (Exception e) {
			throw new NullPointerException("Nhập kho thất bại");
		}

		return importMapper.entityToDto(importEntity);

	}

	@Override
	public SearchRespone<ImportDto> search(Long supplierId, String dateFrom, String dateTo, Integer page) {
		// set criteria for search

		if (page == null || page <= 0) {// check page is null or = 0 => set = 1
			page = 1;
		}
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("created_date").descending());
		LocalDateTime dateFromLdt = DateUtils.convertStringToLocalDateTime(dateFrom);
		LocalDateTime dateToLdt = DateUtils.convertStringToLocalDateTime(dateTo);

		// search
		Page<Import> pageImport = null;
		if (supplierId == null && dateFromLdt == null && dateToLdt == null) {
			pageImport = importRepo.findAll(pageable);
		} else {
			pageImport = importRepo.findByCriteria(supplierId, dateFromLdt, dateToLdt, pageable);
		}

		// create new searchRespone
		SearchRespone<ImportDto> searchRespone = new SearchRespone<ImportDto>();
		// set current page
		searchRespone.setPage(page);
		// set total page
		searchRespone.setTotalPages(pageImport.getTotalPages());
		// set result import
		List<Import> imports = pageImport.getContent();
		List<ImportDto> importDtos = imports.stream().map(importMapper::entityToDto).collect(Collectors.toList());
		searchRespone.setResult(importDtos);

		// return client
		return searchRespone;
	}

	@Override
	public ImportMaterialDetailDto getImportMaterialDetailByImportMaterialId(Long importMaterialId) {
		return importRepo.findImportMaterialDetailByImportMaterialId(importMaterialId);
	}

}
