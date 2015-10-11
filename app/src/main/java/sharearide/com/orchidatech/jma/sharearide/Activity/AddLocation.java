package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DirectedLocationOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.R;

public class AddLocation extends ActionBarActivity implements MapEventsReceiver , LocationListener {
    MapView map;
    //private MapController mapController;
    private FloatingActionButton ok,gps;
    private Toolbar tool_bar;
    private int requestCode;
    final private double MAP_DEFAULT_LATITUDE = 24.350584;
    final private double MAP_DEFAULT_LONGITUDE = 53.9663425;
    private GeoPoint startPoint;
    private GeoPoint endPoint,loc;
    GeoPoint mClickedGeoPoint; //any other way to pass the position to the menu ???
    protected Marker markerStart, markerDestination;
    protected ViaPointInfoWindow mViaPointInfoWindow;
    protected DirectedLocationOverlay myLocationOverlay;
    MyLocationNewOverlay myLocationNewOverlay;
    private String lattitude=null;
    private String longittude=null;
    protected LocationManager mLocationManager;
    private ResourceProxy mResourceProxy;
    private ItemizedOverlay mItemizedOverlay;
    private MyLocationOverlay mMyLocationOverlay;
    private LocationManager locationManager;
    private Location location;
    private boolean isGPSEnabled;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 sec
    double lat, lng;
    private OverlayItem lastPosition = null;
    Context context;
    IMapController mapController;
    private ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        context=this;
        map = (MapView) findViewById(R.id.mapview);

////////////////////////////////////////////////
        // Setup the mapView controller:
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setClickable(true);
        map.setUseDataConnection(false);

        // map.setTileSource(TileSourceFactory.MAPNIK);
   /* location manager */

/////////////////////////////////////
        Intent inte=getIntent();
        requestCode=inte.getIntExtra("Request", -1);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getSupportActionBar().setTitle("Add Your Ride");
        useWIFI();
        stopUsingGPS();


        ok=(FloatingActionButton)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                if(lattitude!=null &longittude!=null ){
                    intent.putExtra("From_Lattitude",lattitude);
                    intent.putExtra("From_Longittude",longittude);

                    if(requestCode == 100) {
                        //  Toast.makeText(AddLocation.this,""+lattitude+"  , "+longittude+"",Toast.LENGTH_LONG).show();
                        setResult(100, intent);
                        finish();
                    }else if(requestCode == 101){
                        // Toast.makeText(AddLocation.this,""+lattitude+"  , "+longittude+"",Toast.LENGTH_LONG).show();
                        setResult(101, intent);
                        finish();
                    }
                }
            }
        });
        map = (MapView) findViewById(R.id.mapview);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, (MapEventsReceiver) this);
        map.getOverlays().add(0, mapEventsOverlay);
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
//        Toast.makeText(this, "Tapped", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Tap on ("+geoPoint.getLatitude()+","+geoPoint.getLongitude()+")", Toast.LENGTH_SHORT).show();

        return false;

    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        Toast.makeText(this, " Location Fetched ...", Toast.LENGTH_SHORT).show();
        lattitude=geoPoint.getLatitude()+"";
        longittude=geoPoint.getLongitude()+"";
      //  Toast.makeText(this, " Location Fetched ..."+lattitude+longittude, Toast.LENGTH_SHORT).show();
//        Intent intent=new Intent();
//        if(lattitude!=null &longittude!=null ){
//            intent.putExtra("From_Lattitude",lattitude);
//            intent.putExtra("From_Longittude",longittude);
//
//            if(requestCode == 100) {
//                //  Toast.makeText(AddLocation.this,""+lattitude+"  , "+longittude+"",Toast.LENGTH_LONG).show();
//                setResult(100, intent);
//                finish();
//            }else if(requestCode == 101){
//                // Toast.makeText(AddLocation.this,""+lattitude+"  , "+longittude+"",Toast.LENGTH_LONG).show();
//                setResult(101, intent);
//                finish();
//            }
//        }
//DO NOTHING FOR NOW:
        return true;

    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lng  = location.getLongitude();
//        startPoint=new GeoPoint(lat, lng);
//        mapController = map.getController();
//        mapController.setZoom(13);
//        mapController.setCenter(startPoint);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private void useGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {

            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
              //  Log.d("GPS : ", "Enabled!");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                     //   Log.d("GGG", "Location not null!");
                        lat = location.getLatitude();
                        lng = location.getLongitude();

                    }
                }
            }
        } else
            showSettingsAlert();
    }

    public void useWIFI() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled && checkConn(context)) {

            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                //Log.d("WIFI : ", "Enabled!");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                      //  Log.d("GGG", "Location not null!");
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        startPoint=new GeoPoint(lat, lng);
                        mapController = map.getController();
                        mapController.setZoom(13);
                        mapController.setCenter(startPoint);
                        Marker startMarker = new Marker(map);
                        startMarker.setPosition(startPoint);
                        startMarker.setTitle("Start point");
//                        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_kml_point).mutate());
//                        startMarker.setImage(getResources().getDrawable(R.drawable.ic_launcher));
                        //startMarker.setInfoWindow(new MarkerInfoWindow(R.layout.bonuspack_bubble_black, map));
                        //startMarker.setDraggable(true);
                        //startMarker.setOnMarkerDragListener(new OnMarkerDragListenerDrawer());
                        map.getOverlays().add(startMarker);

                    }
                }
            }
        } else
            useGPS();
    }
    public boolean checkConn(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() == null)
            return false;
        if (conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else if (!conMgr.getActiveNetworkInfo().isConnected()) {
            return false;
        }
        return false;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Enable GPS");
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 1999);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(AddLocation.this);
        }}
}
