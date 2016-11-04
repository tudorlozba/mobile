package com.android.phoneagenda;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends Fragment implements View.OnClickListener {


    private EditText mContactName;
    private EditText mContactNumber;
    private EditText mContactEmail;
    private Contact contact = null;
    private Button mSaveButton;

    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(MainActivity.CONTACT_ARG_KEY)) {
            Gson gson = new Gson();
            this.contact = gson.fromJson(getArguments().getString(MainActivity.CONTACT_ARG_KEY), Contact.class);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().supportInvalidateOptionsMenu();
        if(contact != null) {
            inflater.inflate(R.menu.menu_add_contact, menu);
            super.onCreateOptionsMenu(menu,inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_contact) {
            DatabaseHelper.deleteContact(contact);
            getActivity().onBackPressed();
            Toast.makeText(getContext(), "Contact deleted!", Toast.LENGTH_SHORT).show();
            return  true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_contact, container, false);
        setHasOptionsMenu(true);

        mContactName = (EditText) v.findViewById(R.id.contact_name);
        mContactNumber = (EditText) v.findViewById(R.id.contact_number);
        mContactEmail = (EditText) v.findViewById(R.id.contact_email);
        mSaveButton = (Button) v.findViewById(R.id.btn_save_contact);
        mSaveButton.setText(getString(R.string.addContact));

        mSaveButton.setOnClickListener(this);
        if (contact != null) {
            mContactName.setText(contact.getName());
            mContactNumber.setText(contact.getNumber());
            mContactEmail.setText(contact.getEmail());
            mSaveButton.setText(getString(R.string.update_contact));
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_contact) {
            if (contact == null) {
                createAndSaveContact();
            } else {
                updateContact();
            }
        }
    }

    private void updateContact() {
        this.contact.setName(mContactName.getText().toString());
        this.contact.setNumber(mContactNumber.getText().toString());
        this.contact.setEmail(mContactEmail.getText().toString());
        DatabaseHelper.updateContact(this.contact);
        getActivity().onBackPressed();
    }

    private void createAndSaveContact() {
        Contact contact = new Contact(mContactName.getText().toString(), mContactNumber.getText().toString(), mContactEmail.getText().toString());
        DatabaseHelper.addContact(contact);
        getActivity().onBackPressed();
    }
}
