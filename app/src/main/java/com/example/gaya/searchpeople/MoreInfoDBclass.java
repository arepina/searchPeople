package com.example.gaya.searchpeople;

import android.net.Uri;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class MoreInfoDBclass extends TableServiceEntity {
    public MoreInfoDBclass(String username, String password) {
        final LoginActivity loginActivity = new LoginActivity();
        this.partitionKey = loginActivity.password;
        this.rowKey = loginActivity.username;
    }

    public MoreInfoDBclass() { }

    private String comment;
    private Uri image;

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Uri getImage() {
        return this.image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

}