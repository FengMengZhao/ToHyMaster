package org.fmz.spring;

public class HelloWorld {
    private String name;
 
    public void setName(String name) {
        this.name = name;
    }
    
    public void sayHello(){
        System.out.println("welcome "+ name +" to spring world...");
    }
}