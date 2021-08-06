package fu.rms.request;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportMaterialRequest {

	@NotNull(message = "Số lượng nhập không được trống")
	@Positive(message = "Số lượng nhập phải lớn hơn 0")
	@Digits(integer = 5,fraction = 3,message = "Số lượng nhập tối đa 5 ký tự số")
	private Double quantityImport;
	
	@NotNull(message = "Đơn giá * số lượng(tổng giá nhập) không được trống")
	@Positive(message = "Đơn giá * số lượng(tổng giá nhập) phải lớn 0")
	@Digits(integer = 20,fraction = 0,message = "Đơn giá * số lượng(tổng giá nhập) tối đa 20 ký tự số")
	private Double sumPrice;
	
	@PositiveOrZero(message = "Ngày hết hạn phải lớn hơn hoặc bằng 0")
	@Digits(integer = 5,fraction = 0,message = "Ngày hết hạn tối đa 5 ký tự số")
	private Integer expireDate;
	
	private Long warehouseId;
	
	@Valid
	@NotNull(message = "Dữ liệu không hợp lệ")
	private MaterialRequest material;
	
}
