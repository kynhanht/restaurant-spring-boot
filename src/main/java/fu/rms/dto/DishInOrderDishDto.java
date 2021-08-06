package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishInOrderDishDto {

	private Long orderDishId;
	
	private Integer quantity;
	
	private Long dishId;
}
