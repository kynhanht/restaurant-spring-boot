package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableDto {

	private Long tableId;
	
	private String tableCode;

	private String tableName;
	
	private Long locationId;

	private Integer minCapacity;

	private Integer maxCapacity;
	
	private Long statusId;
	
	private String statusValue;
	
	private OrderTableDto orderDto;	

}
