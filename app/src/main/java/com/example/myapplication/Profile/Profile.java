package com.example.myapplication.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.example.myapplication.R;
import com.example.myapplication.currentUser;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private TextInputLayout password;
    private EditText userName, email, fullName, phoneNumber;

    String test1, test2, test3;

    EditText editProductName, editProductUsername, editProductEmail, editProductPhoneNumber;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.profile_name);
        phoneNumber = findViewById(R.id.phone_number);

        String dataFullName = AWSMobileClient.getInstance().getUsername();

        // TODO: PULL DB INFO
        // STORE INFO IN ARRAYLIST or just a bunch of strings
        userName.setText(dataFullName);
        email.setText(currentUser.email);
        fullName.setText(currentUser.name);
        phoneNumber.setText(currentUser.phone);
    }

    /**
     * Create a new account when < Textview is clicked
     *
     * @param view
     */
    public void backToMenu(View view) {
        finish();
    }

    /**
     * onClick method when the user clicks the Edit Profile button
     *
     * @param view
     */
    public void saveInformation(View view) {
        // Just to get ready if more things are added
        if (view.getId() == R.id.profile_save_button) {
            new Thread(new Runnable() {
                public void run() {
                    Map m1 = new HashMap();
                    String tempName = fullName.getText().toString();
                    String tempEmail = email.getText().toString();
                    String tempNumber = phoneNumber.getText().toString();
                    test1 = tempName;
                    test2 = tempEmail;
                    test3 = tempNumber;
                    //String tempUsername = userName.getText().toString();
                    m1.put("given_name", tempName);
                    m1.put("email", tempEmail);
                    m1.put("phone_number", tempNumber);
//                    m1.put("preferred_username", tempUsername); //LATER
                    try {
                        System.out.println("ADDING");
                        AWSMobileClient.getInstance().updateUserAttributes(m1);
                        currentUser.name = test1;
                        currentUser.email = test2;
                        currentUser.phone = test3;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            finish();
        }
    }

    public void EditInformation(View view) {
        editProductName = findViewById(R.id.profile_name);
        //editProductUsername = findViewById(R.id.username);
        editProductEmail = findViewById(R.id.email);
        editProductPhoneNumber = findViewById(R.id.phone_number);
        if (view.getId() == R.id.fullNameButton) {
            test(editProductName, view);
//        } else if (view.getId() == R.id.phoneNumberButton) {
//            test(editProductUsername, view);
        } else if (view.getId() == R.id.phoneNumberButton) {
          test(editProductPhoneNumber, view);
        } else if (view.getId() == R.id.emailButton) {
            test(editProductEmail, view);
        }
    }

    public void test(final EditText temp, View view) {
        button = (Button) view;
        button.setVisibility(View.GONE);
        // Goes to Edit Profile Activity
        temp.setEnabled(true);
        // When user presses enter, saves info and exits out of text field
        // TODO Save info
        temp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) ||
                        ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_TAB))) {
                    temp.setEnabled(false);
                    button.setVisibility(View.VISIBLE); // Makes button visible
                    temp.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Displays a Toast with the message.
     *
     * @param message Message to display
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}