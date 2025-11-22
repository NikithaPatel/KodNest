package com.example.student.controller;

import com.example.student.dto.StudentRequestDto;
import com.example.student.dto.StudentResponseDto;
import com.example.student.service.StudentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final String DEFAULT_SORT = "createdAt,desc";

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto requestDto,
                                                            UriComponentsBuilder uriBuilder) {
        StudentResponseDto created = studentService.createStudent(requestDto);
        URI location = uriBuilder.path("/api/students/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        StudentResponseDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDto>> listStudents(@RequestParam(required = false) String branch,
                                                                 @RequestParam(required = false) Integer yop,
                                                                 @RequestParam(required = false) Integer startYop,
                                                                 @RequestParam(required = false) Integer endYop,
                                                                 @RequestParam(required = false) Boolean active,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int size,
                                                                 @RequestParam(required = false, name = "sort") String sortParam) {
        String[] sortParts = parseSort(sortParam);
        Integer effectiveStart = startYop != null ? startYop : yop;
        Integer effectiveEnd = endYop != null ? endYop : yop;
        Page<StudentResponseDto> students = studentService.listStudents(branch,
            active,
            effectiveStart,
            effectiveEnd,
            page,
            size,
            sortParts[0],
            sortParts[1]);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id,
                                                            @Valid @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto updated = studentService.updateStudent(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDto> patchStudent(@PathVariable Long id,
                                                           @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto updated = studentService.patchStudent(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    private String[] parseSort(String sortParam) {
        String effectiveSort = (sortParam == null || sortParam.isBlank()) ? DEFAULT_SORT : sortParam;
        String[] parts = effectiveSort.split(",", 2);
        String property = parts[0].trim();
        String direction = parts.length > 1 ? parts[1].trim() : "asc";
        direction = direction.isBlank() ? "asc" : direction.toLowerCase(Locale.ROOT);
        return new String[] { property, direction };
    }
}
