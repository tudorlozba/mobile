package com.android.phoneagenda;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by tudorlozba on 02/11/2016.
 */
public class DatabaseHelper {

    private static String CONTACT_DB_KEY = "contacts";
    ;

    public static void fetchContacts(final DatabaseListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {
            mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("tag", dataSnapshot.toString());
                    final ArrayList<Contact> contacts = new ArrayList<>();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Contact contact = item.getValue(Contact.class);
                        contacts.add(contact);
                    }
                    listener.onSucces(contacts);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void addContact(Contact contact) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference newRef = mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).push();
        newRef.setValue(contact);
        Log.e("tagg", "added");
    }

    public interface DatabaseListener {
        void onSucces(ArrayList<Contact> contacts);

        void onFail();
    }
}
