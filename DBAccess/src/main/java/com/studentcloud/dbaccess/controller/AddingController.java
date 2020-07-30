package com.studentcloud.dbaccess.controller;

import com.studentcloud.dbaccess.entities.*;
import com.studentcloud.dbaccess.repo.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Controller
public class AddingController {

    @Autowired
    FileRepo fileRepo;
    @Autowired
    UniversityRepo universityRepo;
    @Autowired
    TeacherRepo teacherRepo;
    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    SubjectRepo subjectRepo;
    @Autowired
    CommentRepo commentRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public AddingController() {}

    @GetMapping("/add")
    public String add(Model model){
        return "file-add";
    }

    @PostMapping("/add")
    public @URL String addFile(
            @RequestParam("file") MultipartFile fileUploaded,
            @RequestParam String universityName,
            @RequestParam String departmentName,
            @RequestParam String teacherName,
            @RequestParam String subjectName,
            @RequestParam String fileName
    ) {
        University university;
        Teacher teacher;
        Department department;
        Subject subj;

        university = universityRepo.existsByName(universityName) ?
                universityRepo.findByName(universityName) :
                new University();

        university.setName(universityName);

        teacher = teacherRepo.existsByName(teacherName) ?
                teacherRepo.findByName(teacherName) :
                new Teacher();

        teacher.setName(teacherName);

        department = departmentRepo.existsByName(departmentName) ?
                departmentRepo.findByName(departmentName) :
                new Department();

        department.setName(departmentName);
        department.setUniversity(university);

        subj = subjectRepo.existsByName(subjectName) ?
                subjectRepo.findByName(subjectName) :
                new Subject();

        subj.setName(subjectName);

        fillEntities(university, department, teacher, subj);

        String id = UUID.randomUUID().toString();

        try {
            fileName = saveFile(
                    fileUploaded,
                    universityName,
                    departmentName,
                    teacherName,
                    subjectName,
                    fileName,
                    id);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        java.util.Date uDate = new java.util.Date();
        File file = new File(fileName, university, teacher, department, subj, new java.sql.Date(uDate.getTime()));
        file.setId(id);

        teacher.getFiles().add(file);
        fileRepo.save(file);

        return "redirect:http://localhost:8080/files";
    }

    public String saveFile(
            MultipartFile file,
            String universityName,
            String departmentName,
            String teacherName,
            String subjectName,
            String fileName,
            String id
    ) throws IOException {
        if (file != null) {
            java.io.File uploadDir = new java.io.File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            StringBuilder resultFileNameBuilder = new StringBuilder();
            resultFileNameBuilder
                    .append(universityName)
                    .append("-")
                    .append(departmentName)
                    .append("-")
                    .append(teacherName)
                    .append("-")
                    .append(subjectName)
                    .append("-")
                    .append(fileName)
                    .append("-")
                    .append(id)
                    .append("-")
                    .append(file.getOriginalFilename());

            String resultFileName = uploadPath + resultFileNameBuilder.toString();

            file.transferTo(new java.io.File(resultFileName));
            return resultFileName;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void fillEntities(
            University university,
            Department department,
            Teacher teacher,
            Subject subj
    ) {
        university.getTeachers().add(teacher);
        university.getDepartments().add(department);
        university.getSubjects().add(subj);

        department.getTeachers().add(teacher);
        department.getSubjects().add(subj);

        subj.getDepartments().add(department);
        subj.getTeachers().add(teacher);
        subj.getUniversities().add(university);

        teacher.getDepartments().add(department);
        teacher.getSubjects().add(subj);
        teacher.getUniversities().add(university);
    }
}

