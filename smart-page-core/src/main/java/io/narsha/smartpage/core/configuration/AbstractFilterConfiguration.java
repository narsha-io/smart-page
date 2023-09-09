package io.narsha.smartpage.core.configuration;

import io.narsha.smartpage.core.AbstractRegistrationService;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public abstract class AbstractFilterConfiguration<E, T extends AbstractRegistrationService<E>> {

  protected T init(Class<E> targetClass) throws Exception {
    // TODO NO NEED CLASS<E> WE CAN GET IT FROM SIGNATURE
    var abstractFilterConfiguration = getClass().getGenericSuperclass();

    // TODO check more carefully here
    while (!(abstractFilterConfiguration instanceof ParameterizedType)
        && !((Class) abstractFilterConfiguration)
            .isAssignableFrom(AbstractFilterConfiguration.class)) {
      abstractFilterConfiguration = ((Class) abstractFilterConfiguration).getGenericSuperclass();
    }

    Class<T> persistentClass =
        (Class<T>) ((ParameterizedType) abstractFilterConfiguration).getActualTypeArguments()[1];

    var registrationService = persistentClass.getConstructor().newInstance();
    var internalFilters = findInternals(targetClass);

    for (var filterFactory : internalFilters) {
      final var factoryInstance = (E) filterFactory.getConstructor().newInstance();
      registrationService.register(factoryInstance);
    }

    return registrationService;
  }

  protected Set<Class<?>> findInternals(Class<?> targetClass) {
    Reflections reflections = new Reflections(targetClass.getPackageName());
    return reflections.get(Scanners.SubTypes.of(targetClass).asClass());
  }
}
