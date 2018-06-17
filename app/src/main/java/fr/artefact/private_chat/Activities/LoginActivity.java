package fr.artefact.private_chat.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.artefact.private_chat.Interfaces.SettingsDao;
import fr.artefact.private_chat.Models.Settings;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;


public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mServerUrlView;
    private EditText mClientIdView;
    private EditText mClientSecretView;

    private View mProgressView;
    private View mLoginFormView;

    private SettingsDao settingsDAO;
    private Settings settings;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appDatabase = AppDatabase.getAppDatabase(LoginActivity.this);
        settingsDAO = appDatabase.settingsDao();

        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText("benoitzuing@msn.com");


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mServerUrlView = (EditText) findViewById(R.id.serverUrl);
        mClientIdView = (EditText) findViewById(R.id.clientId);
        mClientSecretView = (EditText) findViewById(R.id.clientSecret);

        try {
            settings = settingsDAO.getWithId(1);
            mPasswordView.setText(settings.getPassword());
            mServerUrlView.setText(settings.getServerUrl());
            mClientIdView.setText(settings.getClientId());
            mClientSecretView.setText(settings.getClientSecret());
        } catch (Exception e) {
            mPasswordView.setText("secret");
            mServerUrlView.setText("http://the-emperors.gotgeeks.com/");
            mClientIdView.setText("2");
            mClientSecretView.setText("C3xdg2vjSytqineBO3qW7cCAxAfd2FBgL8RcgLdn");
        }




        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveSettings()) {
                attemptLogin();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Erreur",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private Boolean saveSettings () {
        Boolean status = false;
        try {
            settings = new Settings();
            settings.setId(1);
            settings.setClientId(mClientIdView.getText().toString());
            settings.setClientSecret(mClientSecretView.getText().toString());
            settings.setEmail(mEmailView.getText().toString());
            settings.setPassword(mPasswordView.getText().toString());
            settings.setServerUrl(mServerUrlView.getText().toString());

            settingsDAO.insert(settings);

            status = true;

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this,
                    "Erreur: " + e.toString(),
                    Toast.LENGTH_LONG
            ).show();
        }

        return status;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);
        mAuthTask = new UserLoginTask();
        mAuthTask.execute((Void) null);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DataRequests.fetchAuthResponse(LoginActivity.this);
            } catch (final Exception e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Erreur d'authentification :'(", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(login);
                    }
                });
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

