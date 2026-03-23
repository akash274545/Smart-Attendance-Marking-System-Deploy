package com.attendance.service;

import com.attendance.entity.ClassEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseClassService {

    @Autowired
    private FirebaseService firebaseService;

    private static final String BASE_PATH = "AttendanceSystem/classes";

    public ClassEntity save(ClassEntity classEntity) {
        String key = classEntity.getId();
        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            classEntity.setId(key);
        }
        firebaseService.saveWithKeySync(BASE_PATH, key, classEntity);
        return classEntity;
    }

    public Optional<ClassEntity> findById(String id) {
        ClassEntity classEntity = firebaseService.getSync(BASE_PATH + "/" + id, ClassEntity.class);
        if (classEntity != null) {
            classEntity.setId(id);
        }
        return Optional.ofNullable(classEntity);
    }

    public Optional<ClassEntity> findByClassCode(String classCode) {
        Map<String, ClassEntity> classesMap = firebaseService.getAllSync(BASE_PATH, ClassEntity.class);
        return classesMap.entrySet().stream()
                .filter(entry -> classCode.equals(entry.getValue().getClassCode()))
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public boolean existsByClassCode(String classCode) {
        return findByClassCode(classCode).isPresent();
    }

    public Long countAllClasses() {
        return firebaseService.countSync(BASE_PATH);
    }

    public List<ClassEntity> findAll() {
        Map<String, ClassEntity> classesMap = firebaseService.getAllSync(BASE_PATH, ClassEntity.class);
        return classesMap.entrySet().stream()
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
    public void delete(String id) {
        firebaseService.deleteSync(BASE_PATH + "/" + id);
    }
}

