package integration.organization;

import integration.DTO;
import model.organization.Person;

public class PersonNameDTO extends DTO<Person, PersonNameDTO> {

	private String fullName;
	private String shortName;

	public PersonNameDTO() {
		super();
	}

	public PersonNameDTO(String fullName, String shortName) {
		this.fullName = fullName;
		this.shortName = shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
