package fu.rms.request;

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
public class MaterialRequest {
	
	@NotEmpty(message = "Mã NVL không được trống")
	@Size(max = 150,message = "Mã NVL tối đa 150 kí tự")
	private String materialCode;
	
	@NotEmpty(message = "Tên NVL không được trống")
	@Size(max = 100,message = "Tên NVL tối đa 100 kí tự")
	private String materialName;
	
	@NotEmpty(message = "Đơn vị không được trống")
	@Size(max = 50,message = "Đơn vị tối đa 50 kí tự")
	private String unit;
	
	@NotNull(message = "Giá nhập không được trống")
	@Positive(message = "Giá nhập phải lớn hơn 0")
	@Digits(integer = 13,fraction = 0,message = "Giá nhập tối đa 13 ký tự số")
	private Double unitPrice;
	
	@PositiveOrZero(message = "Hàng tồn tối thiểu phải lớn hơn 0 hoặc bằng 0")
	@Digits(integer = 5,fraction = 3,message = "Hàng tồn tối thiểu tối đa 5 ký tự số")
	private Double remainNotification;
	
	private Long groupMaterialId;
	
}
