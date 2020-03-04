package com.mark.controller;

import com.mark.course.dto.CourseDTO;
import com.mark.course.service.ICourseService;
import com.mark.thrift.dto.UserDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService courseService;

    @GetMapping("/courseList")
    @ResponseBody
    public List<CourseDTO> courseList(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getAttribute("user");
        System.out.println(user.toString());
        return courseService.courseList();
    }
}
