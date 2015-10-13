package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by sms-1 on 9/21/2015.
 */
public class MapViewActivity extends AppCompatActivity {
    double FromCityLattitude,FromCityLongitude,ToCityLattitude,ToCityLongitude;
    MapView map;
    //private MapController mapController;
    private Toolbar tool_bar;
    private GeoPoint startPoint;
    private GeoPoint endPoint,loc;
    FloatingActionButton ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Disable StrictMode.ThreadPolicy to perform network calls in the UI thread.
        //Yes, it's not the good practice, but this is just a tutorial!
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        getSupportActionBar().setTitle("View Ride ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle b=new Bundle();
        Bundle vv=getIntent().getExtras();

        double FromCityLattitude= vv.getDouble("FromCityLattitude");
        double   FromCityLongitude= vv.getDouble("FromCityLongitude");
        double ToCityLattitude= vv.getDouble("ToCityLattitude");
        double ToCityLongitude= vv.getDouble("ToCityLongitude");

        //Toast.makeText(MapViewActivity.this,"  ,  "+FromCityLongitude
           //     +"  ,  "+FromCityLattitude+"  ,  "+ToCityLongitude+"  ,   "+ToCityLattitude ,Toast.LENGTH_LONG).show();
        map = (MapView) findViewById(R.id.mapview);
        ok=(FloatingActionButton)findViewById(R.id.ok);
        ok.setVisibility(View.GONE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);



// startPoint = new GeoPoint(f, ff);
//endPoint = new GeoPoint(t, tt);
        initializeRoute();
//} else Toast.makeText(MapViewActivity.this,"null value",Toast.LENGTH_LONG).show();

    }

    public void setStartPoint(double aLongitude, double aAltitude) {
        startPoint = new GeoPoint(aLongitude, aAltitude);
    }

    public void setEndPoint(double aLongitude, double aAltitude) {
        endPoint = new GeoPoint(aLongitude, aAltitude);
    }

    private void initializeRoute() {

        GeoPoint startPoint = new GeoPoint(FromCityLattitude, FromCityLongitude);
        IMapController mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);

        // Using the Marker overlay
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Start point");
        map.getOverlays().add(startMarker);
        GeoPoint endPoint = new GeoPoint(ToCityLattitude, ToCityLongitude);

        RoadManager roadManager = new OSRMRoadManager();
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        Road road = roadManager.getRoad(waypoints);
        if (road.mStatus != Road.STATUS_OK)
            Toast.makeText(this, "Error when loading the road - status=" + road.mStatus, Toast.LENGTH_SHORT).show();

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
        map.getOverlays().add(roadOverlay);

        // Showing the Route steps on the map
        FolderOverlay roadMarkers = new FolderOverlay(this);
        map.getOverlays().add(roadMarkers);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_node);
        for (int i = 0; i < road.mNodes.size(); i++) {
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(map);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(nodeIcon);

            // Filling the bubbles
            nodeMarker.setTitle("Step " + i);
            nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setSubDescription(Road.getLengthDurationText(node.mLength, node.mDuration));
            Drawable iconContinue = getResources().getDrawable(R.drawable.ic_continue);
            nodeMarker.setImage(iconContinue);
            // end

            roadMarkers.add(nodeMarker);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
