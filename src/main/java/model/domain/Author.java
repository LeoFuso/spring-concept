package model.domain;

import model.Persisted;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Author extends Persisted {

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;


}
