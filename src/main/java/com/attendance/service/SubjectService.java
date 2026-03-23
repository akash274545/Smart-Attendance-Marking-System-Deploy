package com.attendance.service;

import com.attendance.entity.Subject;
import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    
    @Autowired
    private FirebaseSubjectService firebaseSubjectService;
    
    public Subject saveSubject(Subject subject) {
        return firebaseSubjectService.save(subject);
    }
    
    public List<Subject> findAllSubjects() {
        return firebaseSubjectService.findAll();
    }
    
    public List<Subject> findByTeacher(User teacher) {
        return firebaseSubjectService.findByTeacher(teacher);
    }
    
    public Optional<Subject> findById(String id) {
        return firebaseSubjectService.findById(id);
    }

    public Optional<Subject> findBySubjectCode(String subjectCode) {
        return firebaseSubjectService.findBySubjectCode(subjectCode);
    }
    
    public Long countAllSubjects() {
        return firebaseSubjectService.countAllSubjects();
    }
    
    public Long countByTeacher(User teacher) {
        return firebaseSubjectService.countByTeacher(teacher);
    }
    
    public boolean existsBySubjectCode(String subjectCode) {
        return firebaseSubjectService.existsBySubjectCode(subjectCode);
    }
    
    public Subject createSubject(String subjectName, String subjectCode, String description, User teacher) {
        Subject subject = new Subject();
        subject.setSubjectName(subjectName);
        subject.setSubjectCode(subjectCode);
        subject.setDescription(description);
        subject.setTeacherId(teacher.getId());
        return saveSubject(subject);
    }
    
    public void deleteSubject(String id) {
        firebaseSubjectService.delete(id);
    }
    
    public Subject saveDefaultSubject(Subject subject) {
        return firebaseSubjectService.saveDefaultSubject(subject);
    }
    
    public List<Subject> findAllDefaultSubjects() {
        return firebaseSubjectService.findAllDefaultSubjects();
    }
    
    public boolean existsDefaultSubjectByCode(String subjectCode) {
        return firebaseSubjectService.existsDefaultSubjectByCode(subjectCode);
    }
}
