package org.frost.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Candelario Aguilar Torres
 **/
 public class PackageScanner {

    private List<Class<?>> classList;
    private String packagePath;

    public PackageScanner(Class<?> classz) { //pass
     classList = new ArrayList<>();
     packagePath = classz.getPackageName();

    }

    public static List<Class<?>> listOfClasses() {
        InputStream classzStream = ClassLoader.getSystemResourceAsStream(this.packagePath.replaceAll("[.]","/"));//must change all . seprated paths by "/" or else classloader will throw null pointer exception
        BufferedReader classzBufferedStream = new BufferedReader(new InputStreamReader(classzStream));
        try {
            System.out.println(classzBufferedStream.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

}
