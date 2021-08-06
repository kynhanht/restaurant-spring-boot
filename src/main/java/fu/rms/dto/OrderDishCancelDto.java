package fu.rms.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishCancelDto {

	private Long orderDishCancelId;
	
	private Integer quantityCancel;
	
	private String commentCancel;
	
	private String cancelBy;
	
	private Timestamp cancelDate;
	
	private Long orderDishId;
}
