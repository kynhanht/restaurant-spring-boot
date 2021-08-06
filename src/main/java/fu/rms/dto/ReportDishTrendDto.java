package fu.rms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDishTrendDto {

	private Long reportDishTrendId;
	
	private Long dishId;
	
	private String dishName;
	
	private String dishUnit;				// đơn vị thời điểm đó
	
	private Double materialCost;			// chi phí nguyên vật liệu
	
	private Double dishCost;				// chi phí giá thành sản phẩm
	
	private Double unitPrice;				// giá trên 1 đơn vị
	
	private Integer quantityOk;				// số lượng bán ra
	
	private Integer quantityCancel;			// số lượng hủy
	
	private Double profit;
	
	private String orderCode;				// orderCode của order đó
	
	private Long categoryId;				// thể loại của dish đó
	private String categoryName;
	
	private Long statusId;					// trạng thái của món ăn đó
	private String statusValue;
	
	private Long orderDishId;
	
	private String createdDate;				// trạng thái của món ăn đó
}
