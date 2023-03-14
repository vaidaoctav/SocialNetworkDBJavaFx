package com.example.mysocialnetworkdb.domain.validators;

import com.example.mysocialnetworkdb.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
