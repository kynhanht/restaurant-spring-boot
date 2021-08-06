package fu.rms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.rms.dto.SupplierDto;
import fu.rms.entity.Supplier;
import fu.rms.mapper.SupplierMapper;
import fu.rms.repository.SupplierRepository;
import fu.rms.service.ISupplierService;

@Service
public class SupplierService implements ISupplierService{
	
	@Autowired
	private SupplierRepository supplierRepo;
	
	@Autowired
	private SupplierMapper supplierMapper;

	@Override
	public List<SupplierDto> getAll() {
		List<SupplierDto> listDto = null;
		List<Supplier> listAll = supplierRepo.findAll();
		if(listAll.size() != 0) {
			listDto = listAll.stream().map(supplierMapper::entityToDto).collect(Collectors.toList());
		}
		return listDto;
	}

}
