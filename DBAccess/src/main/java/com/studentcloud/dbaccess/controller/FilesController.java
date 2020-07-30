package com.studentcloud.dbaccess.controller;

import com.studentcloud.dbaccess.entities.*;
import com.studentcloud.dbaccess.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/files")
public class FilesController {
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

    @GetMapping
    public String universities(Model model){
        Iterable<University> universities = universityRepo.findAll();
        model.addAttribute("universities", universities);
        return "universities";
    }

    @GetMapping("/universities/{universityName}")
    public String filesInUniversity(
            Model model,
            @PathVariable String universityName,
            @RequestParam Integer departmentID,
            @RequestParam Integer teacherID,
            @RequestParam Integer subjectID
    ) {
        University university = universityRepo.findByName(universityName);

        Set<Department> departments = departmentRepo.findAllByUniversity_Name(universityName);
        Set<Teacher> teachers = teacherRepo.findAllByUniversitiesContains(university);
        Set<Subject> subjects = subjectRepo.findAllByUniversitiesContains(university);

        model.addAttribute("departments", departments);
        model.addAttribute("teachers", teachers);
        model.addAttribute("subjects", subjects);

        Set<File> files = findFiles(universityName, departmentID, teacherID, subjectID);
        model.addAttribute("files", files);

        return "files";
    }

    public Set<File> findFiles(String universityName, Integer departmentID, Integer teacherID, Integer subjectID) {
        Set<File> filesByUniversity = fileRepo.findAllByUniversity_Name(universityName);

        Set<File> filesByDepartment = (departmentID != null && !departmentID.equals(0)) ?
                fileRepo.findAllByDepartment_Id(departmentID) : filesByUniversity;
        Set<File> filesByTeacher = (teacherID != null && !teacherID.equals(0)) ?
                fileRepo.findAllByTeacher_Id(teacherID) : filesByUniversity;
        Set<File> filesBySubj = (subjectID != null && !subjectID.equals(0)) ?
                fileRepo.findAllBySubj_Id(subjectID) : filesByUniversity;

        filesByUniversity.retainAll(filesByDepartment);
        filesByUniversity.retainAll(filesByTeacher);
        filesByUniversity.retainAll(filesBySubj);

        return filesByDepartment;
    }

    @GetMapping("/{fileID}")
    public String getFile(
            Model model,
            @PathVariable String fileID
    ) {
        Optional<File> file = fileRepo.findById(fileID);
        if (file.isPresent()) model.addAttribute("file", file);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемый файл не существует");

        return "file";
    }

    @PostMapping(value = "/{fileID}")
    public String addComment(
            Model model,
            @RequestParam String name,
            @RequestParam String message,
            @PathVariable String fileID
    ) {
        Optional<File> file = fileRepo.findById(fileID);

        name = name == null || name.length() == 0 ? "Анон" : name;

        Comment comment = new Comment(file.get(), name, message, new java.sql.Date((new java.util.Date()).getTime()));
        file.get().getComments().add(comment);

        commentRepo.save(comment);
        model.addAttribute("file", file);

        return "file";
    }

    @GetMapping("/{fileID}/download")
    public ResponseEntity<Object> downloadFile(
            @PathVariable String fileID
    ) throws IOException {
        Optional<File> fileOpt = fileRepo.findById(fileID);

        java.io.File file = new java.io.File(
                fileOpt.get()
                .getName()
        );
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity =
                ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(file.length())
                        .contentType(MediaType.parseMediaType("application/txt"))
                        .body(resource);

        return responseEntity;
    }
}
