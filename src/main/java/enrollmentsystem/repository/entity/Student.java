package enrollmentsystem.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Document for the collection 'students'.
 *
 * @author Jiang Wu
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Document(collection = "students")
public class Student {

	@Transient
	public static final String SEQUENCE_NAME = "users_sequence";

	@Id private Long id;
	private String firstName;
	private String lastName;
	private String nationality;
	private Set<Enrollment> enrollments;

	public void addEnrollment(Enrollment enrollment) {
		if (enrollments == null) {
			enrollments = ConcurrentHashMap.newKeySet();
		}
		enrollments.add(enrollment);
	}

	public Optional<Enrollment> getEnrollmentForSemester(String semester) {
		return enrollments.stream().filter(e->e.getSemester().equalsIgnoreCase(semester)).findFirst();
	}

    public String getName() {
		return firstName + " " + lastName;
    }
}
