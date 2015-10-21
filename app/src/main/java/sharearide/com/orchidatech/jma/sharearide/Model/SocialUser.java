package sharearide.com.orchidatech.jma.sharearide.Model;

/**
 * Created by tareq on 06/11/2015.
 */
public class SocialUser {

    public String email;
    public String name;
    public String id;
    public String social_id;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String password;

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int network;
    public String avatarURL;

    public static class NetworkType {
        public static final int GOOGLEPLUS = 1;
        public static final int FACEBOOK = 2;
    }
}