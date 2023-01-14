package org.caguilar92;


import org.caguilar92.util.PackageScanner;

/**
 * @author Candelario Aguilar Torres
 **/

public class Main {
    public static void main(String[] args)  {
        PackageScanner scanner = new PackageScanner.Builder(Main.class)
                .scan();


        scanner.getContextClasses();






    }
}