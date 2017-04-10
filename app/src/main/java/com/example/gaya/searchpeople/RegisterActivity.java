package com.example.gaya.searchpeople;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final  LoginActivity loginActivity = new LoginActivity();

        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etTryPassword = (EditText) findViewById(R.id.etTryPassword);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etSurname = (EditText) findViewById(R.id.etSurname);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etCity = (EditText) findViewById(R.id.etCity);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        etUsername.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 "));
        etTryPassword.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 "));
        etPassword.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 "));
        etName.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "));
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etSurname.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "));
        etSurname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etCity.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));

        etTryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPassword.getText().toString().equals(etTryPassword.getText().toString()))
                {
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());
                    shape.getPaint().setColor(0x55FF0000);
                    shape.getPaint().setStyle(Paint.Style.STROKE);
                    shape.getPaint().setStrokeWidth(3);

                    etTryPassword.setBackground(shape);
                }
                else
                {
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());
                    shape.getPaint().setColor(0xFF12FF45);
                    shape.getPaint().setStyle(Paint.Style.STROKE);
                    shape.getPaint().setStrokeWidth(3);

                    etTryPassword.setBackground(shape);
                }

            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError("The name cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(etSurname.getText().toString())) {
                    etSurname.setError("The surname cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(etEmail.getText().toString())) {
                    etEmail.setError("The email cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(etPhone.getText().toString())) {
                    etPhone.setError("The phone cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(etUsername.getText().toString())) {
                    etUsername.setError("The username cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText().toString())) {
                    etPassword.setError("The password cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(etSurname.getText().toString())) {
                    etPassword.setError("Please, confirm password");
                    return;
                }

                try
                {
                    CloudStorageAccount storageAccount =
                            CloudStorageAccount.parse(loginActivity.storageConnectionString);

                    CloudTableClient tableClient = storageAccount.createCloudTableClient();

                    CloudTable cloudTable = tableClient.getTableReference("userinfo");
                    TableOperation retrieveUsernamePassword =
                            TableOperation.retrieve(etUsername.getText().toString(), etPassword.getText().toString(), DBclass.class);

                    DBclass specificEntity =
                            cloudTable.execute(retrieveUsernamePassword).getResultAsType();

                    if (specificEntity != null)
                    {
                        etUsername.setError("This username is already used.");
                        return;

                    }
                    else
                    {
                        DBclass newuser = new DBclass(etUsername.getText().toString(), etPassword.getText().toString());
                        newuser.setEmail(etEmail.getText().toString());
                        newuser.setAge(etAge.getText().toString());
                        newuser.setName(etName.getText().toString());
                        newuser.setSurname(etSurname.getText().toString());
                        newuser.setPhoneNumber(etPhone.getText().toString());
                        loginActivity.username = etUsername.getText().toString();
                        loginActivity.password = etPassword.getText().toString();
                        loginActivity.name = etName.getText().toString();
                        loginActivity.surname = etSurname.getText().toString();
                        loginActivity.phone = etPhone.getText().toString();
                        loginActivity.email = etEmail.getText().toString();
                        loginActivity.age = etAge.getText().toString();


                        TableOperation insertNewuser = TableOperation.insertOrReplace(newuser);

                        cloudTable.execute(insertNewuser);

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Intent phinIntent = new Intent(RegisterActivity.this, PhotoInfoActivity.class);
                RegisterActivity.this.startActivity(phinIntent);
            }
        });

    }
}

