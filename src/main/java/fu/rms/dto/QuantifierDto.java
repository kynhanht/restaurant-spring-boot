package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantifierDto {

	private Long quantifierId;
	
	private Double quantity;
	
	private Double cost;
	
	private String description;
	
	private MaterialQuantifierDto  material;
	
	
}
