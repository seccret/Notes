package com.example.pombee.notes;

import java.util.ArrayList;

public class Notes {
    String name;
    ArrayList<String> content;
    boolean privateNote = false;
    String viewStyle; //should be "checklist", "plain", or "list"

    Notes(String name, ArrayList<String> content, boolean privateNote, String viewStyle){
        this.name = name;
        this.content = content;
        this.privateNote = privateNote;
        this.viewStyle = viewStyle;
    }
}
