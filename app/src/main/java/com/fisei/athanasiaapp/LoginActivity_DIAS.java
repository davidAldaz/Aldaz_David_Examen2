package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fisei.athanasiaapp.objects.AthanasiaGlobal_DIAS;
import com.fisei.athanasiaapp.objects.Product_DIAS;
import com.fisei.athanasiaapp.objects.ShopCartItem_DIAS;
import com.fisei.athanasiaapp.objects.UserClient_DIAS;
import com.fisei.athanasiaapp.services.ProductService_DIAS;
import com.fisei.athanasiaapp.services.ShoppingCartService_DIAS;
import com.fisei.athanasiaapp.services.UserAdminService_DIAS;
import com.fisei.athanasiaapp.services.UserClientService_DIAS;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity_DIAS extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwdEditText;
    private TextView warningTextView;

    private UserClient_DIAS user;

    private String validatePasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeViewComponents();
    }
    private void Login(){
        warningTextView.setText("");
        LoginTask loginTask = new LoginTask();
        loginTask.execute();

    }
    private Boolean LoginAdmin(){
            warningTextView.setText("");
            LoginAdminTask loginAdminTask = new LoginAdminTask();
            loginAdminTask.execute();
        return true;
    }
    private void SignUp(){
        Intent register = new Intent(this, SingUpActivity_DIAS.class);
        startActivity(register);
    }
    private class LoginTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            user = UserClientService_DIAS.Login(emailEditText.getText().toString(), passwdEditText.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(user.JWT != null){
                AthanasiaGlobal_DIAS.ACTUAL_USER.JWT = user.JWT;
                AthanasiaGlobal_DIAS.ACTUAL_USER.ID = user.ID;
                GetUserCartTask getUserCartTask = new GetUserCartTask();
                getUserCartTask.execute();
            } else {
                warningTextView.setText(R.string.label_wrong_email_password);
            }
        }
    }
    private class LoginAdminTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            user = UserAdminService_DIAS.Login(emailEditText.getText().toString(), passwdEditText.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(user.JWT != null){
                AthanasiaGlobal_DIAS.ACTUAL_USER.JWT = user.JWT;
                AthanasiaGlobal_DIAS.ADMIN_PRIVILEGES = true;
                StartAthanasiaActivity();
            } else {
                warningTextView.setText(R.string.label_wrong_email_password);
            }
        }
    }
    private class GetUserCartTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            AthanasiaGlobal_DIAS.SHOPPING_CART = ShoppingCartService_DIAS.GetShopCartFromUserLogged(user.ID);
            List<ShopCartItem_DIAS> tempList = new ArrayList<>();
            for (ShopCartItem_DIAS item: AthanasiaGlobal_DIAS.SHOPPING_CART) {
                Product_DIAS p = ProductService_DIAS.GetSpecifiedProductByID(item.Id);
                if(p.quantity < item.Quantity){
                    item.Quantity = 1;
                }
                tempList.add(new ShopCartItem_DIAS(p.id, p.name, p.imageURL, item.Quantity, p.unitPrice, p.quantity));
            }
            AthanasiaGlobal_DIAS.SHOPPING_CART = tempList;
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            StartAthanasiaActivity();
        }
    }

    private void StartAthanasiaActivity(){
        Intent loginSuccesful = new Intent(this, AthanasiaActivity_DIAS.class);
        startActivity(loginSuccesful);
        finish();
    }
    private void InitializeViewComponents(){
        emailEditText = findViewById(R.id.editTextEmailLogin);
        passwdEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(loginButtonClicked);
        loginButton.setOnLongClickListener(loginAdminButtonClicked);
        Button signUpButton = findViewById(R.id.btnRegister);
        signUpButton.setOnClickListener(signUpButtonClicked);
        warningTextView = findViewById(R.id.textViewLoginFailed);
    }
    private final View.OnClickListener loginButtonClicked = view -> Login();
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();
    private final View.OnLongClickListener loginAdminButtonClicked = view -> LoginAdmin();
}