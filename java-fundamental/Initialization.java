package org.fmz.test;

public class Initialization{

    public int num;
    public static int count;

    public Initialization(){
        System.out.println("I am a constructor"); 
    }
    static {
        System.out.println("I am a static block!");
    }

    public static void main(String args[]){
        //Initialization initial = new Initialization();
        Class c = Class.forName("java.util.LinkedList");
    }
}
