package com.moh.moclock.MoInteractable.MoSelectable;

public class MoSelectableUtils {

    /**
     * selects all the items of the iterable
     * @param items iterable of selectable items
     */
    public static void selectAllItems(Iterable<? extends MoSelectableItem> items){
        turnAllItems(true,items);
    }


    /**
     * deselects all the items of the iterable
     * @param items iterable of selectable items
     */
    public static void deselectAllItems(Iterable<? extends MoSelectableItem> items){
        turnAllItems(false,items);
    }

    /**
     * changes the selected state to the boolean given
     * @param b
     * @param items
     */
    private static void turnAllItems(boolean b,Iterable<? extends MoSelectableItem> items){
        for(MoSelectableItem s : items){
            s.setSelected(b);
        }
    }




}
