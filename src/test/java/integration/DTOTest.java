package integration;

import integration.organization.OnlyBobPersonNameDTO;
import integration.organization.PersonNameDTO;
import model.organization.Person;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DTOTest {

	@Test
	public void getKeyEntityClass() {

		Class entityClass = Person.class;
		Class<PersonNameDTO> dtoClass = PersonNameDTO.class;
		assertEquals(entityClass, DTO.getKeyEntityClass(dtoClass));

		PersonNameDTO personName = new PersonNameDTO();
		assertEquals(entityClass, personName.getKeyEntityClass());

	}

	@Test
	public void convert() {

		Person erick;
		String erickSocialName = "Erick Ferdinand";
		String erickTreatmentPronoun = "Prof. Dr.";
		String erickFirstName = "Erick";
		String erickMiddleName = "Joshua de Martins";
		String erickLastName = "Ferdinand";

		erick = new Person(erickSocialName, erickTreatmentPronoun, erickFirstName, erickMiddleName, erickLastName);

		PersonNameDTO erickNameDTO = DTO.getObjectReference(PersonNameDTO.class);
		erickNameDTO = erickNameDTO.convert(erick);

		String actualShortName = erickNameDTO.getShortName();
		String expectedShortName = "Prof. Dr. E. M. Ferdinand";
		assertEquals(expectedShortName, actualShortName);

		String actualFullName = erickNameDTO.getFullName();
		String expectedFullName = "Prof. Dr. Erick Joshua de Martins Ferdinand";
		assertEquals(expectedFullName, actualFullName);

	}

	@Test
	public void customConvert() {

		String actualShortName;
		String expectedShortName;

		String actualFullName;
		String expectedFullName;

		Person bob;
		String bobSocialName = "Bob Ferdinand";
		String bobTreatmentPronoun = "Prof. Dr.";
		String bobFirstName = "Bob";
		String bobMiddleName = "Joshua de Martins";
		String bobLastName = "Ferdinand";

		bob = new Person(bobSocialName, bobTreatmentPronoun, bobFirstName, bobMiddleName, bobLastName);

		OnlyBobPersonNameDTO onlyBobNameDTO = DTO.getObjectReference(OnlyBobPersonNameDTO.class);
		onlyBobNameDTO = onlyBobNameDTO.convert(bob);

		actualShortName = onlyBobNameDTO.getShortName();
		expectedShortName = "Prof. Dr. B. M. Ferdinand";
		assertEquals(expectedShortName, actualShortName);

		actualFullName = onlyBobNameDTO.getFullName();
		expectedFullName = "Prof. Dr. Bob Joshua de Martins Ferdinand";
		assertEquals(expectedFullName, actualFullName);

		Person erick;
		String erickSocialName = "Erick Ferdinand";
		String erickTreatmentPronoun = "Prof. Dr.";
		String erickFirstName = "Erick";
		String erickMiddleName = "Joshua de Martins";
		String erickLastName = "Ferdinand";

		erick = new Person(erickSocialName, erickTreatmentPronoun, erickFirstName, erickMiddleName, erickLastName);

		onlyBobNameDTO = onlyBobNameDTO.convert(erick);

		assertNull(onlyBobNameDTO);

	}
}