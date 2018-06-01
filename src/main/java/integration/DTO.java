package integration;

import javax.persistence.Id;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DTO<T, D extends DTO> {

	@Id
	private Object id;

	/**
	 * @param entity An entity that will be converted to a DataTransferObject
	 * @return A newly created DataTransferObject from the entity passed as parameter
	 */
	public abstract D convert(T entity);

	/**
	 * @return A newly created entity object corresponding to this instance of the DTO interface
	 */
	public abstract T instantiate();

	/**
	 * @return The persistent object referenced by the implementation of this interface
	 */
	public abstract T getActualEntity();


	/**
	 * <p>Returns a reference from the Entity class</p>
	 *
	 * @return Class<T>
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		return (Class<T>) getTypeArguments(DTO.class, getClass()).get(0);
	}

	/**
	 * <p>Returns a reference from the DataTransferObject class</p>
	 *
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends DTO> getDTOClass() {
		return (Class<? extends DTO>) getTypeArguments(DTO.class, getClass()).get(1);
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
	 * @param baseClass  the base class
	 * @param childClass the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	private static <T> List<Class<?>> getTypeArguments(
			Class<T> baseClass, Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
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
}
