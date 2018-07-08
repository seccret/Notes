package com.example.pombee.notes;


import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {
    public ArrayList<Notes> myNotes;

    Profile(){
        myNotes = new ArrayList<>();
    }
    public void addNote(Notes notes){
        myNotes.add(0,notes);
        ManagerLocalFiles.serializeProfile();
    }

    public void removeNote(Notes notes){
        myNotes.remove(notes);
        ManagerLocalFiles.serializeProfile();
    }
}
