package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
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

import java.util.ArrayList;

import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by sms-1 on 9/21/2015.
 */
public class MapViewActivity extends AppCompatActivity {

    MapView map;
    //private MapController mapController;

    private Toolbar tool_bar;

    private GeoPoint startPoint;
    private GeoPoint endPoint;

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
        setStartPoint(31.52333, 34.44664);
        setEndPoint(31.51381, 34.44193);
        initializeRoute();
       Intent intent=getIntent();
       String cityFrom= intent.getStringExtra("CityFrom");
       String cityTo=intent.getStringExtra("CityTo");
       Toast.makeText(MapViewActivity.this,cityFrom+""+cityTo,Toast.LENGTH_LONG).show();
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

}
