package fu.rms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fu.rms.dto.ImportDto;
import fu.rms.dto.ImportMaterialDto;
import fu.rms.dto.SupplierDto;
import fu.rms.entity.Import;
import fu.rms.utils.DateUtils;

@Component
public class ImportMapper {
	
	@Autowired
	private ImportMaterialMapper importMaterialMapper;
	
	@Autowired
	private SupplierMapper supplierMapper;
	
	public ImportDto entityToDto(Import importEntity) {		
		ImportDto importDto = new ImportDto();
		importDto.setImportId(importEntity.getImportId());
		importDto.setImportCode(importEntity.getImportCode());
		importDto.setTotalAmount(importEntity.getTotalAmount());
		importDto.setComment(importEntity.getComment());
		importDto.setCreatedBy(importEntity.getCreatedBy());
		String createdDate=DateUtils.convertLocalDateTimeToString(importEntity.getCreatedDate());
		importDto.setCreatedDate(createdDate);
		importDto.setLastModifiedBy(importEntity.getLastModifiedBy());
		String lastModifiedDate=DateUtils.convertLocalDateTimeToString(importEntity.getLastModifiedDate());
		importDto.setLastModifiedDate(lastModifiedDate);
		if(importEntity.getImportMaterials() != null && !importEntity.getImportMaterials().isEmpty()) {
			List<ImportMaterialDto> listImportMaterialDto = importEntity.getImportMaterials()
					.stream().map(importMaterialMapper::entityToDto).collect(Collectors.toList());
			importDto.setImportMaterials(listImportMaterialDto);
		}
		if(importEntity.getSupplier()!=null) {
			SupplierDto supplierDto=supplierMapper.entityToDto(importEntity.getSupplier());
			importDto.setSupplier(supplierDto);
		}
		
		return importDto;
	}
}
