package model.domain;

import model.Persisted;

import javax.persistence.Entity;

@Entity
public class Tag extends Persisted {

	private String name;
	private String description;

}
