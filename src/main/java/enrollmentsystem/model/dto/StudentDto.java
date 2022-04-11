package enrollmentsystem.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import enrollmentsystem.model.validation.CreationValidationGroup;
import enrollmentsystem.model.validation.UniqueStudentId;
import enrollmentsystem.repository.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Data Transformation Object for create/update the {@link Student} document.
 *
 * @author Jiang Wu
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentDto {

	@Schema(description = "Unique Identifier of the Student.", example = "1")
	private Long id;

	@Schema(description = "First Name of the Student.", example = "Michael", required = true)
	@NotBlank(message = "{add.student.firstName.notempty}")
	private String firstName;

	@Schema(description = "Last Name of the Student.", example = "Wong", required = true)
	@NotBlank(message = "{add.student.lastName.notempty}")
	private String lastName;

	@Schema(description = "Nationality of the Student.", example = "US", required = false)
	private String nationality;
}
