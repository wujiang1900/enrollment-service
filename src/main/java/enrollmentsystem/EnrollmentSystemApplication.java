package enrollmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
/**
 *
 * @author Jiang Wu
 *
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = {"enrollmentsystem.repository"})
public class EnrollmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnrollmentSystemApplication.class, args);
	}

}
