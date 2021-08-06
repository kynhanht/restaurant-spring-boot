package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialQuantifierDto {

	private Long materialId;
	
	private String materialName;
	
	private String unit;

	private Double unitPrice;
}
