package com.nain.encuesta.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nain.encuesta.R;
import com.nain.encuesta.activities.admin.HomeAdminActivity;
import com.nain.encuesta.activities.user.HomeUserActivity;
import com.nain.encuesta.models.User;
import com.nain.encuesta.providers.AuthProvider;
import com.nain.encuesta.providers.UsersProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    TextView textViewRegister;
    LoginButton loginButton;
    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    AlertDialog mDialog;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    SignInButton mButtonGoogle;
    Button mButtonLogin;
    CircleImageView mCircleImagenViewBack;
    TextView mTextViewRegister;
    TextInputEditText mTextViewEmail, mTextViewPassword;

    private final int REQUEST_CODE_GOOGLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewRegister = findViewById(R.id.textViewRegister);
        mCircleImagenViewBack = findViewById(R.id.circleImageBack);
        mTextViewEmail = findViewById(R.id.textInputEmail);
        mTextViewPassword = findViewById(R.id.textInputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonGoogle = findViewById(R.id.btnLoginGoogle);
        mCallbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        firebaseAuthWithFacebook();

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        mCircleImagenViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void firebaseAuthWithFacebook(){
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mAuthProvider.facebookLogin(loginResult.getAccessToken()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = mAuthProvider.getUid();
                            checkUserExist(id);
                        }else{
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "No se pudo iniciar sesion con google", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK", "facebook:onError", error);
            }
        });
    }

    private void signInGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mDialog.show();
        mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuthProvider.getUid();
                    mDialog.dismiss();
                    checkUserExist(id);
                }
                else {
                    mDialog.dismiss();
                    Log.w("ERROR", "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesion con google", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserExist(final String id) {
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Intent intent = new Intent(LoginActivity.this, HomeUserActivity.class);
                    startActivity(intent);
                }
                else {
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setRole("CLIENT");
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, HomeUserActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "No se pudo almacenar la informacion del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void login(){
        String email = mTextViewEmail.getText().toString();
        String password = mTextViewPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()) {
            if (isEmailValid(email)) {
                mDialog.show();
                mAuthProvider.login(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.dismiss();
                        if (task.isSuccessful()) {
                            mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String role = documentSnapshot.getString("role");
                                        if(role.equals("ADMIN")){
                                            Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }else if(role.equals("CLIENT")){
                                            Intent intent = new Intent(LoginActivity.this, HomeUserActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "El email o la contraseña que ingresaste no son correctas", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Inserte Datos", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
