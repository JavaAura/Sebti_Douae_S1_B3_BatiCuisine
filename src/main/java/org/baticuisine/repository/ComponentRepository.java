package org.baticuisine.repository;

import org.baticuisine.entities.Component;

public interface ComponentRepository<T> {

    void addComponent(T t);
    void updateComponentTaxRate(int id, double taxRate);
}
