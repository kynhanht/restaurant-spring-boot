package fu.rms.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishDto {

	private Long dishId;
	
	private String dishCode;
	
	private String dishName;
	
	private String dishUnit;
	
	private Double defaultPrice;	// giá bán
	
	private Double cost;			// chi phí nguyên vật liệu
	
	private Double dishCost;		// chi phí giá thành sản phẩm
	
	private String description;
	
	private Float timeComplete;			// đơn vị giờ, ví dụ: chủ nhập theo đơn vị phút (45 phút thì quy ra đơn vị giờ để lưu: 0,75)
	
	private String imageUrl;
	
	private Boolean typeReturn;
	
	private List<CategoryDto> categories;
	
	private List<OptionDto> options;

	private List<QuantifierDto> quantifiers;
	

	
	

}
