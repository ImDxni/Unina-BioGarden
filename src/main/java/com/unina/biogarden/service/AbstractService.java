package com.unina.biogarden.service;

import com.unina.biogarden.exceptions.UtenteEsistenteException;

import java.util.Collection;

public abstract class AbstractService<T> {

    public abstract T insert(T entity) throws Exception;


    public abstract Collection<T> fetchAll();


}
