package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.SupplierDto;
import fu.rms.entity.Supplier;

@Component
public class SupplierMapper {

	public SupplierDto entityToDto(Supplier supplier) {		
		SupplierDto supplierDto=new SupplierDto();
		supplierDto.setSupplierId(supplier.getSupplierId());
		supplierDto.setSupplierName(supplier.getSupplierName());
		supplierDto.setPhone(supplier.getPhone());
		return supplierDto;
	}
}
