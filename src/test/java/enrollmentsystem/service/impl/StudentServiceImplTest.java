package enrollmentsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import enrollmentsystem.service.SequenceGeneratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.exception.StudentInfoNotFoundException;
import enrollmentsystem.model.mapper.StudentMapperImpl;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.Student;
import enrollmentsystem.service.StudentService;


import static enrollmentsystem.util.MockedObjects.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests all {@link StudentService} service methods.
 *
 * @author Jiang Wu
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StudentServiceImpl.class, StudentMapperImpl.class})
class StudentServiceImplTest {

	@Autowired
	private StudentService studentService;

	@MockBean
	private StudentRepository studentRepository;

	@MockBean
	private SequenceGeneratorService sequenceGeneratorService;

	@Test
	@DisplayName("Test fetch students when the result set is empty")
	final void testFetchStudents_withEmptyResult() {
		List<StudentDto> fetchedStudents = studentService
				.fetchStudents(mockedSearchRequestDto());

		assertTrue(fetchedStudents.isEmpty());

	}

	@Test
	@DisplayName("Test fetch classes when the resultset is not empty")
	final void testFetchStudents_withResult() {
		when(studentRepository.fetchStudents(mockedSearchRequestDto()))
				.thenReturn(mockedStudents());

		List<StudentDto> fetchedStudents = studentService
				.fetchStudents(mockedSearchRequestDto());

		assertEquals(1, fetchedStudents.size());

	}

	private List<Student> mockedStudents() {
		return Stream.of(mockedStudent()).collect(Collectors.toList());
	}

	@Test
	@DisplayName("Add a student")
	final void testAdd() {

		when(studentRepository.save(any(Student.class)))
				.then(i -> i.getArgument(0));

		StudentDto mockedStudentRequest = mockedStudentRequest();
		studentService.add(mockedStudentRequest);


	}

	@Test
	@DisplayName("Update student")
	final void testUpdate() {

		when(studentRepository.existsById(eq(1L))).thenReturn(true);
		when(studentRepository.save(any(Student.class)))
				.then(i -> i.getArgument(0));

		StudentDto mockedStudentRequest = mockedStudentRequest();
		mockedStudentRequest.setId(1L);
		mockedStudentRequest.setFirstName("UpdatedName");
		StudentDto updatedStudent = studentService.update(mockedStudentRequest);

		assertNotNull(updatedStudent);
		assertEquals(mockedStudentRequest.getFirstName(),
				updatedStudent.getFirstName());
	}

	@Test
	@DisplayName("Update student when id not found")
	final void testUpdate_whenIdNotFound() {

		when(studentRepository.existsById(eq(1L))).thenReturn(false);

		StudentDto mockedStudentRequest = mockedStudentRequest();
		mockedStudentRequest.setFirstName("UpdatedName");

		assertThrows(StudentInfoNotFoundException.class,
				() -> studentService.update(mockedStudentRequest));

	}

	@Test
	@DisplayName("Find a student by its id but student info not found")
	final void testFindById_NotFound() {
		assertThrows(StudentInfoNotFoundException.class, () -> {
			studentService.findById(1L);
		});
	}

	@Test
	@DisplayName("Find a student by its id")
	final void testFindById() {
		when(studentRepository.findById(anyLong()))
				.thenReturn(Optional.of(mockedStudent()));

		StudentDto fetchedStudent = studentService.findById(1L);
		assertNotNull(fetchedStudent);
		assertEquals(1L, fetchedStudent.getId());
	}

}
