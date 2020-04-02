package com.example.cinemavip.UI.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemavip.R;
import com.example.cinemavip.UI.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class SerialActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnAceptarSerial, btnCancelarSerial;
    private EditText edtSerial;
    boolean encontro=false;
    String clavesusada;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hideSystemUI();
        setContentView(R.layout.activity_login_serial);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        edtSerial = findViewById(R.id.editText_serial_login_activity);
        btnAceptarSerial = findViewById(R.id.aceptar_serial);
        btnCancelarSerial = findViewById(R.id.cancelar_serial);






        DocumentReference docRef = db.collection("usuarios").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //Toast toast1 = Toast.makeText(getApplicationContext(), document.get("Premiun").toString()+document.get("FinalPremiun").toString(), Toast.LENGTH_SHORT);toast1.show();
                        if(document.get("Premiun").toString().equals("true"))
                        {

                            String[] partsFechaPremiun = document.get("FinalPremiun").toString().split("/");
                            String[] partsFechaHoy=fecha(0).split("/");
                            boolean PremiunActivo=true;

                            if(Integer.parseInt(partsFechaHoy[2])>Integer.parseInt(partsFechaPremiun[2]))
                            {
                                PremiunActivo=false;

                            }else if(Integer.parseInt(partsFechaHoy[1])>Integer.parseInt(partsFechaPremiun[1]))
                            {
                                PremiunActivo=false;
                            }else  if(Integer.parseInt(partsFechaHoy[0])>Integer.parseInt(partsFechaPremiun[0]))
                            {
                                //Toast toast1 = Toast.makeText(getApplicationContext(), partsFechaHoy[0]+"//"+partsFechaPremiun[0], Toast.LENGTH_LONG);toast1.show();
                                PremiunActivo=false;
                            }

                            if(PremiunActivo==false)
                            {

                                FirebaseUser user;
                                user = FirebaseAuth.getInstance().getCurrentUser();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                Map<String, Object> datos = new HashMap<>();
                                datos.put("Usuario", user.getUid());
                                datos.put("Premiun", false);
                                datos.put("ClaveUsada", "");
                                datos.put("InicioPremiun", "");
                                datos.put("FinalPremiun", "");

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
                            }else
                            {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }

                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        btnCancelarSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);

            }
        });


        btnAceptarSerial.setOnClickListener(new View.OnClickListener() {
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


                                        if(edtSerial.getText().toString().equals(document.getId().toString()))
                                        {
                                            encontro=true;
                                            clavesusada=document.getId();

                                        }
                                    }
                                    if(encontro==true)
                                    {



                                        //___________
                                        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                        DocumentReference docRef = db2.collection("claves").document(clavesusada);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                        if(document.get("Status").toString().equals("true"))
                                                        {

                                                            Toast toast1 = Toast.makeText(getApplicationContext(), "La clave esta usada", Toast.LENGTH_SHORT);toast1.show();
                                                        }
                                                        else
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

                                                            Map<String, Object> datosclave = new HashMap<>();
                                                            datosclave.put("Usuario", user.getUid());
                                                            datosclave.put("Status", true);

                                                            db.collection("claves").document( clavesusada)
                                                                    .set(datosclave)
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

                                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                            startActivity(intent);
                                                        }

                                                    } else {
                                                        //Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    //Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });

                                        //___________




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

        findViewById(R.id.btnGoogleSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(final FirebaseUser currentUser){
        if (currentUser != null ){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            TastyToast.makeText(getApplicationContext(), "Revise usuario y contraseña", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            TastyToast.makeText(getApplicationContext(), "Bienvenido", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //
                            updateUI(null);
                            TastyToast.makeText(getApplicationContext(), "Inicio fallido", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("ERROR SING", "Google sign in failed", e);
                // ...
            }
        }
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

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
