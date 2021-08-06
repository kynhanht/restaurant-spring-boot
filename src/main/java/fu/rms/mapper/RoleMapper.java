package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.RoleDto;
import fu.rms.entity.Role;

@Component
public class RoleMapper {
	
	public RoleDto entityToDto(Role role) {
		RoleDto roleDto = new RoleDto();
		roleDto.setRoleId(role.getRoleId());
		roleDto.setRoleCode(role.getRoleCode());
		roleDto.setRoleName(role.getRoleName());
		roleDto.setDescription(role.getDescription());
		return roleDto;
	}
}
