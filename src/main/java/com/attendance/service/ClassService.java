package com.attendance.service;

import com.attendance.entity.ClassEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {
    
    @Autowired
    private FirebaseClassService firebaseClassService;
    
    public ClassEntity saveClass(ClassEntity classEntity) {
        return firebaseClassService.save(classEntity);
    }
    
    public List<ClassEntity> findAllClasses() {
        return firebaseClassService.findAll();
    }
    
    public Optional<ClassEntity> findByClassCode(String classCode) {
        return firebaseClassService.findByClassCode(classCode);
    }
    
    public Long countAllClasses() {
        return firebaseClassService.countAllClasses();
    }
    
    public boolean existsByClassCode(String classCode) {
        return firebaseClassService.existsByClassCode(classCode);
    }
    
    public ClassEntity createClass(String className, String classCode, String description) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassName(className);
        classEntity.setClassCode(classCode);
        classEntity.setDescription(description);
        return saveClass(classEntity);
    }
    
    public Optional<ClassEntity> findById(String id) {
        return firebaseClassService.findById(id);
    }
    
    public void deleteClass(String id) {
        firebaseClassService.delete(id);
    }
}
