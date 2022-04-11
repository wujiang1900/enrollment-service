package enrollmentsystem.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import enrollmentsystem.repository.entity.CourseClass;

/**
 * 
 * {@link CourseClassRepository} to persist/fetch {@link CourseClass} collection
 * from mongodb.
 * 
 * @author Jiang Wu
 *
 */
public interface CourseClassRepository
		extends
			MongoRepository<CourseClass, Integer> {

	/**
	 * Finds the {@link CourseClass} by its name.
	 * 
	 * @param name
	 * @return
	 */
	Optional<CourseClass> findByName(String name);

}
