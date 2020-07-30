package com.studentcloud.dbaccess.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class File {

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    @Column(name = "file_name", nullable = false)
    private String name;


    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    @JoinColumn(name = "university_id", referencedColumnName="id",nullable = false)
    private University university;


    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    @JoinColumn(name = "teacher_id", referencedColumnName="id",nullable = false)
    private Teacher teacher;

    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    @JoinColumn(name = "department_id",referencedColumnName="id",nullable = false)
    private Department department;

    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    @JoinColumn(name = "subj_id", referencedColumnName="id",nullable = false)
    private Subject subj;

    private Date date;

    public File() {
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToMany(mappedBy = "file")
    public List<Comment> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public File(String name, University university, Teacher teacher, Department department, Subject subj, Date date) {
        this.name = name;
        this.university = university;
        this.teacher = teacher;
        this.department = department;
        this.subj = subj;
        this.date = date;
        this.comments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Subject getSubj() {
        return subj;
    }

    public void setSubj(Subject subj) {
        this.subj = subj;
    }
}
