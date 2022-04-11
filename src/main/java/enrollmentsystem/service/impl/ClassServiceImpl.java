package enrollmentsystem.service.impl;

import static java.text.MessageFormat.format;

import java.util.List;

import enrollmentsystem.exception.StudentInfoNotFoundException;
import enrollmentsystem.model.dto.ClassDto;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import enrollmentsystem.exception.RecordNotFoundException;
import enrollmentsystem.model.mapper.CourseClassMapper;
import enrollmentsystem.repository.CourseClassRepository;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.CourseClass;
import enrollmentsystem.service.ClassService;
import lombok.extern.log4j.Log4j2;
/**
 * An implementation class for {@link ClassService}
 *
 * @author Jiang Wu
 *
 */
@Log4j2
@Service
public class ClassServiceImpl implements ClassService {

	private CourseClassRepository courseClassRepository;
	private StudentRepository studentRepository;
	private CourseClassMapper courseClassMapper;

	public ClassServiceImpl(CourseClassRepository courseClassRepository, StudentRepository studentRepository, CourseClassMapper courseClassMapper) {
		this.courseClassRepository = courseClassRepository;
		this.studentRepository = studentRepository;
		this.courseClassMapper = courseClassMapper;
	}

	@Override
	public List<ClassDto> fetchClasses(Long studentId,
									   String semesterName) {
		log.info("Fetching the classes enrolled for the student id - {} and semester name - {}",
				studentId, semesterName);
		if(!studentRepository.existsById(studentId)) {
			throw new StudentInfoNotFoundException(studentId);
		}
		return toDtos(studentRepository.fetchClasses(studentId, semesterName));
	}

	@Override
	@CachePut(value = "courseClassess", key = "#courseClass.name")
	public void add(ClassDto courseClass) {
		log.info("Saving the class - {} into the database",
				courseClass.getName());
		courseClassRepository.save(toEntity(courseClass));
	}

	@Override
	@Cacheable(value = "courseClasses", key = "#name", unless = "#result==null")
	public ClassDto findByName(String name) throws RecordNotFoundException {
		log.info("Loading the class - {} from the database.", name);
		CourseClass courseClass = courseClassRepository.findByName(name)
				.orElseThrow(() -> new RecordNotFoundException(
						format("Class {0} not found.", name)));
		return toDto(courseClass);
	}

	private CourseClass toEntity(ClassDto courseClass) {
		return courseClassMapper.toEntity(courseClass);
	}

	private ClassDto toDto(CourseClass courseClass) {
		return courseClassMapper.toDto(courseClass);
	}

	private List<ClassDto> toDtos(List<CourseClass> entities) {
		return courseClassMapper.toDtos(entities);
	}

}
