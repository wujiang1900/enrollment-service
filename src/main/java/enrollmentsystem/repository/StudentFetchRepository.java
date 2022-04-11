package enrollmentsystem.repository;

import java.util.List;

import enrollmentsystem.model.dto.EnrollmentRequestDto;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import enrollmentsystem.model.dto.SearchRequestDto;
import enrollmentsystem.repository.entity.CourseClass;
import enrollmentsystem.repository.entity.Student;

/**
 *
 * {@link StudentFetchRepository} is an interface to fetch the records by using
 * {@link MongoOperations} and {@link Aggregation}
 *
 * @author Jiang Wu
 *
 */
public interface StudentFetchRepository {

	/**
	 * Gets the list of students enrolled in a class for a particular semester.
	 *
	 * @param searchRequest
	 * @return
	 */
	List<Student> fetchStudents(SearchRequestDto searchRequest);

	/**
	 *
	 * Gets the list of classes for a particular student for a semester, or the
	 * fully history of classes enrolled.
	 *
	 * @param studentId
	 * @param semesterName
	 * @return
	 */
	List<CourseClass> fetchClasses(Long studentId, String semesterName);

	/**
	 * Helps to drop a student from a class.
	 *
	 * @param request
	 * @return
	 */
	boolean unEnroll(EnrollmentRequestDto request);

}
