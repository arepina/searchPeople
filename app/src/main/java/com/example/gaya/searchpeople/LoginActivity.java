package com.example.gaya.searchpeople;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;

public class LoginActivity extends AppCompatActivity {

    final public String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=gayasearchpeople;" +
                    "AccountKey=SfUDHBj5Lvulzj5U3a2DP9h9Gyexeq2CpF/sDoCx2BZ0SUFheOuA8l6ZzoQTS2rjE5mHiJIYXLwlBz4EWnySWw==";

    public String name, surname, username, password, email, age, phone;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final Button bRegister = (Button) findViewById(R.id.tvRegister);

//        etUsername.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 "));
//        etPassword.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 "));


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.isConnected(v.getContext())) {
                    if (TextUtils.isEmpty(etUsername.getText().toString())) {
                        etUsername.setError("The username cannot be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(etPassword.getText().toString())) {
                        etPassword.setError("The username cannot be empty");
                        return;
                    }
                    NetworkConnector connector = new NetworkConnector(etUsername.getText().toString(), etPassword.getText().toString());
                    connector.execute();
                }
            }
        });

    }

    /**
     * Try to get the educational programs
     */
    class NetworkConnector extends AsyncTask<Void, Void, String> {

        private String userName, userPassword;

        NetworkConnector(String userName, String userPassword) {
            this.userName = userName;
            this.userPassword = userPassword;
        }


        @Override
        protected String doInBackground(Void... params) {
            try {

                CloudStorageAccount storageAccount =
                        CloudStorageAccount.parse(storageConnectionString);
                CloudTableClient tableClient = storageAccount.createCloudTableClient();
                CloudTable cloudTable = tableClient.getTableReference("userInfo");
                TableOperation retrieveUsernamePassword =
                        TableOperation.retrieve(userName, userPassword, DBclass.class);
                DBclass specificEntity =
                        cloudTable.execute(retrieveUsernamePassword).getResultAsType();
                if (specificEntity != null) {
                    password = userPassword;
                    username = userName;
                    email = specificEntity.getEmail();
                    age = specificEntity.getAge();
                    phone = specificEntity.getPhoneNumber();
                    name = specificEntity.getName();
                    surname = specificEntity.getSurname();
                    return "OK";
                } else {
                    return "Username and/or password is wrong!";
                }
            } catch (Exception e) {
                // Output the stack trace.
                e.printStackTrace();
                return "";
            }
        }

        /**
         * finish the loading
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("OK")) {
                Intent phinIntent = new Intent(LoginActivity.this, PhotoInfoActivity.class);
                LoginActivity.this.startActivity(phinIntent);
            } else if (result.length() != 0) {
                etPassword.setError(result);
            }

        }
    }

}
