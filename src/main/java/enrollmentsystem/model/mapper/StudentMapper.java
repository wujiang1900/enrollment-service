package enrollmentsystem.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.repository.entity.Student;

/**
 * This class is a map struct class to map the object from {@link Student} to
 * {@link StudentDto} vice versa.
 *
 * @author Jiang Wu
 *
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

	StudentDto toDto(Student entity);

	List<StudentDto> toDtos(List<Student> entities);

	Student toEntity(StudentDto student);

}
