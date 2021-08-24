package com.moh.moclock.MoGoogleSearch.MoComplexData;

import java.util.ArrayList;
import java.util.List;

public class MoComplexNode extends MoGenericNode {

    private List<MoComplexNode> nodes;

    private boolean isBaseNode;

    public MoComplexNode(String d) {
        super(d);
        this.nodes = new ArrayList<>();
    }


    public MoComplexNode() {
        super("");
        this.nodes = new ArrayList<>();
        this.isBaseNode = true;
    }

    public void add(MoComplexNode mgd) {
        this.nodes.add(mgd);
    }



    public MoComplexNode get(Integer ... indexes) {
        if (indexes.length == 0 || nodes.isEmpty()) {
            return this;
        }
        return nodes.get(indexes[0]).get(1,indexes);
    }

    // getting an element from complex node
    private MoComplexNode get(Integer index, Integer ... indexes) {
        if (indexes.length == index) {
            return this;
        }
        return nodes.get(indexes[index]).get(index + 1,indexes);
    }


    //produces all the value nodes from
    // that complex node outerside (does not go in)
    public ArrayList<String> getValues(Integer ... indexes) {
        MoComplexNode complexNode = this.get(indexes);
        ArrayList<String> values = new ArrayList<>();
        for (MoComplexNode moComplexNode: complexNode.nodes) {
            values.add(moComplexNode.value);
        }
        return values;
    }

    // returns every sub value as well
    public ArrayList<String> getAllValues() {
        ArrayList<String> values = new ArrayList<>();
        if(!this.isBaseNode) {
            // only add it when it is not the first branch
            values.add(this.value);
        }
        for (MoComplexNode complexNode: this.nodes) {
            values.addAll(complexNode.getAllValues());
        }
        return values;
    }





    @Override
    public <T> void display(T... args) {
        T t = (T)(Object) args[0];
        int indentLevel = (int)(Object) t;
        for (int i = 0; i < indentLevel;i++) {
            System.out.print(" ");
        }
        System.out.print(this.value);
        System.out.println();
        for (MoGenericNode mgn : this.nodes) {
            mgn.display(indentLevel + 5);
        }
    }
}
