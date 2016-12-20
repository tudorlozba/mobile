package com.android.phoneagenda;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends BaseActivity implements ContactsListAdapter.ListItemClickListener {


    public static final String CONTACT_ARG_KEY = "contact";
    public static final String ADD_CONTACT_FRAGMENT_TAG = "add_contact_fragment_tag";
    private ViewContactsFragment mViewContacts;
    private boolean isNetworkConnected = false;

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {
                internetBanner.setVisibility(View.GONE);
                isNetworkConnected = true;
                if (DatabaseHelper.contactsToSync(MainActivity.this)) {
//                    Toast.makeText(MainActivity.this, "Must sync", Toast.LENGTH_SHORT).show();
                    syncBanner.setVisibility(View.VISIBLE);
                    SyncService sync = new SyncService();
                    sync.execute();
                } else {
                    Toast.makeText(MainActivity.this, "Up to date", Toast.LENGTH_SHORT).show();
                }
            } else {
                internetBanner.setVisibility(View.VISIBLE);
                isNetworkConnected = false;
            }
        }
    };

    public void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.sync_icon)
                        .setContentTitle("Sync contacts")
                        .setContentText("Contacts awaiting to sync to the cloud");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1234, mBuilder.build());
    }

    private LinearLayout internetBanner;
    private LinearLayout syncBanner;
    private TextView syncText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mViewContacts = new ViewContactsFragment();
        internetBanner = (LinearLayout) findViewById(R.id.internet_banner);
        syncBanner = (LinearLayout) findViewById(R.id.sync_banner);
        syncText = (TextView) findViewById(R.id.sync_text);


//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.main_container, mViewContacts).commit();
        replaceFragmentWithAnimation(mViewContacts, "ViewContacts");
    }

    public void replaceFragmentWithAnimation(android.support.v4.app.Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(this.mConnReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(ADD_CONTACT_FRAGMENT_TAG) != null && getSupportFragmentManager().findFragmentByTag(ADD_CONTACT_FRAGMENT_TAG).isAdded()) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mViewContacts).commit();
            replaceFragmentWithAnimation(mViewContacts, "ViewContacts");
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
        replaceFragmentWithAnimation(mAddContact, ADD_CONTACT_FRAGMENT_TAG);
        // getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mAddContact, ADD_CONTACT_FRAGMENT_TAG).commit();
        setTitle(getString(R.string.editContact));
    }

    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    private class SyncService extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Cursor c = DatabaseHelper.readCachedContacts(MainActivity.this);
            c.moveToFirst();
            int workLoad = 0;

            try {
                while (!c.isAfterLast()) {
                    publishProgress(c.getCount() - workLoad);
                    Thread.sleep(1000);
                    Contact contact = new Contact();
                    contact.setName(c.getString(c.getColumnIndex(LocalDBHelper.ContactEntry.COLUMN_NAME)));
                    contact.setEmail(c.getString(c.getColumnIndex(LocalDBHelper.ContactEntry.COLUMN_EMAIL)));
                    contact.setNumber(c.getString(c.getColumnIndex(LocalDBHelper.ContactEntry.COLUMN_NUMBER)));
                    contact.setDateOfBirth(c.getString(c.getColumnIndex(LocalDBHelper.ContactEntry.COLUMN_DOB)));
                    contact.setId(c.getString(c.getColumnIndex(LocalDBHelper.ContactEntry.COLUMN_ID)));

                    DatabaseHelper.addContact(contact);
                    workLoad++;
                    c.moveToNext();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            updateProgressBanner(values[0]); // updates with the remaining contacts to sync
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DatabaseHelper.deleteCachedContacts(MainActivity.this);
            syncBanner.setVisibility(View.GONE);
            if (mViewContacts.isAdded()) {
                mViewContacts.onResume();
            }
            NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1234);
        }
    }

    private void updateProgressBanner(Integer valuesRemaining) {
        String s = getString(R.string.string_sync_items, valuesRemaining);
        syncText.setText(s);
    }

}
