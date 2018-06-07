package model.address;

import model.Persisted;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Entity;

@Entity
public class VirtualAddress extends Persisted implements Address {

	/**
	 *
	 */
	private InternetAddress internetAddress;

	/**
	 * <p>
	 * This method uses the {@link InternetAddress#validate() validate()} function
	 * to validate the {@link VirtualAddress#internetAddress internetAddress} attribute.
	 * </p>
	 *
	 * @return Either internetAddress is valid or not
	 */
	@Override
	public boolean validate() {
		try {
			this.internetAddress.validate();
			return true;
		} catch (AddressException ignored) {
			return false;
		}
	}
}
