package com.nain.encuesta.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nain.encuesta.R;
import com.nain.encuesta.activities.MainActivity;
import com.nain.encuesta.activities.ProfileActivity;
import com.nain.encuesta.activities.ResultadosActivity;
import com.nain.encuesta.providers.AuthProvider;
import com.nain.encuesta.providers.UsersProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdminActivity extends AppCompatActivity {

    CardView mCardViewDocente, mCardViewResultado, mCardViewProfile, mCardViewCerrar;
    AuthProvider mAuthProvider;
    Toolbar mToolbar;
    UsersProvider mUserProvider;
    String mUsername, mEmail;
    String mImageProfile = "";
    TextView mTxt_name, mTxt_email;
    CircleImageView mCircleImagePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        mCardViewDocente = findViewById(R.id.cardViewDocente);
        mCardViewResultado = findViewById(R.id.cardViewResultado);
        mCardViewProfile = findViewById(R.id.cardViewProfile);
        mCardViewCerrar = findViewById(R.id.cardViewSalir);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UsersProvider();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("ADMINISTRADOR");

        mCircleImagePhoto = findViewById(R.id.imageViewProfile);
        mTxt_name = findViewById(R.id.txt_name);
        mTxt_email = findViewById(R.id.txt_email);

        cargarUsuario();

        mCardViewDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this, DocentesActivity.class);
                startActivity(intent);
            }
        });

        mCardViewResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this, ResultadosActivity.class);
                startActivity(intent);
            }
        });

        mCardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        mCardViewCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void cargarUsuario() {
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    if(documentSnapshot.contains("username")){
                        mUsername = documentSnapshot.getString("username");
                        if(mUsername != null){
                            mTxt_name.setText(mUsername);
                        }else{
                            mTxt_name.setText("ADMINISTRADOR");
                        }
                    }
                    if(documentSnapshot.contains("email")){
                        mEmail = documentSnapshot.getString("email");
                        mTxt_email.setText(mEmail);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if(mImageProfile != null){
                            if(!mImageProfile.isEmpty()) {
                                Picasso.with(HomeAdminActivity.this).load(mImageProfile).into(mCircleImagePhoto);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemProfile){
            Intent intent = new Intent(HomeAdminActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.itemLogout) {
            logout();
        }

        return true;
    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(HomeAdminActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
