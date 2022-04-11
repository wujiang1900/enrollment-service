/**
 *
 */
package enrollmentsystem.service.impl;

import static enrollmentsystem.util.MockedObjects.mockedCourseClass;
import static enrollmentsystem.util.MockedObjects.mockedCourseClassDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import enrollmentsystem.model.dto.ClassDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import enrollmentsystem.exception.RecordNotFoundException;
import enrollmentsystem.model.mapper.CourseClassMapperImpl;
import enrollmentsystem.repository.CourseClassRepository;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.CourseClass;
import enrollmentsystem.service.ClassService;

/**
 * Tests all {@link ClassService} service methods.
 *
 * @author Jiang Wu
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ClassServiceImpl.class,
		CourseClassMapperImpl.class})
class ClassServiceImplTest {

	@Autowired
	private ClassService classService;

	@MockBean
	private StudentRepository studentRepository;

	@MockBean
	private CourseClassRepository courseClassRepository;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link ClassServiceImpl#fetchClasses(java.lang.Long, java.lang.String)}.
	 */
	@Test
	@DisplayName("Test fetch classes when the resulset is empty")
	final void testFetchClasses_withEmptyResult() {
		when(studentRepository.existsById(anyLong())).thenReturn(true);

		List<ClassDto> fetchedClasses = classService
				.fetchClasses(1L, "2022fall");

		assertTrue(fetchedClasses.isEmpty());

	}

	@Test
	@DisplayName("Test fetch classes when the resultset is not empty")
	final void testFetchClasses_withResult() {
		when(studentRepository.existsById(anyLong())).thenReturn(true);
		when(studentRepository.fetchClasses(anyLong(), anyString()))
				.thenReturn(mockedCourseClasses());

		List<ClassDto> fetchedClasses = classService
				.fetchClasses(1L, "2022fall");

		assertEquals(1, fetchedClasses.size());

	}

	private List<CourseClass> mockedCourseClasses() {
		return Stream.of(mockedCourseClass()).collect(Collectors.toList());
	}

	/**
	 * Test method for
	 * {@link ClassServiceImpl#add(ClassDto)}.
	 */
	@Test
	@DisplayName("add a class")
	final void testAdd() {
		when(courseClassRepository.save(any(CourseClass.class)))
				.then(i -> i.getArgument(0));

		ClassDto mockedClassDto = mockedCourseClassDto();
		classService.add(mockedClassDto);
		verify(courseClassRepository, times(1)).save(any(CourseClass.class));
	}

	/**
	 * Test method for
	 * {@link ClassServiceImpl#findByName(java.lang.String)}.
	 */
	@Test
	@DisplayName("find a class by its name but record not found")
	final void testFindByName_NotFound() {
		assertThrows(RecordNotFoundException.class, () -> {
			classService.findByName("3A");
		});
	}

	@Test
	@DisplayName("find a class by its name")
	final void testFindByName() {
		when(courseClassRepository.findByName(anyString()))
				.thenReturn(Optional.of(mockedCourseClass()));

		String className = "3A";
		ClassDto fetchedClass = classService.findByName(className);
		assertNotNull(fetchedClass);
		assertEquals(className, fetchedClass.getName());
	}
}
