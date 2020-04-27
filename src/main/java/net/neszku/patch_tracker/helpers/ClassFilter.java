package net.neszku.patch_tracker.helpers;

import java.util.HashSet;
import java.util.Set;

public class ClassFilter {

    private Set<Class<?>> excludedClasses;

    public ClassFilter() {
        this.excludedClasses = new HashSet<>();
    }

    public ClassFilter exclude(Class<?> clazz) {
        excludedClasses.add(clazz);
        return this;
    }

    public boolean isFiltered(Class<?> clazz) {
        return excludedClasses.contains(clazz);
    }

}
