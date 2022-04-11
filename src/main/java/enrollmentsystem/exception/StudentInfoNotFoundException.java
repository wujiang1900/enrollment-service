package enrollmentsystem.exception;

import static java.text.MessageFormat.format;

/**
 * This class is a custom exception for a student not found in the database.
 *
 * @author Jiang Wu
 *
 */
public class StudentInfoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -159264386600812023L;

	public StudentInfoNotFoundException(Long studentId) {
		super(format("Student id {0} not found.", studentId));
	}
}
