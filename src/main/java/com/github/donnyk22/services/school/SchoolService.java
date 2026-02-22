package com.github.donnyk22.services.school;

import org.springframework.web.multipart.MultipartFile;

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
import com.github.donnyk22.models.forms.users.UsersUpdateForm;
import com.github.donnyk22.models.forms.users.UsersUpdatePasswordForm;

public interface SchoolService {
    // === Attendances ===
    FindResponse<AttendancesDto> findAttendances(AttendancesFindForm form);
    AttendancesDto readAttendance(Integer attendanceId);
    AttendancesDto createAttendance(AttendancesCreateForm body);
    AttendancesDto deleteAttendance(Integer attendanceId);

    // === Classes ===
    FindResponse<ClassesDto> findClasses(ClassesFindForm form);
    ClassesDto readClass(Integer id);
    ClassesDto createClass(ClassesCreateForm body);
    ClassesDto updateClass(Integer id, ClassesCreateForm body);
    ClassesDto deleteClass(Integer id);

    // === Students ===
    FindResponse<StudentsDto> findStudents(StudentsFindForm form);
    StudentsDto readStudent(Integer id);
    StudentsDto createStudent(StudentsCreateForm form, MultipartFile photo);
    StudentsDto updateStudent(Integer id, StudentsCreateForm form, MultipartFile photo);
    StudentsDto deleteStudent(Integer id);
    StudentsDto deleteStudentProfilePic(Integer id);

    // === Teachers ===
    FindResponse<TeachersDto> findTeachers(TeachersFindForm form);
    TeachersDto readTeacher(Integer id);
    TeachersDto createTeacher(TeachersCreateForm form, MultipartFile photo);
    TeachersDto updateTeacher(Integer id, TeachersCreateForm form, MultipartFile photo);
    TeachersDto deleteTeacher(Integer id);
    TeachersDto deleteTeacherProfilePic(Integer id);

    // === Homeroom Teachers ===
    FindResponse<HomeroomTeachersDto> findHomeroomTeachers(HomeroomTeachersFindForm form);
    HomeroomTeachersDto readHomeroomTeacher(Integer id);
    HomeroomTeachersDto createHomeroomTeacher(HomeroomTeachersCreateForm body);
    HomeroomTeachersDto deleteHomeroomTeacher(Integer id);

    // === Users ===
    FindResponse<UsersDto> findUsers(UsersFindForm form);
    UsersDto readUser(Integer id);
    UsersDto createUser(UsersCreateForm form, MultipartFile photo);
    UsersDto updateUser(Integer id, UsersUpdateForm form, MultipartFile photo);
    UsersDto updateUserPassword(Integer id, UsersUpdatePasswordForm form);
    UsersDto deleteUser(Integer id);
}
