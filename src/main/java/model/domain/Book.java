package model.domain;

import model.Persisted;

import javax.persistence.Entity;

@Entity
public class Book extends Persisted {

    private String title;
    private String description;

}
