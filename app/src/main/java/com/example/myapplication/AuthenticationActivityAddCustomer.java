package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateCustomersMutation;
import com.amazonaws.amplify.generated.graphql.CreatePetMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.Map;

import javax.annotation.Nonnull;

import type.CreateCustomersInput;
import type.CreatePetInput;

public class AuthenticationActivityAddCustomer extends AppCompatActivity {
    String name;
    String id;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("WELCOME ","TO THE BLANK ACTIVITY");
        save(currentUser.id,currentUser.name,currentUser.email);
    }

    public String getCognitoName() throws Exception {
        Map m1= AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("given_name").toString();
    }

    public String getCognitoID() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("sub").toString();
    }

    public String getCognitoEmail() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("email").toString();
    }

    private void save(String id,String name,String email) {

        CreateCustomersInput input = CreateCustomersInput.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();

        CreateCustomersMutation addCustomerMutation= CreateCustomersMutation.builder()
                .input(input)
                .build();

        ClientFactory.appSyncClient().mutate(addCustomerMutation).enqueue(mutateCallback22);
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateCustomersMutation.Data> mutateCallback22 = new GraphQLCall.Callback<CreateCustomersMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateCustomersMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    backToAuth();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddCustomerMutation", e);

                }
            });
        }
    };

    //=============================================================================================CREATE
    public void backToAuth(){
        Log.i("LOGING IN ","AS CUSTOMER");
        Intent intent = new Intent(AuthenticationActivityAddCustomer.this, CustomerMenu.class);
        startActivity(intent);
    }
}

/*
type customers @model {
    id: ID!
    name: String
    email: String
    password: String
}
 */