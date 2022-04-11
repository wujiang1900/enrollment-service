package enrollmentsystem.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import enrollmentsystem.repository.entity.Semester;

/**
 * {@link SemesterRepository} to persist/fetch {@link Semester} collection from
 * mongodb.
 * 
 * @author Jiang Wu
 *
 */
public interface SemesterRepository extends MongoRepository<Semester, Integer> {

	/**
	 * Find {@link Semester} by its name.
	 * 
	 * @param name
	 * @return
	 */
	Optional<Semester> findByName(String name);
}
