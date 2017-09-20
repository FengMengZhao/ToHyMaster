package org.fmz.spring;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.BeansException;

public class InitHelloWorld implements BeanPostProcessor {
   public Object postProcessBeforeInitialization(Object bean, String beanName) 
      throws BeansException {
      
      System.out.println("Before Initialization : " + beanName);
      return bean;  // you can return any other object as well
   }
   public Object postProcessAfterInitialization(Object bean, String beanName) 
      throws BeansException {
      
      System.out.println("After Initialization : " + beanName);
      return bean;  // you can return any other object as well
   }
}
