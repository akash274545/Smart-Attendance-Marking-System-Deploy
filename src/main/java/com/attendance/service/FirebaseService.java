package com.attendance.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseService {

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    /**
     * Save an object to Firebase at the specified path
     */
    public <T> CompletableFuture<String> save(String path, T object) {
        DatabaseReference ref = firebaseDatabase.getReference(path);
        ApiFuture<Void> apiFuture = ref.setValueAsync(object);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                apiFuture.get();
                return ref.getKey();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to save data to Firebase", e);
            }
        });
    }

    /**
     * Save an object with a specific key
     */
    public <T> CompletableFuture<Void> saveWithKey(String path, String key, T object) {
        DatabaseReference ref = firebaseDatabase.getReference(path).child(key);
        ApiFuture<Void> apiFuture = ref.setValueAsync(object);
        
        return CompletableFuture.runAsync(() -> {
            try {
                apiFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to save data to Firebase", e);
            }
        });
    }

    /**
     * Get a single object by key
     */
    public <T> CompletableFuture<T> get(String path, Class<T> clazz) {
        CompletableFuture<T> future = new CompletableFuture<>();
        DatabaseReference ref = firebaseDatabase.getReference(path);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    T value = snapshot.getValue(clazz);
                    future.complete(value);
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    /**
     * Get all objects from a path
     */
    public <T> CompletableFuture<Map<String, T>> getAll(String path, Class<T> clazz) {
        CompletableFuture<Map<String, T>> future = new CompletableFuture<>();
        DatabaseReference ref = firebaseDatabase.getReference(path);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, T> result = new HashMap<>();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        T value = child.getValue(clazz);
                        if (value != null) {
                            result.put(child.getKey(), value);
                        }
                    }
                }
                future.complete(result);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    /**
     * Get a list of objects from a path
     */
    public <T> CompletableFuture<List<T>> getList(String path, Class<T> clazz) {
        CompletableFuture<List<T>> future = new CompletableFuture<>();
        DatabaseReference ref = firebaseDatabase.getReference(path);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<T> result = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        T value = child.getValue(clazz);
                        if (value != null) {
                            result.add(value);
                        }
                    }
                }
                future.complete(result);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    /**
     * Query objects with a filter
     */
    public <T> CompletableFuture<List<T>> query(String path, String orderBy, Object value, Class<T> clazz) {
        CompletableFuture<List<T>> future = new CompletableFuture<>();
        DatabaseReference ref = firebaseDatabase.getReference(path);
        
        Query query;
        if (value instanceof String) {
            query = ref.orderByChild(orderBy).equalTo((String) value);
        } else if (value instanceof Double || value instanceof Float) {
            query = ref.orderByChild(orderBy).equalTo(((Number) value).doubleValue());
        } else if (value instanceof Integer || value instanceof Long) {
            query = ref.orderByChild(orderBy).equalTo(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            query = ref.orderByChild(orderBy).equalTo((Boolean) value);
        } else {
            // Convert to String as fallback
            query = ref.orderByChild(orderBy).equalTo(value.toString());
        }
        
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<T> result = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        T obj = child.getValue(clazz);
                        if (obj != null) {
                            result.add(obj);
                        }
                    }
                }
                future.complete(result);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    /**
     * Delete an object
     */
    public CompletableFuture<Void> delete(String path) {
        DatabaseReference ref = firebaseDatabase.getReference(path);
        ApiFuture<Void> apiFuture = ref.removeValueAsync();
        
        return CompletableFuture.runAsync(() -> {
            try {
                apiFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to delete data from Firebase", e);
            }
        });
    }

    /**
     * Check if a path exists
     */
    public CompletableFuture<Boolean> exists(String path) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DatabaseReference ref = firebaseDatabase.getReference(path);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                future.complete(snapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    /**
     * Count objects in a path
     */
    public CompletableFuture<Long> count(String path) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        DatabaseReference ref = firebaseDatabase.getReference(path);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = snapshot.exists() ? snapshot.getChildrenCount() : 0;
                future.complete(count);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    /**
     * Synchronous helper methods (with timeout)
     */
    public <T> T getSync(String path, Class<T> clazz) {
        try {
            return get(path, clazz).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get data from Firebase", e);
        }
    }

    public <T> Map<String, T> getAllSync(String path, Class<T> clazz) {
        try {
            return getAll(path, clazz).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get data from Firebase", e);
        }
    }

    public <T> List<T> getListSync(String path, Class<T> clazz) {
        try {
            return getList(path, clazz).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get data from Firebase", e);
        }
    }

    public String saveSync(String path, Object object) {
        try {
            return save(path, object).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save data to Firebase", e);
        }
    }

    public void saveWithKeySync(String path, String key, Object object) {
        try {
            saveWithKey(path, key, object).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save data to Firebase", e);
        }
    }

    public void deleteSync(String path) {
        try {
            delete(path).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete data from Firebase", e);
        }
    }

    public boolean existsSync(String path) {
        try {
            return exists(path).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to check existence in Firebase", e);
        }
    }

    public Long countSync(String path) {
        try {
            return count(path).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to count data in Firebase", e);
        }
    }
}

