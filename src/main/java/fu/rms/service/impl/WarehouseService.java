package fu.rms.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.dto.WarehouseDto;
import fu.rms.entity.Warehouse;
import fu.rms.mapper.WarehouseMapper;
import fu.rms.repository.WarehouseRepository;
import fu.rms.service.IWarehouseService;


@Service
public class WarehouseService implements IWarehouseService{
	
	@Autowired
	private WarehouseRepository warehouseRepo;
	
	@Autowired
	private WarehouseMapper warehouseMapper;

	@Override
	public List<WarehouseDto> getAll() {
		List<WarehouseDto> listDto = null;
		List<Warehouse> listAll = warehouseRepo.findAll();
		if(listAll.size() != 0) {
			listDto = listAll.stream().map(warehouseMapper::entityToDto).collect(Collectors.toList());
		}
		return listDto;
	}

	
}
