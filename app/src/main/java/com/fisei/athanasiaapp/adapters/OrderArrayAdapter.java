package com.fisei.athanasiaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fisei.athanasiaapp.OrderDetailsActivity_DIAS;
import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.objects.Order_DIAS;
import com.fisei.athanasiaapp.utilities.Utils;

import java.util.List;

public class OrderArrayAdapter extends ArrayAdapter<Order_DIAS> {
    private static class ViewHolder{
        TextView orderDateView;
        TextView orderIDView;
        TextView orderTotalView;
        Button orderInfoBtn;
    }

    public OrderArrayAdapter(Context context, List<Order_DIAS> orderDIASList) {
        super(context, -1, orderDIASList);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Order_DIAS orderDIAS = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_myorders, parent, false);
            viewHolder.orderDateView = (TextView) convertView.findViewById(R.id.textViewOrderDate);
            viewHolder.orderIDView = (TextView) convertView.findViewById(R.id.textViewOrderId);
            viewHolder.orderTotalView = (TextView) convertView.findViewById(R.id.textViewOrderTotal);
            viewHolder.orderInfoBtn = (Button) convertView.findViewById(R.id.btnOrderInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderDateView.setText(Utils.ConvertDate(orderDIAS.Date));
        viewHolder.orderIDView.setText(String.format("%s", orderDIAS.ID));
        viewHolder.orderTotalView.setText(String.format("%s", orderDIAS.Total + " $"));
        viewHolder.orderInfoBtn.setOnClickListener(view -> {
            ShowOrderDetails(orderDIAS);
        });
        return convertView;
    }

    private void ShowOrderDetails(Order_DIAS orderDIAS){
        Intent orderDetails = new Intent(getContext(), OrderDetailsActivity_DIAS.class);
        orderDetails.putExtra("orderID", orderDIAS.ID);
        orderDetails.putExtra("orderUserClient", orderDIAS.UserClientID);
        orderDetails.putExtra("orderDate", orderDIAS.Date);
        orderDetails.putExtra("orderTotal", orderDIAS.Total);
        getContext().startActivity(orderDetails);
    }
}