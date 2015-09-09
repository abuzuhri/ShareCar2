package sharearide.com.orchidatech.jma.sharearide.Model;

/**
 * Created by Shadow on 9/8/2015.
 */
public class SearchResultInfo {

    protected String name;
    protected String time;
    protected String date;

    public SearchResultInfo(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
