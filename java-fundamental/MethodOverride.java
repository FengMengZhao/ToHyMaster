package org.fmz.test;

public class MethodOverride{

    public void override(){
        System.out.println("This is the base class methond.");
    }
}

class ChildMethodOverride extends MethodOverride{
    
    public int override(){
        System.out.println("This is the child class methond.");
    }
}
