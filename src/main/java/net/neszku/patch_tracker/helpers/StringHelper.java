package net.neszku.patch_tracker.helpers;

import info.debatty.java.stringsimilarity.Cosine;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringHelper {

    /**
     * Sorts object by similarity of their string identifiers
     *
     * @param src string to compare
     * @param strings collection of objects
     * @param below similarity varies between 0.0D and 1.0D (lower = better)
     *              so this variable allows u to select for instance values
     *              that are equal or below 0.1D (pretty close to original string)
     * @param unwrapper function to unwrap string from object
     * @param <T> object type
     * @return sorted descending set
     */
    public static <T> Set<T> sortBySimilarity(String src, Collection<T> strings, double below, Function<T, String> unwrapper) {
        Cosine d = new Cosine();
        return strings.stream()
                .map(t -> new AbstractMap.SimpleEntry<>(t,
                    d.distance(
                        src.toLowerCase(),
                        unwrapper.apply(t).toLowerCase()
                    )
                ))
                .sorted(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue))
                .filter(entry -> entry.getValue() <= below)
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Sorts object by similarity of their string identifiers
     *
     * @param src string to compare
     * @param strings collection of objects
     * @param unwrapper function to unwrap string from object
     * @param <T> object type
     * @return sorted descending set
     */
    public static <T> Set<T> sortBySimilarity(String src, Collection<T> strings, Function<T, String> unwrapper) {
        Cosine d = new Cosine();
        return strings.stream()
                .map(t -> new AbstractMap.SimpleEntry<>(t,
                    d.distance(
                        src.toLowerCase(),
                        unwrapper.apply(t).toLowerCase()
                    )
                ))
                .sorted(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue))
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
