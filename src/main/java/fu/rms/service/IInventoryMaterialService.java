package fu.rms.service;

import java.util.List;

import fu.rms.dto.InventoryMaterialDto;
import fu.rms.request.InventoryMaterialRequest;

public interface IInventoryMaterialService {

	void create(List<InventoryMaterialRequest> listRequest);
	
	void update(Long inventoryMaterialId);
	
	List<InventoryMaterialDto> getAll();
}
