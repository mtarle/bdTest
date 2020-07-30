package com.studentcloud.dbaccess.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToMany(
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id",referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id" ,referencedColumnName="id")
    )
    private Set<Subject> subjects;

    @Column(name = "teacher_name", nullable = false, unique = true)
    private String name;

    @ManyToMany(
            mappedBy = "teachers",
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    private Set<University> universities;

    @ManyToMany(
            mappedBy = "teachers",
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
            )
    private Set<Department> departments;

    @OneToMany(mappedBy = "teacher")
    private Set<File> files;

    public Teacher() {
        this.departments = new HashSet<>();
        this.subjects = new HashSet<>();
        this.universities = new HashSet<>();
        this.files = new HashSet<>();
    }

    public Teacher(String name, Set<University> universities, Set<Department> departments, Set<File> files) {
        this.name = name;
        this.universities = universities;
        this.departments = departments;
        this.files = files;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<University> getUniversities() {
        return universities;
    }

    public void setUniversities(Set<University> universities) {
        this.universities = universities;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(getId(), teacher.getId()) &&
                Objects.equals(getName(), teacher.getName());
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
