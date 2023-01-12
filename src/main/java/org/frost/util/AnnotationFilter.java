package org.frost.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Candelario Aguilar Torres
 **/
public class AnnotationFilter {



    public List<Class<?>> filter(List<Class<?>> classList, Class annotation ) {
        if(annotation.isAnnotation()) {
            return classList.stream().filter(c -> c.isAnnotationPresent(annotation))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException();
        }

    }
}
