package com.build2gether.fx.OOP;

import android.net.Uri;
import android.os.Bundle;
import com.facebook.Profile;

/**
 * Created by Tina on 3/4/2016.
 */
public class User {

    private String id;
    private String name;
    private Uri picUri;

    public User(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.picUri = profile.getProfilePictureUri(25, 25);
    }
    public User() {

    }

    public Uri getPicUri() {
        return picUri;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
