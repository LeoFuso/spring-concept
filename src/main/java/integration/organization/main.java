package integration.organization;

import com.annimon.stream.Exceptional;
import com.annimon.stream.function.Function;
import integration.DTO;
import model.organization.Person;

public class main {

	public static void main(String[] args) {
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

		String actualFullName = erickNameDTO.getFullName();
		String expectedFullName = "Prof. Dr. Erick Joshua de Martins Ferdinand";

	}
}
