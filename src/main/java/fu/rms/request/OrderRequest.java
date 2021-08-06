package fu.rms.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

	private Long orderId;
	
	private Long chefStaffId;
	
	private Long statusId;
	
	private Long cashierStaffId;
	
	private Double customerPayment;
	
	private String comment;
}
