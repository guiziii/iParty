package android.tabhost;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends ActionBarActivity //region Implements
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,  TabHost.OnTabChangeListener
        //endregion
{
    //region Variáveis
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static String TAG = "LOG";
    private FragmentTabHost mTabHost;

    private MaterialDialog mMaterialDialog;
    public static final String PREFS_NAME = "0";
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private Toolbar mToolbar;
    TextView txtTitle, txtSubtitle;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);    Log.i("LOG", "onCreate()");

        //callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        /*loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });
        */



        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String login = settings.getString("0", "");
        if(login != "")
        {
            Static.setId(Integer.parseInt(login));
        }
        //region TabHost - Configuração
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.add3)), (Tab1.class), savedInstanceState);

        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.add2)), Tab2.class, savedInstanceState);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.add)), Tab3.class, savedInstanceState);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.add4)), Tab4.class, savedInstanceState);
        mTabHost.addTab(mTabHost.newTabSpec("tab5").setIndicator("", getResources().getDrawable(R.drawable.add5)), Tab5.class, savedInstanceState);
        mTabHost.setOnTabChangedListener(this);


//endregion
        //region Permissão
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                callDialog("É preciso a permission ACCESS_FINE_LOCATION para apresentação dos eventos locais.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            callConnection();
        }

        //endregion

        //region mToolbar - Configuração
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSubtitle = (TextView) findViewById(R.id.txtSubtitle);

        if (Static.getCidade() == null || Static.getEstado() == null)
        {
            txtSubtitle.setText("Clique aqui para selecionar a cidade");
        }
        else
        {
            txtSubtitle.setText(Static.getNomeCIDADE() + " - " + Static.getUfESTADO().replace(" ", "%20"));
        }

        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
//endregion

    }

    //region Pedido - Permissão
    private void callDialog(String message, final String[] permissions) {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage(message)
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    //endregion
    //region Métodos de utilização da permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        callConnection();
                    } else if (permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {


                    } else if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {


                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdate();
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            stopLocationUpdate();
        }
    }


    private synchronized void callConnection() {
        Log.i("LOG", "callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void startLocationUpdate() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    //endregion
    //region LISTENERS
    @Override
    public void onConnected(Bundle bundle) {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.i("LOG", "onConnected(" + bundle + ")");
            Location l = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (l != null) {
                Log.i("LOG", "latitude: " + l.getLatitude());
                Log.i("LOG", "longitude: " + l.getLongitude());
            }


            startLocationUpdate();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "onConnectionFailed(" + connectionResult + ")");
    }

    @Override
    public void onLocationChanged(Location location) {

        // lblLat.setText(Html.fromHtml(location.getLatitude() + "<br />"));
//        lblLong.setText(Html.fromHtml(location.getLongitude() + "<br />"));


        Static.setLatitude(String.valueOf(location.getLatitude()));
        Static.setLongitude(String.valueOf(location.getLongitude()));

        // new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+a+","+b+"&destination=-22.91199364,-43.23021412&key=AIzaSyBnV5B7rEhkPHstsm8jklO9QoEyA2MzVYg");
        //new JsonTask2().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+a+","+b+"&destination=-22.91199364,-43.23021412&key=AIzaSyBnV5B7rEhkPHstsm8jklO9QoEyA2MzVYg");


    }

    //endregion
    //region Transição do Evento para a Lista Principal
    public void switchContent(android.support.v4.app.Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        ft.replace(R.id.realtabcontent, fragment);
        //ft.show(fragment);
        //ft.addToBackStack(null);
        ft.commit();
    }

    //endregion
    //region TabHost - onTabChanged (Troca de telas pelo menu de baixo)
    @Override
    public void onTabChanged(String tabId) {
        if (mTabHost.getCurrentTabTag().toString() == "tab1") {

            Tab1 frag = new Tab1();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.realtabcontent, frag);
            //ft.show(fragment);
            //ft.addToBackStack(null);
            ft.commit();

        }
        if (mTabHost.getCurrentTabTag().toString() == "tab2") {

            Tab2 frag = new Tab2();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.realtabcontent, frag);
            //ft.show(fragment);
            //ft.addToBackStack(null);
            ft.commit();

        }
        if (mTabHost.getCurrentTabTag().toString() == "tab3") {

            Tab3 frag = new Tab3();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.realtabcontent, frag);
            //ft.show(fragment);
            //ft.addToBackStack(null);
            ft.commit();

        }
        if (mTabHost.getCurrentTabTag().toString() == "tab4") {
//android.support.v4.app.Fragment frag2 =new android.support.v4.app.Fragment().getParentFragment(Tab4.class);
            Tab4 frag = new Tab4();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.realtabcontent, frag);
            //ft.show(fragment);
            //ft.addToBackStack(null);
            ft.commit();

        }
        if (mTabHost.getCurrentTabTag().toString() == "tab5") {

            Tab5 frag = new Tab5();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.realtabcontent, frag);
            //ft.show(fragment);
            //ft.addToBackStack(null);
            ft.commit();

        }

    }

    //endregion
    // region Fragment de Seleção de Cidade e Estado
   /* public void openDialogFragment(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in, R.anim.rotate);
        CustomDialogFragment cdf = new CustomDialogFragment(4, 1);
        cdf.show(ft, "dialog");
    }*/


  /*  public void turnOffDialogFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sequential, R.anim.rotate);
        CustomDialogFragment cdf = (CustomDialogFragment) getSupportFragmentManager().findFragmentByTag("dialog");
        if (cdf != null) {
            cdf.dismiss();
            ft.remove(cdf).setCustomAnimations(R.anim.sequential, R.anim.rotate);

        }}*/
  public void openDialogFragment(View view)
  {
      CustomDialogFragment2 frag = new CustomDialogFragment2();
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
      ft.replace(R.id.realtabcontent, frag);
      //ft.show(fragment);
      //ft.addToBackStack(null);
      ft.commit();
  }




    //endregion


    public static LayoutInflater from(Context context)
    {
        LayoutInflater LayoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (LayoutInflater == null)
        {
            throw new AssertionError("LayoutInflater not found.");
        }
        return LayoutInflater;
    }

}
