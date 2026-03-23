package com.attendance.service;

import com.attendance.entity.Subject;
import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseSubjectService {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private FirebaseUserService firebaseUserService;

    private static final String BASE_PATH = "AttendanceSystem/subjects";
    private static final String DEFAULT_SUBJECTS_PATH = "AttendanceSystem/defaultSubjects";

    public Subject save(Subject subject) {
        String key = subject.getId();
        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            subject.setId(key);
        }
        firebaseService.saveWithKeySync(BASE_PATH, key, subject);
        return subject;
    }

    public Optional<Subject> findById(String id) {
        Subject subject = firebaseService.getSync(BASE_PATH + "/" + id, Subject.class);
        if (subject != null) {
            subject.setId(id);
        }
        return Optional.ofNullable(subject);
    }

    public Optional<Subject> findBySubjectCode(String subjectCode) {
        Map<String, Subject> subjectsMap = firebaseService.getAllSync(BASE_PATH, Subject.class);
        return subjectsMap.entrySet().stream()
                .filter(entry -> subjectCode.equals(entry.getValue().getSubjectCode()))
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public List<Subject> findByTeacher(User teacher) {
        Map<String, Subject> subjectsMap = firebaseService.getAllSync(BASE_PATH, Subject.class);
        return subjectsMap.entrySet().stream()
                .filter(entry -> {
                    Subject subject = entry.getValue();
                    return subject.getTeacherId() != null && subject.getTeacherId().equals(teacher.getId());
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Long countAllSubjects() {
        return firebaseService.countSync(BASE_PATH);
    }

    public Long countByTeacher(User teacher) {
        return (long) findByTeacher(teacher).size();
    }

    public boolean existsBySubjectCode(String subjectCode) {
        return findBySubjectCode(subjectCode).isPresent();
    }

    public List<Subject> findAll() {
        Map<String, Subject> subjectsMap = firebaseService.getAllSync(BASE_PATH, Subject.class);
        return subjectsMap.entrySet().stream()
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
    public void delete(String id) {
        firebaseService.deleteSync(BASE_PATH + "/" + id);
    }
    
    public Subject saveDefaultSubject(Subject subject) {
        String key = subject.getId();
        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            subject.setId(key);
        }
        firebaseService.saveWithKeySync(DEFAULT_SUBJECTS_PATH, key, subject);
        return subject;
    }
    
    public List<Subject> findAllDefaultSubjects() {
        Map<String, Subject> subjectsMap = firebaseService.getAllSync(DEFAULT_SUBJECTS_PATH, Subject.class);
        if (subjectsMap == null || subjectsMap.isEmpty()) {
            return new ArrayList<>();
        }
        return subjectsMap.entrySet().stream()
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
    public boolean existsDefaultSubjectByCode(String subjectCode) {
        Map<String, Subject> subjectsMap = firebaseService.getAllSync(DEFAULT_SUBJECTS_PATH, Subject.class);
        if (subjectsMap == null || subjectsMap.isEmpty()) {
            return false;
        }
        return subjectsMap.values().stream()
                .anyMatch(subject -> subjectCode.equals(subject.getSubjectCode()));
    }
}

