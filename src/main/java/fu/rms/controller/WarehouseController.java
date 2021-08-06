package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.WarehouseDto;
import fu.rms.service.IWarehouseService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class WarehouseController {
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@GetMapping("/warehouse/all")
	public List<WarehouseDto> getAll() {
		return warehouseService.getAll();
	}
}
