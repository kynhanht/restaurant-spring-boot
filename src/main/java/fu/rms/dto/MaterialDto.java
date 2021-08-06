package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDto {

	private Long materialId;
	
	private String materialCode;
	
	private String materialName;
	
	private String unit; 			// đơn vị
	
	private Double unitPrice;		// giá cho 1 đơn vị
	
	private Double totalPrice;		// tổng giá: unitPrice*remain
	
	private Double totalImport;			// tổng số lượng tất cả các lần import: tăng mãi
	
	private Double totalExport;			// tổng số lượng các lần export: tăng mãi
	
	private Double remain;				// remain = totalImport - totalExport
	
	private Double remainNotification;	
	
	private GroupMaterialDto groupMaterial;
	
	
	
}
