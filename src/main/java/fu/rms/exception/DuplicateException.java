package fu.rms.exception;

public class DuplicateException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DuplicateException() {
		super();
	}
	
	public DuplicateException(String message) {
		super(message);
	}
	
}
