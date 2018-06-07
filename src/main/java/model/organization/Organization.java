package model.organization;

import model.Persisted;
import model.address.Address;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Created by <a href="https://github.com/LeoFuso">Leonardo Fuso</a> on <i>25/05/2018</i></p>
 * <p>Represents a grouping of common attributes across multiple organizational scope organization types</p>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Organization extends Persisted {

	private String primaryName;
	private String secondaryName;

	@JoinTable
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Address> addressSet;

	public Organization(String primaryName, String secondaryName) {
		this.primaryName = primaryName;
		this.secondaryName = secondaryName;
		this.addressSet = new HashSet<>();
	}

	public Organization() {
		this.addressSet = new HashSet<>();
	}
}
