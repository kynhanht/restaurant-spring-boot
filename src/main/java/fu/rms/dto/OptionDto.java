package fu.rms.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionDto {

	private Long optionId;
	
	private String optionName;
	
	private String optionType;
	
	private String unit;
	
	private Double price;
	
	private Double cost;
	
	private Double optionCost;
	
	private List<QuantifierOptionDto> quantifierOptions;
	
}
