package com.ai.springai.springai22springmcpserver;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    public static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private List<Course> courses;

    @PostConstruct
    public void init() {
        courses = new ArrayList<>();
        courses = List.of(
                new Course("Spring AI Introduction: Building AI Applications in Java with Spring", "https://www.youtube.com/watch?v=yyvjT0v3lpY"),
                new Course("Working with Prompts in Spring AI - Effectively Communicating with LLMs", "https://www.youtube.com/watch?v=ACpLp2KXqgE"),
                new Course("Using Spring AI's Output Parsers to structure the response from LLMs", "https://www.youtube.com/watch?v=CuIr3FiG_fc"));
    }

    @Tool(name = "spring_get_courses", description = "Get a list of courses from Spring Tutorial")
    public List<Course> getCourses() {
        return courses;
    }

    @Tool(name = "dv_get_course", description = "Get a single course from Spring by Title")
    public Course getCourse(String title) {
        return courses.stream().filter(course -> course.title().equals(title)).findFirst().orElse(null);
    }
}
