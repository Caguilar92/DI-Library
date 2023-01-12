package org.frost.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Candelario Aguilar Torres
 **/
 class ContextConfiguration {

    private Map<Class<?>, Object> defaultContextObjects;
    private Map<Class<?>,Object> applicationContextObjects;




    public void build(List<Class<?>>classList) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        this.defaultContextObjects = new HashMap<>();
        this.applicationContextObjects = new HashMap<>();
        buildObjects(classList);
        buildApplicationObjects();
    }

    private void buildApplicationObjects() throws InvocationTargetException, InstantiationException, IllegalAccessException {
            ArrayList<Object> dependencies = new ArrayList<>();

            for(Class<?> classz : defaultContextObjects.keySet()) {
                Constructor<?>[] constructors = classz.getConstructors();
                if(constructors.length == 1) {
                    applicationContextObjects.put(classz,defaultContextObjects.get(classz));
                } else {
                    int constParamLength = 0;
                    Constructor<?> constructor = null;
                    for(Constructor<?> c : constructors) {
                        Class<?>[] parameters = c.getParameterTypes();
                        if(constructor == null) {
                            constructor = c;
                            constParamLength = parameters.length;

                        } else if(parameters.length >= constParamLength) {
                            constructor = c;
                            constParamLength = parameters.length;
                        }

                        validateParameters(parameters);

                        Arrays.stream(parameters).map(cl -> dependencies.add(defaultContextObjects.get(cl)));







                    }
                }
                System.out.println(classz.getName() + " constructor length " + constructors.length);

            }



    }



        private boolean validateParameters(Class<?>[] parameters) {
        String name = null;
        try {
            for(Class<?> classz : parameters)  {
                name = classz.getName();
                if(!classz.isAssignableFrom(defaultContextObjects.get(classz).getClass())) {

                }
            }
            return true;

        } catch (NullPointerException e) {

            throw new RuntimeException("Failed to Construct Object: no object of type " + name + " found. classList argument passed in build() method main not contain required class");
        }


        }



    private void buildObjects(List<Class<?>> classList) {
        for(Class<?>classz: classList) {
            try {
                Constructor<?> defaultConstructor = classz.getConstructor();
                Object object = defaultConstructor.newInstance();
                this.defaultContextObjects.put(classz,object);

            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public Map<Class<?>, Object>getContext() {
        return applicationContextObjects;
    }

}
