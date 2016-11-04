package com.android.phoneagenda;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.google.gson.Gson;

public class MainActivity extends BaseActivity implements ContactsListAdapter.ListItemClickListener{


    public static final String CONTACT_ARG_KEY = "contact";
    public static final String ADD_CONTACT_FRAGMENT_TAG = "add_contact_fragment_tag";
    private ViewContactsFragment mViewContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mViewContacts = new ViewContactsFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_container, mViewContacts).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mAddContact, ADD_CONTACT_FRAGMENT_TAG).commit();
        setTitle(getString(R.string.editContact));
    }
}
