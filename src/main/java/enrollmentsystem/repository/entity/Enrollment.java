package enrollmentsystem.repository.entity;

import lombok.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An embedded document reference for Enrollment.
 *
 * @author Jiang Wu
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@ToString
public class Enrollment {
	private String semester;
	private Set<String> classes;
	private boolean isFullTime;
	private AtomicInteger creditsEnrolled = new AtomicInteger();

	public void addCourseClass(String courseClass, int credit) {
		creditsEnrolled.getAndAdd(credit) ;
		if (classes == null) {
			classes = ConcurrentHashMap.newKeySet();
		}
		classes.add(courseClass);
	}

	public void removeCourseClass(String courseClass, int credit) {
		creditsEnrolled.getAndAdd(-credit);
		classes.remove(courseClass);
	}
}
