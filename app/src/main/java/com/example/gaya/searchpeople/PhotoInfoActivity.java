package com.example.gaya.searchpeople;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;

public class PhotoInfoActivity extends AppCompatActivity {

    private static final int SELECTED_PICTURE = 100;
    Uri uri;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final LoginActivity loginActivity = new LoginActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);
        Button bAddPhoto=(Button) findViewById(R.id.bAddPhoto);
        Button bNext=(Button) findViewById(R.id.bNext);
        final EditText etComment = (EditText) findViewById(R.id.etComment);
        ImageView iv = (ImageView) findViewById(R.id.imageView);

        bAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    CloudStorageAccount storageAccount =
                            CloudStorageAccount.parse(loginActivity.storageConnectionString);

                    CloudTableClient tableClient = storageAccount.createCloudTableClient();

                    CloudTable cloudTable = tableClient.getTableReference("moreuserinfo");
                    // Create a new customer entity.
                    MoreInfoDBclass moreInfoDBclass = new MoreInfoDBclass(loginActivity.username, loginActivity.password);
                    moreInfoDBclass.setImage(uri);
                    moreInfoDBclass.setComment(etComment.getText().toString());

                    TableOperation insertMoreInfo = TableOperation.insertOrReplace(moreInfoDBclass);

                    cloudTable.execute(insertMoreInfo);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Intent phinIntent = new Intent(PhotoInfoActivity.this, UserMainPageActivity.class);
                PhotoInfoActivity.this.startActivity(phinIntent);
            }
        });
            }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE)
        {
            uri = data.getData();
            iv.setImageURI(uri);
        }
    }
}
