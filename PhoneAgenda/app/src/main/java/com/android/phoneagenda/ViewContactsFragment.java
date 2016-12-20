package com.android.phoneagenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import java.util.ArrayList;

import static com.android.phoneagenda.MainActivity.ADD_CONTACT_FRAGMENT_TAG;

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
        adapter = new ContactsListAdapter(getContext(), contacts, (ContactsListAdapter.ListItemClickListener) getActivity());
        list = (ListView) v.findViewById(R.id.contacts_list);
        list.setAdapter(adapter);
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((MainActivity)getActivity()).isNetworkConnected()) {
            ((BaseActivity) getActivity()).showProgressDialog();
            DatabaseHelper.fetchContacts(this);
        }
    }

    @Override
    public void onSucces(ArrayList<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        adapter.notifyDataSetChanged();
        ((BaseActivity)getActivity()).hideProgressDialog();
        //DatabaseHelper.removeCallback();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_dummy_contacts){
            createDummyContacts();
            DatabaseHelper.fetchContacts(this);
            return true;
        } else if(item.getItemId() == R.id.menu_add_new_contact){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new AddContactFragment(), ADD_CONTACT_FRAGMENT_TAG).commit();
            getActivity().setTitle(getString(R.string.addContact));
        } else if(item.getItemId() == R.id.sensors_page){
            Intent intent = new Intent();
            intent.setClass(getContext(), SensorsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
