package org.fmz.spring;


public class HelloWorldPostProcessor {
   private String message;

   public void setMessage(String message){
      this.message  = message;
   }

   public void getMessage(){
      System.out.println("Your Message : " + message);
   }

   public void init(){
       System.out.println("This is the init() method of HelloWorld.");
   }

   public void destroy(){
       System.out.println("This is the destroy() method of HelloWorld.");
   }
}
