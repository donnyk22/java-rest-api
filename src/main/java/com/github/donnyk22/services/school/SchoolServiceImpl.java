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
import org.springframework.util.StringUtils;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.ConflictException;
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
import com.github.donnyk22.models.enums.UserRole;
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
import com.github.donnyk22.utils.Util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService{

    private final AttendancesRepository attendancesRepository;
    private final ClassesRepository classesRepository;
    private final StudentsRepository studentsRepository;
    private final TeachersRepository teachersRepository;
    private final HomeroomTeachersRepository homeroomTeachersRepository;
    private final UsersRepository usersRepository;
    private final FileUtil fileUtil;

    // === Attendances ===

    @Override
    public FindResponse<AttendancesDto> findAttendances(AttendancesFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Attendances> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            Join<Attendances, Students> studentJoin = root.join("studentData", JoinType.LEFT);
            Join<Students, Classes> classJoin = studentJoin.join("classroom", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("status")), likePattern),
                    cb.like(cb.lower(root.get("note")), likePattern),
                    cb.like(cb.lower(studentJoin.get("fullName")), likePattern),
                    cb.like(cb.lower(classJoin.get("className")), likePattern),
                    cb.like(cb.lower(classJoin.get("gradeLevel")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academicYear")), likePattern);
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
                    cb.like(cb.lower(root.get("className")), likePattern),
                    cb.like(cb.lower(root.get("gradeLevel")), likePattern),
                    cb.like(cb.lower(root.get("academicYear")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(root.get("academicYear")), likePattern);
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Classes> result = classesRepository.findAll(spec, pageable);
        return new FindResponse<ClassesDto>()
            .setRecords(result.getContent().stream().map(ClassesMapper::toBaseDtoWithHomeroomTeachers).toList())
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
        Classes classes = ClassesMapper.toEntity(body);
        return ClassesMapper.toBaseDto(classesRepository.save(classes));
    }

    @Override
    @CachePut(value = "classes", key = "#id")
    public ClassesDto updateClass(Integer id, ClassesUpdateForm body) {
        Classes classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        Util.compareVersion(classes.getVersion(), body.getVersion());
        classes = ClassesMapper.toEntity(classes, body);
        return ClassesMapper.toDto(classesRepository.saveAndFlush(classes));
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

            Join<Students, Classes> classJoin = root.join("classroom", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("fullName")), likePattern),
                    cb.like(cb.lower(root.get("address")), likePattern),
                    cb.like(cb.lower(classJoin.get("className")), likePattern),
                    cb.like(cb.lower(classJoin.get("gradeLevel")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academicYear")), likePattern);
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
    public StudentsDto createStudent(StudentsCreateForm form) {
        if (form.getUserId() != null) {
            Users user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + form.getUserId()));
            if (user.getStudentData() != null) {
                throw new ConflictException("User is already associated with a student: " + form.getUserId());
            }
            if (user.getTeacherData() != null) {
                throw new ConflictException("User is already associated with a teacher: " + form.getUserId());
            }
        }
        Students student = StudentsMapper.toEntity(form, fileUtil.saveProfilePic(form.getPhoto()));
        return StudentsMapper.toBaseDto(studentsRepository.save(student));
    }

    @Override
    @CachePut(value = "student", key = "#id")
    public StudentsDto updateStudent(Integer id, StudentsUpdateForm form) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        Util.compareVersion(student.getVersion(), form.getVersion());
        if (form.getUserId() != null) {
            Users user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + form.getUserId()));
            if (user.getStudentData() != null && !user.getStudentData().getId().equals(id)) {
                throw new ConflictException("User is already associated with a student: " + form.getUserId());
            }
            if (user.getTeacherData() != null && !user.getTeacherData().getId().equals(id)) {
                throw new ConflictException("User is already associated with a teacher: " + form.getUserId());
            }
            if (!user.getRole().equals(UserRole.STUDENT.name())){
                throw new BadRequestException("User role must be student: " + form.getUserId());
            }
        }

        String oldPhotoPath = student.getPhoto();
        String newPhotoPath = oldPhotoPath;
        boolean isNewPhotoUploaded = false;

        if (form.getPhoto() != null && !form.getPhoto().isEmpty()) {
            newPhotoPath = fileUtil.saveProfilePic(form.getPhoto());
            isNewPhotoUploaded = true;
        }

        try {
            student = StudentsMapper.toEntity(student, form, newPhotoPath);
            StudentsDto result = StudentsMapper.toDto(studentsRepository.saveAndFlush(student));
            if (isNewPhotoUploaded) {
                fileUtil.deleteProfilePic(oldPhotoPath);
            }
            return result;
        } catch (Exception e) {
            if (isNewPhotoUploaded) {
                fileUtil.deleteProfilePic(newPhotoPath);
            }
            throw new BadRequestException("Failed to update student data: " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "student", key = "#id")
    public StudentsDto deleteStudent(Integer id) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        studentsRepository.deleteById(id);
        return StudentsMapper.toBaseDto(student);
    }

    @Override
    @CachePut(value = "student", key = "#id")
    public StudentsDto deleteStudentProfilePic(Integer id) {
        Students student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        if (!StringUtils.hasLength(student.getPhoto())) {
            throw new ResourceNotFoundException("Student has no profile picture");
        }
        String oldPhotoPath = student.getPhoto();
        student.setPhoto(null);
        StudentsDto result = StudentsMapper.toDto(studentsRepository.saveAndFlush(student));
        fileUtil.deleteProfilePic(oldPhotoPath);
        return result;
    }

    // === Teachers ===

    @Override
    public FindResponse<TeachersDto> findTeachers(TeachersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<Teachers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Teachers, HomeroomTeachers> homeroomJoin = root.join("homeroomTeachers", JoinType.LEFT);
            Join<HomeroomTeachers, Classes> classJoin = homeroomJoin.join("classData", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("fullName")), likePattern),
                    cb.like(cb.lower(root.get("phone")), likePattern),
                    cb.like(cb.lower(root.get("address")), likePattern),
                    cb.like(cb.lower(classJoin.get("className")), likePattern),
                    cb.like(cb.lower(classJoin.get("gradeLevel")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academicYear")), likePattern);
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
    public TeachersDto createTeacher(TeachersCreateForm form) {
        if (form.getUserId() != null) {
            Users user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + form.getUserId()));
            if (user.getStudentData() != null) {
                throw new ConflictException("User is already associated with a student: " + form.getUserId());
            }
            if (user.getTeacherData() != null) {
                throw new ConflictException("User is already associated with a teacher: " + form.getUserId());
            }
        }
        Teachers teacher = TeachersMapper.toEntity(form, fileUtil.saveProfilePic(form.getPhoto()));
        return TeachersMapper.toBaseDto(teachersRepository.save(teacher));
    }

    @Override
    @CachePut(value = "teacher", key = "#id")
    public TeachersDto updateTeacher(Integer id, TeachersUpdateForm form) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        Util.compareVersion(teacher.getVersion(), form.getVersion());
        if (form.getUserId() != null) {
            Users user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + form.getUserId()));
            if (user.getTeacherData() != null && !user.getTeacherData().getId().equals(id)) {
                throw new ConflictException("User is already associated with a student: " + form.getUserId());
            }
            if (user.getTeacherData() != null && !user.getTeacherData().getId().equals(id)) {
                throw new ConflictException("User is already associated with a teacher: " + form.getUserId());
            }
            if (!user.getRole().equals(UserRole.TEACHER.name())){
                throw new BadRequestException("User role must be teacher: " + form.getUserId());
            }
        }

        String oldPhotoPath = teacher.getPhoto();
        String newPhotoPath = oldPhotoPath;
        boolean isNewPhotoUploaded = false;

        if (form.getPhoto() != null && !form.getPhoto().isEmpty()) {
            newPhotoPath = fileUtil.saveProfilePic(form.getPhoto());
            isNewPhotoUploaded = true;
        }

        try {
            teacher = TeachersMapper.toEntity(teacher, form, newPhotoPath);
            TeachersDto result = TeachersMapper.toDto(teachersRepository.saveAndFlush(teacher));
            if (isNewPhotoUploaded) {
                fileUtil.deleteProfilePic(oldPhotoPath);
            }
            return result;
        } catch (Exception e) {
            if (isNewPhotoUploaded) {
                fileUtil.deleteProfilePic(newPhotoPath);
            }
            throw new BadRequestException("Failed to update teacher data: " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "teacher", key = "#id")
    public TeachersDto deleteTeacher(Integer id) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        teachersRepository.deleteById(id);
        return TeachersMapper.toBaseDto(teacher);
    }

    @Override
    @CachePut(value = "teacher", key = "#id")
    public TeachersDto deleteTeacherProfilePic(Integer id) {
        Teachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        if (!StringUtils.hasLength(teacher.getPhoto())) {
            throw new ResourceNotFoundException("Teacher has no profile picture");
        }
        String oldPhotoPath = teacher.getPhoto();
        teacher.setPhoto(null);
        TeachersDto result = TeachersMapper.toDto(teachersRepository.saveAndFlush(teacher));
        fileUtil.deleteProfilePic(oldPhotoPath);
        return result;
    }

    // === Homeroom Teachers ===

    @Override
    public FindResponse<HomeroomTeachersDto> findHomeroomTeachers(HomeroomTeachersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<HomeroomTeachers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<HomeroomTeachers, Classes> classJoin = root.join("classData", JoinType.LEFT);
            Join<HomeroomTeachers, Teachers> teacherJoin = root.join("teacherData", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(classJoin.get("className")), likePattern),
                    cb.like(cb.lower(classJoin.get("gradeLevel")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("fullName")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("phone")), likePattern),
                    cb.like(cb.lower(teacherJoin.get("address")), likePattern)
                );
                predicates.add(predicate);
            }

            if (StringUtils.hasText(form.getAcademicYear())){
                String likePattern = "%" + form.getAcademicYear().toLowerCase() + "%";
                Predicate predicate = cb.like(cb.lower(classJoin.get("academicYear")), likePattern);
                predicates.add(predicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<HomeroomTeachers> result = homeroomTeachersRepository.findAll(spec, pageable);
        return new FindResponse<HomeroomTeachersDto>()
            .setRecords(result.getContent().stream().map(HomeroomTeachersMapper::toDto).toList())
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
        HomeroomTeachers homeroomTeachers = homeroomTeachersRepository.findByClassIdAndTeacherId(body.getClassId(), body.getTeacherId());
        if (homeroomTeachers != null) {
            throw new BadRequestException("The teacher already assigned to this class");
        }
        homeroomTeachers = HomeroomTeachersMapper.toEntity(body);
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
    public UsersDto createUser(UsersCreateForm form) {
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        Users user = UsersMapper.toEntity(form, MediaUtil.ToBase64(form.getPhoto()), new BCryptPasswordEncoder().encode(form.getPassword()));
        return UsersMapper.toBaseDto(usersRepository.save(user));
    }

    @Override
    @SneakyThrows
    @CachePut(value = "user", key = "#id")
    public UsersDto updateUser(Integer id, UsersUpdateForm form) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        Util.compareVersion(user.getVersion(), form.getVersion());
        user = UsersMapper.toEntity(user, form, MediaUtil.ToBase64(form.getPhoto()));
        return UsersMapper.toDto(usersRepository.saveAndFlush(user));
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
