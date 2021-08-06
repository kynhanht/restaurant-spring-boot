package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.InventoryMaterialDto;
import fu.rms.request.InventoryMaterialRequest;
import fu.rms.service.IInventoryMaterialService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class InventoryMaterialController {

	@Autowired
	private IInventoryMaterialService inventoryMaterialService;
	
	@PostMapping("/inventory-material")
	public void createInventory(@RequestBody List<InventoryMaterialRequest> listRequest) {
		inventoryMaterialService.create(listRequest);
	}
	
	@PutMapping("/inventory-material/{id}")
	public void updateInventory(@PathVariable("id") Long inventoryMaterialId) {
		inventoryMaterialService.update(inventoryMaterialId);
	}
	
	@GetMapping("/inventory-material/all")
	public List<InventoryMaterialDto> getAll() {
		return inventoryMaterialService.getAll();
	}
	
}
