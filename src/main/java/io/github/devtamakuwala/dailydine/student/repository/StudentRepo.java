package io.github.devtamakuwala.dailydine.student.repository;

import io.github.devtamakuwala.dailydine.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Integer> {
}
