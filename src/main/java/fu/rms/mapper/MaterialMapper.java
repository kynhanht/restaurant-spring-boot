package fu.rms.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fu.rms.dto.GroupMaterialDto;
import fu.rms.dto.MaterialDto;
import fu.rms.entity.Material;

@Component
public class MaterialMapper {

	@Autowired
	private GroupMaterialMapper groupMaterialMapper;
	
	public MaterialDto entityToDto(Material material) {		
		MaterialDto materialDto = new MaterialDto();
		materialDto.setMaterialId(material.getMaterialId());
		materialDto.setMaterialName(material.getMaterialName());
		materialDto.setMaterialCode(material.getMaterialCode());
		materialDto.setUnit(material.getUnit());
		materialDto.setUnitPrice(material.getUnitPrice());
		materialDto.setTotalPrice(material.getTotalPrice());
		materialDto.setTotalImport(material.getTotalImport());
		materialDto.setTotalExport(material.getTotalExport());
		materialDto.setRemain(material.getRemain());
		materialDto.setRemainNotification(material.getRemainNotification());	
		if(material.getGroupMaterial() != null) {
			GroupMaterialDto groupMaterialDto= groupMaterialMapper.entityToDto(material.getGroupMaterial());
			materialDto.setGroupMaterial(groupMaterialDto);
		}
		return materialDto;
	}
	
	
}
