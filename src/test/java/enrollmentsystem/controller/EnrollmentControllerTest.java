/**
 *
 */
package enrollmentsystem.controller;

import static enrollmentsystem.util.AssertionHelper.*;
import static enrollmentsystem.util.MockedObjects.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

import enrollmentsystem.repository.entity.Enrollment;
import enrollmentsystem.util.MockedObjects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import enrollmentsystem.model.dto.EnrollmentRequestDto;
import enrollmentsystem.repository.CourseClassRepository;
import enrollmentsystem.repository.SemesterRepository;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.Student;
import enrollmentsystem.util.IntegrationTestBase;
import enrollmentsystem.util.NullableArgumentConverter;

/**
 * Tests all {@link EnrollmentController} APIs.
 *
 * @author Jiang Wu
 *
 */
class EnrollmentControllerTest extends IntegrationTestBase {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseClassRepository courseClassRepository;

	@Autowired
	private SemesterRepository semesterRepository;

	private static final String BASE_CONTEXT_PATH = "/enrollments";

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		// Needs to clear the embedded database after each method so that
		// each junit methods can be run independently.
		studentRepository.deleteAll();
		courseClassRepository.deleteAll();
		semesterRepository.deleteAll();
	}

	@Nested
	@DisplayName("Test API to enroll a student into a class for a particular semester")
	class Enroll {

		/**
		 * Test method for
		 * {@link enrollmentsystem.controller.EnrollmentController#enroll(enrollmentsystem.model.dto.EnrollmentRequestDto)}.
		 */
		@Test
		@DisplayName("When no content in the body")
		final void testEnroll_withoutBody() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(post(BASE_CONTEXT_PATH));

			assertBadRequest("Malformed JSON request", resultActions);
		}

		@Test
		@DisplayName("When the values are null in the request")
		final void testEnroll_withNullValues() throws Exception {

			EnrollmentRequestDto request = new EnrollmentRequestDto();
			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					request);

			assertBadRequest("Request has constraint violations", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(3)))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'enroll.enrollmentRequest.studentId')].message",
							hasItem("Id is required.")))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'enroll.enrollmentRequest.semester')].message",
							hasItem("Semester is required.")))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'enroll.enrollmentRequest.courseClass')].message",
							hasItem("Class is required.")));
		}

		@Test
		@DisplayName("When the input is valid but the id is not found in the database")
		final void testEnroll_withValidInputNIdNotFound() throws Exception {
			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto_3B());

			assertNotFound("Student id 1 not found.", resultActions);
		}

		@Test
		@DisplayName("When the course Already Enrolled")
		final void testEnroll_withValidInputNcourseAlreadyEnrolled() throws Exception {
			semesterRepository.save(mockedSemester());
			courseClassRepository.save(mockedCourseClass());
			studentRepository.save(mockedStudent());

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto_3A());
			assertBadRequest("The student has already enrolled in this class.", resultActions);
		}

		@Test
		@DisplayName("When the input is valid")
		final void testEnroll_withValidInputNIdFound() throws Exception {

			semesterRepository.save(mockedSemester());
			courseClassRepository.save(mockedCourseClass());
			studentRepository.save(mockedStudent());

			courseClassRepository.save(mockCourseClass("3B"));

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto_3B());

			assertCreated(resultActions);
			assertStudentEnrolledClassSize(2);
		}
	}

	@Nested
	@DisplayName("Test API to drop a student from a class.")
	class unEnroll {
		/**
		 * Test method for
		 * {@link enrollmentsystem.controller.EnrollmentController#unEnroll(java.lang.String, java.lang.Long)}.
		 */

		@Test
		@DisplayName("When the input is valid but the id is not found in the database")
		final void testUnEnroll_withValidInputNIdNotFound() throws Exception {
			ResultActions resultActions = performDelete(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto_3A());

			assertNotFound("Student id 1 not found.", resultActions);
		}

		@Test
		@DisplayName("When the course not enrolled")
		final void testEnroll_withValidInputNcourseNotEnrolled() throws Exception {

			semesterRepository.save(mockedSemester());
			courseClassRepository.save(mockedCourseClass());
			studentRepository.save(mockedStudent());

			courseClassRepository.save(mockCourseClass("3B"));

			ResultActions resultActions = performDelete(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto_3B());

			assertNotFound("Student TestFN TestLN is not currently enrolled in course 3B.", resultActions);
		}

		@Test
		@DisplayName("When the input is valid")
		final void testUnEnroll_withValidInputNIdFound() throws Exception {

			semesterRepository.save(mockedSemester());
			courseClassRepository.save(mockedCourseClass());
			studentRepository.save(mockedStudent());

			courseClassRepository.save(mockCourseClass("3B"));

			ResultActions resultActions = performDelete(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto_3A());

			assertOK(resultActions);

			assertStudentEnrolledClassSize(0);
		}
	}

	final void assertStudentEnrolledClassSize(int size) {
		Optional<Student> fetchedStudent = studentRepository.findById(1L);

		Assertions.assertTrue(fetchedStudent.isPresent());

		Optional<Enrollment> enrollment = fetchedStudent.get().getEnrollmentForSemester(MockedObjects.currentSemester);
		Assertions.assertEquals(size, enrollment.get().getClasses().size());
	}
}
