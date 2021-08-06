package fu.rms.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto { // for view detail 
	
	private Long orderId;
	
	private String orderCode;
	
	private Long statusId;
	
	private Double totalAmount;
	
	private Integer totalItem;
	
	private Timestamp orderDate;
	
	private String comment;
	
	private Long tableId;
	
	private String tableName;
	
	private List<OrderDishDto> orderDish;
	
	private List<String> message;
	
	private Set<String> messageMaterial;
	
	private String orderTaker;
	
	private String cashier;
	
	private Double customerPayment;
	
	
}
