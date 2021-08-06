package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantifierOptionDto {

	private Long quantifierOptionId;

	private Double quantity;

	private String unit;

	private Double cost;

	private String description;
	
	private MaterialQuantifierDto material;

}
