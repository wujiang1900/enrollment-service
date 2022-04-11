package enrollmentsystem.service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import enrollmentsystem.model.dto.EnrollmentRequestDto;

/**
 * A service class to perform the logic for the enrollments.
 *
 * @author Jiang Wu
 *
 */
@Validated
public interface EnrollmentService {

	/**
	 *
	 * Enroll a student to a class for a particular semester
	 *
	 * @param enrollmentRequest
	 */
	void enroll(@Valid EnrollmentRequestDto enrollmentRequest);

	/**
	 * Drop a student from a class.
	 *
	 * @param enrollmentRequest
	 */
	void unEnroll(@Valid EnrollmentRequestDto enrollmentRequest);
}
