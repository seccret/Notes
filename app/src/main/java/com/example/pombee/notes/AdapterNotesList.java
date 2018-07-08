package com.example.pombee.notes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.mtp.MtpStorageInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterNotesList extends ArrayAdapter<Notes> {

    View v;
    public AdapterNotesList(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterNotesList(Context context, int resource, List<Notes> items) {
        super(context, resource, items);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_note, null);
        }

        final Notes p = getItem(position);

        TextView name = (TextView) v.findViewById(R.id.itemNoteName);
        //Set the details
        if (p != null) {
            if (name != null) {name.setText(p.name);}
        }

        RelativeLayout rl = v.findViewById(R.id.rlBackground);
        final LinearLayout lb = v.findViewById(R.id.llBackground);
        int colorChoice = position % 5;
        if(colorChoice == 0){
            rl.setBackgroundColor(Color.parseColor("#F27280"));
            lb.setBackgroundColor(Color.parseColor("#F9B294"));
        }else if(colorChoice == 1){
            rl.setBackgroundColor(Color.parseColor("#C16C86"));
            lb.setBackgroundColor(Color.parseColor("#F27280"));
        }else if(colorChoice == 2){
            rl.setBackgroundColor(Color.parseColor("#6D5C7D"));
            lb.setBackgroundColor(Color.parseColor("#C16C86"));
        }else if(colorChoice == 3){
            rl.setBackgroundColor(Color.parseColor("#335D7E"));
            lb.setBackgroundColor(Color.parseColor("#6D5C7D"));
        }else if(colorChoice == 4){
            rl.setBackgroundColor(Color.parseColor("#F9B294"));
            lb.setBackgroundColor(Color.parseColor("#335D7E"));
        }


        //On click
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lb.getChildAt(1) == null) {
                    openNote(lb, p, position);
                }else{
                    closeNote(lb,p);
                }


            }
        });

        name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                return true;
            }
        });
        return v;
    }

    /*
    opens a note
     */
    private void openNote(LinearLayout ll, Notes note, int position){

        //add a new note view
        LayoutInflater inflater = (LayoutInflater) MainActivity.mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.item_note_view, null);
        ll.addView(rowView);

        //add the notes content to the note view
        LinearLayout llContent = (LinearLayout) rowView.findViewById(R.id.ll_content);
        ArrayList<String> contentNotes = note.content;
        if(contentNotes.size() == 0){
            final View contentView = inflater.inflate(R.layout.item_note_checklist, null);
            EditText tvCon = contentView.findViewById(R.id.tvChecklist);
            tvCon.setHint("Add new note");

            llContent.addView(contentView);
        }
        for (String con: contentNotes) {
            final View contentView = inflater.inflate(R.layout.item_note_checklist, null);
            EditText tvCon = contentView.findViewById(R.id.tvChecklist);
            tvCon.setText(con);

            llContent.addView(contentView);
        }

        //adjust the size of the whole view
        ll.setLayoutParams(new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ll.requestLayout();

        MainActivity.mainActivity.scrollToItem(position);


    }

    private void closeNote(LinearLayout ll, Notes note){
        ll.removeView(ll.getChildAt(1));

        //adjust the size of the whole view
        ll.setLayoutParams(new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 200));
        ll.requestLayout();
    }
}
