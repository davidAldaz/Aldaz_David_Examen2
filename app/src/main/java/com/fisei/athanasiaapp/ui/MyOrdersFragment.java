package com.fisei.athanasiaapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.adapters.OrderArrayAdapter;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal_DIAS;
import com.fisei.athanasiaapp.objects.Order_DIAS;
import com.fisei.athanasiaapp.services.SaleService_DIAS;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private List<Order_DIAS> myOrderDIASList = new ArrayList<>();
    private OrderArrayAdapter orderArrayAdapter;
    private ListView listView;
    private Bundle bundle = new Bundle();

    public MyOrdersFragment() {
    }
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        listView = (ListView) view.findViewById(R.id.listViewMyOrdersFragment);
        orderArrayAdapter = new OrderArrayAdapter(getContext(), myOrderDIASList);
        GetOrderTask getOrderTask = new GetOrderTask();
        getOrderTask.execute();

        return view;
    }
    class GetOrderTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            myOrderDIASList.clear();
            if(AthanasiaGlobal_DIAS.ADMIN_PRIVILEGES){
                myOrderDIASList = SaleService_DIAS.GetAllSales();
            } else {
                myOrderDIASList = SaleService_DIAS.GetSalesByUserID();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            orderArrayAdapter.clear();
            orderArrayAdapter.addAll(myOrderDIASList);
            orderArrayAdapter.notifyDataSetChanged();
            listView.setAdapter(orderArrayAdapter);
        }
    }
}