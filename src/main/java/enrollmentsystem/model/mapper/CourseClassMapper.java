package enrollmentsystem.model.mapper;

import java.util.List;

import enrollmentsystem.model.dto.ClassDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import enrollmentsystem.repository.entity.CourseClass;
/**
 * This class is a map struct class to map the object from {@link CourseClass}
 * to {@link ClassDto} vice versa.
 *
 * @author Jiang Wu
 *
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseClassMapper {

	CourseClass toEntity(ClassDto dto);

	ClassDto toDto(CourseClass entity);

	List<ClassDto> toDtos(List<CourseClass> entities);

}