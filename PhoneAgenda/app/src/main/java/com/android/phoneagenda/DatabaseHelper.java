package com.android.phoneagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private static DatabaseListener mListener;

    public static void fetchContacts(final DatabaseListener listener) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mListener = listener;
        try {
            mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("tag", dataSnapshot.toString());
                    final ArrayList<Contact> contacts = new ArrayList<>();
                    final ArrayList<String> keys = new ArrayList<>();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Contact contact = item.getValue(Contact.class);
                        contact.setId(item.getKey());
                        contacts.add(contact);

                    }
                    mListener.onSucces(contacts);

                    mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeCallback() {
        mListener = null;
    }


    public static void addContact(Contact contact) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference newRef = mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).push();
        newRef.setValue(contact);
    }

    public static void updateContact(Contact contact) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference newRef = mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).child(contact.getId());
        newRef.setValue(contact);
    }

    public static void deleteContact(Contact contact) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference newRef = mDatabase.child(user.getUid()).child(CONTACT_DB_KEY).child(contact.getId());
        newRef.removeValue();

    }

    public static void cacheContact(Contact contact, Context context) {
        LocalDBHelper cacheDB = new LocalDBHelper(context);
        SQLiteDatabase db = cacheDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocalDBHelper.ContactEntry.COLUMN_ID, contact.getId());
        values.put(LocalDBHelper.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(LocalDBHelper.ContactEntry.COLUMN_NUMBER, contact.getNumber());
        values.put(LocalDBHelper.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(LocalDBHelper.ContactEntry.COLUMN_DOB, contact.getDateOfBirth());

        db.insert(LocalDBHelper.ContactEntry.TABLE_NAME, null, values);

    }


    public static void deleteCachedContacts(Context context) {
        LocalDBHelper cacheDB = new LocalDBHelper(context);
        SQLiteDatabase db = cacheDB.getWritableDatabase();

        String[] params = {"*"};

        db.delete(LocalDBHelper.ContactEntry.TABLE_NAME, null, null);
    }

    public static Cursor readCachedContacts(Context context) {
        LocalDBHelper cacheDB = new LocalDBHelper(context);
        SQLiteDatabase db = cacheDB.getReadableDatabase();

        String[] projection = {
                LocalDBHelper.ContactEntry.COLUMN_ID,
                LocalDBHelper.ContactEntry.COLUMN_NAME,
                LocalDBHelper.ContactEntry.COLUMN_NUMBER,
                LocalDBHelper.ContactEntry.COLUMN_EMAIL,
                LocalDBHelper.ContactEntry.COLUMN_DOB
        };

        Cursor c = db.query(
                LocalDBHelper.ContactEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        return c;
    }

    public static boolean contactsToSync(Context context) {
        Cursor c = readCachedContacts(context);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public interface DatabaseListener {
        void onSucces(ArrayList<Contact> contacts);

        void onFail();
    }
}
