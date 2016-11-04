package com.android.phoneagenda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ContactsListAdapter.ListItemClickListener{


    public static final String CONTACT_ARG_KEY = "contact";
    public static final String ADD_CONTACT_FRAGMENT_TAG = "add_contact_fragment_tag";
    private ViewContactsFragment mViewContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mViewContacts = new ViewContactsFragment();
        AddContactFragment mAddContact = new AddContactFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_container, mViewContacts).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_dummy_contacts){
            mViewContacts.createDummyContacts();
            DatabaseHelper.fetchContacts(mViewContacts);
            return true;
        } else if(item.getItemId() == R.id.menu_add_new_contact){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new AddContactFragment(), ADD_CONTACT_FRAGMENT_TAG).commit();
            setTitle(getString(R.string.addContact));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().findFragmentByTag(ADD_CONTACT_FRAGMENT_TAG) != null && getSupportFragmentManager().findFragmentByTag(ADD_CONTACT_FRAGMENT_TAG).isAdded()){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mViewContacts).commit();
            return;
        }
        super.onBackPressed();

    }

    @Override
    public void onListItemClicked(Contact c) {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(c);
        args.putString(CONTACT_ARG_KEY, json);
        AddContactFragment mAddContact = new AddContactFragment();
        mAddContact.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mAddContact).commit();
        setTitle(getString(R.string.editContact));
    }
}
