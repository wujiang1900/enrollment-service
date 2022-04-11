package enrollmentsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import enrollmentsystem.model.dto.SearchRequestDto;
import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The Student API
 *
 * @author Jiang Wu
 */
@RestController
@Tag(name = "student", description = "the Student API")
public class StudentController {

	private StudentService studentService;

	public StudentController(StudentService service) {
		studentService = service;
	}

	/**
	 * Fetch bulk record: all students in database API to get the list of
	 * students enrolled in a class for a particular semester.
	 *
	 * @return
	 */
	@Operation(summary = "Get the list of students", description = "Get the list of students enrolled in a class for a particular semester", tags = {
			"student"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentDto.class))))})
	@GetMapping("/fetchStudents")
	public List<StudentDto> fetchStudents(
			@Parameter(description = "Class name, it is optional") @RequestParam(name = "class", required = false) final String courseClass,
			@Parameter(description = "Semester name, it is optional") @RequestParam(required = false) String semester,
			@Parameter(description = "Page number, default is 1") @RequestParam(required = true, defaultValue = "1") Integer pageNo,
			@Parameter(description = "Page size, default is 100") @RequestParam(required = true, defaultValue = "100") Integer pageSize,
			@Parameter(description = "Sort direction, default is ASC") @RequestParam(required = true, defaultValue = "ASC") String direction,
			@Parameter(description = "Sort by field, default is id") @RequestParam(required = true, defaultValue = "id") String sortBy) {
		SearchRequestDto searchRequestDto = SearchRequestDto.builder()
				.courseClass(courseClass).semester(semester)
				.pageNo(pageNo).pageSize(pageSize).direction(direction)
				.sortBy(new String[]{sortBy}).build();
		return studentService.fetchStudents(searchRequestDto);
	}

	/**
	 * API to add a new student
	 *
	 * @param student
	 * @return
	 */
	@Operation(summary = "Add a new student", description = "Add the basic information of a student", tags = {
			"student"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Student created", content = @Content(schema = @Schema(implementation = StudentDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "400", description = "Student already exists")})
	@PostMapping("/students")
	@ResponseStatus(HttpStatus.CREATED)
	public long createStudent(
			@Parameter(description = "Student to add. Cannot null or empty.", required = true, schema = @Schema(implementation = StudentDto.class)) @RequestBody StudentDto student) {

		return studentService.add(student);
	}

	/**
	 * API to modify new students
	 *
	 * @param student
	 * @return
	 */
	@Operation(summary = "Update an existing student", description = "Update the basic information of an existing student", tags = {
			"student"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Student not found")})
	@PutMapping("/students")
	public void updateStudent(
			@Parameter(description = "Student to update. Cannot null or empty.", required = true, schema = @Schema(implementation = StudentDto.class)) @RequestBody StudentDto student) {
		studentService.update(student);
	}

	/**
	 * API to get student by id
	 *
	 * @param id
	 * @return
	 */
	@Operation(summary = "Get student by id", description = "Returns a single student", tags = {
			"student"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = StudentDto.class))),
			@ApiResponse(responseCode = "404", description = "Student not found")})
	@GetMapping("/students/{id}")
	public StudentDto findStudentById(
			@Parameter(description = "Id of the student to be obtained. Cannot be empty.", required = true) @PathVariable Long id) {
		return studentService.findById(id);
	}

}
