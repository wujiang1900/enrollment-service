package enrollmentsystem.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import enrollmentsystem.exception.RecordNotFoundException;
import enrollmentsystem.model.dto.ClassDto;
import org.springframework.validation.annotation.Validated;

import enrollmentsystem.repository.entity.CourseClass;
/**
 * A service class to perform the logic for {@link CourseClass}.
 *
 * @author Jiang Wu
 *
 */
@Validated
public interface ClassService {

	/**
	 *
	 * To add a {@link CourseClass}
	 *
	 * @param courseClass
	 * @return
	 */
	void add(@Valid ClassDto courseClass);

	/**
	 *
	 * Get the list of classes for a particular student for a semester, or the
	 * fully history of classes enrolled.
	 *
	 * @param studentId
	 * @param semesterName
	 * @return
	 */
	List<ClassDto> fetchClasses(
			@Valid @NotNull(message = "{fetch.courseclass.id.notnull}") Long studentId,
			String semesterName);

	/**
	 *
	 * Find the class by its name
	 *
	 * @param name
	 * @return
	 */
	ClassDto findByName(
			@Valid @NotEmpty(message = "{find.courseclass.name.notempty}") String name) throws RecordNotFoundException;

}
