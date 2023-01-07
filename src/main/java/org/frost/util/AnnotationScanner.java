package org.frost.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Candelario Aguilar Torres
 **/
public class AnnotationScanner extends PackageScanner {

    public List<Class<?>> findAnnotatedClasses(Class<?> classz, Class annotation) {
        if(classz.isAnnotation()) {
            throw new RuntimeException();
        }

        if(!classz.isAnnotation()) {
            throw new RuntimeException();
        }

        List<Class<?>> classList = listOfClasses(classz);
        List<Class<?>> annotatedClassList = new ArrayList<>();


        for(int i = 0; i < classList.size(); i++) {
            if(classList.get(i).isAnnotationPresent(annotation)) {
                annotatedClassList.add(classList.get(i));
            }
        }
        return annotatedClassList;
    }
}
