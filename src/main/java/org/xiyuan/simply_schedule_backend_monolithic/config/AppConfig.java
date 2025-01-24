package org.xiyuan.simply_schedule_backend_monolithic.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.CoachDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.StudentDto;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping for Coach to CoachDto
        modelMapper.typeMap(Coach.class, CoachDto.class).addMappings(mapper ->
                mapper.using(context -> {
                    List<Student> students = (List<Student>) context.getSource(); // Explicit cast
                    if (students == null || students.isEmpty()) {
                        return Collections.emptyList();
                    }
                    return students.stream().map(Student::getId).collect(Collectors.toList());
                }).map(Coach::getStudents, CoachDto::setStudents)
        );

        // Custom mapping for Student to StudentDto
        modelMapper.typeMap(Student.class, StudentDto.class).addMappings(mapper ->
                mapper.map(src -> src.getCoach().getId(), StudentDto::setCoach)
        );


        return modelMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        // Force english for error messages
        return new FixedLocaleResolver(Locale.ENGLISH);
    }
}
