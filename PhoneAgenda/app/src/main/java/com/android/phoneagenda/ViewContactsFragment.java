package com.android.phoneagenda;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tudorlozba on 03/11/2016.
 */
public class ViewContactsFragment extends Fragment implements DatabaseHelper.DatabaseListener{

    private ArrayList<Contact> contacts = new ArrayList<>();
    private ListView list;
    private ContactsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_contacts_layout,null);
        adapter = new ContactsListAdapter(getContext(), contacts);
        list = (ListView) v.findViewById(R.id.contacts_list);
        list.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseHelper.fetchContacts(this);
    }

    @Override
    public void onSucces(ArrayList<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFail() {

    }

    public void createDummyContacts(){
        for(int i=0; i<10; i++){
            Contact c = new Contact();
            c.setName("Contact " + i);
            c.setEmail("contact" + i + "@mail.com");
            c.setNumber(i + i + i + i + "");
            //contacts.add(c);
            DatabaseHelper.addContact(c);
        }
    }
}
