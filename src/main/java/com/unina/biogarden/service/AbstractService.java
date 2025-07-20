package com.unina.biogarden.service;

import java.util.Collection;

public abstract class AbstractService<T> {

    public abstract T insert(T entity) throws Exception;


    public abstract Collection<T> fetchAll();


}
