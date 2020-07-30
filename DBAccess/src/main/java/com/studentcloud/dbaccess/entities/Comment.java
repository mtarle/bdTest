package com.studentcloud.dbaccess.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }
    )
    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = false)
    private File file;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String message;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Comment() {}

    public Comment(File file, String userName, String message, Date date) {
        this.file = file;
        this.userName = userName;
        this.message = message;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String comment) {
        this.message = comment;
    }
}
