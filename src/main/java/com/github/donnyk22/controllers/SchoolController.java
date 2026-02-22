package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.AttendancesDto;
import com.github.donnyk22.models.dtos.ClassesDto;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.HomeroomTeachersDto;
import com.github.donnyk22.models.dtos.StudentsDto;
import com.github.donnyk22.models.dtos.TeachersDto;
import com.github.donnyk22.models.dtos.UsersDto;
import com.github.donnyk22.models.forms.attendances.AttendancesCreateForm;
import com.github.donnyk22.models.forms.attendances.AttendancesFindForm;
import com.github.donnyk22.models.forms.classes.ClassesCreateForm;
import com.github.donnyk22.models.forms.classes.ClassesFindForm;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersCreateForm;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersFindForm;
import com.github.donnyk22.models.forms.students.StudentsCreateForm;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.models.forms.teachers.TeachersCreateForm;
import com.github.donnyk22.models.forms.teachers.TeachersFindForm;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersFindForm;
import com.github.donnyk22.models.forms.users.UsersUpdatePasswordForm;
import com.github.donnyk22.services.school.SchoolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Tag(
    name = "School APIs",
    description = "School management APIs"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/school")
@Validated //for validating @RequestParam, @PathVariable, @RequestHeader
public class SchoolController {

    private final SchoolService schoolService;

    // === Attendances ===

    @Operation(
        summary = "Get student attendances",
        description = "Retrieve and search attendance records"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/attendances")
    public ResponseEntity<ApiResponse<FindResponse<AttendancesDto>>> findAttendances(@ModelAttribute @Valid AttendancesFindForm form) {
        FindResponse<AttendancesDto> result = schoolService.findAttendances(form);
        ApiResponse<FindResponse<AttendancesDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Attendances retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get student attendances",
        description = "Retrieve attendance records by attendance ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/attendances/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendancesDto>> readAttendance(@PathVariable @NotNull(message = "Attendance ID is required") Integer attendanceId) {
        AttendancesDto result = schoolService.readAttendance(attendanceId);
        ApiResponse<AttendancesDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Attendances retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create attendance record",
        description = "Create a new attendance record for a student"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @PostMapping("/attendances")
    public ResponseEntity<ApiResponse<AttendancesDto>> createAttendance(@RequestBody @Valid AttendancesCreateForm body) {
        AttendancesDto result = schoolService.createAttendance(body);
        ApiResponse<AttendancesDto> response = new ApiResponse<>(HttpStatus.CREATED.value(),
            "Attendance created successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete attendance record",
        description = "Delete an attendance record by attendance ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @DeleteMapping("/attendances/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendancesDto>> deleteAttendance(@PathVariable @NotNull(message = "Attendance ID is required") Integer attendanceId) {
        AttendancesDto result = schoolService.deleteAttendance(attendanceId);
        ApiResponse<AttendancesDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Attendance deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    // === Classes ===

    @Operation(
        summary = "Get classes",
        description = "Retrieve and search classes"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<FindResponse<ClassesDto>>> findClasses(@ModelAttribute @Valid ClassesFindForm form) {
        FindResponse<ClassesDto> result = schoolService.findClasses(form);
        ApiResponse<FindResponse<ClassesDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Classes retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get class by ID",
        description = "Retrieve class details by class ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/classes/{classesId}")
    public ResponseEntity<ApiResponse<ClassesDto>> readClass(@PathVariable @NotNull(message = "Class ID is required") Integer classesId) {
        ClassesDto result = schoolService.readClass(classesId);
        ApiResponse<ClassesDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Class retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create class",
        description = "Create a new class"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @PostMapping("/classes")
    public ResponseEntity<ApiResponse<ClassesDto>> createClass(@RequestBody @Valid ClassesCreateForm body) {
        ClassesDto result = schoolService.createClass(body);
        ApiResponse<ClassesDto> response = new ApiResponse<>(HttpStatus.CREATED.value(),
            "Class created successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update class",
        description = "Update class details by class ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @PostMapping("/classes/{classesId}")
    public ResponseEntity<ApiResponse<ClassesDto>> updateClass(@PathVariable @NotNull(message = "Class ID is required") Integer classesId, @RequestBody @Valid ClassesCreateForm body) {
        ClassesDto result = schoolService.updateClass(classesId, body);
        ApiResponse<ClassesDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Class updated successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete class",
        description = "Delete a class by class ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @DeleteMapping("/classes/{classesId}")
    public ResponseEntity<ApiResponse<ClassesDto>> deleteClass(@PathVariable @NotNull(message = "Class ID is required") Integer classesId) {
        ClassesDto result = schoolService.deleteClass(classesId);
        ApiResponse<ClassesDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Class deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    // === Students ===

    @Operation(
        summary = "Get students",
        description = "Retrieve and search students"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<FindResponse<StudentsDto>>> findStudents(@ModelAttribute @Valid StudentsFindForm form) {
        FindResponse<StudentsDto> result = schoolService.findStudents(form);
        ApiResponse<FindResponse<StudentsDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Students retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get student by ID",
        description = "Retrieve student details by student ID"
    )
    @GetMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<StudentsDto>> readStudent(@PathVariable @NotNull(message = "Student ID is required") Integer studentId) {
        StudentsDto result = schoolService.readStudent(studentId);
        ApiResponse<StudentsDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Student retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create student",
        description = "Create a new student with profile picture"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @PostMapping(
        value = "/students", 
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse<StudentsDto>> createStudent(
        @ModelAttribute @Valid StudentsCreateForm form,
        @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        StudentsDto result = schoolService.createStudent(form, image);
        return ResponseEntity.ok(new ApiResponse<>(
            HttpStatus.CREATED.value(),
            "Student created successfully",
            result
        ));
    }

    @Operation(
        summary = "Update student",
        description = "Update student details by student ID"
    )
    @PutMapping(
        value = "/students/{studentId}",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse<StudentsDto>> updateStudent(
        @PathVariable @NotNull(message = "Student ID is required") Integer studentId,
        @ModelAttribute @Valid StudentsCreateForm form,
        @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        StudentsDto result = schoolService.updateStudent(studentId, form, image);
        ApiResponse<StudentsDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Student updated successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete student",
        description = "Delete a student by student ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<StudentsDto>> deleteStudent(@PathVariable @NotNull(message = "Student ID is required") Integer studentId) {
        StudentsDto result = schoolService.deleteStudent(studentId);
        ApiResponse<StudentsDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Student deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete student profile picture",
        description = "Delete a student's profile picture by student ID"
    )
    @DeleteMapping("/students/{studentId}/profile-pic")
    public ResponseEntity<ApiResponse<StudentsDto>> deleteStudentProfilePic(@PathVariable @NotNull(message = "Student ID is required") Integer studentId) {
        StudentsDto result = schoolService.deleteStudentProfilePic(studentId);
        ApiResponse<StudentsDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Student profile picture deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    // === Teachers ===

    @Operation(
        summary = "Get teachers",
        description = "Retrieve and search teachers"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse<FindResponse<TeachersDto>>> findTeachers(@ModelAttribute @Valid TeachersFindForm form) {
        FindResponse<TeachersDto> result = schoolService.findTeachers(form);
        ApiResponse<FindResponse<TeachersDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Teachers retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get teacher by ID",
        description = "Retrieve teacher details by teacher ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<ApiResponse<TeachersDto>> readTeacher(@PathVariable @NotNull(message = "Teacher ID is required") Integer teacherId) {
        TeachersDto result = schoolService.readTeacher(teacherId);
        ApiResponse<TeachersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Teacher retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create teacher",
        description = "Create a new teacher with profile picture"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @PostMapping(
        value = "/teachers", 
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse<TeachersDto>> createTeacher(
        @ModelAttribute @Valid TeachersCreateForm form,
        @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        TeachersDto result = schoolService.createTeacher(form, image);
        return ResponseEntity.ok(new ApiResponse<>(
            HttpStatus.CREATED.value(),
            "Teacher created successfully",
            result
        ));
    }

    @Operation(
        summary = "Update teacher",
        description = "Update teacher details by teacher ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @PutMapping(
        value = "/teachers/{teacherId}",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse<TeachersDto>> updateTeacher(
        @PathVariable @NotNull(message = "Teacher ID is required") Integer teacherId,
        @ModelAttribute @Valid TeachersCreateForm form,
        @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        TeachersDto result = schoolService.updateTeacher(teacherId, form, image);
        ApiResponse<TeachersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Teacher updated successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete teacher",
        description = "Delete a teacher by teacher ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @DeleteMapping("/teachers/{teacherId}")
    public ResponseEntity<ApiResponse<TeachersDto>> deleteTeacher(@PathVariable @NotNull(message = "Teacher ID is required") Integer teacherId) {
        TeachersDto result = schoolService.deleteTeacher(teacherId);
        ApiResponse<TeachersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Teacher deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete teacher profile picture",
        description = "Delete a teacher's profile picture by teacher ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @DeleteMapping("/teachers/{teacherId}/profile-pic")
    public ResponseEntity<ApiResponse<TeachersDto>> deleteTeacherProfilePic(@PathVariable @NotNull(message = "Teacher ID is required") Integer teacherId) {
        TeachersDto result = schoolService.deleteTeacherProfilePic(teacherId);
        ApiResponse<TeachersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Teacher profile picture deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    // === Homeroom Teachers ===

    @Operation(
        summary = "Get homeroom teachers",
        description = "Retrieve and search homeroom teachers"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/homeroom-teachers")
    public ResponseEntity<ApiResponse<FindResponse<HomeroomTeachersDto>>> findHomeroomTeachers(@ModelAttribute @Valid HomeroomTeachersFindForm form) {
        FindResponse<HomeroomTeachersDto> result = schoolService.findHomeroomTeachers(form);
        ApiResponse<FindResponse<HomeroomTeachersDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Homeroom teachers retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get homeroom teacher by ID",
        description = "Retrieve homeroom teacher details by homeroom teacher ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @GetMapping("/homeroom-teachers/{homeroomTeacherId}")
    public ResponseEntity<ApiResponse<HomeroomTeachersDto>> readHomeroomTeacher(@PathVariable @NotNull(message = "Homeroom teacher ID is required") Integer homeroomTeacherId) {
        HomeroomTeachersDto result = schoolService.readHomeroomTeacher(homeroomTeacherId);
        ApiResponse<HomeroomTeachersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Homeroom teacher retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create homeroom teacher",
        description = "Create a new homeroom teacher"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @PostMapping("/homeroom-teachers")
    public ResponseEntity<ApiResponse<HomeroomTeachersDto>> createHomeroomTeacher(@RequestBody @Valid HomeroomTeachersCreateForm body) {
        HomeroomTeachersDto result = schoolService.createHomeroomTeacher(body);
        ApiResponse<HomeroomTeachersDto> response = new ApiResponse<>(HttpStatus.CREATED.value(),
            "Homeroom teacher created successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete homeroom teacher",
        description = "Delete a homeroom teacher by homeroom teacher ID"
    )
    @PreAuthorize("hasAnyRole(UserRole.ADMIN, UserRole.TEACHER)")
    @DeleteMapping("/homeroom-teachers/{homeroomTeacherId}")
    public ResponseEntity<ApiResponse<HomeroomTeachersDto>> deleteHomeroomTeacher(@PathVariable @NotNull(message = "Homeroom teacher ID is required") Integer homeroomTeacherId) {
        HomeroomTeachersDto result = schoolService.deleteHomeroomTeacher(homeroomTeacherId);
        ApiResponse<HomeroomTeachersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Homeroom teacher deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    // === Users ===

    @Operation(
        summary = "Get users",
        description = "Retrieve and search users"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<FindResponse<UsersDto>>> findUsers(@ModelAttribute @Valid UsersFindForm form) {
        FindResponse<UsersDto> result = schoolService.findUsers(form);
        ApiResponse<FindResponse<UsersDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Users retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieve user details by user ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UsersDto>> readUser(@PathVariable @NotNull(message = "User ID is required") Integer userId) {
        UsersDto result = schoolService.readUser(userId);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "User retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create user",
        description = "Create a new user with profile picture"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @PostMapping(
        value = "/users", 
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse<UsersDto>> createUser(
        @ModelAttribute @Valid UsersCreateForm form,
        @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        UsersDto result = schoolService.createUser(form, image);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.CREATED.value(),
            "User created successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update user",
        description = "Update user details by user ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @PutMapping(
        value = "/users/{userId}",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ApiResponse<UsersDto>> updateUser(
        @PathVariable @NotNull(message = "User ID is required") Integer userId,
        @ModelAttribute @Valid UsersCreateForm form,
        @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        UsersDto result = schoolService.updateUser(userId, form, image);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "User updated successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update user password",
        description = "Update user password by user ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<ApiResponse<UsersDto>> updateUserPassword(
        @PathVariable @NotNull(message = "User ID is required") Integer userId,
        @ModelAttribute @Valid UsersUpdatePasswordForm form
    ) {
        UsersDto result = schoolService.updateUserPassword(userId, form);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "User password updated successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete user",
        description = "Delete a user by user ID"
    )
    @PreAuthorize("hasRole(UserRole.ADMIN)")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UsersDto>> deleteUser(@PathVariable @NotNull(message = "User ID is required") Integer userId) {
        UsersDto result = schoolService.deleteUser(userId);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "User deleted successfully",
            result);
        return ResponseEntity.ok(response);
    }
}
