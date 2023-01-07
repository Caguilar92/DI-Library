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




    private List<Class<?>> listOfClasses(String path, List<Class<?>>list) {
        List<Class<?>> classList = list;
        String packagePath = path;


        InputStream classzInputStream = ClassLoader.getSystemResourceAsStream(packagePath.replaceAll("[.]","/"));//must change all . seprated paths by "/" or else classloader will throw null pointer exception
        BufferedReader classzBufferedStream = new BufferedReader(new InputStreamReader(classzInputStream));

        try {

            String line;
            int packagesTraversed = 0;

            while((line = classzBufferedStream.readLine()) != null) {
                if(line.endsWith(".class")) {
                    while(packagesTraversed > 0) {
                        packagePath = packagePath.substring(0,packagePath.lastIndexOf("."));
                        packagesTraversed--;
                    }
                    classList.add(stringNameToClass(line,packagePath));
                } else {
                    packagePath += "." + line;
                    packagesTraversed++;
                    listOfClasses(packagePath,classList);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classList;
    }

    public List<Class<?>>listOfClasses(Class<?> classz) {
        ArrayList<Class<?>> classList= new ArrayList<>();
        String path = classz.getPackageName();
        return listOfClasses(path,classList);
    }

    private Class<?> stringNameToClass(String classzName, String path) {
        String fullyQualifiedPath = path + "." +classzName.substring(0,classzName.lastIndexOf("."));
        Class<?> classz = null;

        try {
            classz = Class.forName(fullyQualifiedPath);
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classz;
    }

}
