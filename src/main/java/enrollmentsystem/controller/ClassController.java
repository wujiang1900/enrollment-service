package enrollmentsystem.controller;

import java.util.List;

import enrollmentsystem.model.dto.ClassDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import enrollmentsystem.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The Class API
 *
 * @author Jiang Wu
 */
@RestController
@Tag(name = "class", description = "the Class API")
public class ClassController {

	@Autowired
	private ClassService classService;

	/**
	 * API to get the list of classes for a particular student for a semester,
	 * or the fully history of classes enrolled.
	 *
	 * @return
	 */
	@Operation(summary = "Get the list of classes", description = "Get  the list of classes for a particular student for a semester, or the fully history of classes enrolled.", tags = {
			"class"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClassDto.class))))})
	@GetMapping("/fetchClasses")
	public List<ClassDto> fetchClasses(
			@Parameter(description = "Semester name, it is optional") @RequestParam(required = false) String semester,
			@Parameter(description = "Student id", required = true) @RequestParam(name = "id") Long studentId) {
		return classService.fetchClasses(studentId, semester);
	}

	/**
	 * API to add a new class
	 *
	 * @param courseClass
	 * @return
	 */
	@Operation(summary = "Add a new class", description = "Add the basic information of class", tags = {
			"class"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Class created", content = @Content(schema = @Schema(implementation = ClassDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input")})
	@PostMapping("/classes")
	@ResponseStatus(HttpStatus.CREATED)
	public void createClass(
			@Parameter(description = "Class to add. Cannot null or empty.", required = true, schema = @Schema(implementation = ClassDto.class)) @RequestBody ClassDto courseClass) {
		classService.add(courseClass);
	}

	/**
	 * API to get class by name
	 *
	 * @param name
	 * @return
	 */
	@Operation(summary = "Get class by name", description = "Returns a single class", tags = {
			"class"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ClassDto.class))),
			@ApiResponse(responseCode = "404", description = "Class not found")})
	@GetMapping("/classes/{name}")
	public ResponseEntity<Object> findClassByName(
			@Parameter(description = "Name of the class to be obtained. Cannot be empty.", required = true) @PathVariable String name) {

		ClassDto courseClass = classService.findByName(name);

		return ResponseEntity.ok(courseClass);
	}

}
