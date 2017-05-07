package android.tabhost;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.tabhost.adapters.GetDataAdapter;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//                                          MAPA
public class Tab4 extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    //region Toolbar
    TextView txtTitle, txtSubtitle;
    private Toolbar mToolbar;
    //endregion
    private static final String TAG = "Gmapfragment";
    String latitude;
    Double longitude;
    LatLng newLat;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    String GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/SelectEventJson.php";
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    String JSON_LATITUDE = "latitude_evento";
    String JSON_LONGITUDE = "longitude_evento";
    String JSON_IMAGE_TITLE_NAME = "nome_evento";
    JSONArray array = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        //region Toolbar
        txtTitle = (TextView)getActivity().findViewById(R.id.txtTitle);
        txtSubtitle= (TextView)getActivity().findViewById(R.id.txtSubtitle);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.tb_main);
        //endregion
        //region Toolbar
        try
        {
            txtTitle.setText("Mapa");
            //txtSubtitle.setVisibility(View.INVISIBLE);
            txtSubtitle.setText("Encontre o seu evento e use o GPS!");
        }
        catch(Exception e){}






        //endregion
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);

            //callAccessLocation();
            //CRIANDO MARCADOR
            // LatLng Marcador2 = new LatLng(-23.1764511, -45.8884048);
            //MarkerOptions marker2 = new MarkerOptions();
            //marker2.position(Marcador2);
            //marker2.title("Tenente Nevio Baracho");
            //mMap.addMarker(marker2);
            if (Static.getNomeCIDADE() != null)
            {
                GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/ListCity.php?cidade_evento=" + Static.getNomeCIDADE().replace(" ", "%20").trim();
            } else {
                GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/SelectEventJson.php";
            }

            //region WebCall
            jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL2,

                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            array = response;
                            JSON_PARSE_DATA_AFTER_WEBCALL(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            requestQueue = Volley.newRequestQueue(getActivity());

            requestQueue.add(jsonArrayRequest);

//endregion




            } catch (SecurityException ex) {
                Log.e(TAG, "ERROR", ex);

            }
        }



    @Override
    public void onMapClick(LatLng latLng)
    {

    }



    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            final GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);
                GetDataAdapter2.setImageTitleNamee(json.getString(JSON_IMAGE_TITLE_NAME));
                GetDataAdapter2.setLatitudeEvento(json.getString(JSON_LATITUDE));
                GetDataAdapter2.setLongitudeEvento(json.getString(JSON_LONGITUDE));
                LatLng Marcador = new LatLng(-Double.valueOf(json.getString(JSON_LONGITUDE).replace("-", "")), -Double.valueOf(json.getString(JSON_LATITUDE).replace("-", "")));
                MarkerOptions marker = new MarkerOptions();
                marker.position(Marcador);
                marker.title(json.getString(JSON_IMAGE_TITLE_NAME));
                mMap.addMarker(marker);

                // new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+json.getString(JSON_LONGITUDE)+","+json.getString(JSON_LATITUDE)+"&destination=-22.91199364,-43.23021412");

            } catch (JSONException e) {

                e.printStackTrace();
            }
        //    GetDataAdapter1.add(GetDataAdapter2);
        }




    }
}
