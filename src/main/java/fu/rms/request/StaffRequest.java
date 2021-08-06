package fu.rms.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRequest {
	
	@Size(max = 80, message = "Email tối đa 80 kí tự")
	private String email;
	
	@NotEmpty(message = "Mật khẩu không được trống")
	@Size(min = 8, max = 14, message = "Mật khẩu phải từ 8 đến 14 kí tự")
	private String password;
	
	@NotEmpty(message = "Tên không được trống")
	@Size(max = 80, message = "Tên tối đa 80 kí tự")
	private String fullname;
	
	@NotEmpty(message = "Số điện thoại không được trống")
	@Size(min = 10, max = 10, message = "Số điện thoại phải là 10 kí tự")
	private String phone;
	
	@Size(max = 80, message = "Địa chỉ tối đa 80 kí tự")
	private String address;

	private Long roleId;
}
