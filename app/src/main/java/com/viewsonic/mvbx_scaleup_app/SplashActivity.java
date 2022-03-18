package com.viewsonic.mvbx_scaleup_app;

import static com.viewsonic.mvbx_scaleup_app.OktaLoginApplication.oktaManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.okta.oidc.AuthenticationPayload;
import com.okta.oidc.AuthorizationStatus;
import com.okta.oidc.ResultCallback;
import com.okta.oidc.Tokens;
import com.okta.oidc.util.AuthorizationException;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    private Button btnLogin;
    private AuthenticationPayload payload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (oktaManager.isAuthenticated()) {

        }
        Log.e("Pan","AUTH : " + oktaManager.isAuthenticated());

        btnLogin = findViewById(R.id.loginButton);
        setupOktaCallback();
        setupViews();
    }
    private void setupOktaCallback(){
        oktaManager.registerWebAuthCallback(getAuthCallback(), this);
    }

    private void setupViews(){
        payload = new AuthenticationPayload.Builder().build();
        btnLogin.setOnClickListener(view -> oktaManager.signIn(this,payload));
    }

    private void goToHomePage(){
        Log.e("Pan","goToHomePage ....");
        startActivity(new Intent(this,MainActivity.class));
    }

    private ResultCallback<AuthorizationStatus, AuthorizationException> getAuthCallback(){
        return new ResultCallback<AuthorizationStatus, AuthorizationException>() {
            @Override
            public void onSuccess(@NonNull AuthorizationStatus result) {
                switch (result){
                    case AUTHORIZED :
                        Log.d(TAG, "AUTHORIZED");
                        try {
                            Tokens tokens = oktaManager.getTokens();
                            Log.d(TAG, "tokens : " + tokens);
                        } catch (AuthorizationException e) {
                            e.printStackTrace();
                        }
                        goToHomePage();
                        break;
                    case SIGNED_OUT:
                        Log.d(TAG, "Signed out");
                        break;
                    case CANCELED:
                        Log.d(TAG, "Canceled");
                        break;
                    case ERROR:
                        Log.d(TAG, "Error");
                        break;
                    case EMAIL_VERIFICATION_AUTHENTICATED:
                        Log.d(TAG, "Email verification authenticated");
                        break;
                    case EMAIL_VERIFICATION_UNAUTHENTICATED:
                        Log.d(TAG, "Email verification unauthenticated");
                        break;
                }

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Canceled");
            }

            @Override
            public void onError(@Nullable String msg, @Nullable AuthorizationException exception) {
                Log.d(TAG, "Error: " + msg);
                Log.d(TAG, "exception: " + exception.errorDescription);
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //must pass the results back to the WebAuthClient.
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"requestCode : " + requestCode);
        Log.e(TAG,"resultCode : " + resultCode);
        Log.e(TAG,"data : " + data);
    }
}
