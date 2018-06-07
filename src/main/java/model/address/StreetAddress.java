package model.address;

import model.Persisted;
import org.apache.commons.lang3.NotImplementedException;

import javax.persistence.Entity;

/**
 * <p>Created by <a href="https://github.com/LeoFuso">Leonardo Fuso</a> on <i>25/05/2018</i></p>
 *
 * <p>This class represents the concept of street address and all the behavior associated with this idea</p>
 */
@Entity
public class StreetAddress extends Persisted implements Address {

	/**
	 * <p>Representing the number value of a Street Address</p>
	 */
	private Integer number;

	/**
	 * <p>The actual Street Name</p>
	 */
	private String name;

	/**
	 * <p>City name, not abbreviated</p>
	 */
	private String city;

	/**
	 * <p>State name abbreviation</p>
	 */
	private String state;

	/**
	 * <p>This represents the postal code used by the country where this address is supposed to exist, and will probably be used by the validation method.</p>
	 * <p>Even though most countries have their own way of validating a postal code, they do have one, anyway.</p>
	 */
	private String postalServiceCode;

	/**
	 * <p>This method does not yet have an implementation, nor a good solution for it.</p>
	 * <p>
	 * One possible solution, as pointed out in this <a href="https://stackoverflow.com/a/28496144">answer</a>
	 * is to use a RestAPI provided by <a href="http://www.geonames.org/export/web-services.html">GeoNames</a>
	 * </p>
	 *
	 * @return Returns whether the full address is a valid street address or not.
	 */
	@Override
	public boolean validate() {
		throw new NotImplementedException("See Javadoc for details.");
	}
}
