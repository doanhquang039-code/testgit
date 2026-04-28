package com.example.hr.controllers;

import com.example.hr.models.*;
import com.example.hr.service.AuthUserHelper;
import com.example.hr.service.CourseManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/lms")
@RequiredArgsConstructor
public class LMSController {
    
    private final CourseManagementService courseService;
    private final AuthUserHelper authUserHelper;
    
    // ===== User Views =====
    
    @GetMapping("/courses")
    @PreAuthorize("isAuthenticated()")
    public String courseCatalog(@RequestParam(required = false) String category,
                                @RequestParam(required = false) String search,
                                Model model) {
        List<Course> courses;
        
        if (search != null && !search.isBlank()) {
            courses = courseService.searchCourses(search);
        } else if (category != null && !category.isBlank()) {
            courses = courseService.getCoursesByCategory(category);
        } else {
            courses = courseService.getActiveCourses();
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchKeyword", search);
        
        return "user1/course-catalog";
    }
    
    @GetMapping("/my-courses")
    @PreAuthorize("isAuthenticated()")
    public String myCourses(Authentication auth, Model model) {
        User user = authUserHelper.getCurrentUser(auth);
        List<CourseEnrollment> enrollments = courseService.getUserEnrollments(user);
        
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("completedCount", courseService.getCompletedCoursesCount(user));
        
        return "user1/my-courses";
    }
    
    @GetMapping("/course/{id}")
    @PreAuthorize("isAuthenticated()")
    public String courseDetail(@PathVariable Integer id, Authentication auth, Model model) {
        Course course = courseService.getActiveCourses().stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Course not found"));
        
        User user = authUserHelper.getCurrentUser(auth);
        var enrollment = courseService.getEnrollment(user, course);
        
        model.addAttribute("course", course);
        model.addAttribute("lessons", courseService.getCourseLessons(course));
        model.addAttribute("enrollment", enrollment.orElse(null));
        model.addAttribute("isEnrolled", enrollment.isPresent());
        
        return "user1/course-detail";
    }
    
    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public String enrollCourse(@PathVariable Integer courseId, 
                              Authentication auth,
                              RedirectAttributes ra) {
        try {
            User user = authUserHelper.getCurrentUser(auth);
            Course course = courseService.getActiveCourses().stream()
                .filter(c -> c.getId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Course not found"));
            
            courseService.enrollUser(user, course);
            ra.addFlashAttribute("success", "Đăng ký khóa học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/lms/course/" + courseId;
    }
    
    // ===== Admin Views =====
    
    @GetMapping("/admin/courses")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminCourseList(Model model) {
        model.addAttribute("courses", courseService.getActiveCourses());
        return "admin/course-list";
    }
    
    @GetMapping("/admin/course/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }
    
    @PostMapping("/admin/course/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes ra) {
        try {
            courseService.createCourse(course);
            ra.addFlashAttribute("success", "Tạo khóa học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lms/admin/courses";
    }
}
