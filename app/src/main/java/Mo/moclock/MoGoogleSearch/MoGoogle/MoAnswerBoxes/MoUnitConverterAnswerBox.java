package Mo.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import Mo.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;


import java.util.ArrayList;

public class MoUnitConverterAnswerBox extends MoAnswerBox {


    //public static final String


    public MoUnitConverterAnswerBox(String d) {
        super(d);
    }

    @Override
    public ArrayList<String> getSplitters() {
        return null;
    }

    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     *
     * @param args arguments that are passed to this method via other classes
     */
    @Override
    public <T> void display(T... args) {

    }
}
