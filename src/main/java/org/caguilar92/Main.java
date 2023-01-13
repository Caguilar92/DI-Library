package org.caguilar92;


import org.caguilar92.util.ContextConfiguration;
import org.caguilar92.util.PackageScanner;
import org.caguilar92.util.Scanner;
import org.caguilar92.util.test_annotations.Component;
import org.caguilar92.util.test_annotations.Repository;
import org.caguilar92.util.test_annotations.Service;

/**
 * @author Candelario Aguilar Torres
 **/

public class Main {
    public static void main(String[] args)  {

        Scanner packageScanner = new PackageScanner.Builder(Main.class)
                .filterByAnnotation(Component.class)
                .filterByAnnotation(Service.class)
                .filterByAnnotation(Repository.class)
                .scan();




        ContextConfiguration context = new ContextConfiguration(packageScanner);




    }
}