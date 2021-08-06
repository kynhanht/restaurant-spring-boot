package fu.rms.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishDto {

	private Long orderDishId;
	
	private Integer quantity;
	
	private Double sellPrice;
	
	private Double sumPrice;
	
	private String comment;
	
	private String commentCancel;
	
	private Long orderOrderId;
	
	private Integer quantityCancel;
	
	private Integer quantityOk;
	
	private String createBy;
	
	private Timestamp createDate;
	
	private String modifiedBy;
	
	private Timestamp modifiedDate;
	
	private Long statusStatusId;
	
	private String statusStatusValue;
	
	private DishOrderDto dish; // dishdto for orderdish
	
	private List<OrderDishOptionDto> orderDishOptions;
	
	private List<OrderDishCancelDto> orderDishCancels;
	
}
