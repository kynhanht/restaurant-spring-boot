package fu.rms.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequest {
	
	@NotEmpty(message = "Mã thực đơn không được trống")
	@Size(max = 150, message = "Mã thực đơn tối đa 150 kí tự")
	private String dishCode;
	
	@NotEmpty(message = "Tên thực đơn không được trống")
	@Size(max = 100, message = "Tên thực đơn tối đa 100 kí tự")
	private String dishName;
	
	@NotEmpty(message = "Đơn vị không được trống")
	@Size(max = 50,message = "Đơn vị tối đa 50 kí tự")
	private String dishUnit;
	
	@NotNull(message = "Giá bán không được trống")
	@Positive(message = "Giá bán phải lớn hơn 0")
	@Digits(integer = 20,fraction = 0,message = "Giá bán tối đa 20 ký tự số")
	private Double defaultPrice;
	
	@NotNull(message = "Giá nhập không được trống")
	@Positive(message = "Giá nhập phải lớn hơn 0")
	@Digits(integer = 20,fraction = 0,message = "Giá nhập tối đa 20 ký tự số")
	private Double cost;
	
	@NotNull(message = "Giá thành được trống")
	@Positive(message = "Giá thành phải lớn hơn 0")
	@Digits(integer = 20,fraction = 0,message = "Giá thành tối đa 20 ký tự số")
	private Double dishCost;
	
	@Size(max = 200, message = "Mô tả món ăn tối đa 200 kí tự")
	private String description;
	
	@PositiveOrZero(message = "Thời gian hoàn thành phải lớn hơn hoặc bằng 0")
	@Digits(integer = 5,fraction = 0,message = "Thời gian hoàn thành tối đa 5 ký tự số")
	private Float timeComplete;
	
	//check sau
	private String imageUrl;
	
	@NotNull(message = "Loại sản phẩm không được trống")
	private Boolean typeReturn;
	
	private Long [] categoryIds;
	
	private Long [] optionIds;
	
	@Valid
	@NotEmpty(message = "Chưa chọn nguyên vật liệu")
	private List<QuantifierRequest> quantifiers;
	

}
