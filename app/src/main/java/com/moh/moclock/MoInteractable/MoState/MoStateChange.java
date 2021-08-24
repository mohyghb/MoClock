package com.moh.moclock.MoInteractable.MoState;

public interface MoStateChange {

    /**
     * notifies an item inside recycler view
     * that it has been changed
     * @param position of the item that was changed
     */
    void notifyItemChanged(int position);


}
