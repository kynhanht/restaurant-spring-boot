package fu.rms.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.InventoryMaterialDto;
import fu.rms.entity.Export;
import fu.rms.entity.ExportMaterial;
import fu.rms.entity.Import;
import fu.rms.entity.ImportMaterial;
import fu.rms.entity.InventoryMaterial;
import fu.rms.entity.Material;
import fu.rms.exception.NotFoundException;
import fu.rms.exception.NullPointerException;
import fu.rms.mapper.InventoryMaterialMapper;
import fu.rms.repository.ExportRepository;
import fu.rms.repository.ImportRepository;
import fu.rms.repository.InventoryMaterialRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.request.InventoryMaterialRequest;
import fu.rms.service.IInventoryMaterialService;
import fu.rms.utils.DateUtils;
import fu.rms.utils.Utils;

@Service
public class InventoryMaterialService implements IInventoryMaterialService{

	@Autowired
	private InventoryMaterialRepository inventoryMaterialRepo;
	
	@Autowired
	private InventoryMaterialMapper inventoryMaterialMapper;
	
	@Autowired
	private ImportRepository importRepo;
	
	@Autowired
	private ExportRepository exportRepo;
	
	@Autowired
	private MaterialRepository materialRepo;
	
	@Override
	@Transactional
	public void create(List<InventoryMaterialRequest> listRequest) {
	
		try {
			if(listRequest != null && !listRequest.isEmpty()) {
				InventoryMaterial entity = null;
				
				Import imp = new Import();
				List<ImportMaterial> importMaterials = new ArrayList<ImportMaterial>();
				ImportMaterial importMaterial = null;
				
				Export export = new Export();
				List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();
				ExportMaterial exportMaterial = null;
				
				Material material = null;
				
				boolean checkImport = false;
				boolean checkExport = false;
				for (InventoryMaterialRequest request : listRequest) {
					entity	= new InventoryMaterial();
					entity.setInventoryCode(request.getInventoryCode());
					entity.setInventoryDate(DateUtils.convertStringToLocalDateTime(request.getInventoryDate()));
					entity.setMaterialId(request.getMaterialId());
					entity.setMaterialName(request.getMaterialName());
					entity.setMaterialUnit(request.getMaterialUnit());
					entity.setRemainSystem(request.getRemainSystem());
					entity.setRemainFact(request.getRemainFact());
					if(request.getRemainSystem() >= request.getRemainFact()) {
						entity.setQuantityDifferent(Utils.subtractBigDecimalToDouble(request.getRemainSystem(), request.getRemainFact()));
					}else {
						entity.setQuantityDifferent(Utils.subtractBigDecimalToDouble(request.getRemainFact(), request.getRemainSystem()));
					}
					entity.setReason(request.getReason());
					entity.setProcess(request.getProcess());
					inventoryMaterialRepo.save(entity);
					
					if(request.getProcess() == StatusConstant.INVENTORY_ADD 			// th???c t??? c??n > h??? th???ng
							&& request.getRemainFact() > request.getRemainSystem()) {	// x??? l?? nh???p th??m kho
						importMaterial = new ImportMaterial();
						material = materialRepo.findById(request.getMaterialId()).orElseThrow(
								() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));
						Double totalImportNew = Utils.sumBigDecimalToDouble(material.getTotalImport(), request.getQuantityDifferent());
						material.setTotalImport(totalImportNew);						// set l???i total import
						material.setRemain(request.getRemainFact());					// set l???i remain
						material.setTotalPrice(Utils.multiBigDecimalToDouble(request.getRemainFact(), material.getUnitPrice()));	// set l???i t???ng gi??
						importMaterial.setMaterial(material);
						importMaterial.setUnitPrice(0d);								// nh???p gi?? 0
						importMaterial.setSumPrice(0d);
						importMaterial.setQuantityImport(entity.getQuantityDifferent());	// nh???p s??? l?????ng kh??c bi???t
						importMaterial.setImports(imp);
						importMaterials.add(importMaterial);
						checkImport = true;
						
					}else if(request.getProcess() == StatusConstant.INVENTORY_MINUS 	// th???c t??? c??n < h??? th???ng
							&& request.getRemainFact() < request.getRemainSystem()) {	// x??? l?? xu???t kho tr??n system cho remain = fact
						exportMaterial = new ExportMaterial();
						material = materialRepo.findById(request.getMaterialId()).orElseThrow(
								() -> new NotFoundException(MessageErrorConsant.ERROR_NOT_FOUND_MATERIAL));
						Double totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), request.getQuantityDifferent());
						material.setTotalExport(totalExportNew);						// set l???i total export
						material.setRemain(request.getRemainFact());					// set l???i remain
						material.setTotalPrice(Utils.multiBigDecimalToDouble(request.getRemainFact(), material.getUnitPrice()));
						exportMaterial.setMaterial(material);
						exportMaterial.setUnitPrice(0d);
						exportMaterial.setQuantityExport(entity.getQuantityDifferent());	// s??? l?????ng xu???t
						exportMaterial.setExport(export);
						exportMaterials.add(exportMaterial);
						checkExport = true;
					}
				}
				if(checkImport) {
					imp.setImportCode(Utils.generateInventoryImExCode(listRequest.get(0).getInventoryCode()));
					imp.setTotalAmount(0d);
					imp.setComment("Ki???m k?? kho");
					imp.setImportMaterials(importMaterials);
					importRepo.save(imp);
				}
				if(checkExport) {
					export.setExportCode(Utils.generateInventoryImExCode(listRequest.get(0).getInventoryCode()));
					export.setComment("Ki???m k?? kho");
					export.setExportMaterials(exportMaterials);
					exportRepo.save(export);
				}
			}	
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
	}

	@Override
	@Transactional
	public void update(Long inventoryMaterialId) {
		
		try {
			if(inventoryMaterialId != null) {
				InventoryMaterial inventoryMaterial = inventoryMaterialRepo.findById(inventoryMaterialId).orElseThrow(
						() -> new NotFoundException("C?? g?? ???? kh??ng ????ng x???y ra"));
				if(inventoryMaterial.getProcess() == StatusConstant.INVENTORY_ADD 			// th???c t??? c??n > h??? th???ng
						&& inventoryMaterial.getRemainFact() > inventoryMaterial.getRemainSystem()) {	// x??? l?? nh???p th??m kho
					
					Import imp = new Import();
					List<ImportMaterial> importMaterials = new ArrayList<ImportMaterial>();
					ImportMaterial importMaterial = null;
					
					Material material = null;
					
					importMaterial = new ImportMaterial();
					material = materialRepo.findById(inventoryMaterial.getMaterialId()).orElseThrow(
							() -> new NotFoundException("C?? g?? ???? kh??ng ????ng x???y ra"));
					Double totalImportNew = Utils.sumBigDecimalToDouble(material.getTotalImport(), inventoryMaterial.getQuantityDifferent());
					material.setTotalImport(totalImportNew);						// set l???i total import
					material.setRemain(inventoryMaterial.getRemainFact());					// set l???i remain
					material.setTotalPrice(Utils.multiBigDecimalToDouble(inventoryMaterial.getRemainFact(), material.getUnitPrice()));	// set l???i t???ng gi??
					importMaterial.setMaterial(material);
					importMaterial.setUnitPrice(0d);								// nh???p gi?? 0
					importMaterial.setSumPrice(0d);
					importMaterial.setQuantityImport(inventoryMaterial.getQuantityDifferent());	// nh???p s??? l?????ng kh??c bi???t
					importMaterial.setImports(imp);
					importMaterials.add(importMaterial);

					imp.setImportCode(Utils.generateInventoryImExCode(inventoryMaterial.getInventoryCode()));
					imp.setTotalAmount(0d);
					imp.setComment("Ki???m k?? kho");
					imp.setImportMaterials(importMaterials);
					importRepo.save(imp);
					
					inventoryMaterial.setProcess(StatusConstant.INVENTORY_ADD);
					inventoryMaterialRepo.save(inventoryMaterial);
					
				}else if(inventoryMaterial.getProcess() == StatusConstant.INVENTORY_MINUS 	// th???c t??? c??n < h??? th???ng
						&& inventoryMaterial.getRemainFact() < inventoryMaterial.getRemainSystem()) {	// x??? l?? xu???t kho tr??n system cho remain = fact
					Export export = new Export();
					List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();
					ExportMaterial exportMaterial = new ExportMaterial();
					
					Material material = null;
					
					material = materialRepo.findById(inventoryMaterial.getMaterialId()).orElseThrow(
							() -> new NotFoundException("C?? g?? ???? kh??ng ????ng x???y ra"));
					Double totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalImport(), inventoryMaterial.getQuantityDifferent());
					material.setTotalExport(totalExportNew);						// set l???i total export
					material.setRemain(inventoryMaterial.getRemainFact());					// set l???i remain
					material.setTotalPrice(Utils.multiBigDecimalToDouble(inventoryMaterial.getRemainFact(), material.getUnitPrice()));
					exportMaterial.setMaterial(material);
					exportMaterial.setUnitPrice(0d);
					exportMaterial.setQuantityExport(inventoryMaterial.getQuantityDifferent());	// s??? l?????ng xu???t
					exportMaterial.setExport(export);
					exportMaterials.add(exportMaterial);
					
					export.setExportCode(Utils.generateInventoryImExCode(inventoryMaterial.getInventoryCode()));
					export.setComment("Ki???m k?? kho");
					export.setExportMaterials(exportMaterials);
					exportRepo.save(export);
					
					inventoryMaterial.setProcess(StatusConstant.INVENTORY_MINUS);
					inventoryMaterialRepo.save(inventoryMaterial);
				}
			}
			
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
	}

	@Override
	public List<InventoryMaterialDto> getAll() {
		
		try {
			List<InventoryMaterialDto> listDto = new ArrayList<InventoryMaterialDto>();
			List<InventoryMaterial> listEntity = inventoryMaterialRepo.findAll();
			if(listEntity != null && listEntity.size() >= 1) {
				listEntity.stream().sorted(Comparator.comparing(InventoryMaterial::getInventoryDate));
				listDto = listEntity.stream().map(inventoryMaterialMapper::entityToDto).collect(Collectors.toList());
			}
			return listDto;
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
	}

}
