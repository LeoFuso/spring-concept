package integration.organization;

import integration.DTO;
import model.organization.Person;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class OnlyBobPersonNameDTO extends DTO<Person, OnlyBobPersonNameDTO> {

	private String fullName;
	private String shortName;

	public OnlyBobPersonNameDTO() {
		super();
	}

	@Override
	public PropertyMap<Person, OnlyBobPersonNameDTO> getPropertyMap() {

		final String name = "Bob";

		/*
		 THIS IS A DISASTER
		 I HATE MODEL MAPPER
		 THIS DOES NOT WORK AND I DON'T KNOW WHY
		 I'M VERY SAD RN
		 */

		//Condition<Person, OnlyBobPersonNameDTO> isBob = context -> context.getSource().getFirstName().equals(name);
		Condition<?, ?> isBob = (Condition<Person, OnlyBobPersonNameDTO>) context -> context.getSource().getFirstName().equals(name);

		return new PropertyMap<>() {
			@Override
			protected void configure() {
				when(isBob).map().setFullName(source.getFullName());
				when(isBob).map().setShortName(source.getShortName());
			}
		};
	}

	@Override
	protected Converter<Person, OnlyBobPersonNameDTO> getConverter() {

		/* THIS DOES NOT WORK EITHER */
		return context -> {
			Person source = context.getSource();
			OnlyBobPersonNameDTO destination = context.getDestination();

			if (source.getFirstName().equals("Bob")) {
				destination.setFullName(source.getFullName());
				destination.setShortName(source.getShortName());
				return destination;
			} else
				return null;
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
