package fu.rms.exception;

public class CreateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CreateException() {
		super();
	}
	
	public CreateException(String message) {
		super(message);
	}
}
