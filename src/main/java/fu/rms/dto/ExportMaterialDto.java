package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportMaterialDto {

	private Long exportMaterialId;
	
	private Double quantityExport;
	
	private Double unitPrice;
	
	private String materialName;
	
	private String materialUnit;
}
