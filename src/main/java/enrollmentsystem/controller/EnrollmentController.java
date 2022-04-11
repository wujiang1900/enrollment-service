package enrollmentsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import enrollmentsystem.model.dto.EnrollmentRequestDto;
import enrollmentsystem.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The Enrollment API
 *
 * @author Jiang Wu
 *
 */
@RestController
@Tag(name = "enrollment", description = "the Enrollment API")
public class EnrollmentController {

	private EnrollmentService enrollmentService;

	public EnrollmentController(EnrollmentService service) {
		enrollmentService = service;
	}

	/**
	 * API to enroll a student into a class for a particular semester
	 *
	 * @param enrollmentRequest
	 * @return
	 */
	@Operation(summary = "Enroll a student into a class",
				description = "Enroll a student into a class for a particular semester",
				tags = {"enrollment"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "successful operation", content = @Content(schema = @Schema(implementation = EnrollmentRequestDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "404", description = "Student not found")})
	@PostMapping("/enrollments")
	@ResponseStatus(HttpStatus.CREATED)
	public void enroll(
			@Parameter(description = "Enrollment Request. Cannot null or empty.", required = true,
						schema = @Schema(implementation = EnrollmentRequestDto.class))
			@RequestBody EnrollmentRequestDto enrollmentRequest) {
		enrollmentService.enroll(enrollmentRequest);
	}

	/**
	 * API to drop a student from a class.
	 *
	 * @param enrollmentRequest
	 */
	@Operation(summary = "Drop a student from a class",
			description = "Drop a student from a class",
			tags = {"unEnrollment"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "Student not found")})
	@DeleteMapping("/enrollments")
	public void unEnroll(
			@Parameter(description = "Enrollment Request. Cannot null or empty.", required = true,
					schema = @Schema(implementation = EnrollmentRequestDto.class))
			@RequestBody EnrollmentRequestDto enrollmentRequest) {
		enrollmentService.unEnroll(enrollmentRequest);
	}
}
