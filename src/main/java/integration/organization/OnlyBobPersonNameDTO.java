package integration.organization;

import integration.DTO;
import model.organization.Person;
import org.modelmapper.Converter;

public class OnlyBobPersonNameDTO extends DTO<Person, OnlyBobPersonNameDTO> {

	private String fullName;
	private String shortName;

	public OnlyBobPersonNameDTO() {
		super();
	}

	@Override
	public Converter<Person, OnlyBobPersonNameDTO> getConverter() {

		return context -> {

			Person person = context.getSource();
			OnlyBobPersonNameDTO dto = new OnlyBobPersonNameDTO();

			if (person.getFirstName().equals("Bob")){

				dto.setFullName(person.getFullName());
				dto.setShortName(person.getShortName());
				return dto;

			}else{
				dto.setFullName(null);
				dto.setShortName(null);
				return dto;
			}
		};
	}

	public OnlyBobPersonNameDTO(String fullName, String shortName) {
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
