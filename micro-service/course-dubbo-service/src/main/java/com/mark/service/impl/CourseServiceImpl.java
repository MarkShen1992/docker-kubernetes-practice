package com.mark.service.impl;

import com.mark.course.dto.CourseDTO;
import com.mark.course.service.ICourseService;
import com.mark.dao.CourseDao;
import com.mark.thrift.ServiceProvider;
import com.mark.thrift.dto.TeacherDTO;
import com.mark.thrift.user.UserInfo;
import org.apache.dubbo.config.annotation.Service;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public List<CourseDTO> courseList() {
        List<CourseDTO> courseDTOList = courseDao.listCourse();
        if (courseDTOList != null) {
            for (CourseDTO courseDTO : courseDTOList) {
                Integer teacherId = courseDao.getCourseTeacher(courseDTO.getId());
                if (teacherId != null) {
                    try {
                        UserInfo userInfo = serviceProvider.getUserService().getTeacherById(teacherId);
                        courseDTO.setTeacherDTO(trans2Teacher(userInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return courseDTOList;
    }

    private TeacherDTO trans2Teacher(UserInfo userInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userInfo, teacherDTO);
        return teacherDTO;
    }
}
