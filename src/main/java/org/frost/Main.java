package org.frost;

import org.frost.util.AnnotationScanner;
import org.frost.util.Component;


/**
 * @author Candelario Aguilar Torres
 **/
@Component
public class Main {
    public static void main(String[] args) {
        AnnotationScanner packageScanner = new AnnotationScanner();
        System.out.println(packageScanner.findAnnotatedClasses(Main.class,Component.class));

    }
}