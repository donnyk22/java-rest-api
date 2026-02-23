package com.github.donnyk22.services.school;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.ResourceNotFoundException;
import com.github.donnyk22.models.dtos.AttendancesDto;
import com.github.donnyk22.models.dtos.ClassesDto;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.HomeroomTeachersDto;
import com.github.donnyk22.models.dtos.StudentsDto;
import com.github.donnyk22.models.dtos.TeachersDto;
import com.github.donnyk22.models.dtos.UsersDto;
import com.github.donnyk22.models.entities.Attendances;
import com.github.donnyk22.models.entities.Classes;
import com.github.donnyk22.models.entities.HomeroomTeachers;
import com.github.donnyk22.models.entities.Students;
import com.github.donnyk22.models.entities.Teachers;
import com.github.donnyk22.models.entities.Users;
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
import com.github.donnyk22.models.mappers.AttendancesMapper;
import com.github.donnyk22.models.mappers.ClassesMapper;
import com.github.donnyk22.models.mappers.HomeroomTeachersMapper;
import com.github.donnyk22.models.mappers.StudentsMapper;
import com.github.donnyk22.models.mappers.TeachersMapper;
import com.github.donnyk22.models.mappers.UsersMapper;
import com.github.donnyk22.repositories.AttendancesRepository;
import com.github.donnyk22.repositories.ClassesRepository;
import com.github.donnyk22.repositories.HomeroomTeachersRepository;
import com.github.donnyk22.repositories.StudentsRepository;
import com.github.donnyk22.repositories.TeachersRepository;
import com.github.donnyk22.repositories.UsersRepository;
import com.github.donnyk22.utils.FileUtil;
import com.github.donnyk22.utils.MediaUtil;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Service
@Transactional
@AllArgsConstructor
public class SchoolServiceImpl implements SchoolService{

    private final AttendancesRepository attendancesRepository;
    private final ClassesRepository classesRepository;
    private final StudentsRepository studentsRepository;
    private final TeachersRepository teachersRepository;
    private final HomeroomTeachersRepository homeroomTeachersRepository;
    private final UsersRepository usersRepository;

    // === Attendances ===

    @Override
    public FindResponse<AttendancesDto> findAttendances(AttendancesFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Attendances> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            Join<Attendances, Students> studentJoin = root.join("students", JoinType.LEFT);
            Join<Students, Classes> classJoin = studentJoin.join("classes", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("status")), likePattern),
                    cb.like(cb.lower(root.get("note")), likePattern),
                    cb.like(cb.lower(studentJoin.get("full_name")), likePattern),
                    cb.like(cb.lower(classJoin.get("class_name")), likePattern),
                    cb.like(cb.lower(classJoin.get("grade_level")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academic_year")), likePattern);
                predicates.add(predicate);
            }

            if (form.getStartRangeDate() != null && form.getEndRangeDate() != null) {
                Predicate predicate = cb.between(root.get("date"), form.getStartRangeDate(), form.getEndRangeDate());
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Attendances> result = attendancesRepository.findAll(spec, pageable);
        return new FindResponse<AttendancesDto>()
            .setRecords(result.getContent().stream().map(AttendancesMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "attendance", key = "#id")
    public AttendancesDto readAttendance(Integer id) {
        Attendances attendance = attendancesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found: " + id));
        return AttendancesMapper.toDto(attendance);
    }

    @Override
    public AttendancesDto createAttendance(AttendancesCreateForm body) {
        Attendances attendance = AttendancesMapper.toEntity(new Attendances(), body);
        return AttendancesMapper.toBaseDto(attendancesRepository.save(attendance));
    }

    @Override
    @CacheEvict(value = "attendance", key = "#id")
    public AttendancesDto deleteAttendance(Integer id) {
        Attendances attendance = attendancesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found: " + id));
        attendancesRepository.deleteById(id);
        return AttendancesMapper.toBaseDto(attendance);
    }

    // === Classes ===

    @Override
    public FindResponse<ClassesDto> findClasses(ClassesFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Classes> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("class_name")), likePattern),
                    cb.like(cb.lower(root.get("grade_level")), likePattern),
                    cb.like(cb.lower(root.get("academic_year")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(root.get("academic_year")), likePattern);
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Classes> result = classesRepository.findAll(spec, pageable);
        return new FindResponse<ClassesDto>()
            .setRecords(result.getContent().stream().map(ClassesMapper::toBaseDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())            
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "classes", key = "#id")
    public ClassesDto readClass(Integer id) {
        Classes classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        return ClassesMapper.toDto(classes);
    }

    @Override
    public ClassesDto createClass(ClassesCreateForm body) {
        Classes classes = ClassesMapper.toEntity(new Classes(), body);
        return ClassesMapper.toBaseDto(classesRepository.save(classes));
    }

    @Override
    @CachePut(value = "classes", key = "#id")
    public ClassesDto updateClass(Integer id, ClassesCreateForm body) {
        Classes classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        classes = ClassesMapper.toEntity(classes, body);
        return ClassesMapper.toDto(classesRepository.save(classes));
    }

    @Override
    @CacheEvict(value = "classes", key = "#id")
    public ClassesDto deleteClass(Integer id) {
        Classes classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        classesRepository.deleteById(id);
        return ClassesMapper.toBaseDto(classes);
    }

    // === Students ===

    @Override
    public FindResponse<StudentsDto> findStudents(StudentsFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Students> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Students, Classes> classJoin = root.join("classes", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("full_name")), likePattern),
                    cb.like(cb.lower(root.get("address")), likePattern),
                    cb.like(cb.lower(classJoin.get("class_name")), likePattern),
                    cb.like(cb.lower(classJoin.get("grade_level")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academic_year")), likePattern);
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Students> result = studentsRepository.findAll(spec, pageable);
        return new FindResponse<StudentsDto>()
            .setRecords(result.getContent().stream().map(StudentsMapper::toBaseDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "student", key = "#id")
    public StudentsDto readStudent(Integer id) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        return StudentsMapper.toDto(student);
    }

    @Override
    public StudentsDto createStudent(StudentsCreateForm form, MultipartFile photo) {
        form.setPhoto(FileUtil.saveProfilePic(photo));
        Students student = StudentsMapper.toEntity(new Students(), form);
        return StudentsMapper.toBaseDto(studentsRepository.save(student));
    }

    @Override
    @CachePut(value = "student", key = "#id")
    public StudentsDto updateStudent(Integer id, StudentsCreateForm form, MultipartFile photo) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        if (photo != null) {
            form.setPhoto(FileUtil.saveProfilePic(photo));
        } else {
            form.setPhoto(student.getPhoto());
        }
        student = StudentsMapper.toEntity(student, form);
        return StudentsMapper.toDto(studentsRepository.save(student));
    }

    @Override
    @CacheEvict(value = "student", key = "#id")
    public StudentsDto deleteStudent(Integer id) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        FileUtil.deleteProfilePic(student.getPhoto());
        studentsRepository.deleteById(id);
        return StudentsMapper.toBaseDto(student);
    }

    @Override
    @CachePut(value = "student", key = "#id")
    public StudentsDto deleteStudentProfilePic(Integer id) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        FileUtil.deleteProfilePic(student.getPhoto());
        student.setPhoto(null);
        return StudentsMapper.toDto(studentsRepository.save(student));
    }

    // === Teachers ===

    @Override
    public FindResponse<TeachersDto> findTeachers(TeachersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Teachers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Teachers, HomeroomTeachers> homeroomJoin = root.join("homeroom_teachers", JoinType.LEFT);
            Join<HomeroomTeachers, Classes> classJoin = homeroomJoin.join("classes", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("full_name")), likePattern),
                    cb.like(cb.lower(root.get("phone")), likePattern),
                    cb.like(cb.lower(root.get("address")), likePattern),
                    cb.like(cb.lower(classJoin.get("class_name")), likePattern),
                    cb.like(cb.lower(classJoin.get("grade_level")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academic_year")), likePattern);
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Teachers> result = teachersRepository.findAll(spec, pageable);
        return new FindResponse<TeachersDto>()
            .setRecords(result.getContent().stream().map(TeachersMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "teacher", key = "#id")
    public TeachersDto readTeacher(Integer id) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        return TeachersMapper.toDto(teacher);
    }

    @Override
    public TeachersDto createTeacher(TeachersCreateForm form, MultipartFile photo) {
        form.setPhoto(FileUtil.saveProfilePic(photo));
        Teachers teacher = TeachersMapper.toEntity(new Teachers(), form);
        return TeachersMapper.toBaseDto(teachersRepository.save(teacher));
    }

    @Override
    @CachePut(value = "teacher", key = "#id")
    public TeachersDto updateTeacher(Integer id, TeachersCreateForm form, MultipartFile photo) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        if (photo != null) {
            form.setPhoto(FileUtil.saveProfilePic(photo));
        } else {
            form.setPhoto(teacher.getPhoto());
        }
        teacher = TeachersMapper.toEntity(teacher, form);
        return TeachersMapper.toDto(teachersRepository.save(teacher));
    }

    @Override
    @CacheEvict(value = "teacher", key = "#id")
    public TeachersDto deleteTeacher(Integer id) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        FileUtil.deleteProfilePic(teacher.getPhoto());
        teachersRepository.deleteById(id);
        return TeachersMapper.toBaseDto(teacher);
    }

    @Override
    @CachePut(value = "teacher", key = "#id")
    public TeachersDto deleteTeacherProfilePic(Integer id) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        FileUtil.deleteProfilePic(teacher.getPhoto());
        teacher.setPhoto(null);
        return TeachersMapper.toDto(teachersRepository.save(teacher));
    }

    // === Homeroom Teachers ===

    @Override
    public FindResponse<HomeroomTeachersDto> findHomeroomTeachers(HomeroomTeachersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<HomeroomTeachers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<HomeroomTeachers, Classes> classJoin = root.join("classes", JoinType.LEFT);
            Join<HomeroomTeachers, Teachers> teacherJoin = root.join("teachers", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(classJoin.get("class_name")), likePattern),
                    cb.like(cb.lower(classJoin.get("grade_level")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("full_name")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("phone")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("address")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academic_year")), likePattern);
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<HomeroomTeachers> result = homeroomTeachersRepository.findAll(spec, pageable);
        return new FindResponse<HomeroomTeachersDto>()
            .setRecords(result.getContent().stream().map(HomeroomTeachersMapper::toBaseDtoWithTeacher).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "homeroomTeacher", key = "#id")
    public HomeroomTeachersDto readHomeroomTeacher(Integer id) {
        HomeroomTeachers homeroomTeachers = homeroomTeachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found: " + id));
        return HomeroomTeachersMapper.toDto(homeroomTeachers);
    }

    @Override
    public HomeroomTeachersDto createHomeroomTeacher(HomeroomTeachersCreateForm body) {
        HomeroomTeachers homeroomTeachers = HomeroomTeachersMapper.toEntity(body);
        return HomeroomTeachersMapper.toBaseDto(homeroomTeachersRepository.save(homeroomTeachers));
    }

    @Override
    @CachePut(value = "homeroomTeacher", key = "#id")
    public HomeroomTeachersDto deleteHomeroomTeacher(Integer id) {
        HomeroomTeachers homeroomTeachers = homeroomTeachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found: " + id));
        homeroomTeachersRepository.deleteById(id);
        return HomeroomTeachersMapper.toBaseDto(homeroomTeachers);
    }

    // === Users ===

    @Override
    public FindResponse<UsersDto> findUsers(UsersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Users> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Users, Teachers> teacherJoin = root.join("teacherData", JoinType.LEFT);
            Join<Users, Students> studentJoin = root.join("studentData", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("username")), likePattern),
                    cb.like(cb.lower(root.get("email")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("fullName")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("phone")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("address")), likePattern),
                    cb.like(cb.lower(studentJoin.get("fullName")), likePattern),
                    cb.like(cb.lower(studentJoin.get("address")), likePattern)                    
                );
                predicates.add(predicate);
            }
            
            if (StringUtils.hasText(form.getRole())) {
                predicates.add(cb.equal(root.get("role"), form.getRole()));
            }
            
            Boolean isActive = form.getIsActive();
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
            
        };
        Page<Users> result = usersRepository.findAll(spec, pageable);
        return new FindResponse<UsersDto>()
            .setRecords(result.getContent().stream().map(UsersMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public UsersDto readUser(Integer id) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return UsersMapper.toDto(user);
    }

    @Override
    @SneakyThrows
    public UsersDto createUser(UsersCreateForm form, MultipartFile image) {
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        form.setPhoto(MediaUtil.ToBase64(image));
        Users user = UsersMapper.toCreateUserEntity(form, new BCryptPasswordEncoder().encode(form.getPassword()));
        return UsersMapper.toBaseDto(usersRepository.save(user));
    }

    @Override
    @SneakyThrows
    @CachePut(value = "user", key = "#id")
    public UsersDto updateUser(Integer id, UsersUpdateForm form, MultipartFile image) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        form.setPhoto(MediaUtil.ToBase64(image));
        user = UsersMapper.toUpdateUserEntity(user, form);
        return UsersMapper.toDto(usersRepository.save(user));
    }

    @Override
    public UsersDto updateUserPassword(Integer id, UsersUpdatePasswordForm form) {
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        return UsersMapper.toBaseDto(usersRepository.save(user));
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public UsersDto deleteUser(Integer id) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        usersRepository.deleteById(id);
        return UsersMapper.toBaseDto(user);
    }
    
}
