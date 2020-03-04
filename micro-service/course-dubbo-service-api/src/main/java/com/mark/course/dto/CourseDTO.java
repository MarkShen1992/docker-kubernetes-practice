package com.mark.course.dto;

import com.mark.thrift.dto.TeacherDTO;

import java.io.Serializable;

public class CourseDTO implements Serializable {

    private int id;
    private String title;
    private String description;
    private TeacherDTO teacherDTO;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TeacherDTO getTeacherDTO() {
        return teacherDTO;
    }

    public void setTeacherDTO(TeacherDTO teacherDTO) {
        this.teacherDTO = teacherDTO;
    }
}
