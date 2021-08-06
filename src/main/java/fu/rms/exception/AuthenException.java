package fu.rms.exception;

public class AuthenException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AuthenException() {
		super();
	}
	
	public AuthenException(String message) {
		super(message);
	}

}
