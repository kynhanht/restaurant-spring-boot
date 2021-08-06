package fu.rms.request;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportRequest {
	
	@NotEmpty(message = "Mã Phiếu không được trống")
	@Size(max = 150, message = "Mã phiếu tối đa 150 kí tự")
	private String importCode;

	@NotNull(message = "Tổng giá không được trống")
	@Positive(message = "Tổng giá phải lớn hơn 0")
	@Digits(integer = 20,fraction = 0,message = "Tổng giá tối đa 20 ký tự số")
	private Double totalAmount;
	
	@Size(max = 200,message = "Ghi chú tối đa 200 kí tự")
	private String comment;
	
	private Long supplierId;
		
	@Valid
	@NotNull(message = "Dữ liệu không hợp lệ")
	private ImportMaterialRequest importMaterial;
}
