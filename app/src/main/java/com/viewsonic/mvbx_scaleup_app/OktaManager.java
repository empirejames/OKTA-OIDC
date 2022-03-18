package com.viewsonic.mvbx_scaleup_app;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.okta.oidc.AuthenticationPayload;
import com.okta.oidc.AuthorizationStatus;
import com.okta.oidc.CustomConfiguration;
import com.okta.oidc.OIDCConfig;
import com.okta.oidc.Okta;
import com.okta.oidc.RequestCallback;
import com.okta.oidc.ResultCallback;
import com.okta.oidc.Tokens;
import com.okta.oidc.clients.sessions.SessionClient;
import com.okta.oidc.clients.web.WebAuthClient;
import com.okta.oidc.storage.security.DefaultEncryptionManager;
import com.okta.oidc.util.AuthorizationException;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;


public class OktaManager {
    private Context mContext;
    private OIDCConfig mConfig;
    private WebAuthClient mWebAuth;
    private SessionClient mSessionClient;
    private String mOidc_discovery_url = "https://auth.myviewboard.com/oidc/v1/.well-known/openid-configuration";
    private String mOidc_discovery_url_s = "https://auth.stage.myviewboard.com/oidc/v1/.well-known/openid-configuration";
    private boolean isProduction = false;

    public OktaManager(Context context) {
        this.mContext = context;
        init();
    }

    public void init() {
        isProduction = false;

        mConfig = new OIDCConfig.Builder()
                .clientId("mvb-scale-up")
                .discoveryUri(isProduction ? mOidc_discovery_url : mOidc_discovery_url_s)
                .redirectUri("com.viewsonic.myviewboard.scaleup:/oauthredirect")
                .endSessionRedirectUri("com.viewsonic.myviewboard.scaleup:/logout")
                .scopes("openid", "profile", "email", "offline_access")
                .create();



        //mConfig = new OIDCConfig.Builder().withJsonFile(mContext,R.raw.auth_client).create();
        mWebAuth = new Okta.WebAuthBuilder()
                .withConfig(mConfig)
                .withContext(mContext)
                .withCallbackExecutor(null)
                .withEncryptionManager(new DefaultEncryptionManager(mContext))
                .setRequireHardwareBackedKeyStore(true)
                .create();

        mSessionClient = mWebAuth.getSessionClient();
    }


    Tokens getTokens() throws AuthorizationException {
        return mSessionClient.getTokens();
    }

    Boolean isAuthenticated() {
        return mSessionClient.isAuthenticated();
    }

    void registerWebAuthCallback(ResultCallback<AuthorizationStatus, AuthorizationException> callback, Activity activity) {
        mWebAuth.registerCallback(callback, activity);
    }

    void signIn(Activity activity, AuthenticationPayload payload) {
        mWebAuth.signIn(activity, payload);
    }

    void signOut(Activity activity, RequestCallback<Integer, AuthorizationException> callback) {
        mWebAuth.signOut(activity, callback);

    }

    void clearUserData() {
        mSessionClient.clear();
    }
}
