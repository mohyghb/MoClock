package com.moh.moclock.MoInteractable.MoDelete;



import com.moh.moclock.MoInteractable.MoSelectable.MoSelectableList;

public interface MoListDeletable extends MoSelectableList {

    /**
     * when we want to set a mo delete for that adapter
     * @param d
     */
    void setMoDelete(MoDelete d);


    /**
     * notifies the data set changed
     * either used when we are transitioning to
     * delete mode or out of it
     */
    void notifySituationChanged();

    /**
     * traverses through the list
     * and deletes the selected elements appropriately
     */
    void deleteSelected();

    /**
     *
     * @returns the size of list
     */
    int size();

}
