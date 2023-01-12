package org.frost;


import org.frost.util.PackageScanner;
import org.frost.util.annotations.Component;
import org.frost.util.annotations.Repository;
import org.frost.util.annotations.Service;

/**
 * @author Candelario Aguilar Torres
 **/

public class Main {
    public static void main(String[] args)  {

        PackageScanner packageScanner = new PackageScanner.Builder(Main.class)
                .filterByAnnotation(Component.class)
                .filterByAnnotation(Service.class)
                .filterByAnnotation(Repository.class)
                .scan();
        System.out.println(packageScanner.getContextClasses());



    }
}