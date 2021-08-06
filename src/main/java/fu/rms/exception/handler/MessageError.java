package fu.rms.exception.handler;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageError {

	private HttpStatus status;
	private List<String> messages;

	public MessageError(HttpStatus status, List<String> messages) {
		this.status = status;
		this.messages = messages;
	}

	public MessageError(HttpStatus status, String message) {
		this.status = status;
		this.messages = Arrays.asList(message);
	}

}
