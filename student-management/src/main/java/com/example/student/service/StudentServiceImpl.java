package com.example.student.service;

import com.example.student.dto.StudentRequestDto;
import com.example.student.dto.StudentResponseDto;
import com.example.student.exception.BadRequestException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.StudentMapper;
import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import java.util.Locale;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final String DEFAULT_SORT = "createdAt";

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDto createStudent(StudentRequestDto requestDto) {
        validateRequest(requestDto);
        Student student = StudentMapper.toEntity(requestDto);
        Student saved = studentRepository.save(student);
        return StudentMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        Student student = findActiveStudent(id);
        return StudentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> listStudents(String branch,
                                                 Boolean active,
                                                 Integer startYop,
                                                 Integer endYop,
                                                 int page,
                                                 int size,
                                                 String sortBy,
                                                 String sortDirection) {
        validateYopRange(startYop, endYop);
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), buildSort(sortBy, sortDirection));
        Specification<Student> specification = buildSpecification(branch, active, startYop, endYop);
        return studentRepository.findAll(specification, pageable)
            .map(StudentMapper::toDto);
    }

    @Override
    public StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto) {
        validateRequest(requestDto);
        Student existing = findActiveStudent(id);
        applyFullUpdate(existing, requestDto);
        Student saved = studentRepository.save(existing);
        return StudentMapper.toDto(saved);
    }

    @Override
    public StudentResponseDto patchStudent(Long id, StudentRequestDto requestDto) {
        if (requestDto == null) {
            throw new BadRequestException("Student data is required for patch");
        }
        Student existing = findActiveStudent(id);
        StudentMapper.updateEntityFromDto(requestDto, existing);
        Student saved = studentRepository.save(existing);
        return StudentMapper.toDto(saved);
    }

    @Override
    public void deleteStudent(Long id) {
        Student existing = findActiveStudent(id);
        existing.setDeleted(true);
        existing.setActive(false);
        studentRepository.save(existing);
    }

    private Student findActiveStudent(Long id) {
        if (id == null) {
            throw new BadRequestException("Student id is required");
        }
        return studentRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    private void validateRequest(StudentRequestDto requestDto) {
        if (requestDto == null) {
            throw new BadRequestException("Student data is required");
        }
    }

    private void validateYopRange(Integer startYop, Integer endYop) {
        if (startYop != null && endYop != null && startYop > endYop) {
            throw new BadRequestException("startYop cannot be greater than endYop");
        }
    }

    private void applyFullUpdate(Student target, StudentRequestDto source) {
        target.setFullName(source.getFullName());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setBranch(source.getBranch());
        target.setYop(source.getYop());
        target.setActive(source.isActive());
        target.setDeleted(source.isDeleted());
    }

    private Sort buildSort(String sortBy, String sortDirection) {
        String property = (sortBy == null || sortBy.isBlank()) ? DEFAULT_SORT : sortBy;
        Sort.Direction direction = Sort.Direction.fromOptionalString(Objects.toString(sortDirection, "ASC"))
            .orElse(Sort.Direction.ASC);
        return Sort.by(direction, property);
    }

    private Specification<Student> buildSpecification(String branch,
                                                      Boolean active,
                                                      Integer startYop,
                                                      Integer endYop) {
        Specification<Student> spec = (root, query, cb) -> cb.isFalse(root.get("deleted"));

        if (branch != null && !branch.isBlank()) {
            String normalizedBranch = branch.toLowerCase(Locale.ROOT);
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("branch")), normalizedBranch));
        }
        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }
        if (startYop != null && endYop != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("yop"), startYop, endYop));
        } else if (startYop != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("yop"), startYop));
        } else if (endYop != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("yop"), endYop));
        }

        return spec;
    }
}
