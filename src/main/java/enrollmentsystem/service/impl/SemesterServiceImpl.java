package enrollmentsystem.service.impl;

import static java.text.MessageFormat.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import enrollmentsystem.model.dto.SemesterDto;
import enrollmentsystem.exception.RecordNotFoundException;
import enrollmentsystem.model.mapper.SemesterMapper;
import enrollmentsystem.repository.SemesterRepository;
import enrollmentsystem.repository.entity.Semester;
import enrollmentsystem.service.SemesterService;
import lombok.extern.log4j.Log4j2;

/**
 * An implementation class for {@link SemesterService}.
 *
 * @author Jiang Wu
 *
 */
@Log4j2
@Service
public class SemesterServiceImpl implements SemesterService {

	@Autowired
	private SemesterRepository semesterRepository;

	@Autowired
	private SemesterMapper semesterMapper;

	@Override
	@CachePut(value = "semesters", key = "#semester.name")
	public SemesterDto add(SemesterDto semester) {
		log.info("Saving the semester - {} into the database",
				semester.getName());
		return toDto(semesterRepository.save(toEntity(semester)));
	}

	@Override
	@Cacheable(value = "semesters", key = "#name", unless = "#result==null")
	public SemesterDto findByName(String name) {
		log.info("Loading the semester - {} from the database.", name);
		Semester semester = semesterRepository.findByName(name)
				.orElseThrow(() -> new RecordNotFoundException(
						format("Semester {0} not found.", name)));
		return toDto(semester);
	}

	private Semester toEntity(SemesterDto semester) {
		return semesterMapper.toEntity(semester);
	}

	private SemesterDto toDto(Semester semester) {
		return semesterMapper.toDto(semester);
	}

}
