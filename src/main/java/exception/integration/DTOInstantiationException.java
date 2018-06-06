package exception.integration;

import integration.DTO;

public class DTOInstantiationException extends RuntimeException {

	/**
	 * <p>
	 * This exception should be thrown in cases where it is not possible to instantiate an entity
	 * class from a DataTransferObject
	 * </p>
	 *
	 * @param entity   Referenced Entity
	 * @param dtoClass Class that threw the exception
	 */
	public DTOInstantiationException(Class<?> entity, Class<? extends DTO> dtoClass) {
		super("Class " + entity.getSimpleName() + " can not be instantiated from DataTransferObject " + dtoClass.getSimpleName());
	}

	public DTOInstantiationException(String message) {
		super(message);
	}

	public DTOInstantiationException() {
	}
}
