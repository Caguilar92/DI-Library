package org.frost.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Candelario Aguilar Torres
 **/
 public class ContextConfiguration {

    private Map<Class<?>, Object> defaultContextObjects;
    private Map<Class<?>,Object> applicationContextObjects;

    private Scanner scanner;


public ContextConfiguration(Scanner scanner) {
        this.scanner = scanner;
        this.defaultContextObjects = new HashMap<>();
        this.applicationContextObjects = new HashMap<>();
        buildDefaultContextObjects();
}


    private void buildDefaultContextObjects() {
        hasDefaultConstructor(scanner.getContextClasses());



    }










        private boolean hasDefaultConstructor(List<Class<?>> contextClasses) {
            for(Class<?> classz : contextClasses) {
                boolean hasDefaultconstructor = false;
                for(Constructor<?> constructor : classz.getConstructors()) {
                    if(constructor.getParameterCount() == 0) {
                        hasDefaultconstructor = true;
                    }
                }

                if(hasDefaultconstructor == false) {
                    throw new IllegalStateException("0 args constructor required: 0 args constructor for " + classz + " not found");
                }
            }

            return true;
        }

    public Map<Class<?>, Object>getContext() {
        return applicationContextObjects;
    }

}
