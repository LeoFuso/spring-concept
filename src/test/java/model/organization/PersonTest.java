package model.organization;

import org.junit.Test;

import static org.junit.Assert.*;

public class PersonTest {

    @Test
    public void testCreatingAPerson(){

        String socialName = null;
        String treatmentPronoun = "Prof. Dr.";
        String firstName = "Leonardo";
        String middleName = "Fuso Martins";
        String lastName = "Nuzzo";

        Person person = new Person(socialName,treatmentPronoun, firstName, middleName, lastName);

        String firstAndLastName = person.getFirstAndLastName();
        String fullName = person.getFullName();
        String abreviation = person.getShortName();
    }


}