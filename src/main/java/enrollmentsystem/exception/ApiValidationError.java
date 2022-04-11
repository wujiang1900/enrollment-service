package enrollmentsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A Class for holding the validation errors.
 *
 * @author Jiang Wu
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiValidationError {
	private String field;
	private Object rejectedValue;
	private String message;
}
