package fu.rms.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantifierOptionRequest {

	private Long quantifierOptionId;

	@NotNull(message = "Định lượng không được trống")
	@PositiveOrZero(message = "Định lượng phải lớn hơn hoặc bằng 0")
	@Digits(integer = 5,fraction = 3,message = "Định lượng tối đa 5 ký tự số")
	private Double quantity;

	@NotNull(message = "Đơn giá* Định lượng(chi phí) không được trống")
	@PositiveOrZero(message = "Đơn giá* Định lượng(chi phí) phải lớn hơn hoặc bằng 0")
	@Digits(integer = 20,fraction = 0,message = "Đơn giá* Định lượng(chi phí) tối đa 20 ký tự số")
	private Double cost;

	@Size(max =200,message = "Mô tả tối đa là 200 kí tự")
	private String description;
	
	@NotNull(message = "Nguyên vật liệu không hợp lệ")
	private Long materialId;
}	
