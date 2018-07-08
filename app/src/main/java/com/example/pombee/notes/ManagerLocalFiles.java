package com.example.pombee.notes;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ManagerLocalFiles {

    public static void serializeProfile(){
        Profile prof = MainActivity.mainActivity.profile;

        try{
            File file = File.createTempFile("notes", "txt");
            FileOutputStream fout = MainActivity.mainActivity.openFileOutput("my_notes_profile", MainActivity.mainActivity.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(prof);
            oos.close();

            FileOutputStream FOS = new FileOutputStream(file);
            oos = new ObjectOutputStream(FOS);
            oos.writeObject(prof);
            oos.close();

            Log.e("Serializer", "Profile Updated");
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.mainActivity, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(MainActivity.mainActivity, "IOException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static boolean deserialzeProfile() {
        Profile prof;

        try {
            FileInputStream fin = MainActivity.mainActivity.openFileInput("my_notes_profile");
            loadProfileFromBytes(fin);

            return true;

            //TODO also need to add the new profile to DynamoDb
        } catch (Exception ex) {
        }

        return false;
    }

    public static void loadProfileFromBytes(FileInputStream fin) throws Exception{
        ObjectInputStream ois = new ObjectInputStream(fin);
        Profile prof = (Profile) ois.readObject();
        ois.close();

        MainActivity.mainActivity.profile = prof;
    }
}
