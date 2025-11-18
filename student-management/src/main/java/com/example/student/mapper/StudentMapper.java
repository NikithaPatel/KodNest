package com.example.student.mapper;

import com.example.student.dto.StudentRequestDto;
import com.example.student.dto.StudentResponseDto;
import com.example.student.model.Student;

public class StudentMapper {

    private StudentMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Student toEntity(StudentRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setBranch(dto.getBranch());
        student.setYop(dto.getYop());
        student.setActive(dto.isActive());
        student.setDeleted(dto.isDeleted());
        return student;
    }

    public static StudentResponseDto toDto(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponseDto(
            student.getId(),
            student.getFullName(),
            student.getEmail(),
            student.getPhone(),
            student.getBranch(),
            student.getYop(),
            student.isActive(),
            student.getCreatedAt(),
            student.getUpdatedAt(),
            student.isDeleted()
        );
    }

    public static void updateEntityFromDto(StudentRequestDto dto, Student student) {
        if (dto == null || student == null) {
            return;
        }
        if (dto.getFullName() != null) {
            student.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null) {
            student.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            student.setPhone(dto.getPhone());
        }
        if (dto.getBranch() != null) {
            student.setBranch(dto.getBranch());
        }
        if (dto.getYop() != null) {
            student.setYop(dto.getYop());
        }
        student.setActive(dto.isActive());
        student.setDeleted(dto.isDeleted());
    }
}
