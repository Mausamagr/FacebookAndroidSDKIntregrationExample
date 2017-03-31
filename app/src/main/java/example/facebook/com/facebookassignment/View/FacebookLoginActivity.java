package example.facebook.com.facebookassignment.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import example.facebook.com.facebookassignment.R;

public class FacebookLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult>{

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isLoggedIn()) {
            launcherUserLikesActivity();
            this.finish();
        }

        setContentView(R.layout.activity_facebook_login);
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManager, this);
    }


    private boolean isLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void launcherUserLikesActivity() {
        Intent intent = new Intent(FacebookLoginActivity.this, FbUsersLikesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d("fb_login_sdk", "callback success" + loginResult.getAccessToken().getToken());
        launcherUserLikesActivity();
    }

    @Override
    public void onCancel() {
        Log.d("fb_login_sdk", "callback cancel");
    }

    @Override
    public void onError(FacebookException error) {
        Log.d("fb_login_sdk", "callback onError");
    }
}
