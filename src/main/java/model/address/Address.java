package model.address;

/**
 * <p>Created by <a href="https://github.com/LeoFuso">Leonardo Fuso</a> on <i>25/05/2018</i></p>
 * <p>
 * Created in order to provide a unified interface for the various types of address that an organization may have,
 * and their respective behaviors.
 * </p>
 */
public interface Address {

    boolean validate();

}
