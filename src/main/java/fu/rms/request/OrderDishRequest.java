package fu.rms.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishRequest {

	private Long orderDishId;
	
	private Integer quantityReturn;
	
	private Long orderId;
	
	private String modifiedBy;
	
	private Long dishId;
}
