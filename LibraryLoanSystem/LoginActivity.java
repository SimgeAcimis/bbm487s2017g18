package com.example.enes_karabulut.library;

/**
 * Created by enes_karabulut on 7.04.2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText editTextUserName;
    private EditText editTextPassword;
    public static final String USER_NAME = "USERNAME";
    String username;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    public void invokeLogin(View view){
        username = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();
        login(username,password);
    }
    private void login(final String username, String password) {
        class LoginAsync extends AsyncTask<String, Void, String>{
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading...");
            }
            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://www.pcarsiv.com/library/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("1907", username);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }
        LoginAsync la = new LoginAsync();
        la.execute(username, password);
    }
}
