package fu.rms.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fu.rms.dto.ImportMaterialDto;
import fu.rms.dto.MaterialDto;
import fu.rms.dto.WarehouseDto;
import fu.rms.entity.ImportMaterial;
import fu.rms.utils.DateUtils;

@Component
public class ImportMaterialMapper {

	@Autowired
	private MaterialMapper materialMapper;
	
	@Autowired
	private WarehouseMapper warehouseMapper;
	
	public ImportMaterialDto entityToDto(ImportMaterial importMaterial) {		
		ImportMaterialDto importMaterialDto = new ImportMaterialDto();
		importMaterialDto.setImportMaterialId(importMaterial.getImportMaterialId());
		importMaterialDto.setQuantityImport(importMaterial.getQuantityImport());
		importMaterialDto.setPrice(importMaterial.getUnitPrice());
		importMaterialDto.setSumPrice(importMaterial.getSumPrice());
		String exprireDate=DateUtils.convertLocalDateTimeToString(importMaterial.getExpireDate());
		importMaterialDto.setExpireDate(exprireDate);
		if(importMaterial.getMaterial() != null) {
			MaterialDto materialDto = materialMapper.entityToDto(importMaterial.getMaterial());
			importMaterialDto.setMaterial(materialDto);
		}
		if(importMaterial.getWarehouse()!=null) {
			WarehouseDto warehouseDto=warehouseMapper.entityToDto(importMaterial.getWarehouse());
			importMaterialDto.setWarehouse(warehouseDto);
		}
		return importMaterialDto;
	}
}
