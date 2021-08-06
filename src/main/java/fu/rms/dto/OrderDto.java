package fu.rms.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

	private Long orderId;
	
	private String orderCode;
	
	private Long statusId;
	
	private String statusValue;
	
	private String comment;
	
	private Double totalAmount;
	
	private Integer totalItem;
	
	private Double customerPayment;
	
	private Timestamp orderDate;
	
	private String timeOrder; //time order
	
	private Timestamp paymentDate;	
	
	private Timestamp modifiedDate;
	
	private String modifiedBy;
	
	private String createBy;
	
	private Long tableId;
	
	private String timeToComplete;
	
	private Long orderTakerStaffId;
	private String orderTakerStaffCode;
	
	private Long chefStaffId;
	private String chefStaffCode;
	
	private Long cashierStaffId;
	private String cashierStaffCode;
	
	
	List<OrderDishDto> orderDish;
	
}
