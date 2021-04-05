package com.example.myapplication.Supplier;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.amazonaws.amplify.generated.graphql.ListSupplierssQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;


public class DisplaySupplierFragment extends Fragment {


    private final String TAG = DisplaySupplierFragment.class.getSimpleName();
    String s = "something";
    ListView obj;
    private List<String> suppliers;
    private ArrayList<ListSupplierssQuery.Item> supplierList;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private final GraphQLCall.Callback<ListSupplierssQuery.Data> queryCallback = new GraphQLCall.Callback<ListSupplierssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListSupplierssQuery.Data> response) {

            //mPets = new ArrayList<>(response.data().listItemzs().items());
//            supplierList = new ArrayList<>(response.data().listItemss().items());
            supplierList = new ArrayList<>(response.data().listSupplierss().items());


            Log.i(TAG, "Retrieved list items: " + supplierList.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    myFunc(supplierList);

                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_supplier, container, false);

        supplierList = new ArrayList<>();

//        ClientFactory.init(this);
        ClientFactory.init(getActivity());
//        s = getIntent().getStringExtra("cogUser");

//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, supplierList);
//        ListView listView = view.findViewById(R.id.supplier_display_listview);
//        listView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Query list data when we return to the screen
        query();
    }

    public void query() {
        ClientFactory.appSyncClient().query(ListSupplierssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    public void myFunc(List<ListSupplierssQuery.Item> items) {

        //This loop will concatenate all our item names with their associated price.
        final ArrayList lastAL = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
//            lastAL.add(i, items.get(i).name() + "    " + items.get(i).quantity());
            lastAL.add(items.get(i).name());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lastAL);
        obj = view.findViewById(R.id.supplier_display_listview);
        obj.setAdapter(arrayAdapter);
    }


}