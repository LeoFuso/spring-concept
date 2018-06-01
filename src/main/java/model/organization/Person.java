package model.organization;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;

@Entity
public class Person extends Organization {

	/**
	 * <p>For cases like: Dr, Dra, Mr, etc.</p>
	 */
	@Getter
	@Setter
	private String treatmentPronoun;

	/**
	 * <p>Only the first name of a person</p>
	 */
	@Getter
	private String firstName;

	/**
	 * <p>All the names of a person, except the first and last, separated by a space.</p>
	 */
	@Getter
	@Setter
	private String middleName;

	/**
	 * <p>Only the last name of a person</p>
	 */
	@Getter
	private String lastName;

	/**
	 * @param socialName       <p>The <b>Social Name</b>, if any, and if the person preferred to use it instead of its official name.</p>
	 *                         <p>There are no validation rules, nor preferable format for this parameter. It is a free parameter and can be null.</p>
	 * @param treatmentPronoun <p>It's okay to be null. There is no associated validation or standardization.</p>
	 * @param firstName        <p>It can not be null. It is used as validation, along with the last name.</p>
	 * @param middleName       <p>It can be null, it can also be composed of several words, without any specific format.</p>
	 * @param lastName         <p> It can not be null. It is used as validation, along with the first name. </p>
	 */
	public Person(String socialName, String treatmentPronoun, String firstName, String middleName, String lastName) {

		super(Person.setupPrimaryName(firstName, middleName, lastName), socialName);

		this.treatmentPronoun = treatmentPronoun;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;

	}

	private static String setupPrimaryName(String firstName, String middleName, String lastName) {

		Validate.notNull(firstName, lastName);

		StringBuilder builder = new StringBuilder();
		builder.append(firstName).append(" ");
		if (middleName != null)
			builder.append(middleName).append(" ");
		return builder.append(lastName).toString();
	}

	public void setFirstName(String firstName) {
		Validate.notNull(firstName);
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		Validate.notNull(lastName);
		this.lastName = lastName;
	}

	public String getFullName() {
		StringBuilder builder = new StringBuilder();

		if (this.treatmentPronoun != null)
			builder.append(this.treatmentPronoun).append(" ");

		builder.append(this.firstName).append(" ");

		if (this.middleName != null)
			builder.append(this.middleName).append(" ");

		return builder.append(this.lastName).toString();
	}

	public String getFirstAndLastName() {

		StringBuilder builder = new StringBuilder();

		if (this.treatmentPronoun != null)
			builder.append(this.treatmentPronoun).append(" ");

		return builder
				.append(this.firstName).append(" ")
				.append(this.lastName).toString();
	}

	public String getShortName() {

		String firstPart;
		/* Treatment methods are not abbreviated */
		if (this.treatmentPronoun != null)
			firstPart = this.treatmentPronoun + " ";
		else
			firstPart = "";

		/* Returns the first letter of the first name in the correct form */
		String secondPart = this.firstName.substring(0, 1).toUpperCase() + ". ";

		String thirdPart;
		if (this.middleName != null) {

			/* Returns the last middle name in the correct format */
			String middleNames[] = middleName.split(" ");

			thirdPart = middleNames[middleNames.length - 1];
			thirdPart = thirdPart.substring(0, 1).toUpperCase() + ". ";

		} else
			thirdPart = "";

		StringBuilder builder = new StringBuilder();

		return builder
				.append(firstPart)
				.append(secondPart)
				.append(thirdPart)
				.append(lastName).toString();
	}
}
