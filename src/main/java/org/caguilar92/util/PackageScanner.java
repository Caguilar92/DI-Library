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


    /*
    Example flow for the method below:
                path = "org.caguilar92"
                Package is read for any line ending with .class
                If line read does not end with .class, the line read is a package name("util" in this example).
                "util" will be appended to the path. Now path = "org.caguilar92.util" and a recursive call is made passing in this new path.
                When there are no more lines to be read in "org.caguilar92.util" control goes back to the first method call where the next line is read.
                If the new line read ends with .class, the path and package need to be passed to the classNameResovler() in order to obtain an object of the class.
                In order to this "util" needs to be removed from "org.cauilar92.util" because that path has already been scanned.
                After removal the path is now "org.caguilar92" and the class name read can be passed to the classNameResolver();


     */

    private void scan(String path, List<Class<?>>classList) {//1.



        InputStream classzInputStream = ClassLoader.getSystemResourceAsStream(path.replaceAll("[.]","/"));// path formatted to in order to be read by the ClassLoader. returns and inputstream of the package.

        BufferedReader classzBufferedStream = new BufferedReader(new InputStreamReader(classzInputStream));// wrap inputstream in BuffreredInputstream in order to read the stream one line at a time.

        try {

            String line; //stores the line read
            boolean pathWasModified = false;//tracks if path has been modified.



            while((line = classzBufferedStream.readLine()) != null) { //read the line first then checks if its null

                if(line.endsWith(".class")) {

                      if(pathWasModified) {
                          path = path.substring(0, path.lastIndexOf("."));//remove appended package name from path
                          pathWasModified = false;
                      }


                    classList.add(classNameResolver(line,path));// resolve string name to classObject and load into List.
                } else {

                    path += "." + line;//append package name to path to create a path to the new package
                    pathWasModified = true;//because the new package name was appended, pathWasModified is now equal to true;
                    scan(path,classList);//recursive call with the path to the new package
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }



    }





    private Class<?> classNameResolver(String className, String path) {

        String fullyQualifiedPath = path + "." +className.substring(0,className.lastIndexOf(".")); //concatenates the path and string name and removes the .class from the end of the string.

        Class<?> classz = null;

        try {
            classz = Class.forName(fullyQualifiedPath);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return classz;
    }

    /*
    checks the classes in the classList for any annotations specified in the annotationList.
    If class is annotated, it is added to the local filteredClasses List.
    When complete return List of filtered classes.
     */
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
        if(annotationList.isEmpty()) { //returns all classes in application if annotationList is empty.
            return classList;
        }

        return filterClasses(); //returns List of classes annotated with the annotations in the annotationList.
    }



}
