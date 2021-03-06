package enrollmentsystem.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Data Transformation Object for searching the student records based on the
 * criteria.
 *
 * @author Jiang Wu
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class SearchRequestDto {
	@Schema(description = "Name of the Semester.", example = "2022fall", required = false)
	private String semester;
	@Schema(description = "Name of the Class.", example = "3A", required = false)
	private String courseClass;
	@Schema(description = "Page Number", example = "1", required = false)
	private Integer pageNo;
	@Schema(description = "Page Size", example = "10", required = false)
	private Integer pageSize;
	@Schema(description = "Direction (ASC/DESC)", example = "ASC", required = false)
	private String direction;
	@Schema(description = "Sort By", example = "id", required = false)
	private String[] sortBy;
}
