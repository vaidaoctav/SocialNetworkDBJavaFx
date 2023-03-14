package com.example.mysocialnetworkdb.repository;

import com.example.mysocialnetworkdb.exceptions.RepositoryException;
import com.example.mysocialnetworkdb.exceptions.ValidationException;

public interface Repository<E, ID>{

    E find(ID id) throws RepositoryException;
    Iterable<E> findAll();
    E save(E entity) throws RepositoryException, ValidationException;
    E delete(E entity) throws RepositoryException, ValidationException;
    E update(E updateEntity) throws RepositoryException;
}
