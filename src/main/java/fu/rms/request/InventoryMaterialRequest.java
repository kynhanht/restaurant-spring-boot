package fu.rms.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryMaterialRequest {

	private Long inventoryMaterialId;
	
	private String inventoryCode;
	
	private String inventoryDate;
	
	private Long materialId;
	
	private String materialName;
	
	private String materialUnit;
	
	private Double remainSystem;
	
	private Double remainFact;
	
	private Double quantityDifferent;
	
	private String reason;
	
	private Integer process;
}
