package sharearide.com.orchidatech.jma.sharearide.Chat;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sharearide.com.orchidatech.jma.sharearide.R;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    public static String email = "shareef@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHash();

        try {
            adapter = new SimpleCursorAdapter(this, R.layout.main_list_item, null, new String[]{
                    DataProvider.COL_NAME,
                    DataProvider.COL_COUNT}, new int[]{
                    R.id.text1,
                    R.id.text2}, 0);

            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    switch (view.getId()) {
                        case R.id.text2:
                            int count = cursor.getInt(columnIndex);
                            if (count > 0) {
                                ((TextView) view).setText(String.format("%d new message%s", count, count == 1 ? "" : "s"));
                            }
                            return true;
                    }
                    return false;
                }
            });
            setListAdapter(adapter);

            ActionBar actionBar = getActionBar();
            actionBar.setDisplayShowTitleEnabled(false);

            getLoaderManager().initLoader(0, null, this);

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "I exception " + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }

    private void getHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.i("key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                AddContactDialog dialog = new AddContactDialog();
                dialog.show(getFragmentManager(), "AddContactDialog");
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Common.PROFILE_ID, String.valueOf(id));
        startActivity(intent);
    }

    // ----------------------------------------------------------------------------

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DataProvider.CONTENT_URI_PROFILE, new String[]{
                DataProvider.COL_ID, DataProvider.COL_NAME,
                DataProvider.COL_COUNT}, null, null,
                DataProvider.COL_ID + " DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
