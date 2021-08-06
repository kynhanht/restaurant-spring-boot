package fu.rms.respone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespone {

	private String token;

	private Long staffId;

	private String staffCode;

	private String roleName;
}
