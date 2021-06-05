package com.example.myapplication.Supplier;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amazonaws.amplify.generated.graphql.ListSupplierssQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;


public class DisplaySupplierFragment extends Fragment {

    private final String TAG = DisplaySupplierFragment.class.getSimpleName();
    private ArrayList<ListSupplierssQuery.Item> supplierList;
    private RecyclerView recyclerView;
    private DisplaySupplierAdapter mAdapter;
    // private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_supplier, container, false);

        supplierList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.supplier_display_recyclerview);

        ClientFactory.init(getActivity());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResumeFragment()");
        // Toast.makeText(getActivity(), "onResume :" + TAG, Toast.LENGTH_SHORT).show();
        ClientFactory.appSyncClient().query(ListSupplierssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private final GraphQLCall.Callback<ListSupplierssQuery.Data> queryCallback = new GraphQLCall.Callback<ListSupplierssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListSupplierssQuery.Data> response) {

            supplierList = new ArrayList<>(response.data().listSupplierss().items());

            Log.i(TAG, "Retrieved list items: " + supplierList.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mAdapter = new DisplaySupplierAdapter(supplierList, getActivity());
                    recyclerView.setAdapter(mAdapter);
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

}