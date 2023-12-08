package com.aadhil.cineworlddigital.fragment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.User;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class Register extends Fragment {
    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutMain));

        // Check validity and register the user, and then redirect to login
        Button button1 = fragment.findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Register
                String fname = ((EditText) fragment.findViewById(R.id.editTextText)).getText().toString();
                String lname = ((EditText) fragment.findViewById(R.id.editTextText2)).getText().toString();
                String mobile = ((EditText) fragment.findViewById(R.id.editTextPhone2)).getText().toString();
                String password = ((EditText) fragment.findViewById(R.id.editTextTextPassword2)).getText().toString();

                String regexPattern = "0((7[01245678][0-9])|(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)[0234579])[0-9]{6}";

                if(fname.isEmpty() || lname.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Enter all data", Toast.LENGTH_SHORT).show();
                } else if(!Pattern.compile(regexPattern).matcher(mobile).matches()) {
                    Toast.makeText(getContext(), "Enter a valid mobile", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 4 || password.length() > 20) {
                    Toast.makeText(getContext(), "Password length must within 4-20", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    User user = new User(fname, lname, mobile, password);

                    // Check if there is already an user
                    db.collection("users")
                        .whereEqualTo("mobile", mobile)
                        .whereEqualTo("password", password)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                    Toast.makeText(getContext(), "Already an user with this credentials!", Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    redirectToLogin(navigator);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                                                    alertBuilder.setTitle("Error While Proceed")
                                                            .setMessage("Registration is not success! Please try again later.")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // Do nothing
                                                                }
                                                            }).create().show();
                                                }
                                            });
                                }
                            }
                        });
                }
            }
        });

        // Change the view to login
        Button button2 = fragment.findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLogin(navigator);
            }
        });

        // Signing via Google
        SignInButton buttonGoogle = fragment.findViewById(R.id.sign_in_button);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                SignInClient signInClient = Identity.getSignInClient(getActivity().getApplicationContext());

                GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.google_web_client_id)).build();

                Task<PendingIntent> signinIntent = signInClient.getSignInIntent(signInIntentRequest);
                signinIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        ActivityResultLauncher<IntentSenderRequest> signinLauncher =
                                registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                                        new ActivityResultCallback<ActivityResult>() {
                                            @Override
                                            public void onActivityResult(ActivityResult result) {

                                            }
                                        });

                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest
                                .Builder(pendingIntent).build();

                        signinLauncher.launch(intentSenderRequest);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setTitle("Error While Proceed")
                                .setMessage("Something went wrong! Please try again later.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing
                                    }
                                }).create().show();
                    }
                });
            }
        });
    }

    private void redirectToLogin(ActivityNavigator navigator) {
        navigator.setRedirection(new ActivityNavigator.NavigationManager() {
            @Override
            public void redirect() {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setFragment(MainActivity.LOGIN_FRAGMENT);
            }
        });
    }

    private void checkUserValidity(String mobile, String password) {

    }
}