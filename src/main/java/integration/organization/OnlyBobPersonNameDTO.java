package integration.organization;

import integration.DTO;
import model.organization.Person;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

import java.util.stream.Stream;

public class OnlyBobPersonNameDTO extends DTO<Person, OnlyBobPersonNameDTO> {

	private String fullName;
	private String shortName;

	public OnlyBobPersonNameDTO() {
		super();
	}

	@Override
	public Stream<OnlyBobPersonNameDTO> getCustomMapping(Person keyEntity) {

		final String name = "Bob";

		return Stream.of(keyEntity)
				.map(person -> {

					if(!keyEntity.getFirstName().equals(name))
						return null;

					OnlyBobPersonNameDTO dto = new OnlyBobPersonNameDTO();
					dto.setFullName(person.getFullName());
					dto.setShortName(person.getShortName());

					return dto;
				});
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
