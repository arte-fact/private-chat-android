package fr.artefact.private_chat.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.artefact.private_chat.Interfaces.UserDao;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;


public class SettingsFragment extends Fragment {


    private TextView mUserNumber;

    private UserDao userDao;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        AppDatabase appDatabase = AppDatabase.getAppDatabase(this.getActivity().getBaseContext());
        userDao = appDatabase.userDao();

        mUserNumber = view.findViewById(R.id.userNumber);

        try {
            mUserNumber.setText("User number: " + userDao.getAppOwner().getName());
        } catch (Exception e) {
            mUserNumber.setText("Unknown user number...");
        }

        return view;

    }
}

