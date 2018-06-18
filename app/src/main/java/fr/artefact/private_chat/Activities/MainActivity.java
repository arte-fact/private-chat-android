package fr.artefact.private_chat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;

public class MainActivity extends FragmentActivity {
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    /**
     * The adapter definition of the fragments.
     */
    FragmentPagerAdapter _fragmentPagerAdapter;

    /**
     * The ViewPager that hosts the section contents.
     */
    ViewPager _viewPager;

    /**
     * List of fragments.
     */

    List<Fragment> _fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeNavigationMenu();

        _fragments = new ArrayList<>();

        // Add each fragment to our list.
        _fragments.add(new HomeActivity());
        _fragments.add(new LoginActivity());

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

        // Set the default fragment.
        this.openFragment(0);

        try {
            String token = AppDatabase.getAppDatabase(getApplicationContext()).authResponseDao().getAll().getAccessToken();
            DataRequests.fetchUsers(token, getApplicationContext());
            openFragment(0);
        }catch (Exception e) {
            openFragment(1);
        }
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
                        openFragment(1);
                        return true;
                    case R.id.navigation_contacts:
                        return true;
                    case R.id.navigation_settings:
                        openFragment(0);
                        return true;
                }
                return false;
            }
        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
// TODO: 17/06/18 Organiser systeme de login
// TODO: 17/06/18 deconnection, register 
// TODO: 17/06/18 activité parametre de connection telecharge les clés d'api en attaquant une route url mdp 
// TODO: 17/06/18 barre d'action en bas de l'appli 
// TODO: 17/06/18 Sauvegarder données utilisateur
// TODO: 17/06/18 Creer activité utilisateur pour pouvoir creer des conversation et des amitiés
// TODO: 17/06/18 styliser les vues de message 
// TODO: 17/06/18 mode consultation hors connection 
// TODO: 17/06/18 encryptage des messages avec clé partagée entre utilisateurs seulement  
