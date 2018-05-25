package model.domain;

import model.Persisted;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Book extends Persisted {

    private String title;
    private String description;
    private String isbn;

    /**
     * @see model.domain.Author#books
     */
    @ManyToMany(mappedBy = "books")
    private Set<Author> authorSet;


}
