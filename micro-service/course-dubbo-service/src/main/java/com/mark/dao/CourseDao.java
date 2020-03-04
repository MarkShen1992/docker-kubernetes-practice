package com.mark.dao;

import com.mark.course.dto.CourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseDao {

    @Select("SELECT * FROM pe_course")
    List<CourseDTO> listCourse();

    /**
     * 根据 course id 查询用户 id
     * @return
     */
    @Select("SELECT user_id FROM pr_user_course WHERE course_id = #{courseId}")
    Integer getCourseTeacher(@Param("courseId") int courseId);
}
