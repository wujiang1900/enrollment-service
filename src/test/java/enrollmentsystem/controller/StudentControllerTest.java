/**
 *
 */
package enrollmentsystem.controller;

import static enrollmentsystem.util.AssertionHelper.assertBadRequest;
import static enrollmentsystem.util.AssertionHelper.assertCreated;
import static enrollmentsystem.util.AssertionHelper.assertMethodNotAllowed;
import static enrollmentsystem.util.AssertionHelper.assertNotFound;
import static enrollmentsystem.util.AssertionHelper.assertOK;
import static enrollmentsystem.util.MockedObjects.mockedStudent;
import static enrollmentsystem.util.MockedObjects.mockedStudentRequest;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.repository.StudentRepository;
import enrollmentsystem.repository.entity.Student;
import enrollmentsystem.util.IntegrationTestBase;
import enrollmentsystem.util.NullableArgumentConverter;

/**
 * Tests all {@link StudentController} APIs.
 *
 * @author Jiang Wu
 *
 */
class StudentControllerTest extends IntegrationTestBase {

	private static final String BASE_CONTEXT_PATH = "/students";

	@Autowired
	private StudentRepository studentRepository;

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		studentRepository.deleteAll();
	}

	@Nested
	@DisplayName("Test API to add a new student")
	class CreateStudent {

		@Test
		@DisplayName("When the input is valid")
		final void testCreateStudent_withValidInput() throws Exception {

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedStudentRequest());

			assertCreated(resultActions);
		}

		/**
		 * Test method for
		 * {@link enrollmentsystem.controller.StudentController#createStudent(StudentDto)}.
		 */
		@Test
		@DisplayName("When no content in the body")
		final void testCreateStudent_withoutBody() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(post(BASE_CONTEXT_PATH));

			assertBadRequest("Malformed JSON request", resultActions);
		}

		@Test
		@DisplayName("When the values are null in the request")
		final void testCreateStudent_withNullValues() throws Exception {

			StudentDto semester = new StudentDto();

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					semester);

			assertBadRequest("Request has constraint violations", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(2)))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'add.student.firstName')].message",
							hasItem("First name is required.")))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'add.student.lastName')].message",
							hasItem("Last name is required.")));
		}
	}

	@Nested
	@DisplayName("Test API to update the student")
	class UpdateStudent {

		/**
		 * Test method for
		 * {@link enrollmentsystem.controller.StudentController#updateStudent(StudentDto)}.
		 */
		@Test
		@DisplayName("When no content in the body")
		final void testUpdateStudent_withoutBody() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(put(BASE_CONTEXT_PATH));

			assertBadRequest("Malformed JSON request", resultActions);
		}

		@Test
		@DisplayName("When the values are null in the request")
		final void testUpdateStudent_withNullValues() throws Exception {

			StudentDto semester = new StudentDto();

			ResultActions resultActions = performPut(BASE_CONTEXT_PATH,
					semester);

			assertBadRequest("Request has constraint violations", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(2)))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'update.student.firstName')].message",
							hasItem("First name is required.")))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'update.student.lastName')].message",
							hasItem("Last name is required.")));
		}

		@Test
		@DisplayName("When the input is valid but the id not found")
		final void testUpdateStudent_withValidInputNIdNotFound()
				throws Exception {

			ResultActions resultActions = performPut(BASE_CONTEXT_PATH,
					mockedStudentRequest());

			assertNotFound("Student id 1 not found.", resultActions);
		}

		@Test
		@DisplayName("When the input is valid")
		final void testUpdateStudent_withValidInput() throws Exception {

			studentRepository.save(mockedStudent());

			ResultActions resultActions = performPut(BASE_CONTEXT_PATH,
					mockedStudentRequest());

			assertOK(resultActions);
		}

	}

	@Nested
	@DisplayName("Test API to get the student by id")
	class FindStudentById {
		/**
		 * Test method for
		 * {@link enrollmentsystem.controller.StudentController#findStudentById(java.lang.Long)}.
		 */
		@Test
		@DisplayName("When no value in the path variable")
		final void testFindStudentById_noPathProvided() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(get(BASE_CONTEXT_PATH));

			assertMethodNotAllowed(resultActions);
		}

		@Test
		@DisplayName("When the input is valid but no record found")
		final void testFindStudentById_validInputNoRecordFound()
				throws Exception {
			ResultActions resultActions = mockMvc
					.perform(get(BASE_CONTEXT_PATH + "/{id}", "123"));

			assertNotFound("Student id 123 not found.", resultActions);
		}

		@Test
		@DisplayName("When the input is valid and the record is found")
		final void testFindStudentById_validRecordFound() throws Exception {

			Student mockedStudent = mockedStudent();
			studentRepository.save(mockedStudent);

			ResultActions resultActions = mockMvc
					.perform(get(BASE_CONTEXT_PATH + "/{id}", "1"));

			assertOK(resultActions).andExpect(jsonPath("$").isNotEmpty())
					.andExpect(jsonPath("$.firstName",
							is(mockedStudent.getFirstName())))
					.andExpect(jsonPath("$.lastName",
							is(mockedStudent.getLastName())))
					.andExpect(jsonPath("$.id", is(1)));

		}

	}

	@Nested
	@DisplayName("Test API to get the list of students")
	class FetchStudent {

		@ParameterizedTest
		@CsvSource({"3A, 2022fall, 1, 10, ASC, id",
				"3A, null, null, null, null, null",
				"3A, 2022fall, null, null, null, null",
				"3A, 2022fall, null, null, null, null",
				"null, 2022fall, null, null, null, null",
				"null, null, null, null, null, null"})
		@DisplayName("When the input is valid")
		final void testFetchStudents_ByStudentIdNSemesterWithEmptyResult(
				String courseClass, String semester,
				@ConvertWith(NullableArgumentConverter.class) String pageNo,
				@ConvertWith(NullableArgumentConverter.class) String pageSize,
				@ConvertWith(NullableArgumentConverter.class) String direction,
				@ConvertWith(NullableArgumentConverter.class) String sortBy)
				throws Exception {

			studentRepository.save(mockedStudent());

			ResultActions resultActions = mockMvc.perform(get("/fetchStudents")
					.param("class", courseClass)
					.param("semester", semester).param("pageNo", pageNo)
					.param("pageSize", pageSize).param("direction", direction)
					.param("sortBy", sortBy));

			assertOK(resultActions).andExpect(jsonPath("$").isArray());
		}

	}

}
