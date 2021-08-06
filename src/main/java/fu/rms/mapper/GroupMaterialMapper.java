package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.GroupMaterialDto;
import fu.rms.entity.GroupMaterial;

@Component
public class GroupMaterialMapper {
	
	public GroupMaterialDto entityToDto(GroupMaterial group) {
		GroupMaterialDto groupDto=new GroupMaterialDto();
		groupDto.setGroupId(group.getGroupId());
		groupDto.setGroupName(group.getGroupName());
		return groupDto;
	}
}
