package com.moh.moclock.MoGoogleSearch.MoComplexData;

public class MoSimpleNode extends MoGenericNode {


    // end of a node
    public MoSimpleNode(String d) {
        super(d);
    }


    @Override
    public <T> void display(T... args) {
        T t = (T)(Object) args[0];
        int indentLevel = (int)(Object) t;
        for (int i = 0; i < indentLevel;i++) {
            System.out.print(" ");
        }
        System.out.println(this.value);
    }
}
