package com.fisei.athanasiaapp.services;

import com.fisei.athanasiaapp.objects.Product_DIAS;
import com.fisei.athanasiaapp.utilities.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProductService_DIAS {
    public static List<Product_DIAS> GetAllProducts(){
        List<Product_DIAS> productDIASList = new ArrayList<>();
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.PRODUCTS);
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray list = data.getJSONArray("data");
                for(int i = 0; i < list.length(); ++i){
                    JSONObject products = list.getJSONObject(i);
                    productDIASList.add(new Product_DIAS(
                            products.getInt("id"),
                            products.getString("name"),
                            products.getString("genre"),
                            products.getInt("quantity"),
                            products.getDouble("unitPrice"),
                            products.getDouble("cost"),
                            URLs.PRODUCTS_IMAGES + products.getString("imageURL")));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return productDIASList;
    }
    public static Product_DIAS GetSpecifiedProductByID(int id){
        Product_DIAS productDIAS = new Product_DIAS(0, "", "", 0, 0, 0, "");
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.PRODUCTS + "/" + id);
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray list = data.getJSONArray("data");
                for(int i = 0; i < list.length(); ++i){
                    JSONObject products = list.getJSONObject(i);
                    productDIAS = new Product_DIAS(
                            products.getInt("id"),
                            products.getString("name"),
                            products.getString("genre"),
                            products.getInt("quantity"),
                            products.getDouble("unitPrice"),
                            products.getDouble("cost"),
                            URLs.PRODUCTS_IMAGES + products.getString("imageURL"));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return productDIAS;
    }

}