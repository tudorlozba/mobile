package com.android.phoneagenda;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends Fragment implements View.OnClickListener {


    private EditText mContactName;
    private EditText mContactNumber;
    private EditText mContactEmail;

    public AddContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_contact, container, false);
        mContactName = (EditText)v.findViewById(R.id.contact_name);
        mContactNumber = (EditText)v.findViewById(R.id.contact_number);
        mContactEmail = (EditText)v.findViewById(R.id.contact_email);
        v.findViewById(R.id.btn_save_contact).setOnClickListener(this);

        getActivity().getActionBar().setHomeButtonEnabled(true);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_save_contact){
            createAndSaveContact();
        }
    }

    private void createAndSaveContact() {
        Contact contact = new Contact(mContactName.getText().toString(), mContactNumber.getText().toString(), mContactEmail.getText().toString());
        DatabaseHelper.addContact(contact);
    }
}
