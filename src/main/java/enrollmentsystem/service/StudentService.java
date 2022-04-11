package enrollmentsystem.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import enrollmentsystem.model.dto.SearchRequestDto;
import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.model.validation.CreationValidationGroup;
import enrollmentsystem.repository.entity.Student;

/**
 * A service class to perform the logic for {@link Student}.
 *
 * @author Jiang Wu
 *
 */
@Validated
public interface StudentService {

	/**
	 * Update the information in the existing student record.
	 *
	 * @param student
	 * @return
	 */
	StudentDto update(@Valid StudentDto student);

	/**
	 *
	 * Add a new student with the basic information.
	 *
	 * @param student
	 * @return
	 */
	@Validated(CreationValidationGroup.class)
	long add(@Valid StudentDto student);

	/**
	 *
	 * Find Student by ID
	 *
	 * @param id
	 * @return
	 */
	StudentDto findById(@Valid @NotNull(message = "Id is required.") Long id);

	/**
	 *
	 * Get the list of students enrolled in a class for a particular semester.
	 *
	 * @param searchRequest
	 * @return
	 */
	List<StudentDto> fetchStudents(@Valid SearchRequestDto searchRequest);

}
