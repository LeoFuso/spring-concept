package integration;

import exceptional.Exceptional;
import org.apache.commons.lang3.Validate;
import org.modelmapper.ModelMapper;
import util.stream.CollectOne;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 * This class is intended to provide solutions to problems commonly associated with operations related to
 * the DataTransferObject Pattern
 * </p>
 *
 * <p>
 * There are two important parameters in the signature: The original Class, dubbed as KeyEntity, referenced as {@link T}, and
 * the DataTransferObject Class, relative to KeyEntity, referenced as {@link D}, which extends the abstract {@link DTO} class
 * </p>
 * <p>Usage: </p>
 * <pre>
 * {@code FooDTO extends DTO<Foo, FooDTO> }
 * </pre>
 *
 * @param <T> The original class, the KeyEntity
 * @param <D> The DataTransferObject class, witch relates to the KeyEntity and extends DTO abstract class
 * @see <a href="https://martinfowler.com/eaaCatalog/dataTransferObject.html">DataTransferObject Pattern</a>
 */
public abstract class DTO<T, D extends DTO> {

	@Id
	private Object id;

	private static final ModelMapper mapper = new ModelMapper();

	protected DTO() { /* empty */ }

	/**
	 * <p>
	 * Converts an KeyEntity object to an instance of DataTransferObject
	 * </p>
	 *
	 * @param keyEntity An KeyEntity object that will be converted to a DataTransferObject object
	 * @return A newly created DataTransferObject object from the KeyEntity object passed as parameter
	 */
	public D convert(T keyEntity) {

		Class<D> classReference = this.getDataTransferObjectClass();

		D object = DTO.getObjectReference(classReference);

		Exceptional<Stream<D>> streamExceptional = Exceptional.of(() -> this.getCustomMapping(keyEntity));

		if(!streamExceptional.isPresent()){
			mapper.map(keyEntity, object);
			return object;
		}

		return streamExceptional.get()
				.collect(CollectOne.singletonCollector());
	}

	public Stream<D> getCustomMapping(T keyEntity) { throw new UnsupportedOperationException(); }

	/**
	 * <p>
	 * Used to update an KeyEntity object based on the data from this DataTransferObject object
	 * </p>
	 *
	 * @param keyEntity KeyEntity object which is going to be updated
	 * @return The updated version of the KeyEntity object passed as parameter
	 */
	public T update(T keyEntity) {
		throw new UnsupportedOperationException();
	}

	/**
	 * <p>
	 * Converts an DataTransferObject object to an instance of KeyEntity
	 * </p>
	 *
	 * @return A newly created KeyEntity object from this DataTransferObject instance
	 */
	public T instantiate() {
		throw new UnsupportedOperationException();
	}

	/**
	 * <p>
	 * Used to fetch the persisted KeyEntity object from the DataBase, if any. It's useful for the conversion
	 * of nested DataTransferObject objects
	 * </p>
	 *
	 * @return The persisted instance of the KeyEntity object referenced by, if any.
	 */
	public T getPersistedKeyEntity() {
		throw new UnsupportedOperationException();
	}

	/**
	 * <p>
	 * Returns the Class reference corresponding to the Key Entity used in the construction
	 * of this DataTransferObject object
	 * </p>
	 *
	 * <p>
	 * Refers to the first argument needed to implement the DataTransferObject abstract class
	 * </p>
	 *
	 * @return Returns the Class reference corresponding to the KeyEntity used in the
	 * construction of this DataTransferObject object
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getKeyEntityClass() {
		return (Class<T>) getTypeArguments(this.getClass()).get(0);
	}

	/**
	 * <p>
	 * Return the Class reference of the DataTransferObject implementation of the DTO abstract class
	 * </p>
	 *
	 * <p>
	 * Refers to the second argument needed to implement the DataTransferObject abstract class
	 * </p>
	 *
	 * @return Return the Class reference of the DataTransferObject implementation of the DTO abstract class
	 */
	@SuppressWarnings("unchecked")
	private Class<D> getDataTransferObjectClass() {
		return (Class<D>) getTypeArguments(this.getClass()).get(1);
	}

	/**
	 * <p>
	 * Returns the Class reference corresponding to the Key Entity used in the construction of the
	 * DataTransferObject class passed as parameter
	 * </p>
	 *
	 * <p>
	 * Refers to the second argument needed to implement the DataTransferObject abstract class
	 * </p>
	 *
	 * @param reference The Class reference from the DataTransferObject class
	 * @param <D>       DataTransferObject class
	 * @param <T>       KeyEntity class
	 * @return Returns the Class reference corresponding to the Key Entity used in the construction of the
	 * DataTransferObject class passed as parameter
	 */
	@SuppressWarnings("unchecked")
	public static <T, D extends DTO> Class<T> getKeyEntityClass(Class<D> reference) {
		return (Class<T>) DTO.getTypeArguments(reference).get(0);
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable
	 * type.
	 *
	 * @param type the type
	 * @return the underlying class
	 */
	private static Class<?> getClass(Type type) {

		if (type instanceof Class) {
			return (Class) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic
	 * base class.
	 *
	 * @param childClass the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	private static <T> List<Class<?>> getTypeArguments(Class<? extends T> childClass) {

		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();

		Class baseClass = DTO.class;
		Type type = childClass;

		/* start walking up the inheritance hierarchy until we hit baseClass */
		while (!getClass(type).equals(baseClass)) {

			if (type instanceof Class) {

				/* there is no useful information for us in raw types, so just keep going. */
				type = ((Class) type).getGenericSuperclass();

			} else {

				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();

				for (int i = 0; i < actualTypeArguments.length; i++)
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);

				if (!rawType.equals(baseClass))
					type = rawType.getGenericSuperclass();

			}
		}

		/*
		 * finally, for each actual type argument provided to baseClass, determine (if possible)
		 * the raw class for that type argument.
		 */
		Type[] actualTypeArguments;

		if (type instanceof Class)
			actualTypeArguments = ((Class) type).getTypeParameters();
		else
			actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();

		List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();

		/* resolve types by chasing down type variables. */
		for (Type baseType : actualTypeArguments) {

			while (resolvedTypes.containsKey(baseType))
				baseType = resolvedTypes.get(baseType);

			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}

	/**
	 * <p>Constructs a new object belonging to the DataTransferObject class passed as parameter.</p>
	 * <p>Used to scape the static context</p>
	 * <p>Useful for the convert methods: {@link #convert(Object)}, {@link #instantiate()} </p>
	 *
	 * @param classReference A class reference of an implementation of the DataTransferObject abstract class
	 * @return A new instance of the received DataTransferObject class
	 */
	public static <D extends DTO> D getObjectReference(@NotNull Class<D> classReference) {

		Validate.notNull(classReference);

		try {
			return classReference.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * <p>
	 * In the case of a DataTransferObject referring to an KeyEntity that is persistent in some type of
	 * database or has some unique identifier that needs to be used.
	 * </p>
	 *
	 * @param id The unique identifier, which can not be null.
	 */
	public void setId(@NotNull Object id) {
		Validate.notNull(id);
		this.id = id;
	}

	public Object getId() {
		return id;
	}
}

