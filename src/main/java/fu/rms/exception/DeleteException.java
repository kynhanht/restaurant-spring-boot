package fu.rms.exception;

public class DeleteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeleteException() {
		super();
	}
	
	public DeleteException(String message) {
		super(message);
	}
}
