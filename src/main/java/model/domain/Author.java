package model.domain;

import model.Persisted;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Author extends Persisted {

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private InternetAddress email;

    /**
     * Hibernate does not have a good implementation of the DELETE operation
     * for the ArrayList class, the implementation for the HashSet class
     * is much more efficient.
     *
     * @see <a href="https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/"> A blog post demonstrating this </a>
     */
    @JoinTable
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Book> bookSet;

    public Author() {
        this.bookSet = new HashSet<>();
    }

    public Author(String firstName, String lastName, String email) throws AddressException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = new InternetAddress(email);
        this.bookSet = new HashSet<>();
    }

}
