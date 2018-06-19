package fr.artefact.private_chat.Activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.artefact.private_chat.Fragments.ContactsFragment;
import fr.artefact.private_chat.Fragments.HomeFragment;
import fr.artefact.private_chat.Fragments.SettingsFragment;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Utilities.PusherClient;

public class MainActivity extends FragmentActivity {

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    BottomNavigationView bottomNavigationView;
    FragmentPagerAdapter _fragmentPagerAdapter;
    ViewPager _viewPager;
    List<Fragment> _fragments;
    AppDatabase db;
    PusherClient pusherClient;
    List<Conversation> conversations;

    public HomeFragment homeFragment;
    public ContactsFragment contactsFragment;
    public SettingsFragment settingsFragment;

    int HOME_FRAGMENT = 0;
    int CONTACTS_FRAGMENT = 1;
    int SETTINGS_FRAGMENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        homeFragment = new HomeFragment();
        contactsFragment = new ContactsFragment();
        settingsFragment = new SettingsFragment();

        db = AppDatabase.getAppDatabase(getApplicationContext());
        setContentView(R.layout.activity_main);
        makeNavigationMenu();
        setViewPager();
        attemptFetchingData();

        try {
            conversations = db.conversationDao().getAll();
            subscribeChannels();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erreur de connection au service pusher",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptFetchingData() {
        try {
            String token = db.authResponseDao().getAll().getAccessToken();
            DataRequests.fetchAll(token, getApplicationContext());
            bottomNavigationView.getMenu().getItem(HOME_FRAGMENT).setChecked(true);
        }catch (Exception e) {
            _viewPager.setCurrentItem(SETTINGS_FRAGMENT);
            bottomNavigationView.getMenu().getItem(SETTINGS_FRAGMENT).setChecked(true);
        }
    }

    private void setViewPager() {
        // Add each fragment to our list.
        _fragments = new ArrayList<>();
        _fragments.add(HOME_FRAGMENT, homeFragment);
        _fragments.add(CONTACTS_FRAGMENT, contactsFragment);
        _fragments.add(SETTINGS_FRAGMENT, settingsFragment);

        // Setup the fragments, defining the number of fragments, the screens and titles.
        this._fragmentPagerAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return _fragments.size();
            }

            @Override
            public Fragment getItem(final int position) {
                return _fragments.get(position);
            }
        };


        this._viewPager = (ViewPager) this.findViewById(R.id.main_view_pager);
        this._viewPager.setAdapter(this._fragmentPagerAdapter);
    }

    public void openFragment(final int fragment) {
        this._viewPager.setCurrentItem(fragment);
    }

    private void makeNavigationMenu () {
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        openFragment(HOME_FRAGMENT);
                        return true;
                    case R.id.navigation_contacts:
                        openFragment(CONTACTS_FRAGMENT);
                        return true;
                    case R.id.navigation_settings:
                        openFragment(SETTINGS_FRAGMENT);
                        return true;
                }
                return false;
            }
        };
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    protected void subscribeChannels() {
        pusherClient = new PusherClient();

        for (Conversation conversation: conversations) {
            pusherClient.subscribeChannel(conversation.getId());
        }
    }

    void unSubscribeChannels() {
        for (Conversation conversation: conversations) {
            List<Conversation> conversations = db.conversationDao().getAll();
            pusherClient.unSubscribeChannel(conversation.getId());
        }
    }

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public ContactsFragment getContactFragment() {
        return contactsFragment;
    }
}
// TODO: 17/06/18 Organiser systeme de login
// TODO: 17/06/18 deconnection, register 
// TODO: 17/06/18 activité parametre de connection telecharge les clés d'api en attaquant une route url mdp 
// TODO: 17/06/18 Sauvegarder données utilisateur
// TODO: 17/06/18 Creer activité utilisateur pour pouvoir creer des conversation et des amitiés
// TODO: 17/06/18 styliser les vues de message 
// TODO: 17/06/18 mode consultation hors connection 
// TODO: 17/06/18 encryptage des messages avec clé partagée entre utilisateurs seulement  
