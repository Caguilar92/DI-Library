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
 */

/**
 * Scans the current package and sub-packages of the declared source for classes. This class can also filter the class search by annotations.
 * This class implemnts the builder pattern to instantiate objects of this class.
 *
 * Example: Scanner packageScanner = new PackageScanner.Builder(Main.class)
 *                                          .scan(); -> will scan for all classess in Main.class' package and sub-packages.
 *
 *        or  Scanner packageScanner = new PackageScanner.Builder(Main.class)
 *                                          .filterByAnnotation(Component.class)
 *                                          .filterByAnnotation(Service.class)
 *                                          .scan(); -> will search for classess with annoatations provided by the user. In this example
 *                                                      classess annotated with @Component and @Service in Main.class' package and sub-packages will be scanned for.
 *
 **/

  public class PackageScanner implements Scanner{


    private final List<Class<?>> classList; //collection that will be used to load classes by the private scan() method .
    private final List<Class<?>> annotationList; //annotations user wants to use to filter classes.


    /**
     * @param builder static inner class used to build the PackageScanner Object.
     * The Constructor is private and can, in order to prevent instantiation from any outside entity.
     * Objects of this class should only be created by this class' Builder inner class.
     *
     *    Example: Scanner packageScanner = new PackageScanner.Builder(Main.class)
     *                                                         .scan(); -> when the scanners getClassContext() is called, all classes in the application will be returned.
     *
     *               Scanner packageScanner = new PackageScanner.Builder(Main.class)
     *                                                         .filterByAnnotation(Component.class)
     *                                                         .filterByAnnotation(Service.class)
     *                                                         .scan();-> when the scanners getClassContext() is called, all classes annotated with the arguments of the
     *                                                                      filterByAnnotation() method will be returned.
     *
     */
    private PackageScanner(Builder builder) {
        classList = new ArrayList<>();
        annotationList = builder.annotationsList;
        scan(builder.source.getPackageName(),classList);//pass in the path name and the reference to the list that the classes should be loaded into
    }


        /**
         * Builder class to build the PackageScanner Object. Users can get the all the classes
         * scanned in the application by only calling the .scan() method, or they can filter out classes by annotations
         * passed in the arguments of the .filterByAnnotation() within this class.
         * Note: if the .filterByAnnotation() method is used, the Scanner object will still load all
         * the classes into the arraylist member field. Only when the user request the objects through the
         * getContextClasses() will the classes be filtered out and returned.
         */

    public static class Builder {
        private final List<Class<?>> annotationsList = new ArrayList<>();
        private final Class<?> source;


        /**
         *@param source is the class within the package that is to be scanned. All packages
         *               and sub-packages the source class belongs to will be scanned.
         */
        public Builder(Class<?> source) {

            this.source = source;
        }

            /**
             *
             * @param annotation is used to filter out classes when the .getContextClasses() method is used.
             * @return the current Builder object.
             */
        public Builder filterByAnnotation(Class<?> annotation) {
            annotationsList.add(annotation);
            return this;
        }

        /**
         * Creates a PackageScanner object with Builder initialized fields.
         */

        public PackageScanner scan() {

            return new PackageScanner(this);
        }

    }



    private void scan(String path, List<Class<?>>classList) {  // path is obtained by the source.getPackageName() initialized in the private PackageScanner constructor.




        InputStream classzInputStream = ClassLoader.getSystemResourceAsStream(path.replaceAll("[.]","/"));//must change all . separated paths by "/" or else classloader will throw null pointer exception

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
            System.out.println(e.getCause());
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
