package com.example.service;

import java.util.List;
import java.util.Optional;

public interface DefaultService<T> {
    T save(T t);
    T update(T t);
    void delete(T t);
    List<T> findAll();
    Optional<T> findOne(long id);
}
