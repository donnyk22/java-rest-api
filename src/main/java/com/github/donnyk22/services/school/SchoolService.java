package com.github.donnyk22.services.school;

import com.github.donnyk22.models.dtos.MstAttendancesDto;
import com.github.donnyk22.models.dtos.MstClassesDto;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstHomeroomTeachersDto;
import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.dtos.MstTeachersDto;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.forms.attendances.AttendancesCreateForm;
import com.github.donnyk22.models.forms.attendances.AttendancesFindForm;
import com.github.donnyk22.models.forms.classes.ClassesCreateForm;
import com.github.donnyk22.models.forms.classes.ClassesFindForm;
import com.github.donnyk22.models.forms.classes.ClassesUpdateForm;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersCreateForm;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersFindForm;
import com.github.donnyk22.models.forms.students.StudentsCreateForm;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.models.forms.students.StudentsUpdateForm;
import com.github.donnyk22.models.forms.teachers.TeachersCreateForm;
import com.github.donnyk22.models.forms.teachers.TeachersFindForm;
import com.github.donnyk22.models.forms.teachers.TeachersUpdateForm;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersFindForm;
import com.github.donnyk22.models.forms.users.UsersUpdateForm;
import com.github.donnyk22.models.forms.users.UsersUpdatePasswordForm;

public interface SchoolService {
    // === Attendances ===
    FindResponse<MstAttendancesDto> findAttendances(AttendancesFindForm form);
    MstAttendancesDto readAttendance(Integer attendanceId);
    MstAttendancesDto createAttendance(AttendancesCreateForm body);
    MstAttendancesDto deleteAttendance(Integer attendanceId);

    // === Classes ===
    FindResponse<MstClassesDto> findClasses(ClassesFindForm form);
    MstClassesDto readClass(Integer id);
    MstClassesDto createClass(ClassesCreateForm body);
    MstClassesDto updateClass(Integer id, ClassesUpdateForm body);
    MstClassesDto deleteClass(Integer id);

    // === Students ===
    FindResponse<MstStudentsDto> findStudents(StudentsFindForm form);
    MstStudentsDto readStudent(Integer id);
    MstStudentsDto createStudent(StudentsCreateForm form);
    MstStudentsDto updateStudent(Integer id, StudentsUpdateForm form);
    MstStudentsDto deleteStudent(Integer id);
    MstStudentsDto deleteStudentProfilePic(Integer id);

    // === Teachers ===
    FindResponse<MstTeachersDto> findTeachers(TeachersFindForm form);
    MstTeachersDto readTeacher(Integer id);
    MstTeachersDto createTeacher(TeachersCreateForm form);
    MstTeachersDto updateTeacher(Integer id, TeachersUpdateForm form);
    MstTeachersDto deleteTeacher(Integer id);
    MstTeachersDto deleteTeacherProfilePic(Integer id);

    // === Homeroom Teachers ===
    FindResponse<MstHomeroomTeachersDto> findHomeroomTeachers(HomeroomTeachersFindForm form);
    MstHomeroomTeachersDto readHomeroomTeacher(Integer id);
    MstHomeroomTeachersDto createHomeroomTeacher(HomeroomTeachersCreateForm body);
    MstHomeroomTeachersDto deleteHomeroomTeacher(Integer id);

    // === Users ===
    FindResponse<MstUsersDto> findUsers(UsersFindForm form);
    MstUsersDto readUser(Integer id);
    MstUsersDto createUser(UsersCreateForm form);
    MstUsersDto updateUser(Integer id, UsersUpdateForm form);
    MstUsersDto updateUserPassword(Integer id, UsersUpdatePasswordForm form);
    MstUsersDto deleteUser(Integer id);
}
