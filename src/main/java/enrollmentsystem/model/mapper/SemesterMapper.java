package enrollmentsystem.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import enrollmentsystem.model.dto.SemesterDto;
import enrollmentsystem.repository.entity.Semester;
/**
 * This class is a map struct class to map the object from {@link Semester}
 * to {@link SemesterDto} vice versa.
 *
 * @author Jiang Wu
 *
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SemesterMapper {

	Semester toEntity(SemesterDto dto);

	SemesterDto toDto(Semester entity);

}
