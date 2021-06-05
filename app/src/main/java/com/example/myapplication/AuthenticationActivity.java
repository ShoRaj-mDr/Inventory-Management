package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreatePetMutation;
import com.amazonaws.amplify.generated.graphql.GetCustomersQuery;
import com.amazonaws.amplify.generated.graphql.GetEmployeesQuery;
import com.amazonaws.amplify.generated.graphql.GetPetQuery;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.model.Credentials;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.EmployeeMenu.EmployeeMenu;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.Map;
import java.util.Timer;

import javax.annotation.Nonnull;

import type.CreatePetInput;

public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();
    boolean loggedIn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if(currentUser.loggingIn) {
            Log.i("ADMIN","Admin Login");
        }

        if(currentUser.admin){
            if(currentUser.customer){
                //currentUser.customer=false;
                logInAsCustomer();
            }
            else if(currentUser.employee){
                //currentUser.employee=false;
                logInAsEmployee();
            }
            else {
                logInAsAdmin();
            }

        }

        ClientFactory.init(this);

        if(currentUser.loggingOut){
            Intent nextIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            ProcessPhoenix.triggerRebirth(AuthenticationActivity.this, nextIntent);
        }

        //while(!loggedIn) {
        //if(!loggedIn) {
            AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {

                    loggedIn = true;
                    Log.i(TAG, userStateDetails.getUserState().toString());

                    switch (userStateDetails.getUserState()) {
                        case SIGNED_IN:
                            if (!currentUser.hasData) {
                                try {
                                    //Pull user data from Cognito and save it locally.
                                    pullUserData();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //Check our 3 DB tables to see what type of user is logging in.
                                Log.i("MAIN MENU", "WE ARE BACK");
                                isAdmin(currentUser.id);
                                isEmployee(currentUser.id);
                                isCustomer(currentUser.id);

                            }
                            break;

                        case SIGNED_OUT:
                            showSignIn2();
                            break;

                        default:
                            AWSMobileClient.getInstance().signOut();
                            showSignIn();
                            break;
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, e.toString());
                    showSignIn2();
                }
            });
        }

    //======================================================================================showSignIn
    //This function is called when the user is signed out.
    //The .nextActivity() might need to be this activity, so the user info can be viewed.
    private void showSignIn2() {
        try {
            CognitoCachingCredentialsProvider provider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-2_si1cYK2IO",
                    Regions.US_EAST_2);
            provider.clear();

           // provider.refresh();
            currentUser.loggingIn=true;
            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(AuthenticationActivityLoading.class).build());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    //======================================================================================isAdmin
    //Check to see if the current user is in the admin table.
    //Im using the "Pet" table for now, will replace later. So pet==admin
    public void isAdmin(String myID) {
        ClientFactory.appSyncClient().query(GetPetQuery.builder().id(myID).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback3);
    }

    private GraphQLCall.Callback<GetPetQuery.Data>queryCallback3=new GraphQLCall.Callback<GetPetQuery.Data>() {
        @Override //final after nonnull
        public void onResponse(@Nonnull Response<GetPetQuery.Data> response) {
                    Log.i("ADMIN", response.data().toString());
                    if(response.data().getPet()!=null) {
                        //admin = true;
                        if(currentUser.admin==false) {
                            Log.i("ADMIN", "###LEAVING");
                            currentUser.admin = true;
                            logInAsAdmin();
                        }
                    }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.i("AUTHACT QUERY", "RESPONSE not RECIEVED");
        }
    };

    //======================================================================================isEmployee
    //Check to see if the current user is in the employee table.
    public void isEmployee(String myID) {
        Log.i("##########################################", "EMP");
        ClientFactory.appSyncClient().query(GetEmployeesQuery.builder().id(myID).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<GetEmployeesQuery.Data>queryCallback=new GraphQLCall.Callback<GetEmployeesQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetEmployeesQuery.Data> response) {
            Log.i("AUTHACT QUERY", "RESPONSE RECIEVED");
            Log.i("EMPLOYEE", response.data().toString());
            if(response.data().getEmployees()!=null) {
                currentUser.employee=true;
                logInAsEmployee();
            }
        }
        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.i("AUTHACT QUERY", "RESPONSE not RECIEVED");
        }
    };

    //======================================================================================isCustomer
    //Check to see if the current user is in the customer table.
    public void isCustomer(String myID) {
        ClientFactory.appSyncClient().query(GetCustomersQuery.builder().id(myID).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback2);
    }

    private GraphQLCall.Callback<GetCustomersQuery.Data>queryCallback2=new GraphQLCall.Callback<GetCustomersQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetCustomersQuery.Data> response) {
            Log.i("AUTHACT QUERY", "RESPONSE RECIEVED");
            Log.i("CUSTOMER", response.data().toString());
            if(response.data().getCustomers()!=null) {
                currentUser.customer=true;
                logInAsCustomer();
            }
            else{
                if(!currentUser.admin){
                    if(!currentUser.employee){
                        if(!currentUser.customer){
                            Intent intent = new Intent(AuthenticationActivity.this, AuthenticationActivityAddCustomer.class);
                            startActivity(intent);
                        }
                    }
                }

            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.i("AUTHACT QUERY", "RESPONSE not RECIEVED");
        }
    };

    //======================================================================================isCustomer
    public void logInAsCustomer(){
        Log.i("LOGING IN ","AS CUSTOMER");
        Intent intent = new Intent(AuthenticationActivity.this, CustomerMenu.class);
        startActivity(intent);
    }

    public void logInAsEmployee(){
        Log.i("LOGING IN ","AS EMPLOYEE");
        Intent intent = new Intent(AuthenticationActivity.this, EmployeeMenu.class);
        startActivity(intent);
    }

    public void logInAsAdmin(){
        Log.i("LOGING IN ","AS ADMIN");
        Intent intent = new Intent(AuthenticationActivity.this, AdminMenu.class);
        startActivity(intent);
    }

    //======================================================================================pullUserData
    //All of these functions are used to pull user information from AWS Cognito.
    public void pullUserData() throws Exception {
        currentUser.name= getCognitoName();
        currentUser.id= getCognitoID();
        currentUser.phone= getCognitoPhoneNumber();
        currentUser.email= getCognitoEmail();
        currentUser.hasData=true;
        Log.i("===========:", "User ID: "+currentUser.id+"\nName: "+currentUser.name+"\nPhone: "+currentUser.phone+"\nEmail: "+currentUser.email);
        //Run once to be an admin.
        //save();
    }

    public boolean getCognitoStatus(){
        return AWSMobileClient.getInstance().isSignedIn();
    }

    public String getCognitoName() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("given_name").toString();
    }

    public String getCognitoID() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("sub").toString();
    }

    public String getCognitoPhoneNumber() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("phone_number").toString();
    }

    public String getCognitoEmail() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("email").toString();
    }

    //======================================================================================showSignIn
    //This function is called when the user is signed out.
    //The .nextActivity() might need to be this activity, so the user info can be viewed.
    private void showSignIn() {
        try {
            if(currentUser.hasData) {
                currentUser.loggingIn = true;
            }
            currentUser.loggingIn = true;
            currentUser.hasData=false;

            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(AuthenticationActivityAddCustomer.class).build());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    //=============================================================================CREATE
    private void save() {
        CreatePetInput input = CreatePetInput.builder()
                .id(currentUser.id)
                .name(currentUser.name)
                .description("admin")
                .build();
        CreatePetMutation addPetMutation = CreatePetMutation.builder()
                .input(input)
                .build();
        ClientFactory.appSyncClient().mutate(addPetMutation).enqueue(mutateCallback22);
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreatePetMutation.Data> mutateCallback22 = new GraphQLCall.Callback<CreatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreatePetMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(DisplayItems.this, "Added Item", Toast.LENGTH_SHORT).show();
                    // DisplayItems.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                }
            });
        }
    };
}
