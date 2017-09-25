package org.fmz.test;

public class Initialization{

    public int num = 1;
    public static int count;

    public Initialization(){
        System.out.println("I am a constructor"); 
        num = 2;
    }
    static {
        System.out.println("I am a static block!");
    }

    public static void main(String args[]) throws Exception{
        //Initialization initial = new Initialization();
        Class.forName("org.fmz.test.Initialization").newInstance();
    }
}
