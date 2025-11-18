package com.example.student.service;

import com.example.student.dto.StudentRequestDto;
import com.example.student.dto.StudentResponseDto;
import org.springframework.data.domain.Page;

public interface StudentService {

    StudentResponseDto createStudent(StudentRequestDto requestDto);

    StudentResponseDto getStudentById(Long id);

    Page<StudentResponseDto> listStudents(String branch,
                                          Boolean active,
                                          Integer startYop,
                                          Integer endYop,
                                          int page,
                                          int size,
                                          String sortBy,
                                          String sortDirection);

    StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto);

    StudentResponseDto patchStudent(Long id, StudentRequestDto requestDto);

    void deleteStudent(Long id);
}
