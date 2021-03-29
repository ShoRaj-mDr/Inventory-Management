//package com.example.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
//
////import com.basgeekball.awesomevalidation.AwesomeValidation;
////import com.basgeekball.awesomevalidation.ValidationStyle;
//
//public class Register extends AppCompatActivity
//        implements View.OnClickListener {
//
//    // Initialize variables
//    private EditText registerName;
//    private EditText registerEmail;
//    private EditText registerPassword;
//    private EditText registerConfirmPassword;
//    private Button registerSignupButton;
//
//
//    // Password requirement with special character, numbers...
//    // private final String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{5,}";
//    private final String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).{5,}";
//    // Using Github awesomeValidation library: https://github.com/thyrlian/AwesomeValidation
//    // Using AwesomeValidation to check for Invalid inputs
//    //AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
//    String RegexNumber = "[/^\\d+\\.?\\d*$/]+";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        // Initialization
//        registerName = findViewById(R.id.register_name);
//        registerEmail = findViewById(R.id.register_email);
//        registerPassword = findViewById(R.id.register_password);
//        registerConfirmPassword = findViewById(R.id.register_confirm_password);
//        registerSignupButton = findViewById(R.id.register_signup_button);
//
//        //validateUserInput();
//
//        // Signup button Clicked
//        registerSignupButton.setOnClickListener(this);
//
//        final EditText editRegisterName = (EditText) registerName;
//        final EditText editRegisterEmail = (EditText) registerEmail;
//        final EditText editRegisterPassword = (EditText) registerPassword;
//        final EditText editRegisterConfirmPassword = (EditText) registerConfirmPassword;
//
//        // Placeholder for Register Name
//        editRegisterName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    editRegisterName.setHint("Enter Name");
//                } else {
//                    editRegisterName.setHint("");
//                }
//            }
//        });
//
//        // Placeholder for Register Email
//        editRegisterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    editRegisterEmail.setHint("Enter Email");
//                } else {
//                    editRegisterEmail.setHint("");
//                }
//            }
//        });
//
//        // Placeholder for Register Password
//        editRegisterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    editRegisterPassword.setHint("Enter Password");
//                } else {
//                    editRegisterPassword.setHint("");
//                }
//            }
//        });
//
//        // Placeholder for Register Confirm Password
//        editRegisterConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    editRegisterConfirmPassword.setHint("Enter Password Again");
//                } else {
//                    editRegisterConfirmPassword.setHint("");
//                }
//            }
//        });
//        // setup AWS service configuration. Choosing default configuration
//        ClientConfiguration clientConfiguration = new ClientConfiguration();
////AdminAddUserToGroupRequest ar= new AdminAddUserToGroupRequest().withGroupName("WMSCustomer").withUsername("cesar").withUserPoolId("us-east-2_si1cYK2IO");
//// Create a CognitoUserPool object to refer to your user pool
//        CognitoUserPool userPool = new CognitoUserPool(Register.this, "us-east-2_si1cYK2IO", registerName.getText().toString(), registerPassword.getText().toString(), clientConfiguration);
//
//// Create a CognitoUserAttributes object and add user attributes
//        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
//
//// Add the user attributes. Attributes are added as key-value pairs
//// Adding user's given name.
//// Note that the key is "given_name" which is the OIDC claim for given name
//        userAttributes.addAttribute("given_name", userGivenName);
//
//// Adding user's phone number
//        userAttributes.addAttribute("phone_number", phoneNumber);
//
//// Adding user's email address
//        userAttributes.addAttribute("email", emailAddress);
//
//
//
//
//
//    }
//
////    public void validateUserInput() {
////        mAwesomeValidation.addValidation(com.example.warehousemanagmentsystem491b.Login.Register.this, R.id.register_name, "[a-zA-Z\\s]+", R.string.err_register_name);
////        mAwesomeValidation.addValidation(com.example.warehousemanagmentsystem491b.Login.Register.this, R.id.register_email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_register_email);
////        mAwesomeValidation.addValidation(com.example.warehousemanagmentsystem491b.Login.Register.this, R.id.register_password, regexPassword, R.string.err_register_password);
////        mAwesomeValidation.addValidation(com.example.warehousemanagmentsystem491b.Login.Register.this, R.id.register_confirm_password, R.id.register_password, R.string.err_register_password_confirmation);
////    }
//
//
//    @Override
//    public void onClick(View view) {
//
////        if (mAwesomeValidation.validate()) {
//            displayToast("Account Created");
//
//            // move to login activity after creating an account
//            Intent intentLogin = new Intent(getApplicationContext(), myItems.class);
//            startActivity(intentLogin);
////
////        } else
////            displayToast("Error in Sign up");
//    }
//
//
//    /**
//     * Displays a Toast with the message.
//     *
//     * @param message Message to display
//     */
//    public void displayToast(String message) {
//        Toast.makeText(getApplicationContext(), message,
//                Toast.LENGTH_SHORT).show();
//    }
//
//}
//
///*
//     Website Referenced:
//         - https://stackoverflow.com/questions/44164170/android-edittext-with-different-floating-label-and-placeholder/44165904
// */