package com.example.student.repository;

import com.example.student.model.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    List<Student> findByDeletedFalse();

    Optional<Student> findByIdAndDeletedFalse(Long id);

    List<Student> findByBranchAndDeletedFalse(String branch);

    List<Student> findByBranchAndActiveAndDeletedFalse(String branch, boolean active);

    List<Student> findByYopBetweenAndDeletedFalse(Integer startYear, Integer endYear);

    List<Student> findByActiveAndDeletedFalse(boolean active);
}
