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
import com.github.donnyk22.models.dtos.MstAttendancesDto;
import com.github.donnyk22.models.dtos.MstClassesDto;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstHomeroomTeachersDto;
import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.dtos.MstTeachersDto;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstAttendances;
import com.github.donnyk22.models.entities.MstClasses;
import com.github.donnyk22.models.entities.MstHomeroomTeachers;
import com.github.donnyk22.models.entities.MstStudents;
import com.github.donnyk22.models.entities.MstTeachers;
import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.enums.Method;
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
import com.github.donnyk22.models.mappers.MstAttendancesMapper;
import com.github.donnyk22.models.mappers.MstClassesMapper;
import com.github.donnyk22.models.mappers.MstHomeroomTeachersMapper;
import com.github.donnyk22.models.mappers.MstStudentsMapper;
import com.github.donnyk22.models.mappers.MstTeachersMapper;
import com.github.donnyk22.models.mappers.MstUsersMapper;
import com.github.donnyk22.repositories.MstAttendancesRepository;
import com.github.donnyk22.repositories.MstClassesRepository;
import com.github.donnyk22.repositories.MstHomeroomTeachersRepository;
import com.github.donnyk22.repositories.MstStudentsRepository;
import com.github.donnyk22.repositories.MstTeachersRepository;
import com.github.donnyk22.repositories.MstUsersRepository;
import com.github.donnyk22.services.audittrails.AuditTrailsService;
import com.github.donnyk22.utils.EntityUtil;
import com.github.donnyk22.utils.FileUtil;
import com.github.donnyk22.utils.MediaUtil;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService{

    private final MstAttendancesRepository attendancesRepository;
    private final MstClassesRepository classesRepository;
    private final MstStudentsRepository studentsRepository;
    private final MstTeachersRepository teachersRepository;
    private final MstHomeroomTeachersRepository homeroomTeachersRepository;
    private final MstUsersRepository usersRepository;
    private final AuditTrailsService auditTrailsService;
    private final FileUtil fileUtil;

    // === Attendances ===

    @Override
    public FindResponse<MstAttendancesDto> findAttendances(AttendancesFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<MstAttendances> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            Join<MstAttendances, MstStudents> studentJoin = root.join("studentData", JoinType.LEFT);
            Join<MstStudents, MstClasses> classJoin = studentJoin.join("classroom", JoinType.LEFT);

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

        Page<MstAttendances> result = attendancesRepository.findAll(spec, pageable);
        return new FindResponse<MstAttendancesDto>()
            .setRecords(result.getContent().stream().map(MstAttendancesMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "attendance", key = "#id")
    public MstAttendancesDto readAttendance(Integer id) {
        MstAttendances attendance = attendancesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found: " + id));
        return MstAttendancesMapper.toDto(attendance);
    }

    @Override
    public MstAttendancesDto createAttendance(AttendancesCreateForm body) {
        MstAttendances attendance = MstAttendancesMapper.toEntity(new MstAttendances(), body);
        attendance = attendancesRepository.save(attendance);
        auditTrailsService.create(Method.POST, attendance, "Create attendance record");
        return MstAttendancesMapper.toBaseDto(attendance);
    }

    @Override
    @CacheEvict(value = "attendance", key = "#id")
    public MstAttendancesDto deleteAttendance(Integer id) {
        MstAttendances attendance = attendancesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found: " + id));
        attendancesRepository.deleteById(id);
        auditTrailsService.create(Method.DELETE, attendance, "Delete attendance record");
        return MstAttendancesMapper.toBaseDto(attendance);
    }

    // === Classes ===

    @Override
    public FindResponse<MstClassesDto> findClasses(ClassesFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<MstClasses> spec = (root, query, cb) -> {
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

        Page<MstClasses> result = classesRepository.findAll(spec, pageable);
        return new FindResponse<MstClassesDto>()
            .setRecords(result.getContent().stream().map(MstClassesMapper::toBaseDtoWithHomeroomTeachers).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())            
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "classes", key = "#id")
    public MstClassesDto readClass(Integer id) {
        MstClasses classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        return MstClassesMapper.toDto(classes);
    }

    @Override
    public MstClassesDto createClass(ClassesCreateForm body) {
        MstClasses classes = MstClassesMapper.toEntity(body);
        classes = classesRepository.save(classes);
        auditTrailsService.create(Method.POST, classes, "Create classroom record");
        return MstClassesMapper.toBaseDto(classes);
    }

    @Override
    @CachePut(value = "classes", key = "#id")
    public MstClassesDto updateClass(Integer id, ClassesUpdateForm body) {
        MstClasses classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        EntityUtil.compareVersion(classes.getVersion(), body.getVersion());
        classes = MstClassesMapper.toEntity(classes, body);
        classes = classesRepository.saveAndFlush(classes);
        auditTrailsService.create(Method.PUT, classes, "Updated classroom record");
        return MstClassesMapper.toDto(classes);
    }

    @Override
    @CacheEvict(value = "classes", key = "#id")
    public MstClassesDto deleteClass(Integer id) {
        MstClasses classes = classesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
        classesRepository.deleteById(id);
        auditTrailsService.create(Method.DELETE, classes, "Delete classroom record");
        return MstClassesMapper.toBaseDto(classes);
    }

    // === Students ===

    @Override
    public FindResponse<MstStudentsDto> findStudents(StudentsFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<MstStudents> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<MstStudents, MstClasses> classJoin = root.join("classroom", JoinType.LEFT);

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

        Page<MstStudents> result = studentsRepository.findAll(spec, pageable);
        return new FindResponse<MstStudentsDto>()
            .setRecords(result.getContent().stream().map(MstStudentsMapper::toBaseDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "student", key = "#id")
    public MstStudentsDto readStudent(Integer id) {
        MstStudents student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        return MstStudentsMapper.toDto(student);
    }

    @Override
    public MstStudentsDto createStudent(StudentsCreateForm form) {
        if (form.getUserId() != null) {
            MstUsers user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + form.getUserId()));
            if (user.getStudentData() != null) {
                throw new ConflictException("User is already associated with a student: " + form.getUserId());
            }
            if (user.getTeacherData() != null) {
                throw new ConflictException("User is already associated with a teacher: " + form.getUserId());
            }
        }
        MstStudents student = MstStudentsMapper.toEntity(form, fileUtil.saveProfilePic(form.getPhoto()));
        student = studentsRepository.save(student);
        auditTrailsService.create(Method.POST, student, "Create student record");
        return MstStudentsMapper.toBaseDto(student);
    }

    @Override
    @CachePut(value = "student", key = "#id")
    public MstStudentsDto updateStudent(Integer id, StudentsUpdateForm form) {
        MstStudents student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        EntityUtil.compareVersion(student.getVersion(), form.getVersion());
        if (form.getUserId() != null) {
            MstUsers user = usersRepository.findById(form.getUserId())
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
            student = MstStudentsMapper.toEntity(student, form, newPhotoPath);
            MstStudentsDto result = MstStudentsMapper.toDto(studentsRepository.saveAndFlush(student));
            if (isNewPhotoUploaded) {
                fileUtil.deleteProfilePic(oldPhotoPath);
            }
            auditTrailsService.create(Method.PUT, student, "Update student record");
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
    public MstStudentsDto deleteStudent(Integer id) {
        MstStudents student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        studentsRepository.deleteById(id);
        auditTrailsService.create(Method.DELETE, student, "Delete student record");
        return MstStudentsMapper.toBaseDto(student);
    }

    @Override
    @CachePut(value = "student", key = "#id")
    public MstStudentsDto deleteStudentProfilePic(Integer id) {
        MstStudents student = studentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        if (!StringUtils.hasLength(student.getPhoto())) {
            throw new ResourceNotFoundException("Student has no profile picture");
        }
        String oldPhotoPath = student.getPhoto();
        student.setPhoto(null);
        student = studentsRepository.saveAndFlush(student);
        MstStudentsDto result = MstStudentsMapper.toDto(student);
        fileUtil.deleteProfilePic(oldPhotoPath);
        auditTrailsService.create(Method.PATCH, student, "Delete student profile picture");
        return result;
    }

    // === Teachers ===

    @Override
    public FindResponse<MstTeachersDto> findTeachers(TeachersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<MstTeachers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<MstTeachers, MstHomeroomTeachers> homeroomJoin = root.join("homeroomTeachers", JoinType.LEFT);
            Join<MstHomeroomTeachers, MstClasses> classJoin = homeroomJoin.join("classData", JoinType.LEFT);

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

        Page<MstTeachers> result = teachersRepository.findAll(spec, pageable);
        return new FindResponse<MstTeachersDto>()
            .setRecords(result.getContent().stream().map(MstTeachersMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "teacher", key = "#id")
    public MstTeachersDto readTeacher(Integer id) {
        MstTeachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        return MstTeachersMapper.toDto(teacher);
    }

    @Override
    public MstTeachersDto createTeacher(TeachersCreateForm form) {
        if (form.getUserId() != null) {
            MstUsers user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + form.getUserId()));
            if (user.getStudentData() != null) {
                throw new ConflictException("User is already associated with a student: " + form.getUserId());
            }
            if (user.getTeacherData() != null) {
                throw new ConflictException("User is already associated with a teacher: " + form.getUserId());
            }
        }
        MstTeachers teacher = MstTeachersMapper.toEntity(form, fileUtil.saveProfilePic(form.getPhoto()));
        teacher = teachersRepository.save(teacher);
        auditTrailsService.create(Method.POST, teacher, "Create teacher record");
        return MstTeachersMapper.toBaseDto(teacher);
    }

    @Override
    @CachePut(value = "teacher", key = "#id")
    public MstTeachersDto updateTeacher(Integer id, TeachersUpdateForm form) {
        MstTeachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        EntityUtil.compareVersion(teacher.getVersion(), form.getVersion());
        if (form.getUserId() != null) {
            MstUsers user = usersRepository.findById(form.getUserId())
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
            teacher = MstTeachersMapper.toEntity(teacher, form, newPhotoPath);
            teacher = teachersRepository.saveAndFlush(teacher);
            MstTeachersDto result = MstTeachersMapper.toDto(teacher);
            if (isNewPhotoUploaded) {
                fileUtil.deleteProfilePic(oldPhotoPath);
            }
            auditTrailsService.create(Method.PUT, teacher, "Update teacher record");
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
    public MstTeachersDto deleteTeacher(Integer id) {
        MstTeachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        teachersRepository.deleteById(id);
        auditTrailsService.create(Method.DELETE, teacher, "Delete teacher record");
        return MstTeachersMapper.toBaseDto(teacher);
    }

    @Override
    @CachePut(value = "teacher", key = "#id")
    public MstTeachersDto deleteTeacherProfilePic(Integer id) {
        MstTeachers teacher = teachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        if (!StringUtils.hasLength(teacher.getPhoto())) {
            throw new ResourceNotFoundException("Teacher has no profile picture");
        }
        String oldPhotoPath = teacher.getPhoto();
        teacher.setPhoto(null);
        teacher = teachersRepository.saveAndFlush(teacher);
        MstTeachersDto result = MstTeachersMapper.toDto(teacher);
        fileUtil.deleteProfilePic(oldPhotoPath);
        auditTrailsService.create(Method.PATCH, teacher, "Delete teacher profile picture");
        return result;
    }

    // === Homeroom Teachers ===

    @Override
    public FindResponse<MstHomeroomTeachersDto> findHomeroomTeachers(HomeroomTeachersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<MstHomeroomTeachers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<MstHomeroomTeachers, MstClasses> classJoin = root.join("classData", JoinType.LEFT);
            Join<MstHomeroomTeachers, MstTeachers> teacherJoin = root.join("teacherData", JoinType.LEFT);

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

        Page<MstHomeroomTeachers> result = homeroomTeachersRepository.findAll(spec, pageable);
        return new FindResponse<MstHomeroomTeachersDto>()
            .setRecords(result.getContent().stream().map(MstHomeroomTeachersMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "homeroomTeacher", key = "#id")
    public MstHomeroomTeachersDto readHomeroomTeacher(Integer id) {
        MstHomeroomTeachers homeroomTeachers = homeroomTeachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found: " + id));
        return MstHomeroomTeachersMapper.toDto(homeroomTeachers);
    }

    @Override
    public MstHomeroomTeachersDto createHomeroomTeacher(HomeroomTeachersCreateForm body) {
        MstHomeroomTeachers homeroomTeachers = homeroomTeachersRepository.findByClassIdAndTeacherId(body.getClassId(), body.getTeacherId());
        if (homeroomTeachers != null) {
            throw new BadRequestException("The teacher already assigned to this class");
        }
        homeroomTeachers = MstHomeroomTeachersMapper.toEntity(body);
        homeroomTeachers = homeroomTeachersRepository.save(homeroomTeachers);
        auditTrailsService.create(Method.POST, homeroomTeachers, "Create homeroom teacher record");
        return MstHomeroomTeachersMapper.toBaseDto(homeroomTeachers);
    }

    @Override
    @CachePut(value = "homeroomTeacher", key = "#id")
    public MstHomeroomTeachersDto deleteHomeroomTeacher(Integer id) {
        MstHomeroomTeachers homeroomTeachers = homeroomTeachersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found: " + id));
        homeroomTeachersRepository.deleteById(id);
        auditTrailsService.create(Method.DELETE, homeroomTeachers, "Delete homeroom teacher record");
        return MstHomeroomTeachersMapper.toBaseDto(homeroomTeachers);
    }

    // === Users ===

    @Override
    public FindResponse<MstUsersDto> findUsers(UsersFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<MstUsers> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<MstUsers, MstTeachers> teacherJoin = root.join("teacherData", JoinType.LEFT);
            Join<MstUsers, MstStudents> studentJoin = root.join("studentData", JoinType.LEFT);

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
        Page<MstUsers> result = usersRepository.findAll(spec, pageable);
        return new FindResponse<MstUsersDto>()
            .setRecords(result.getContent().stream().map(MstUsersMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public MstUsersDto readUser(Integer id) {
        MstUsers user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return MstUsersMapper.toDto(user);
    }

    @Override
    @SneakyThrows
    public MstUsersDto createUser(UsersCreateForm form) {
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        if(usersRepository.findByEmail(form.getEmail()) != null){
            throw new ConflictException("Email already exist");
        }
        if(usersRepository.findByUsername(form.getUsername()) != null){
            throw new ConflictException("Username already exist");
        }
        MstUsers user = MstUsersMapper.toEntity(form, MediaUtil.ToBase64(form.getPhoto()), new BCryptPasswordEncoder().encode(form.getPassword()));
        user = usersRepository.save(user);
        auditTrailsService.create(Method.POST, user, "Create user record");
        return MstUsersMapper.toBaseDto(user);
    }

    @Override
    @SneakyThrows
    @CachePut(value = "user", key = "#id")
    public MstUsersDto updateUser(Integer id, UsersUpdateForm form) {
        MstUsers user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        EntityUtil.compareVersion(user.getVersion(), form.getVersion());
        MstUsers checkExistingEmail = usersRepository.findByEmail(form.getEmail());
        if(checkExistingEmail != null && !checkExistingEmail.getId().equals(user.getId())){
            throw new ConflictException("Email already exist");
        }
        MstUsers checkExistingUsername = usersRepository.findByUsername(form.getUsername());
        if(checkExistingUsername != null && !checkExistingUsername.getId().equals(user.getId())){
            throw new ConflictException("Username already exist");
        }
        user = MstUsersMapper.toEntity(user, form, MediaUtil.ToBase64(form.getPhoto()));
        user = usersRepository.saveAndFlush(user);
        auditTrailsService.create(Method.PUT, user, "Update user record");
        return MstUsersMapper.toDto(user);
    }

    @Override
    public MstUsersDto updateUserPassword(Integer id, UsersUpdatePasswordForm form) {
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        MstUsers user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        user = usersRepository.save(user);
        auditTrailsService.create(Method.PATCH, user, "Update user password");
        return MstUsersMapper.toBaseDto(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public MstUsersDto deleteUser(Integer id) {
        MstUsers user = usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        usersRepository.deleteById(id);
        auditTrailsService.create(Method.DELETE, user, "Delete user record");
        return MstUsersMapper.toBaseDto(user);
    }
    
}
