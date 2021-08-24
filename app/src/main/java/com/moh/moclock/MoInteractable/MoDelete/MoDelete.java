package com.moh.moclock.MoInteractable.MoDelete;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moh.moclock.MoAnimation.MoAnimation;
import com.moh.moclock.MoRunnable.MoRunnableUtils;


// this class is used to make a recycler view or a list view's items deletable
// uses MoAnimation
public class MoDelete {

    private Activity activity;

    // delete views that are only shown when the
    // delete mode is on
    private View[] deleteViews;
    // normal views that are shown to the user when
    // it is not in delete mode
    private View[] normalViews;

    // shows the total items selected
    private TextView counterTextView;
    private String counterMessage;
    // select all button
    private CheckBox selectAllCheckBox;
    // canceling the delete mode
    private Button cancelButton;
    // performing the actual delete
    private Button deleteButton;
    //progress bar for visual purposes (indeterminute)
    private ProgressBar progressBar;

    private Runnable onDeletePressed;


    // mutual class
    private MoListDeletable listAdapter;

    private int visibleAnimation;
    private int goneAnimation;
    private int visible = View.VISIBLE;
    private int invisible = View.INVISIBLE;
    private boolean isInDeleteMode = false;
    private boolean showOneActionAtTime = true;
    private int selectedSize;
    private String deleteMessage = "Delete was successful";




    public MoDelete(Activity a,MoListDeletable listDeletable){
        this.activity = a;
        this.listAdapter = listDeletable;
    }

    public MoDelete setListDeletable(MoListDeletable listDeletable){
        this.listAdapter = listDeletable;
        return this;
    }

    public MoDelete setDeleteViews(int ... views){
        this.deleteViews = new View[views.length];
        for(int i = 0;i < views.length; i++){
            View view = this.activity.findViewById(views[i]);
            this.deleteViews[i] = view;
        }
        return this;
    }

    public MoDelete setNormalViews(int ... views){
        this.normalViews = new View[views.length];
        for(int i = 0;i < views.length; i++){
            View view = this.activity.findViewById(views[i]);
            this.normalViews[i] = view;
        }
        return this;
    }


    public MoDelete setVisibleAnimation(int a){
        this.visibleAnimation = a;
        return this;
    }

    public MoDelete setGoneAnimation(int a){
        this.goneAnimation = a;
        return this;
    }


    // set counter of items
    public MoDelete setCounterView(int ctv,String message){
        this.counterTextView = activity.findViewById(ctv);
        this.counterMessage = message;
        return this;
    }


    public MoDelete setCancelButton(int cancelButton){
        this.cancelButton = activity.findViewById(cancelButton);
        this.cancelButton.setOnClickListener(view -> cancel());
        return this;
    }

    public MoDelete setDeleteButton(int deleteButton){
        this.deleteButton = activity.findViewById(deleteButton);
        this.deleteButton.setOnClickListener(view -> {
            progressBar.setIndeterminate(true);
            // delete
            Handler h = new Handler();
            h.postDelayed(() -> {
                listAdapter.deleteSelected();
                progressBar.setIndeterminate(false);
                cancel();
                MoRunnableUtils.runIfNotNull(onDeletePressed);
                Toast.makeText(activity, deleteMessage, Toast.LENGTH_SHORT).show();
            },100);
        });
        return this;
    }

    public MoDelete setProgressBar(int progressBar){
        this.progressBar = activity.findViewById(progressBar);
        this.progressBar.setIndeterminate(false);
        return this;
    }

    public MoDelete setOnDeletePressed(Runnable r){
        this.onDeletePressed = r;
        return this;
    }

    public MoDelete showOnlyOneActionAtTime(boolean b){
        this.showOneActionAtTime = b;
        return this;
    }

    public MoDelete setSelectAllCheckBox(int selectAll){
        this.selectAllCheckBox = activity.findViewById(selectAll);
        this.selectAllCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!compoundButton.isPressed()){
                // return if the actual button was not pressed
                // we might have changed the checked state
                // based on what we saw fit at that time
                return;
            }
            if(b){
                selectAll();
            }else{
                deselectAll();
            }
        });
        return this;
    }






    /**
     * shows the delete views and hides the normal views
     */
    private void activateDeleteMode(){
        for(View v: deleteViews){
            MoAnimation.animateNoTag(v,visible,visibleAnimation);
        }
        for(View v: normalViews){
            MoAnimation.animateNoTag(v,invisible,goneAnimation);
        }
        updateCounter();
    }


    /**
     * shows the normal views and hides the delete views
     */
    private void deactivateDeleteMode(){
        for(View v: deleteViews){
            MoAnimation.animateNoTag(v,invisible,goneAnimation);
        }
        for(View v: normalViews){
            MoAnimation.animateNoTag(v,visible,visibleAnimation);
        }
        // every time we get out of it
        // we need to deselect all elements
        listAdapter.deselectAllElements();
    }


    /**
     * activates the delete mode if it is in the delete mode
     * and deactivates it if it is not in the delete mode
     * @param isInDeleteMode
     */
    public void setDeleteMode(boolean isInDeleteMode){
        // change the boolean
        this.isInDeleteMode = isInDeleteMode;
        // do other work to activate or deactivate the situation
        if(isInDeleteMode){
            activateDeleteMode();
        }else{
            deactivateDeleteMode();
        }
        // let the list know what to do when you are done
        this.listAdapter.notifySituationChanged();
    }


    /**
     * updates the counter message
     * to the size of actually selected elements
     */
    public void updateCounter(){
        if(this.counterTextView!=null){
            this.counterTextView.setText(this.selectedSize + this.counterMessage);
        }

    }



    private void selectAll(){
        this.listAdapter.selectAllElements();
    }

    private void deselectAll(){
        this.listAdapter.deselectAllElements();
    }


    public boolean isInDeleteMode() {
        return isInDeleteMode;
    }

    // returns true if this is in delete mode
    // used for when user presses back button
    // so we need to cancel this before leaving the activity
    public boolean canCancel(){
        return this.isInDeleteMode;
    }

    // cancels the delete mode
    // sets it to false
    public void cancel(){
        setDeleteMode(false);
    }

    /**
     * used when the activity is rebuilt causing the
     * delete mode to not show
     */
    public void onResume(){
        if(isInDeleteMode){
            setDeleteMode(true);
        }else{
            setDeleteMode(false);
        }
    }



    // selected size functions

    public int getSelectedSize() {
        return selectedSize;
    }

    // sets the selected size
    public void setSelectedSize(int s){
        this.selectedSize = s;
        update();
    }

    /**
     * increments the selected size if b is true (which means
     *  the element was selected)
     * or decrements it if it is false
     * @param b
     */
    public void notifySizeChange(boolean b){
        if(b){
            this.selectedSize++;
        }else{
            this.selectedSize--;
        }
        update();
    }


    /**
     * updates all the aspects of the delete mode
     */
    private void update(){
        updateCounter();
        updateSelectAll();
        updateActions();
        listAdapter.notifySituationChanged();
    }


    // for select all button
    private void updateSelectAll(){
        if(this.selectedSize == listAdapter.size()){
            // then it should be turned on
            this.selectAllCheckBox.setChecked(true);
        }else{
            this.selectAllCheckBox.setChecked(false);
        }
    }

    /**
     * updates the action buttons of the bottom bar
     */
    private void updateActions(){
        if(showOneActionAtTime){
            if(this.selectedSize > 0){
                // then delete should be shown
                this.deleteButton.setVisibility(View.VISIBLE);
                this.cancelButton.setVisibility(View.GONE);
            }else{
                // then show cancel
                this.deleteButton.setVisibility(View.GONE);
                this.cancelButton.setVisibility(View.VISIBLE);
            }
        }
    }



}
