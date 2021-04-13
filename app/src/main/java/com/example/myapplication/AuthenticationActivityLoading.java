package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.GetCustomersQuery;
import com.amazonaws.amplify.generated.graphql.GetEmployeesQuery;
import com.amazonaws.amplify.generated.graphql.GetPetQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.EmployeeMenu.EmployeeMenu;

import java.util.Map;

import javax.annotation.Nonnull;

public class AuthenticationActivityLoading extends AppCompatActivity {
    String id="";
    boolean hasID;
    String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("MAIN MENU22222222222", "WE ARE BACK");
        Thread myThread = new Thread(myRunnable);
        myThread.start();
        Log.i("MAIN MENU22222222222", "WE ARE BACK");


    }
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            while (!hasID) {
                try {
                    Thread.sleep(4000); // Waits for 1 second (1000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                new CountDownTimer(5000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//                        Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + millisUntilFinished / 1000);
//
//                    }
//
//                    public void onFinish() {
//                        //mTextField.setText("done!");
//                        Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + "done!");
//
//                    }
//                }.start();
                Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + "done!");
                String ss = "" + getCognitoStatus();
                Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", ss);
                String s2 = "";
                Log.i("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", s2);

                try {
                    s2 = getCognitoID();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!s2.equals("")) {
                    Log.i("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", s2);
                    hasID = true;
                    id = s2;
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
            }
        }
    };
    public void waitForID() {
        Log.i("MAIN MENU333333333", "WE ARE BACK");

        while(!hasID) {
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + millisUntilFinished / 1000);

                }

                public void onFinish() {
                    //mTextField.setText("done!");
                    Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + "done!");
                    String ss = "" + getCognitoStatus();
                    Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", ss);
                    String s2 = "";
                    Log.i("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", s2);

                    try {
                        s2 = getCognitoID();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!s2.equals("")) {
                        Log.i("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", s2);

                        //currentUser.loggingIn = false;
                        hasID = true;
                        id = s2;

                        try {
                            //Pull user data from Cognito and save it locally.
                            pullUserData();
//                                    Intent intent = new Intent(AuthenticationActivity.this, AuthenticationActivityAddCustomer.class);
//                                    startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                                Intent i2 = new Intent(AuthenticationActivity.this, AdminMenu.class);
//                                startActivity(i2);

                            //Check our 3 DB tables to see what type of user is logging in.
                            Log.i("MAIN MENU", "WE ARE BACK");

                            isAdmin(currentUser.id);

                            isEmployee(currentUser.id);

                            isCustomer(currentUser.id);


                    }
                }
            }.start();
        }
    }
    public void waitAndSignIn(){
        if(getCognitoStatus()) {
            AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    //loggedIn = true;
                   /// Log.i(TAG, userStateDetails.getUserState().toString());
                    Log.i("[p]p2][p2][p2][p2][p2][2][][][][][][][]", "$$$$$$$$$$$$$$$");
                }

                @Override
                public void onError(Exception e) {
                   // Log.e(TAG, e.toString());
                    Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "$$$$$$$$$$$$$$$");
                }
            });


            if (!currentUser.hasData) {
                try {
                    //Pull user data from Cognito and save it locally.
                    pullUserData();
//                                    Intent intent = new Intent(AuthenticationActivity.this, AuthenticationActivityAddCustomer.class);
//                                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                //mTextField.setText("done!");
                Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", "seconds remaining: " + "done!");
                String ss=""+getCognitoStatus();
                Log.i("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERORRR", ss);

                currentUser.loggingIn=false;
                if(getCognitoStatus()){


                    if (!currentUser.hasData) {
//                        try {
//                            //Pull user data from Cognito and save it locally.
//                            pullUserData();
////                                    Intent intent = new Intent(AuthenticationActivity.this, AuthenticationActivityAddCustomer.class);
////                                    startActivity(intent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

//                                Intent i2 = new Intent(AuthenticationActivity.this, AdminMenu.class);
//                                startActivity(i2);

                        //Check our 3 DB tables to see what type of user is logging in.
                        Log.i("MAIN MENU", "WE ARE BACK");

                        isAdmin(currentUser.id);

                        isEmployee(currentUser.id);

                        isCustomer(currentUser.id);
                    }

                }

            }
        }.start();
    }
    //======================================================================================isAdmin
    //Check to see if the current user is in the admin table.
    //Im using the "Pet" table for now, will replace later. So pet==admin
    public void isAdmin(String myID) {
        Log.i("##########################################", "ADMIN");
        ClientFactory.appSyncClient().query(GetPetQuery.builder().id(myID).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback3);
    }
    private GraphQLCall.Callback<GetPetQuery.Data>queryCallback3=new GraphQLCall.Callback<GetPetQuery.Data>() {
        @Override //final after nonnull
        public void onResponse(@Nonnull Response<GetPetQuery.Data> response) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            //Toast.makeText(AuthenticationActivity.this, "Updated admin view AUTH ACTIVITY", Toast.LENGTH_LONG).show();
            //DisplayItems.this.finish();
            Log.i("AUTHACT QUERY/////////////////////////////////////////////////", "RESPONSE RECIEVED");
            Log.i("ADMIN", response.data().toString());
            if(response.data().getPet()!=null) {
                //admin = true;
                if(currentUser.admin==false) {
                    Log.i("ADMIN", "###LEAVING");
                    currentUser.admin = true;
                    //logInAsAdmin();

                    AuthenticationActivityLoading.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("UI thread", "I am the UI thread");
                            logInAsAdmin();

                        }
                    });





                }
            }
//                }
//            });

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.i("AUTHACT QUERY", "RESPONSE not RECIEVED");

        }
    };
    //======================================================================================isAdmin

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
            else{
                Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "Not Employee");
            }
        }
        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.i("AUTHACT QUERY", "RESPONSE not RECIEVED");

        }
    };
    //======================================================================================isEmployee

    //======================================================================================isCustomer
    //Check to see if the current user is in the customer table.
    public void isCustomer(String myID) {
//        ClientFactory.appSyncClient().query(GetCustomersQuery.builder().id("59a0f631-452c-4db7-a11f-6e5ef8adc1f6").build())
//                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
//                .enqueue(queryCallback2);
        Log.i("##########################################", "Customer");

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
                Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "Not Customer");
                if(!currentUser.admin){
                    if(!currentUser.employee){
                        if(!currentUser.customer){
                            Intent intent = new Intent(AuthenticationActivityLoading.this, AuthenticationActivityAddCustomer.class);
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
        Intent intent = new Intent(AuthenticationActivityLoading.this, CustomerMenu.class);
        startActivity(intent);
    }
    public void logInAsEmployee(){
        Log.i("LOGING IN ","AS EMPLOYEE");
        Intent intent = new Intent(AuthenticationActivityLoading.this, EmployeeMenu.class);
        startActivity(intent);
    }
    public void logInAsAdmin(){
        Log.i("LOGING IN ","AS ADMIN");
        Intent intent = new Intent(AuthenticationActivityLoading.this, AdminMenu.class);
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
    //======================================================================================pullUserData

}