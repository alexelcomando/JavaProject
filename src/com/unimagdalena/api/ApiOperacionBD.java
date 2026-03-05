
package com.unimagdalena.api;

import java.util.List;

public interface ApiOperacionBD<T> {

    public List<T> SelectFrom();

    public T insertInto(T obj, String ruta);

    public int getSerial();

    public int numRows();

    public Boolean deleteFrom(int indice);

    public Boolean updateSet(int indice, T obj, String ruta);

    public T getOne(int indice);
    
}
