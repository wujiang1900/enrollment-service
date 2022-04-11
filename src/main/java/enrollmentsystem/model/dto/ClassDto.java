package enrollmentsystem.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import enrollmentsystem.repository.entity.CourseClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Data Transformation Object for {@link CourseClass}.
 *
 * @author Jiang Wu
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClassDto {

	@Schema(description = "Name of the Class.", example = "3A", required = true)
	@NotEmpty(message = "{add.courseclass.name.notempty}")
	private String name;

	@Schema(description = "Fixed Credit/Unit of the Class. Some harder classes be 4 credits while easier one could 2 or 3 credits.", example = "4", required = true)
	@Max(value = 4, message = "{add.courseclass.credit.max}")
	@Min(value = 2, message = "{add.courseclass.credit.min}")
	@NotNull
	private Integer credit;
}
