package com.aadhil.cineworlddigital.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.PaymentActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.Card;
import com.aadhil.cineworlddigital.model.Invoice;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PayNow extends Fragment {
    private Card card = null;
    private SharedPreferences preferences;

    public PayNow() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_now, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        preferences = getActivity()
                .getSharedPreferences("user_preferences", Context.MODE_PRIVATE);

        EditText nameOnCardEdit = fragment.findViewById(R.id.editTextText6);
        EditText cardNumEdit = fragment.findViewById(R.id.editTextText8);
        EditText cvvEdit = fragment.findViewById(R.id.editTextText7);
        EditText expireMonthEdit = fragment.findViewById(R.id.editTextText9);
        EditText expireYearEdit = fragment.findViewById(R.id.editTextText10);

        // Load Card Details
        if(loadSavedCardDetails()) {
            CheckBox loadCardCheck = fragment.findViewById(R.id.checkBox4);
            loadCardCheck.setVisibility(View.VISIBLE);
            loadCardCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        String[] expireDate = card.getExpireDate().split(",");

                        nameOnCardEdit.setText(card.getNameOnCard());
                        cardNumEdit.setText(card.getCardNumber());
                        cvvEdit.setText(card.getCvv());
                        expireMonthEdit.setText(expireDate[0]);
                        expireYearEdit.setText(expireDate[1]);
                    } else {
                        nameOnCardEdit.setText("");
                        cardNumEdit.setText("");
                        cvvEdit.setText("");
                        expireMonthEdit.setText("");
                        expireYearEdit.setText("");
                    }
                }
            });
        }

        // Check settings and check saveCardCheckBox or not
        if(preferences.getBoolean("allowSaveCard", false)) {
            CheckBox saveCardCheck = fragment.findViewById(R.id.checkBox3);
            saveCardCheck.setChecked(true);
        }

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutPayment));

        // Click to pay
        Button button = fragment.findViewById(R.id.button25);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameOnCardEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the Name on Card", Toast.LENGTH_SHORT).show();
                } else if(cardNumEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the Card Number", Toast.LENGTH_SHORT).show();
                } else if(cvvEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the CVV", Toast.LENGTH_SHORT).show();
                } else if(expireMonthEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the Expire Month", Toast.LENGTH_SHORT).show();
                } else if(expireYearEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the Expire Year", Toast.LENGTH_SHORT).show();
                } else if(cvvEdit.getText().toString().length() != 3) {
                    Toast.makeText(getContext(), "Enter a valid cvv", Toast.LENGTH_SHORT).show();
                } else if(Integer.valueOf(expireMonthEdit.getText().toString()) > 12
                        || Integer.valueOf(expireMonthEdit.getText().toString()) < 1) {
                    Toast.makeText(getContext(), "Enter a valid month", Toast.LENGTH_SHORT).show();
                } else {
                    // If user select to save card, save details
                    CheckBox saveCardCheck = fragment.findViewById(R.id.checkBox3);
                    if(saveCardCheck.isChecked()) {

                        if(preferences.getBoolean("allowSaveCard", false)) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("nameOnCard", nameOnCardEdit.getText().toString());
                            editor.putString("cardNumber", cardNumEdit.getText().toString());
                            editor.putString("cvv", cvvEdit.getText().toString());
                            editor.putString("expireDate",
                                    expireMonthEdit.getText().toString() + "/"
                                            + expireYearEdit.getText().toString());
                            editor.apply();
                        }
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("allowSaveCard", false);
                        editor.apply();
                    }

                    // Add checkout details to firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("users")
                        .whereEqualTo("mobile", MainActivity.currentUser.getMobile())
                        .whereEqualTo("password", MainActivity.currentUser.getPassword())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (DocumentSnapshot user : task.getResult().getDocuments()) {
                                        String userId = user.getId();

                                        // Get Datetime
                                        String datetime = LocalDateTime.now()
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                                        // Get random transaction id
                                        String transactionId = UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 16);

                                        // Create a new invoice instance
                                        Invoice invoice = new Invoice(
                                                transactionId,
                                                PaymentActivity.checkoutInfo.getMovieId(),
                                                userId,
                                                datetime,
                                                PaymentActivity.checkoutInfo.getPrice()
                                        );

                                        // Set invoice to static invoice variable
                                        PaymentActivity.invoice = invoice;

                                        // Add invoice to firestore
                                        db.collection("invoices").add(invoice)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                                                        @Override
                                                        public void redirect() {
                                                            getActivity().getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.fragmentContainerView18, CompletePayment.class, null)
                                                                .commit();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    }
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
    }

    private boolean loadSavedCardDetails() {
        if(preferences.getBoolean("allowSaveCard", false)) {
            String nameOnCard = preferences.getString("nameOnCard", null);
            String cardNumber = preferences.getString("cardNumber", null);
            String expireDate = preferences.getString("expireDate", null);
            String cvv = preferences.getString("cvv", null);

            if(nameOnCard != null && cardNumber != null && expireDate != null && cvv != null) {
                card = new Card(nameOnCard, cardNumber, cvv, expireDate);
                return true;
            }
        }
        return false;
    }
}