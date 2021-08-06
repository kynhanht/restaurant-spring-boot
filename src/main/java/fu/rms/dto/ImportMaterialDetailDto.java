package fu.rms.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"importCode","createdDate","supplierName","warehouseName","materialName","unit","quantity","unitPrice","totalAmount","expireDate"})
public interface ImportMaterialDetailDto {

	String getImportCode();
	
	String getCreatedDate();
	
	String getSupplierName();
	
	String getWarehouseName();
	
	String getMaterialName();
	
	String getUnit();
	
	Double getQuantity();
	
	Double getUnitPrice();
	
	Double getTotalAmount();
	
	String getExpireDate();
}
