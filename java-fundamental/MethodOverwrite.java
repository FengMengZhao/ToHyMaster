package org.fmz.test;

public class MethodOverwrite{

    public void overwrite(int one, int another){
        System.out.println('1');
    }

    public int overwrite(int one, int another, int third){
        return 0;
    }

    public static void main(String args[]){
        new MethodOverwrite().overwrite(1, 2);
        System.out.println(new MethodOverwrite().overwrite(1, 2, 3));
    }
}
