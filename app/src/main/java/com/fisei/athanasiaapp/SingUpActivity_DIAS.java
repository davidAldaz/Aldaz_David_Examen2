package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fisei.athanasiaapp.models.ResponseAthanasia_DIAS;
import com.fisei.athanasiaapp.objects.UserClient_DIAS;
import com.fisei.athanasiaapp.services.UserClientService_DIAS;

import org.json.JSONObject;

import java.net.URL;

public class SingUpActivity_DIAS extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCedula;
    private EditText editTextPassword;
    private TextView errorTextView;
    private Button buttonSignUp;
    private ResponseAthanasia_DIAS responseTask = new ResponseAthanasia_DIAS(false, "");

    private String validatePasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        InitializeViewComponents();



    }
    private class SignUpTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            UserClient_DIAS newUser = new UserClient_DIAS(0, editTextName.getText().toString(),
                    editTextEmail.getText().toString() + "@ath.com",
                    editTextCedula.getText().toString(), "");
            responseTask = UserClientService_DIAS.SignUpNewUser(newUser, editTextPassword.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(responseTask.Success){
                StartLoginActivity();
            } else {
                errorTextView.setText(responseTask.Message);
            }
            responseTask.Success = false;
        }
    }
    private void InitializeViewComponents(){
        editTextEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        editTextName = (EditText) findViewById(R.id.editTextSignUpName);
        editTextCedula = (EditText) findViewById(R.id.editTextSignUpCedula);
        editTextPassword = (EditText) findViewById(R.id.editTextSignUpPassword);
        errorTextView = (TextView) findViewById(R.id.textViewSignUpFail1);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(signUpButtonClicked);
    }
    private void SignUp(){
        validatePasswd = editTextPassword.getText().toString();
        if(editTextEmail.getText().toString().isEmpty() || editTextName.getText().toString().isEmpty() ||
                editTextCedula.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
            errorTextView.setText(R.string.fields_empty_error);
        }else if(validatePasswd.length() < 6 || validatePasswd.length() > 10){
            errorTextView.setText("La contraseña debe contener \n mínimo 6 caracteres y máximo 10");
        }else if(!validatePasswd.matches(".*[!@#$%^&*+=?-].*")) {
            errorTextView.setText("Debe ingresar al menos un caracter especial");
        }else if(!validatePasswd.matches(".*\\d.*")) {
            errorTextView.setText("Debe ingresar al menos un número");
        }else if(!validatePasswd.matches(".*[a-z].*")) {
            errorTextView.setText("Debe contener una letra minúscula");
        }else if(!validatePasswd.matches(".*[A-Z].*")) {
            errorTextView.setText("Debe contener una letra mayúscula");
        }else {
            errorTextView.setText("Validado");
            //errorTextView.setText("");
            //SignUpTask signUpTask = new SignUpTask();
            //signUpTask.execute();
        }
    }
    private void StartLoginActivity(){
        Intent backLogin = new Intent(this, LoginActivity_DIAS.class);
        startActivity(backLogin);
        Toast.makeText(this, "Your register was successful", Toast.LENGTH_SHORT).show();
    }
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();

}