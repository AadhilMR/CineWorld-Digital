package com.aadhil.cineworlddigital;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.aadhil.cineworlddigital.fragment.Login;
import com.aadhil.cineworlddigital.fragment.Register;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_FRAGMENT = 1;
    public static final int REGISTER_FRAGMENT = 2;

    // Biometric
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(MainActivity.LOGIN_FRAGMENT);

        // Request Fingerprint
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "No Biometric Hardware", Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Error with Hardware", Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "No fingerprint present", Toast.LENGTH_LONG).show();
            default:
                System.out.println("No Way");
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CineWorld Digital")
                .setDescription("Use fingerpritn to login.").setDeviceCredentialAllowed(true)
                .build();

        // biometricPrompt.authenticate(promptInfo);
    }

    public void setFragment(int fragmentType) {
        Class fragmentClass = null;
        @AnimRes int enterAnimId = 0;
        @AnimRes int exitAnimId = 0;

        if(fragmentType == MainActivity.LOGIN_FRAGMENT) {

            fragmentClass = Login.class;
            enterAnimId = R.anim.enter_from_left;
            exitAnimId = R.anim.exit_to_right;

        } else if(fragmentType == MainActivity.REGISTER_FRAGMENT) {

            fragmentClass = Register.class;
            enterAnimId = R.anim.enter_from_right;
            exitAnimId = R.anim.exit_to_left;

        }

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(enterAnimId, exitAnimId)
                    .add(R.id.fragmentContainerView, fragmentClass, null)
                    .commit();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}