package model.address;

import lombok.Data;
import lombok.NonNull;
import model.Persisted;
import org.apache.commons.lang3.NotImplementedException;

import javax.persistence.Entity;

/**
 * <p>Created by <a href="https://github.com/LeoFuso">Leonardo Fuso</a> on <i>27/05/2018</i></p>
 * <p>
 * This class presents the concept of a phone number and all its behavior associated with it.
 * </p>
 * <p>
 * For this, it uses the standard defined by the International Telecommunication Union (ITU),
 * more specifically the implementation of this standard used in North America. The North American Numbering Plan (NANP).
 * </p>
 * <p><i>
 * It is strongly encouraged that you override this class and its methods if you wish to use specific validation rules
 * for other countries. This is valid since the implementation of the Telephone numbering plan can vary
 * from country to country.
 * </i></p>
 */
@Data
@Entity
public class PhoneAddress extends Persisted implements Address {
    /**
     * <p>As defined in the Telephone numbering plan, this attribute represents the first part of a well-formed telephone number.</p>
     * <p>In this case, the code of the country where the number exists.</p>
     */
    @NonNull
    private String countryCode;

    /**
     * <p>As defined in the Telephone numbering plan, this attribute represents the second part of a well-formed telephone number.</p>
     * <p>In this case, the code of the area inside a country where the number exists.</p>
     */
    @NonNull
    private String areaCode;

    /**
     * <p>As defined in the Telephone numbering plan, this attribute represents the third part of a well-formed telephone number.</p>
     * <p>
     * The actual device number that the object of this class will refer to.
     * </p>
     */
    @NonNull
    private String number;

    @Override
    public boolean validate() {
        throw new NotImplementedException("Not implemented yet");
    }
}
