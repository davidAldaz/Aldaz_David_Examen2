package com.fisei.athanasiaapp.ui;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.adapters.ShopItemArrayAdapter;
import com.fisei.athanasiaapp.dialog.CheckoutDialogFragment_DIAS;
import com.fisei.athanasiaapp.models.SaleDetails_DIAS;
import com.fisei.athanasiaapp.models.SaleRequest_DIAS;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal_DIAS;
import com.fisei.athanasiaapp.objects.ShopCartItem_DIAS;
import com.fisei.athanasiaapp.services.SaleService_DIAS;
import com.fisei.athanasiaapp.services.ShoppingCartService_DIAS;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShopCartFragment extends Fragment {

    private ShopItemArrayAdapter itemArrayAdapter;
    private ListView listView;
    private TextView total;
    private Button checkout;
    private Button saveCart;

    private Boolean SuccesfulSale = false;

    public ShopCartFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_shop_cart, container, false);
        List<ShopCartItem_DIAS> list = new ArrayList<>();
        checkout = view.findViewById(R.id.btnCheckout);
        saveCart = view.findViewById(R.id.btnSaveCart);
        listView = (ListView) view.findViewById(R.id.listViewShopCartFragment);
        total = (TextView) view.findViewById(R.id.textViewTotalShop);
        itemArrayAdapter = new ShopItemArrayAdapter(getContext(), list);

        checkout.setOnClickListener(view1 -> { OpenDialog(); });
        saveCart.setOnClickListener(view1 -> { ExecuteSaveCartTask();});
        itemArrayAdapter.clear();
        list = AthanasiaGlobal_DIAS.SHOPPING_CART;
        itemArrayAdapter.addAll(list);
        itemArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(itemArrayAdapter);

        checkout.setEnabled(!CheckIsListViewIsEmpty(listView));
        saveCart.setEnabled(!CheckIsListViewIsEmpty(listView));
        UpdateTotalView(total);
        itemArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                checkout.setEnabled(!CheckIsListViewIsEmpty(listView));
                saveCart.setEnabled(!CheckIsListViewIsEmpty(listView));
                UpdateTotalView(total);
            }
        });
        return view;
    }

    private Boolean CheckIsListViewIsEmpty(ListView lW){
        return lW.getAdapter().isEmpty();
    }
    private void UpdateTotalView(TextView totalView){
        double total = 0;
        for (ShopCartItem_DIAS item: AthanasiaGlobal_DIAS.SHOPPING_CART) {
            total += item.UnitPrice * item.Quantity;
        }
        total *= 1.1;
        totalView.setText( "Total: " + String. format("%.2f", total)+ " $" );
    }
    private void OpenDialog(){
        Bundle bundleDialog = new Bundle();
        CheckoutDialogFragment_DIAS dialog = new CheckoutDialogFragment_DIAS();
        dialog.setTargetFragment(getTargetFragment(), 0);
        bundleDialog.putString("title", "Are you sure?");
        dialog.show(this.getChildFragmentManager(), "Quiz Results");
    }

    public void ExecuteSaleTask(){
        AddSaleTask task = new AddSaleTask();
        task.execute();
    }
    public void ExecuteSaveCartTask(){
        SaveCartTask saveCartTask = new SaveCartTask();
        saveCartTask.execute();
    }
    class AddSaleTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            SaleRequest_DIAS sale = new SaleRequest_DIAS();
            List<SaleDetails_DIAS> details = new ArrayList<>();
            for (ShopCartItem_DIAS item: AthanasiaGlobal_DIAS.SHOPPING_CART) {
                details.add(new SaleDetails_DIAS(item.Id, item.Quantity));
            }
            sale.UserClientID = AthanasiaGlobal_DIAS.ACTUAL_USER.ID;
            sale.SaleDetails_DIAS = details;
            SuccesfulSale = SaleService_DIAS.AddNewSale(sale);
            ShoppingCartService_DIAS.DeleteCart(AthanasiaGlobal_DIAS.ACTUAL_USER.ID);
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(SuccesfulSale){
                Toast.makeText(getContext(), "Succesful Sale", Toast.LENGTH_SHORT).show();
                AthanasiaGlobal_DIAS.SHOPPING_CART.clear();
                itemArrayAdapter.UpdateArrayAdapter();
            } else {
                Toast.makeText(getContext(), "Failed Sale", Toast.LENGTH_SHORT).show();
            }
            SuccesfulSale = false;
        }
    }
    class SaveCartTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            SuccesfulSale = ShoppingCartService_DIAS.SaveCart(AthanasiaGlobal_DIAS.SHOPPING_CART, AthanasiaGlobal_DIAS.ACTUAL_USER.ID);
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(SuccesfulSale){
                Toast.makeText(getContext(), "Cart saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Cart cannot be saved", Toast.LENGTH_SHORT).show();
            }
            SuccesfulSale = false;
        }
    }

}