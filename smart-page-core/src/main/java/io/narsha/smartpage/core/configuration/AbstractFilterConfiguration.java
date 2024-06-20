package io.narsha.smartpage.core.configuration;

import io.narsha.smartpage.core.AbstractRegistrationService;
import io.narsha.smartpage.core.exceptions.InternalException;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 * Abstract class that will automatically register filters corresponding to the type parameters
 *
 * @param <E> type of the filters
 * @param <T> registration type corresponding to the filter types
 */
public abstract class AbstractFilterConfiguration<E, T extends AbstractRegistrationService<E>> {

  /** default constructor */
  public AbstractFilterConfiguration() {}

  /**
   * Trigger the auto-registration
   *
   * @param targetClass interface that we want to find all implementations
   * @return a registration service that will be init with the implementations found
   * @throws Exception if reflections Exception
   */
  protected T init(Class<E> targetClass) throws Exception {
    // TODO NO NEED CLASS<E> WE CAN GET IT FROM SIGNATURE
    var abstractFilterConfiguration = getClass().getGenericSuperclass();

    // TODO check more carefully here
    while (!(abstractFilterConfiguration instanceof ParameterizedType)
        && !((Class) abstractFilterConfiguration)
            .isAssignableFrom(AbstractFilterConfiguration.class)) {
      abstractFilterConfiguration = ((Class) abstractFilterConfiguration).getGenericSuperclass();
    }

    var type = ((ParameterizedType) abstractFilterConfiguration).getActualTypeArguments()[1];

    Class<T> persistentClass;

    if (type instanceof Class clazz) {
      persistentClass = clazz;
    } else if (type instanceof ParameterizedType parameterizedType) {
      persistentClass = (Class<T>) parameterizedType.getRawType();
    } else {
      throw new InternalException();
    }

    var registrationService = persistentClass.getConstructor().newInstance();
    var internalFilters = findInternals(targetClass);

    for (var filterFactory : internalFilters) {
      final var factoryInstance = (E) filterFactory.getConstructor().newInstance();
      registrationService.register(factoryInstance);
    }

    return registrationService;
  }

  /**
   * Will find in the same package as the targetClass parameter instances of the type targetClass
   *
   * @param targetClass the interface that we want to find the implementations
   * @return Set of the class that implements the interface targetClass
   */
  protected Set<Class<?>> findInternals(Class<?> targetClass) {
    Reflections reflections = new Reflections(targetClass.getPackageName());
    return reflections.get(Scanners.SubTypes.of(targetClass).asClass());
  }
}
