package fu.rms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.StaffDto;
import fu.rms.request.StaffRequest;
import fu.rms.service.IStaffService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class StaffController {

	@Autowired
	private IStaffService staffService;
	
	@GetMapping("/staffs")
	public List<StaffDto> getAll(){
		return staffService.getAll();
	}
	
	@PostMapping("/staffs")
	public StaffDto create(@RequestBody @Valid StaffRequest staffRequest) {
		return staffService.create(staffRequest);
	}
}
