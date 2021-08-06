package fu.rms.exception;

public class UpdateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UpdateException() {
		super();
	}

	public UpdateException(String message) {
		super(message);
	}
}
