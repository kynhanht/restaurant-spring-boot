package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishOrderDto { // for orderdish
	
	private Long dishId;
	
	private String dishName;
	
	private String dishUnit;
	
	private String dishCode;
	
	private Double defaultPrice;
	
	private Boolean typeReturn;
	
	private Double cost;
	
	private Double dishCost;
}
