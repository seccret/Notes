package com.example.pombee.notes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity = null;
    public Profile profile;
    ListView lvNotes = null;
    private int lastVisibleItem = 0;
    private int lastY = 0;
    private boolean onTop = true;
    private boolean newNoteVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        ManagerLocalFiles.deserialzeProfile();
        if(profile == null){
            profile = new Profile();
        }


        final LinearLayout llmainActivity = findViewById(R.id.llmainActivity);
        final LinearLayout llNewNoteBackground = findViewById(R.id.llNewNoteBackground);

        ArrayList<Notes> myNotes = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();
        content.add("hej");
        content.add("då");
        for(int i = 0; i< 20; i++){
            Notes note = new Notes("Note"+ i,content, false, "checklist");
            myNotes.add(note);
        }
        profile.myNotes = myNotes;

    /*    final ListView lvNotes = findViewById(R.id.lvNotes);
        final AdapterNotesList anl = new AdapterNotesList(mainActivity,R.layout.item_note, mainActivity.profile.myNotes);
        lvNotes.setAdapter(anl);*/


        lvNotes = findViewById(R.id.lvNotes);
        final AdapterNotesList anl = new AdapterNotesList(mainActivity,R.layout.item_note, mainActivity.profile.myNotes);
        lvNotes.setAdapter(anl);
        lvNotes.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        final EditText newNoteText = (EditText) findViewById(R.id.etNewNote);
        newNoteText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Notes newNote = new Notes(newNoteText.getText().toString(),new ArrayList<String>(), false, "checklist");
                    profile.myNotes.add(newNote);
                    anl.notifyDataSetChanged();
                    newNoteText.getText().clear();
                    return true;
                }
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newNoteText.getWindowToken(), 0);
                return false;
            }
        });


        lvNotes.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isOnTop(lvNotes);
                if(onTop && scrollState == SCROLL_STATE_IDLE){
                    displayNewNote(llmainActivity,llNewNoteBackground);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(newNoteVisible && isScrollingDown(view,firstVisibleItem)){
                    hideNewNote(llmainActivity,llNewNoteBackground);
                    Log.e("HEEYY", "Scrollingdown");
                }
            }
        });

    }

    public boolean isScrollingDown(AbsListView view, int firstVisibleItem){
        boolean scrollingDown = false;
        int top = 0;
        if(view.getChildAt(0) != null){
            top = view.getChildAt(0).getTop();
        }

        if(firstVisibleItem > lastVisibleItem){
            //scrroll down
            scrollingDown = true;
        }else if(firstVisibleItem < lastVisibleItem){
            //scroll up
            scrollingDown = false;
        }else{
            if(top < lastY){
                //scroll down
                scrollingDown = true;
            }else if(top > lastY){
                //scroll up
                scrollingDown = false;
            }
        }

        lastVisibleItem = firstVisibleItem;
        lastY = top;
        return scrollingDown;
    }
    public void isOnTop(ListView listview){
        if (listview.getChildCount()>0 && listview.getChildAt(0).getTop()==0
                && listview.getFirstVisiblePosition() == 0){
            onTop = true;
        }else{
            onTop = false;
        }
    }

    private void displayNewNote(LinearLayout llmainActivity, LinearLayout llNewNoteBackground){
       // TransitionManager.beginDelayedTransition(llmainActivity);
        ViewGroup.LayoutParams params = llNewNoteBackground.getLayoutParams();
        params.height = 200;
        llNewNoteBackground.setLayoutParams(params);
        newNoteVisible = true;
    }

    private void hideNewNote(LinearLayout llmainActivity, LinearLayout llNewNoteBackground){
       // TransitionManager.beginDelayedTransition(llmainActivity);
        ViewGroup.LayoutParams params = llNewNoteBackground.getLayoutParams();
        params.height = 1;
        llNewNoteBackground.setLayoutParams(params);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newNoteVisible = false;
    }

    public void scrollToItem(int position) {
        lvNotes.smoothScrollToPositionFromTop(position,0,250);
    }
}
