package fu.rms.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishChefRequest {
	private Long orderDishId;
	
	private Long chefStaffId;
	
	private Long dishId;
	
	private Long statusId;
	
	private String dishName;
}
