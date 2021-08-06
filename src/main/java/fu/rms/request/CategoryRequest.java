package fu.rms.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

	@NotEmpty(message = "Tên nhóm thực đơn không được trống")
	@Size(max = 100, message = "Tên nhóm thực đơn tối đa là 100 kí tự")
	private String categoryName;
	
	@Size(max = 200, message = "Mô tả tối đa là 200 kí tự")
	private String description;
	
	@Max(value = 5,message = "Mức độ ưu tiên không hợp lệ")
	@Min(value = 1, message = "Mức độ ưu tiên không hợp lệ")
	private Integer priority;
}
