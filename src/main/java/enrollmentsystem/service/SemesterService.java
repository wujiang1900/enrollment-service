package enrollmentsystem.service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import enrollmentsystem.model.dto.SemesterDto;
import enrollmentsystem.repository.entity.Semester;
/**
 * A service class to perform the logic for {@link Semester}.
 *
 *
 * @author Jiang Wu
 *
 */
@Validated
public interface SemesterService {

	/**
	 * Add a semester
	 *
	 * @param semester
	 * @return
	 */
	SemesterDto add(@Valid SemesterDto semester);

	/**
	 * Find a semester by its name
	 *
	 * @param name
	 * @return
	 */
	SemesterDto findByName(
			@Valid @NotEmpty(message = "{find.semester.name.notempty}") String name);

}
