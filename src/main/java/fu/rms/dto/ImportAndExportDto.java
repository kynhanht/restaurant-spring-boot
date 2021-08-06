package fu.rms.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"code", "supplierName", "warehouseName", "type", "quantity", "unitPrice", "totalAmount", "createdDate"})

public interface ImportAndExportDto {

	String getCode();
	
	String getSupplierName();
	
	String getWarehouseName();
	
	String getType();
	
	Double getQuantity();
	
	Double getUnitPrice();
	
	Double getTotalAmount();
	
	String getCreatedDate();
	
	
}
