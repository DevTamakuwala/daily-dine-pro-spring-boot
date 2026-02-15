package io.github.devtamakuwala.dailydine.student.controller;

import io.github.devtamakuwala.dailydine.student.model.Student;
import io.github.devtamakuwala.dailydine.student.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/docker-demo")
public class StudentController {

    @Autowired
    StudentRepo studentRepo;

    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentRepo.findAll();
    }

    @PostMapping("/students")
    public String addStudent() {

        System.out.println("Adding student");
        Student s = new Student();
        s.setName("Dev");
        s.setAge(21);
        studentRepo.save(s);

        Student s1 = new Student();
        s1.setName("Jenil");
        s1.setAge(23);
        studentRepo.save(s1);

        Student s2 = new Student();
        s2.setName("Sajjad");
        s2.setAge(22);
        studentRepo.save(s2);

        Student s3 = new Student();
        s3.setName("Akshay");
        s3.setAge(22);
        studentRepo.save(s3);

        return "Students created";
    }
}
