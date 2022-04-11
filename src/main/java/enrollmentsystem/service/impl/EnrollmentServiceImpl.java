package enrollmentsystem.service.impl;

import static enrollmentsystem.model.constant.AppConstants.MAX_CREDIT_PER_SEMESTER;
import static enrollmentsystem.model.constant.AppConstants.MIN_CREDIT_FOR_FULLTIME;
import static java.text.MessageFormat.format;

import java.util.Optional;

import enrollmentsystem.exception.ClassAlreadyEnrolledException;
import enrollmentsystem.exception.RecordNotFoundException;
import enrollmentsystem.model.dto.ClassDto;
import org.springframework.stereotype.Service;

import enrollmentsystem.model.dto.EnrollmentRequestDto;
import enrollmentsystem.model.dto.SemesterDto;
import enrollmentsystem.exception.InvalidEnrollmentRequestException;
import enrollmentsystem.exception.StudentInfoNotFoundException;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.Enrollment;
import enrollmentsystem.repository.entity.Student;
import enrollmentsystem.service.ClassService;
import enrollmentsystem.service.EnrollmentService;
import enrollmentsystem.service.SemesterService;
import lombok.extern.log4j.Log4j2;

/**
 * An implementation class for {@link EnrollmentService}.
 *
 * @author Jiang Wu
 *
 */
@Log4j2
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

	private ClassService classService;
	private SemesterService semesterService;
	private StudentRepository studentRepository;

	public EnrollmentServiceImpl(ClassService classService, SemesterService semesterService, StudentRepository studentRepository) {
		this.classService = classService;
		this.semesterService = semesterService;
		this.studentRepository = studentRepository;
	}

	@Override
	public void enroll(EnrollmentRequestDto request) {
		log.debug("Enrolling the student {} into the class {} for the semester {}",
				request.getStudentId(), request.getSemester(), request.getCourseClass());
		enroll(request, true);
	}

	private void enroll(EnrollmentRequestDto request, boolean enrolling) {
		Student fetchedStudent = findOrElesThrowEx(request.getStudentId());
		SemesterDto semester = semesterService.findByName(request.getSemester());
		ClassDto course = classService.findByName(request.getCourseClass());

		Enrollment enrollment = findEnrollment(fetchedStudent, semester, course.getName(), enrolling);

		if(enrolling)
			enrollment.addCourseClass(request.getCourseClass(), course.getCredit());
		else
			enrollment.removeCourseClass(request.getCourseClass(), course.getCredit());

		int sumOfCredits = enrollment.getCreditsEnrolled().get();
		log.debug("Total credit for the semester - {} = {} ", semester.getName(), sumOfCredits);

		if (enrolling && sumOfCredits > MAX_CREDIT_PER_SEMESTER) {
			log.error("Total credit {} for the semester exceeds the limit {}",
					sumOfCredits, MAX_CREDIT_PER_SEMESTER);

			throw new InvalidEnrollmentRequestException();
		}

		enrollment.setFullTime(sumOfCredits >= MIN_CREDIT_FOR_FULLTIME);
		studentRepository.save(fetchedStudent);
	}

	private Enrollment findEnrollment(Student fetchedStudent, SemesterDto semester, String course, boolean enrolling) {
		log.debug("Finding the existing enrollments for the student {} and the semester {}",
				fetchedStudent.getId(), semester.getName());

		Optional<Enrollment> existingEnrollmentOpt = findExistingEnrollment(fetchedStudent, semester, course, enrolling);

		if (existingEnrollmentOpt.isPresent()) {
			log.debug("Found the current enrollments for the student {} and the semester {}",
					fetchedStudent.getId(), semester.getName());
			return existingEnrollmentOpt.get();
		} else {
			if(!enrolling)
				handleCourseNotFoundForUnEnrolling(fetchedStudent, course);

			log.debug("No current enrollments for the student {} and the semester {}",
					fetchedStudent.getId(), semester.getName());
			Enrollment enrollment = new Enrollment();
			enrollment.setSemester(semester.getName());
			fetchedStudent.addEnrollment(enrollment);
			return enrollment;
		}
	}

	private void handleCourseNotFoundForUnEnrolling(Student fetchedStudent, String course) {
		String student = fetchedStudent.getName();
		throw new RecordNotFoundException(format("Student {0} is not currently enrolled in course {1}.", student, course));
	}


	private Optional<Enrollment> findExistingEnrollment(Student fetchedStudent,
														SemesterDto semester, String course, boolean enrolling) {
		return fetchedStudent.getEnrollments() == null
				? Optional.empty()
				: fetchedStudent.getEnrollments().parallelStream()
						.filter(e -> {
							if(semester.getName().equalsIgnoreCase(e.getSemester())) {
								if(e.getClasses().contains(course)) {
									if(enrolling) {
										String student = fetchedStudent.getName();
										throw new ClassAlreadyEnrolledException(format("The student {0} has already enrolled in class {1}.",
												student, course));
									}
									else
										return true;
								}
								else
									if(enrolling) return true;
									else handleCourseNotFoundForUnEnrolling(fetchedStudent, course);
							}
							return false;
						})
						.findFirst();
	}

	private Student findOrElesThrowEx(Long id) {
		return studentRepository.findById(id).orElseThrow(() -> new StudentInfoNotFoundException(id));
	}

	@Override
	public void unEnroll(EnrollmentRequestDto request) {
		log.debug("Un-enrolling the student {} from the class {} for the semester {}",
				request.getStudentId(), request.getSemester(), request.getCourseClass());
		enroll(request, false);
//		studentRepository.unEnroll(request);
	}

}
