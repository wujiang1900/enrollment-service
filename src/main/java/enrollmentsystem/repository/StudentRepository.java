package enrollmentsystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import enrollmentsystem.repository.entity.Student;

/**
 * {@link StudentRepository} to persist/fetch {@link Student} collection from
 * mongodb.
 *
 * @author Jiang Wu
 *
 */
public interface StudentRepository
		extends
			MongoRepository<Student, Long>,
		StudentFetchRepository {
	boolean existsByFirstNameAndLastNameAndNationality(String f, String l, String n);

}
