package com.android.phoneagenda;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends Fragment implements View.OnClickListener {


    private EditText mContactName;
    private EditText mContactNumber;
    private EditText mContactEmail;
    private Contact contact = null;
    private Button mSaveButton;
    private EditText mDateOfBirth;
    private DatePickerDialog dateOfBirthPicker;
    private SimpleDateFormat dateFormatter;

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
        mDateOfBirth = (EditText) v.findViewById(R.id.dateOfBirth);
        mDateOfBirth.setInputType(InputType.TYPE_NULL);
        mDateOfBirth.setOnClickListener(this);
        mSaveButton = (Button) v.findViewById(R.id.btn_save_contact);
        mSaveButton.setText(getString(R.string.addContact));
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        mSaveButton.setOnClickListener(this);
        if (contact != null) {
            mContactName.setText(contact.getName());
            mContactNumber.setText(contact.getNumber());
            mContactEmail.setText(contact.getEmail());
            mDateOfBirth.setText(contact.getDateOfBirth());
            mSaveButton.setText(getString(R.string.update_contact));
        }

        Calendar newCalendar = Calendar.getInstance();
        dateOfBirthPicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

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
        } else if (v == mDateOfBirth){
            dateOfBirthPicker.show();
        }
    }

    private void updateContact() {
        this.contact.setName(mContactName.getText().toString());
        this.contact.setNumber(mContactNumber.getText().toString());
        this.contact.setEmail(mContactEmail.getText().toString());
        this.contact.setDateOfBirth(mDateOfBirth.getText().toString()
        );
        DatabaseHelper.updateContact(this.contact);
        getActivity().onBackPressed();
    }

    private void createAndSaveContact() {
        Contact contact = new Contact(mContactName.getText().toString(), mContactNumber.getText().toString(), mContactEmail.getText().toString(), mDateOfBirth.getText().toString());

        if(((MainActivity)getActivity()).isNetworkConnected()) {
            //store in online DB directly
            DatabaseHelper.addContact(contact);
        } else {
            //cache the contact in the local DB.
            DatabaseHelper.cacheContact(contact, getContext());
            ((MainActivity)getActivity()).showNotification();
        }
        getActivity().onBackPressed();
    }
}
