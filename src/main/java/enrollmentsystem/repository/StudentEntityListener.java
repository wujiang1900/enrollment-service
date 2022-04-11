package enrollmentsystem.repository;


import enrollmentsystem.repository.entity.Student;
import enrollmentsystem.service.SequenceGeneratorService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class StudentEntityListener extends AbstractMongoEventListener<Student> {

    private SequenceGeneratorService sequenceGenerator;

    public StudentEntityListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Student> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Student.SEQUENCE_NAME));
        }
    }
}
