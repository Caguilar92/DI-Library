package org.frost;

import org.frost.util.PackageScanner;

import java.util.Scanner;

/**
 * @author Candelario Aguilar Torres
 **/
public class Main {
    public static void main(String[] args) {
        PackageScanner packageScanner = new PackageScanner();
        System.out.println(packageScanner.listOfClasses(Main.class));

    }
}