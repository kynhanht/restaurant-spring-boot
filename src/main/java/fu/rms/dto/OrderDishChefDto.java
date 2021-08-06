package fu.rms.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDishChefDto {

	private Long orderDishId;
	
	private Long dishId;
	
	private String dishName;
	
	private Long statusId;
	
	private String statusValue;
	
	private String comment;
	
	private Integer quantityOk;
	
	private String orderTime;
	
	private Timestamp createdDate;
	
	private boolean checkNotification;
	
	private Float timeToComplete;
	
	private List<OrderDishOptionChefDto> orderDishOptions;
	
}
