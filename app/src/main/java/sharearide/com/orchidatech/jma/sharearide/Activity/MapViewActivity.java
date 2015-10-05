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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
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

    MapView map;
    //private MapController mapController;

    private Toolbar tool_bar;

    private GeoPoint startPoint;
    private GeoPoint endPoint,loc;

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

        map = (MapView) findViewById(R.id.mapview);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
      
        //e.onLongPress(onC)
       // Intent intent = getIntent();
//        String cityFrom = intent.getStringExtra("CityFrom");
//        String cityTo = intent.getStringExtra("CityTo");
//        Toast.makeText(MapViewActivity.this, cityFrom + "" + cityTo, Toast.LENGTH_LONG).show();
//////////////////////////

//      setStartPoint(31.52333, 34.44664);
//      setEndPoint(31.51381, 34.44193);
//



        // initializeRoute();
//       Toast.makeText(this,getLocationFromAddress(cityFrom).getLongitude()+" , "+getLocationFromAddress(cityFrom).getLongitude(),Toast.LENGTH_LONG).show();
//        Toast.makeText(this,getLocationFromAddress(cityTo).getLongitude()+" , "+getLocationFromAddress(cityTo).getLongitude(),Toast.LENGTH_LONG).show();


    }

    public void setStartPoint(double aLongitude, double aAltitude) {
        startPoint = new GeoPoint(aLongitude, aAltitude);
    }

    public void setEndPoint(double aLongitude, double aAltitude) {
        endPoint = new GeoPoint(aLongitude, aAltitude);
    }

    private void initializeRoute() {

        //GeoPoint startPoint = new GeoPoint(31.52333, 34.44664);
        IMapController mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);

        // Using the Marker overlay
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Start point");
        //startMarker.setIcon(getResources().getDrawable(R.drawable.marker_kml_point).mutate());
        //startMarker.setImage(getResources().getDrawable(R.drawable.ic_launcher));
        //startMarker.setInfoWindow(new MarkerInfoWindow(R.layout.bonuspack_bubble_black, map));
        //startMarker.setDraggable(true);
        //startMarker.setOnMarkerDragListener(new OnMarkerDragListenerDrawer());
        map.getOverlays().add(startMarker);


        RoadManager roadManager = new OSRMRoadManager();

        //roadManager roadManager = new MapQuestRoadManager("YOUR_API_KEY");
        //roadManager.addRequestOption("routeType=bicycle");
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        //GeoPoint endPoint = new GeoPoint(31.51381, 34.44193);
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

    public GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }


//    public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
//
//        Projection proj = mapView.getProjection();
//        startPoint = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
//        proj = mapView.getProjection();
//         loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
//        String longitude = Double
//                .toString(((double) loc.getLongitudeE6()) / 1000000);
//        String latitude = Double
//                .toString(((double) loc.getLatitudeE6()) / 1000000);
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "Longitude: "
//                        + longitude + " Latitude: " + latitude, Toast.LENGTH_SHORT);
//        toast.show();
//        return true;
//    }
//
//    private void addLocation(double lat, double lng) {
//        // ---Add a location marker---
//
//        startPoint = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
//
//        Drawable marker = getResources().getDrawable(
//                android.R.drawable.star_big_on);
//
//        int markerWidth = marker.getIntrinsicWidth();
//        int markerHeight = marker.getIntrinsicHeight();
//
//        marker.setBounds(0, markerHeight, markerWidth, 0);
//
//        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
//                getApplicationContext());
//
//
//
//        map.invalidate();
//    }
class myClass extends MapEventsOverlay {

    public myClass(Context ctx, MapEventsReceiver receiver) {
        super(ctx, receiver);
    }

    GeoPoint loc, startPoint;

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
        Projection proj = mapView.getProjection();
        startPoint = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
        proj = mapView.getProjection();
        loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
        String longitude = Double
                .toString(((double) loc.getLongitudeE6()) / 1000000);
        String latitude = Double
                .toString(((double) loc.getLatitudeE6()) / 1000000);
        Toast toast = Toast.makeText(MapViewActivity.this,
                "Longitude: "
                        + longitude + " Latitude: " + latitude, Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }

    private void addLocation(double lat, double lng) {
        // ---Add a location marker---

        startPoint = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

        Drawable marker = getResources().getDrawable(
                android.R.drawable.star_big_on);

        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();

        marker.setBounds(0, markerHeight, markerWidth, 0);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
                getApplicationContext());


        map.invalidate();
    }
}}
