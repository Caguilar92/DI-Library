package org.frost.util;

import org.frost.util.annotations.Component;

import javax.naming.Context;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author Candelario Aguilar Torres
 **/
 public class ApplicationContext {

     private ContextConfiguration contextConfiguration;


    private Map<Class<?>,Object> contextObjects;




     public Object getContextObject(Class<?> classname) {
        return contextObjects.get(classname);
     }

     public Map<Class<?>,Object> getContextObjects() {
         return contextObjects;
     }
     public void setContextConfiguration(ContextConfiguration contextConfiguration) {
         this.contextConfiguration = contextConfiguration;
     }
}
