package enrollmentsystem.service.impl;

import static enrollmentsystem.util.MockedObjects.mockedSemester;
import static enrollmentsystem.util.MockedObjects.mockedSemesterDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import enrollmentsystem.model.dto.SemesterDto;
import enrollmentsystem.exception.RecordNotFoundException;
import enrollmentsystem.model.mapper.SemesterMapperImpl;
import enrollmentsystem.repository.SemesterRepository;
import enrollmentsystem.repository.entity.Semester;
import enrollmentsystem.service.SemesterService;

/**
 * Tests all {@link SemesterService} service methods.
 *
 * @author Jiang Wu
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SemesterServiceImpl.class,
		SemesterMapperImpl.class})
class SemesterServiceImplTest {

	@Autowired
	private SemesterService semesterService;

	@MockBean
	private SemesterRepository semesterRepository;

	@Test
	@DisplayName("Add a semester")
	final void testAdd() {

		when(semesterRepository.save(any(Semester.class)))
				.then(i -> i.getArgument(0));

		SemesterDto mockedSemesterDto = mockedSemesterDto();
		SemesterDto addedSemester = semesterService.add(mockedSemesterDto);

		assertNotNull(addedSemester);
		assertEquals(mockedSemesterDto.getName(), addedSemester.getName());
	}

	@Test
	@DisplayName("Find a semester by its name but record not found")
	final void testFindByName_NotFound() {
		assertThrows(RecordNotFoundException.class, () -> {
			semesterService.findByName("3A");
		});
	}

	@Test
	@DisplayName("Find a semester by its name")
	final void testFindByName() {
		when(semesterRepository.findByName(anyString()))
				.thenReturn(Optional.of(mockedSemester()));

		String semester = "2022fall";
		SemesterDto fetchedClass = semesterService.findByName(semester);
		assertNotNull(fetchedClass);
		assertEquals(semester, fetchedClass.getName());
	}

}
