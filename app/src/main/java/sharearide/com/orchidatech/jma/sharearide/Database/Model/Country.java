package sharearide.com.orchidatech.jma.sharearide.Database.Model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Shadow on 9/10/2015.
 */
@Table(name = "Countries", id = BaseColumns._ID)
public class Country extends Model {

    @Column(name = "remote_id", unique = true)
    public long remoteId;

    @Column(name = "Name")
    public String name;

    @Column(name = "Alpha")
    public String alpha;


    public Country() {

    }
}
