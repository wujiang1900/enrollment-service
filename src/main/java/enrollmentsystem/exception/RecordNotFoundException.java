package enrollmentsystem.exception;

/**
 *
 * This class is a custom exception for a record not found in the database.
 *
 * @author Jiang Wu
 *
 */
public class RecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6018623423843866056L;

	public RecordNotFoundException(String message) {
		super(message);
	}

}