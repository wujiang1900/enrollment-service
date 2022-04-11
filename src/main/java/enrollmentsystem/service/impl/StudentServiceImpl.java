package enrollmentsystem.service.impl;

import static java.text.MessageFormat.format;

import java.util.List;

import enrollmentsystem.service.SequenceGeneratorService;
import org.springframework.stereotype.Service;

import enrollmentsystem.model.dto.SearchRequestDto;
import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.exception.ApiRequestException;
import enrollmentsystem.exception.StudentInfoNotFoundException;
import enrollmentsystem.model.mapper.StudentMapper;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.Student;
import enrollmentsystem.service.StudentService;
import lombok.extern.log4j.Log4j2;

/**
 * An implementation class for {@link StudentService}.
 *
 * @author Jiang Wu
 *
 */
@Log4j2
@Service
public class StudentServiceImpl implements StudentService {

	private StudentRepository studentRepository;
	private SequenceGeneratorService sequenceGeneratorService;
	private StudentMapper studentMapper;

	public StudentServiceImpl(StudentRepository studentRepository, SequenceGeneratorService sequenceGeneratorService, StudentMapper studentMapper) {
		this.studentRepository = studentRepository;
		this.sequenceGeneratorService = sequenceGeneratorService;
		this.studentMapper = studentMapper;
	}

	@Override
	public long add(StudentDto student) {
		if (isExists(student)) {
			log.error("Student {} already exists", student);
			throw new ApiRequestException(
					format("Student already exists"));
		}
		student.setId(sequenceGeneratorService.generateSequence(Student.SEQUENCE_NAME));

		log.info("Saving the student - {} into the database", student.getId());
		return studentRepository.save(toEntity(student)).getId();
	}

	@Override
	public StudentDto update(StudentDto student) {
		if (isExistsById(student)) {
			log.info("Updating the student - {} into the database",
					student.getId());
			return toDto(studentRepository.save(toEntity(student)));
		} else {
			log.error("Student id {} not found", student.getId());
			throw new StudentInfoNotFoundException(student.getId());
		}
	}

	@Override
	public List<StudentDto> fetchStudents(SearchRequestDto searchRequest) {
		log.info("Loading the students from the database.");
		return studentMapper
				.toDtos(studentRepository.fetchStudents(searchRequest));
	}

	@Override
	public StudentDto findById(Long id) {
		Student fetchedStudent = findOrElesThrowEx(id);
		return toDto(fetchedStudent);
	}

	private boolean isExists(StudentDto student) {
		return studentRepository.existsByFirstNameAndLastNameAndNationality
				(student.getFirstName(), student.getLastName(), student.getNationality());
	}

	private Student toEntity(StudentDto student) {
		return studentMapper.toEntity(student);
	}

	private StudentDto toDto(Student entity) {
		return studentMapper.toDto(entity);
	}

	private boolean isExistsById(StudentDto student) {
		return studentRepository.existsById(student.getId());
	}

	private Student findOrElesThrowEx(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new StudentInfoNotFoundException(id));
	}
}
