package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.SupplierDto;
import fu.rms.service.ISupplierService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class SupplierController {
	
	@Autowired
	private ISupplierService supplierService;
	
	@GetMapping("/supplier/all")
	public List<SupplierDto> getAll() {
		return supplierService.getAll();
	}
}
