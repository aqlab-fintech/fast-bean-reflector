package com.aqlab.dynamicobjectproperties.util;

import com.aqlab.dynamicobjectproperties.property.BeanProperty;
import com.aqlab.dynamicobjectproperties.property.BeanPropertyFactory;
import com.aqlab.dynamicobjectproperties.property.ObjectProperty;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Methods which utilizes the Dynamic Object Properies (DOP) framework
 */
public final class ObjectPropertyUtils {
    private ObjectPropertyUtils() {
    }

    /**
     * Create a view over the target object which implements {@code java.util.Map}. The unique identifiers of the properties will be the keys of the resulting map.
     * <p>
     * Changes to the target object directly or through the map are reflected immediately both ways.
     *
     * @param targetObject the target object
     * @param properties   a collection of BeanProperty instances to be exposed
     * @param <ObjectT>    the target object type generic argument
     * @return a map backed by the provided {@code targetObject}
     */
    public static <ObjectT> Map<String, Object> createMap(final ObjectT targetObject, final Collection<ObjectProperty<ObjectT>> properties) {
        return createMap(targetObject, properties, ObjectProperty::getUniqueIdentifier);
    }


    /**
     * Create a view over the target object which implements {@code java.util.Map}. The keys are generated by the keyGenerator function.
     * <p>
     * Changes to the target object directly or through the map are reflected immediately both ways.
     *
     * @param targetObject the target object
     * @param properties   a collection of ObjectProperty instances to be exposed
     * @param keyGenerator the function to generate keys for the resulting map
     * @param <ObjectT>    the target object type generic argument
     * @return a map backed by the provided {@code targetObject}
     */
    public static <ObjectT> Map<String, Object> createMap(final ObjectT targetObject, final Collection<ObjectProperty<ObjectT>> properties, final Function<ObjectProperty<ObjectT>, String> keyGenerator) {
        return new PropertyMap<>(targetObject, properties.stream().collect(Collectors.toMap(keyGenerator, p -> p)));
    }

    /**
     * Create a view over the target object which implements {@code java.util.Map}.
     * <p>
     * Changes to the target object directly or through the map are reflected immediately both ways.
     *
     * @param targetObject  the target object
     * @param propertiesMap a map of ObjectProperty instances. The keys of this map will be the keys of the resulting map.
     * @param <ObjectT>     the target object type generic argument
     * @return a map backed by the provided {@code targetObject}
     */
    public static <ObjectT> Map<String, Object> createMap(final ObjectT targetObject, final Map<String, ObjectProperty<ObjectT>> propertiesMap) {
        return new PropertyMap<>(targetObject, propertiesMap);
    }

    /**
     * Create a view over the target object which implements {@code java.util.Map}. The bean property names will be the keys of the resulting map.
     * <p>
     * Changes to the target object directly or through the map are reflected immediately both ways.
     *
     * @param targetObject the target object
     * @param properties   a collection of BeanProperty instances to be exposed
     * @param <ObjectT>    the target object type generic argument
     * @return a map backed by the provided {@code targetObject}
     */
    public static <ObjectT> Map<String, Object> createMapFromBeanProperties(final ObjectT targetObject, final Collection<BeanProperty<ObjectT>> properties) {
        return new PropertyMap<>(targetObject, properties.stream().collect(Collectors.toMap(BeanProperty::getPropertyName, p -> p)));
    }

    /**
     * Create a view over the target object which implements {@code java.util.List}.
     * <p>
     * Changes to the target object directly or through the list are reflected immediately both ways.
     *
     * @param targetObject the target object
     * @param propertyList a list of ObjectProperty instances. The ordering of this list is the ordering of the resulting list.
     * @param <ObjectT>    the target object type generic argument
     * @return a map backed by the provided {@code targetObject}
     */
    public static <ObjectT> List<Object> createList(final ObjectT targetObject, final List<ObjectProperty<ObjectT>> propertyList) {
        return new PropertyList<>(targetObject, propertyList);
    }

    /**
     * Compare 2 objects
     *
     * @param o1         target object 1
     * @param o2         target object 2
     * @param properties a collection of properties which values will be compared
     * @param <ObjectT>  the target object type generic argument
     * @return unique {@link ObjectProperty}s which the given objects have different values
     */
    public static <ObjectT> Collection<ObjectProperty<ObjectT>> diff(final ObjectT o1, final ObjectT o2, final Collection<? extends ObjectProperty<ObjectT>> properties) {
        if (o1 == o2) {
            return Collections.emptySet();
        }
        if (o1 == null || o2 == null) {
            return (Collection) properties;
        }
        return properties.stream().filter(p -> !Objects.equals(p.get(o1), p.get(o2))).collect(Collectors.toSet());
    }

    /**
     * Compare 2 objects
     *
     * @param o1         target object 1
     * @param o2         target object 2
     * @param objectType the target object type. To be used as the parameter for {@link BeanPropertyFactory#getAllBeanProperties} for a list of properties to compare.
     * @param <ObjectT>  the target object type generic argument
     * @return unique {@link ObjectProperty}s which the given objects have different values
     */
    public static <ObjectT> Collection<ObjectProperty<ObjectT>> diff(final ObjectT o1, final ObjectT o2, final Class<?> objectType) {
        return diff(o1, o2, BeanPropertyFactory.INSTANCE.getAllBeanProperties(objectType));
    }
}
