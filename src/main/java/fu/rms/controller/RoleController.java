package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.RoleDto;
import fu.rms.service.IRoleService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class RoleController {

	@Autowired
	private IRoleService roleService;
	
	@GetMapping("/roles")
	public List<RoleDto> getAll(){
		return roleService.getAll();
	}
}
