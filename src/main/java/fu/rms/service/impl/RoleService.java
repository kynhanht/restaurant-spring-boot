package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.dto.RoleDto;
import fu.rms.entity.Role;
import fu.rms.mapper.RoleMapper;
import fu.rms.repository.RoleRepository;
import fu.rms.service.IRoleService;

@Service
public class RoleService implements IRoleService {

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<RoleDto> getAll() {
		List<Role> listRole = roleRepo.findAll();
		List<RoleDto> dtos = listRole.stream().map(roleMapper::entityToDto).collect(Collectors.toList());
		return dtos;

	}

}
