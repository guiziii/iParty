package android.tabhost;

        import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.tabhost.adapters.GetDataAdapter;
import android.tabhost.adapters.RecyclerViewAdapter;
import android.tabhost.interfaces.RecyclerViewOnClickListenerHack;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//                                             LISTA PRINCIPAL

public class Tab1 extends Fragment implements RecyclerViewOnClickListenerHack{
    private FragmentTabHost mTabHost;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    @Override
    public void onClickListener(View view, int position) {

    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }

    RecyclerViewAdapter mAdapter ;

    GestureDetector mGestureDetector;

    SwipeRefreshLayout mSwipeRefreshLayout;
    List<GetDataAdapter> GetDataAdapter1;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    String GET_JSON_DATA_HTTP_URL = "http://guiziii.esy.es/SelectEventJson.php";
    String GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/ListCity.php?cidade_evento=";
    //  ----------------------------------------------------------------------------------------------------
    //  |EXEMPLO DA NOVA FORMA DE PESQUISA COM CIDADE E ESTADO                                               |
    //  |http://guiziii.esy.es/ListCitybyState.php?cidade_evento=Sao%20Jose%20dos%20Campos&estado_evento=SP  |
    //  ----------------------------------------------------------------------------------------------------
    String JSON_IMAGE_URL = "img_evento";
    String JSON_DATA = "data_evento";
    String JSON_HORA = "hora_evento";
    String JSON_IDEVENTO = "id_evento";
    String JSON_IMAGE_TITLE_NAME = "nome_evento";
    String JSON_PRECO = "preco_evento";
    String JSON_LATITUDE = "latitude_evento";
    String JSON_LONGITUDE = "longitude_evento";
    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState;
    private static int dyb;

    private Toolbar mToolbar;
    TextView txtTitle, txtSubtitle;
    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.tab1_view, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swifeRefresh);
        //mListener = listener;

        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        mToolbar = (Toolbar) getActivity().findViewById(R.id.tb_main);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                {
                    dyb = dy;
                    // Scrolling down
                    //Toast.makeText(getActivity(), "Scrolling down", Toast.LENGTH_SHORT).show();
                    //mToolbar.hideOverflowMenu();
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().hide();

                } else
                {


                    // Scrolling up
                    //  Toast.makeText(getActivity(), "Scrolling up", Toast.LENGTH_SHORT).show();
                    mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                {
                    mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().show();
                }
                else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().show();
                }
                else
                {
                    mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().show();
                }
            }
        });



        txtTitle = (TextView)getActivity().findViewById(R.id.txtTitle);
        txtSubtitle= (TextView)getActivity().findViewById(R.id.txtSubtitle);
        txtTitle.setText("iParty");
        txtSubtitle.setVisibility(View.VISIBLE);




        mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
        try
        {
            if (Static.getNomeCIDADE() == null || Static.getUfESTADO() == null)
            {

                txtSubtitle.setText("Clique aqui para selecionar a cidade");
            }
            else
            {

                txtSubtitle.setText(Static.getNomeCIDADE()+" - " + Static.getUfESTADO());
            }

        }               catch (Exception e){}

        txtSubtitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if(mTabHost.getCurrentTabTag().toString() == "tab1")
                {

                GetDataAdapter1 = new ArrayList<>();
                recyclerView.setHasFixedSize(true);
                recyclerViewlayoutManager = new LinearLayoutManager(getActivity());
                GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/ListCity.php?cidade_evento=" + Static.getNomeCIDADE().replace(" ","%20").trim();
                JSON_DATA_WEB_CALL();

                }





            }

            @Override
            public void afterTextChanged(Editable s)
            {



            }
        });



//        mToolbar.setSubtitleTextColor(android.graphics.Color.rgb(255, 255, 255));
        // mToolbar.setTitleTextColor(android.graphics.Color.rgb(255, 255, 255));

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                   ((MainActivity) getActivity()).openDialogFragment(v);
                }
                catch (Exception e){}


            }
        });

        try
        {
            //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
            //img.setAnimation(hyperspaceJumpAnimation);
            Field f = mSwipeRefreshLayout.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView)f.get(mSwipeRefreshLayout);
            img.setBackgroundResource(R.color.black_de);
            img.setImageResource(R.drawable.evento);
        }
        catch (NoSuchFieldException e) {e.printStackTrace();}
        catch (IllegalAccessException e) {e.printStackTrace();}

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


                GetDataAdapter1 = new ArrayList<>();

                recyclerView.setHasFixedSize(true);

                recyclerViewlayoutManager = new LinearLayoutManager(getActivity());

                recyclerView.setLayoutManager(recyclerViewlayoutManager);
                if(Static.getNomeCIDADE() != null) {
                    GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/ListCity.php?cidade_evento=" + Static.getNomeCIDADE().replace(" ", "%20").trim();
                    Log.i("LOG", "Cidade: " + Static.getNomeCIDADE());
                    Log.i("LOG", "Estado: " + Static.getUfESTADO());
                }
                JSON_DATA_WEB_CALL();

            }
        });




        if(Static.getNomeCIDADE() != null)
        {
            GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/ListCity.php?cidade_evento=" + Static.getNomeCIDADE().replace(" ", "%20").trim();
        }
        else
        {
            GET_JSON_DATA_HTTP_URL2 = "http://guiziii.esy.es/SelectEventJson.php";
        }
        JSON_DATA_WEB_CALL();

        return view;
    }



    //region Metodo pra manter o estado da lista
    @Override
    public void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null)
        {
            //if(dyb>0){((ActionBarActivity)getActivity()).getSupportActionBar().hide();}
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);

        }
    }
    //endregion

    //region Chamada no Host
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL2,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
        mSwipeRefreshLayout.setRefreshing(false);

            mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
            ((ActionBarActivity)getActivity()).getSupportActionBar().show();


    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            final GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);
                class JsonTask extends AsyncTask<String,String,String> {


                    @Override
                    protected String doInBackground(String... params) {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL(params[0]);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.connect();
                            InputStream stream = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(stream));
                            StringBuffer buffer = new StringBuffer();
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                buffer.append(line);
                            }
                            String finalJson = buffer.toString();
                            JSONObject parentObject = new JSONObject(finalJson);

                            JSONArray parentArray = parentObject.getJSONArray("routes");



                            JSONObject finalObject = parentArray.getJSONObject(0);

                            JSONArray parentArray2 = finalObject.getJSONArray("legs");

                            JSONObject finalObject2 = parentArray2.getJSONObject(0);

                            JSONObject parentArray3 = finalObject2.getJSONObject("start_location");




                            String distance = parentArray3.getString("lng");

                            return distance;

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                            try {
                                if (reader != null) {
                                    reader.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                    @Override
                    protected void  onPostExecute(String result)
                    {super.onPostExecute(result);
                        if(result !=null)
                        {
                            GetDataAdapter2.setDistancia(result);

                        }
                        else
                        {
                            GetDataAdapter2.setDistancia("Nothing");
                        }


                    }
                }
                GetDataAdapter2.setImageTitleNamee(json.getString(JSON_IMAGE_TITLE_NAME));
                GetDataAdapter2.setDataImage(json.getString(JSON_DATA));
                GetDataAdapter2.setHoraImage(json.getString(JSON_HORA));
                GetDataAdapter2.setId_evento(json.getString(JSON_IDEVENTO));
                GetDataAdapter2.setImageServerUrl(json.getString(JSON_IMAGE_URL));
                GetDataAdapter2.setPreco(json.getString(JSON_PRECO));
                GetDataAdapter2.setLatitudeEvento(json.getString(JSON_LATITUDE));
                GetDataAdapter2.setLongitudeEvento(json.getString(JSON_LONGITUDE));
                // new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+json.getString(JSON_LONGITUDE)+","+json.getString(JSON_LATITUDE)+"&destination=-22.91199364,-43.23021412");

            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, getActivity());

        recyclerView.setAdapter(recyclerViewadapter);



    }

//endregion


}