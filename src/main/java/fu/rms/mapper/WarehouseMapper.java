package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.WarehouseDto;
import fu.rms.entity.Warehouse;

@Component
public class WarehouseMapper {

	public WarehouseDto entityToDto(Warehouse warehouse) {		
		WarehouseDto warehouseDto=new WarehouseDto();
		warehouseDto.setWarehouseId(warehouse.getWarehouseId());
		warehouseDto.setName(warehouse.getName());
		return warehouseDto;
	}
}
