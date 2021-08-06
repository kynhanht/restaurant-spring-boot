package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportMaterialDto {

	private Long importMaterialId;
	
	private Double quantityImport;
	
	private Double price;
	
	private Double sumPrice;
	
	private String expireDate;
	
	private WarehouseDto warehouse;
	
	private MaterialDto material;
	
	
}
