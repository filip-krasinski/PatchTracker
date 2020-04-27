package net.neszku.patch_tracker.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class Reflections {

    /**
     * Gets the classes contained in certain package
     * that are subtypes of provided class and then
     * tries to instantiate it
     *
     * @param packageName - package to look from
     * @param subtype - the subtype class to match
     * @throws IllegalStateException if failed to instantiate class
     * @return set of instantiated classes
     */
    public static <T> Set<T> getAndInstantiate(String packageName, Class<T> subtype) {
        return getAndInstantiate(packageName, subtype, null);
    }

    /**
     * Gets the classes contained in certain package
     * that are subtypes of provided class and then
     * tries to instantiate it
     *
     * @param packageName - package to look from
     * @param subtype - the subtype class to match
     * @param filter - the filter
     * @throws IllegalStateException if failed to instantiate class
     * @return set of instantiated classes
     */
    public static <T> Set<T> getAndInstantiate(String packageName, Class<T> subtype, ClassFilter filter) {
        return getClassesInPackage(packageName, filter).stream()
                .filter(subtype::isAssignableFrom)
                .filter(clazz -> !clazz.equals(subtype))
                .map(clazz -> {
                    try {
                        return (T) clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Lists .class files contained in certain package
     *
     * @param packageName - package to look from
     * @return list of .class files
     */
    public static Set<Class<?>> getClassesInPackage(String packageName, ClassFilter filter) {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace(".", "/");
        File jar = new File(Reflections.class.getProtectionDomain().getCodeSource().getLocation().getFile());

        try (JarInputStream stream = new JarInputStream(new FileInputStream(jar))) {

            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                String name = entry.getName();

                if (name.endsWith(".class") && name.contains(path)) {
                    String classPath = name.substring(0, entry.getName().length() - ".class".length())
                            .replaceAll("[|/]", ".");

                    Class<?> clazz = Class.forName(classPath);
                    if (filter == null) {
                        classes.add(clazz);
                        continue;
                    }

                    if (!filter.isFiltered(clazz)) {
                        classes.add(clazz);
                    }
                }

            }

        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return classes;
    }

}