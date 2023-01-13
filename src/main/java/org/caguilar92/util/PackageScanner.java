package org.caguilar92.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Candelario Aguilar Torres
 *
 * Scans the current package and sub-packages of the declared source for classes. This class can also filter the class search by annotations.
 * This class implemnts the builder pattern to instantiate objects of this class.
 *
 * Example: Scanner packageScanner = new PackageScanner.Builder
 *                                          .scan(Main.class); -> will search for all classess.
 *
 *        or  Scanner packageScanner = new PackageScanner.Builder
 *                                          .filterByAnnotation(Component.class)
 *                                          .filterByAnnotation(Service.class)
 *                                          .scan(); -> will search for classess annoated with @Component and @Service.
 *
 **/

  public class PackageScanner implements Scanner{


    private final List<Class<?>> classList; //collection of classes to be utilized by the user.
    private final List<Class<?>> annotationList; //annotations user wants to use to filter classes.



    private PackageScanner(Builder builder) {
        classList = new ArrayList<>();
        annotationList = builder.annotationsList;
        scan(builder.source.getPackageName(),classList);
    }



    public static class Builder {
        private final List<Class<?>> annotationsList = new ArrayList<>();
        private final Class<?> source;

        public Builder(Class<?> source) {
            this.source = source;
        }
        public Builder filterByAnnotation(Class<?> annotation) {
            annotationsList.add(annotation);
            return this;
        }

        public PackageScanner scan() {
            return new PackageScanner(this);
        }

    }



    private void scan(String path, List<Class<?>>classList) {




        InputStream classzInputStream = ClassLoader.getSystemResourceAsStream(path.replaceAll("[.]","/"));//must change all . seprated paths by "/" or else classloader will throw null pointer exception
        BufferedReader classzBufferedStream = new BufferedReader(new InputStreamReader(classzInputStream));

        try {

            String line;
            int packagesTraversed = 0;

            while((line = classzBufferedStream.readLine()) != null) {
                if(line.endsWith(".class")) {
                    while(packagesTraversed > 0) {
                        path = path.substring(0,path.lastIndexOf("."));
                        packagesTraversed--;
                    }
                    classList.add(classNameResolver(line,path));
                } else {
                    path += "." + line;
                    packagesTraversed++;
                    scan(path,classList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }





    private Class<?> classNameResolver(String className, String path) {
        String fullyQualifiedPath = path + "." +className.substring(0,className.lastIndexOf("."));
        Class<?> classz = null;

        try {
            classz = Class.forName(fullyQualifiedPath);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return classz;
    }

    private List<Class<?>>filterClasses() {
        List<Class<?>> filteredClasses = new ArrayList<>();
        for(Class<?> classz : classList) {
            for(Class<?> annotation : annotationList) {
                if(classz.isAnnotationPresent((Class<? extends Annotation>) annotation)) {
                    filteredClasses.add(classz);
                }
            }
        }
        return filteredClasses;
    }
    @Override
    public List<Class<?>>getContextClasses() {
        if(annotationList.isEmpty()) {
            return classList;
        }

        return filterClasses();
    }



}
