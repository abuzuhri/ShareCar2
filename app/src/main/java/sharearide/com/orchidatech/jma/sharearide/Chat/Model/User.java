package sharearide.com.orchidatech.jma.sharearide.Chat.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Shadow on 9/14/2015.
 */
public class User {

    public long id;

    public String displayName;

    public Drawable image;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
