package org.fmz.spring;

//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.DisposableBean;

public class HelloWorldScope {
   private String message;

   public void setMessage(String message){
      this.message  = message;
   }

   public void getMessage(){
      System.out.println("Your Message : " + message);
   }

   /*
   @Override
   public void afterPropertiesSet(){
       System.out.println("This is the afterPropertiesSet() method of InitializingBean.");
   }
   */

   public void init(){
       System.out.println("This is the init() method of HelloWorld.");
   }

   public void destroy(){
       System.out.println("This is the destroy() method of HelloWorld.");
   }
}
