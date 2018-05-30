package model.organization;

import org.junit.Test;

import static org.junit.Assert.*;

public class PersonTest {

	@Test
	public void testCreatingAPersonAndNameMethods() {

		Person erick;
		String erickSocialName = "Erick Ferdinand";
		String erickTreatmentPronoun = "Prof. Dr.";
		String erickFirstName = "Erick";
		String erickMiddleName = "Joshua de Martins";
		String erickLastName = "Ferdinand";

		erick = new Person(erickSocialName, erickTreatmentPronoun, erickFirstName, erickMiddleName, erickLastName);

		String actualFirstAndLastName = erick.getFirstAndLastName();
		String expectedFirstAndLastName = "Prof. Dr. Erick Ferdinand";
		assertEquals(expectedFirstAndLastName, actualFirstAndLastName);

		String actualFullName = erick.getFullName();
		String expectedFullName = "Prof. Dr. Erick Joshua de Martins Ferdinand";
		assertEquals(expectedFullName, actualFullName);

		String actualShortName = erick.getShortName();
		String expectedShortName = "Prof. Dr. E. M. Ferdinand";
		assertEquals(expectedShortName, actualShortName);

	}

}