package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.InventoryMaterialDto;
import fu.rms.entity.InventoryMaterial;
import fu.rms.utils.DateUtils;

@Component
public class InventoryMaterialMapper {

	public InventoryMaterialDto entityToDto(InventoryMaterial entity) {
		InventoryMaterialDto dto = new InventoryMaterialDto();
		dto.setInventoryMaterialId(entity.getInventoryMaterialId());
		dto.setInventoryCode(entity.getInventoryCode());
		dto.setInventoryDate(DateUtils.convertTimeToString(entity.getInventoryDate()));
		dto.setMaterialId(entity.getMaterialId());
		dto.setMaterialName(entity.getMaterialName());
		dto.setMaterialUnit(entity.getMaterialUnit());
		dto.setRemainSystem(entity.getRemainSystem());
		dto.setRemainFact(entity.getRemainFact());
		dto.setQuantityDifferent(entity.getQuantityDifferent());
		dto.setReason(entity.getReason());
		dto.setProcess(entity.getProcess());
		return dto;
	}
}
