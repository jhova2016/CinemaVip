package com.example.cinemavip.UI.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cinemavip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AccountActivity extends AppCompatActivity {
    private TextView nombre, email;
    private ImageView foto, settingsImg;
    private Button logOut, teleG, FaceB,premiun;
    private Toolbar toolbar;
    private static String FACEBOOK_URL = "https://www.facebook.com/CineDarkPlayOficial";
    private static String FACEBOOK_PAGE_ID = "CineDarkV2Oficial";
    private AlertDialog AlertSettings;
    private SharedPreferences RGPDSing;
    android.app.AlertDialog DialogFiltros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_account);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        RGPDSing = getSharedPreferences("rgpdSing", Context.MODE_PRIVATE);

        nombre = findViewById(R.id.nombreUsuario);
        email = findViewById(R.id.emailUsuario);
        foto = findViewById(R.id.imgPerfil);
        logOut = findViewById(R.id.logOut);
        teleG = findViewById(R.id.comunityTelegram);
        FaceB = findViewById(R.id.comunityFb);
        settingsImg = findViewById(R.id.config_button);
        premiun = findViewById(R.id.premiun);


        settingsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertSettings.show();
            }
        });

        premiun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFiltro();
                DialogFiltros.show();
            }
        });


        teleG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/cinedark"));
                startActivity(telegram);
            }
        });

        FaceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getApplicationContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });

        if(user != null){
            email.setText(user.getEmail());

            if(user.getDisplayName() != null){
                nombre.setText(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null){
                Picasso.get().load(user.getPhotoUrl()).into(foto);
            }
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SetUpAlertSettings();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void SetUpAlertSettings() {
        AlertDialog.Builder Maker = new AlertDialog.Builder(this);
        View SettingsAlertView = getLayoutInflater().inflate(R.layout.alert_dialog_settings, null);
        final CheckBox chGDRP = SettingsAlertView.findViewById(R.id.ChbxAcept);
        chGDRP.setChecked(RGPDSing.getBoolean("isSingned", false));

        Button bntSave = SettingsAlertView.findViewById(R.id.BtnSave);

        bntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = RGPDSing.edit();
                editor.putBoolean("isSingned", chGDRP.isChecked());
                editor.apply();
                AlertSettings.dismiss();
            }
        });

        Maker.setView(SettingsAlertView);
        Maker.setCancelable(false);
        AlertSettings = Maker.create();
    }

    boolean encontro=false;
    String clavesusada;
    String fecha(int n)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int calendarTime = Calendar.DAY_OF_MONTH;
        int temp = calendar.get(calendarTime);
        calendar.set(calendarTime, temp+n);
        SimpleDateFormat formatoFecha = new SimpleDateFormat();
        formatoFecha.setTimeZone(TimeZone.getTimeZone("GMT-6"));
        Date fechaSum = calendar.getTime();
        formatoFecha.applyPattern("dd/MM/yyyy");
        String fechaRespuesta = formatoFecha.format(fechaSum);
        return fechaRespuesta;

    }
    private void DialogFiltro()
    {


        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        View view =View.inflate(this,R.layout.dialog,null);
        final EditText clave=view.findViewById(R.id.codigo);
        Button  cancelar=view.findViewById(R.id.cancelar);

        Button  aceptar=view.findViewById(R.id.descargar);

        //builder.setCancelable(false);
        builder.setView(view);
        DialogFiltros=builder.create();

        aceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                encontro=false;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("claves")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                        if(clave.getText().toString().equals(document.getId().toString()))
                                        {
                                            encontro=true;
                                            clavesusada=document.getId();

                                        }
                                    }
                                    if(encontro==true)
                                    {

                                         FirebaseUser user;
                                        FirebaseAuth mAuth;
                                        user = FirebaseAuth.getInstance().getCurrentUser();

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        Map<String, Object> datos = new HashMap<>();
                                        datos.put("Usuario", user.getUid());
                                        datos.put("Premiun", true);
                                        datos.put("ClaveUsada", clavesusada);
                                        datos.put("InicioPremiun", fecha(0));
                                        datos.put("FinalPremiun", fecha(7));


                                        db.collection("usuarios").document( user.getUid())
                                                .set(datos)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Log.d(TAG, "DocumentSnapshot successfully written!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Log.w(TAG, "Error writing document", e);
                                                    }
                                                });

                                        db.collection("claves").document(clavesusada)
                                                .delete() .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                               // Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                      //  Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });

                                        DialogFiltros.dismiss();

                                    }
                                    else
                                    {
                                        Toast toast1 = Toast.makeText(getApplicationContext(), "Clave incorrecta", Toast.LENGTH_SHORT);toast1.show();
                                    }
                                } else {
                                    //Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });



            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFiltros.dismiss();

            }
        });

    }

}
