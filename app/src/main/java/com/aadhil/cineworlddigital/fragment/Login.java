package com.aadhil.cineworlddigital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.HomeActivity;
import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.User;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class Login extends Fragment {
    public Login() {
        // Required empty public constructor  sign_in_button_reg  sign_in_button_reg
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutMain));

        // Check user validity and login
        Button button1 = fragment.findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Login
                String mobile = ((EditText) fragment.findViewById(R.id.editTextPhone)).getText().toString();
                String password = ((EditText) fragment.findViewById(R.id.editTextTextPassword)).getText().toString();

                String regexPattern = "0((7[01245678][0-9])|(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)[0234579])[0-9]{6}";

                if(mobile.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Enter all data", Toast.LENGTH_SHORT).show();
                } else if(!Pattern.compile(regexPattern).matcher(mobile).matches()) {
                    Toast.makeText(getContext(), "Enter a valid mobile", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    
                    db.collection("users")
                            .whereEqualTo("mobile", mobile)
                            .whereEqualTo("password", password)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                for(DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                    MainActivity.currentUser = snapshot.toObject(User.class);
                                    navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                                        @Override
                                        public void redirect() {
                                            Intent intent = new Intent(fragment.getContext(), HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getContext(), "Wrong Details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Change the view to registration
        Button button2 = fragment.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.setFragment(MainActivity.REGISTER_FRAGMENT);
                    }
                });
            }
        });
    }
}