package enrollmentsystem.util;

import java.time.LocalDate;

import enrollmentsystem.model.dto.*;
import enrollmentsystem.model.dto.StudentDto;
import enrollmentsystem.repository.entity.CourseClass;
import enrollmentsystem.repository.entity.Enrollment;
import enrollmentsystem.repository.entity.Semester;
import enrollmentsystem.repository.entity.Student;

/**
 * Mocked Objects for Api's junits.
 *
 * @author Jiang Wu
 *
 */
public final class MockedObjects {

	private MockedObjects() {

	}

	public static final String currentSemester = "2022fall";

	public static Student mockedStudent() {
		Student student = new Student();
		student.setId(1L);
		student.setFirstName("TestFN");
		student.setLastName("TestLN");
		student.setNationality("US");
		Enrollment enrollment = new Enrollment();
		enrollment.addCourseClass(mockedCourseClass().getName(), 4);
		enrollment.setSemester(mockedSemester().getName());
		student.addEnrollment(enrollment);
		return student;
	}

	public static Semester mockedSemester() {
		Semester semester = new Semester();
		semester.setName(currentSemester);
		semester.setStartDate(LocalDate.now());
		semester.setEndDate(LocalDate.now().plusDays(90));
		return semester;
	}

	public static SemesterDto mockedSemesterDto() {
		SemesterDto semester = new SemesterDto();
		semester.setName(currentSemester);
		semester.setStartDate(LocalDate.now());
		semester.setEndDate(LocalDate.now().plusDays(90));
		return semester;
	}

	public static CourseClass mockedCourseClass() {
		return mockCourseClass("3A");
	}

	public static CourseClass mockCourseClass(String courseClassString) {
		CourseClass courseClass = new CourseClass();
		courseClass.setName(courseClassString);
		courseClass.setCredit(4);
		return courseClass;
	}

	public static ClassDto mockedCourseClassDto() {
		ClassDto courseClass = new ClassDto();
		courseClass.setName("3A");
		courseClass.setCredit(4);
		return courseClass;
	}

	public static StudentDto mockedStudentRequest() {
		StudentDto student = new StudentDto();
		student.setId(1L);
		student.setFirstName("TestFN");
		student.setLastName("TestLN");
		student.setNationality("US");
		return student;
	}

	public static EnrollmentRequestDto mockedEnrollmentRequestDto_3A() {
		EnrollmentRequestDto request = new EnrollmentRequestDto();
		request.setCourseClass("3A");
		request.setSemester(currentSemester);
		request.setStudentId(1L);
		return request;
	}

	public static EnrollmentRequestDto mockedEnrollmentRequestDto() {
		return mockedEnrollmentRequestDto_3B();
	}

	public static EnrollmentRequestDto mockedEnrollmentRequestDto_3B() {
		EnrollmentRequestDto request = new EnrollmentRequestDto();
		request.setCourseClass("3B");
		request.setSemester(currentSemester);
		request.setStudentId(1L);
		return request;
	}

	public static SearchRequestDto mockedSearchRequestDto() {
		SearchRequestDto request = new SearchRequestDto();
		request.setCourseClass("3A");
		request.setSemester(currentSemester);
		request.setPageNo(1);
		request.setPageSize(10);
		request.setSortBy(new String[]{"id"});
		request.setDirection("ASC");
		return request;
	}

}
