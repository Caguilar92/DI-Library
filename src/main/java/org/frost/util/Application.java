package org.frost.util;

import org.frost.util.annotations.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Candelario Aguilar Torres
 **/
public class Application {
    private static ApplicationContext applicationContext;
    private static ContextConfiguration contextConfiguration;
    private static PackageScanner packageScanner;
    private static AnnotationFilter annotationFilter;
    private static final Class annotation = Component.class;



    public static void run(Class<?> source) {

        applicationContext = new ApplicationContext();
        contextConfiguration = new ContextConfiguration();

        annotationFilter = new AnnotationFilter();




    }




    private static List<Class<?>> getAnnotatedClasses(List<Class<?>> classList) {
        return annotationFilter.filter(classList,annotation);

    }


    private static void configure(List<Class<?>> classList) {
        try {
            contextConfiguration.build(classList);

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
