package enrollmentsystem.model.dto;

import static enrollmentsystem.model.constant.AppConstants.APP_DATE_FORMAT;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import enrollmentsystem.repository.entity.Semester;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Data Transformation Object for {@link Semester} entity.
 *
 * @author Jiang Wu
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SemesterDto {

	@Schema(description = "Name of the Semester.", example = "2022fall", required = true)
	@NotNull(message = "{add.semester.name.notempty}")
	private String name;

	@Schema(description = "Start Date of the Semester.", example = "2022-08-24", required = false)
	@JsonFormat(pattern = APP_DATE_FORMAT)
	private LocalDate startDate;

	@Schema(description = "End Date of the Semester.", example = "2022-12-18", required = false)
	@JsonFormat(pattern = APP_DATE_FORMAT)
	private LocalDate endDate;
}
