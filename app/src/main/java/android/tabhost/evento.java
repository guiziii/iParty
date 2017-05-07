package android.tabhost;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.tabhost.adapters.ConfigRetrieve;
import android.tabhost.adapters.ConfigRetrieveCliente;
import android.tabhost.adapters.ServerImageParseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class evento extends Fragment
{
    private FragmentTabHost mTabHost=null;
    //region Desnecessário no momento
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public evento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment evento.
     */
    // TODO: Rename and change types and number of parameters
    public static evento newInstance(String param1, String param2) {
        evento fragment = new evento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //endregion
    TextView NomeEvento,DataEvento,Horaevento,DescEvento,PrecoEvento,DistanciaE,EnderecoE,CriadorE;
    String idcli;

    private Toolbar mToolbar;
    ImageLoader imageLoader1;
    public NetworkImageView networkImageView2 ;

    private ProgressDialog loading;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_evento, container, false);

        NomeEvento = (TextView)view.findViewById(R.id.txtNomeEvento);
        DataEvento = (TextView)view.findViewById(R.id.txtData);
        Horaevento = (TextView)view.findViewById(R.id.txtHora);
        DescEvento = (TextView)view.findViewById(R.id.txtDescEvento);
        PrecoEvento =(TextView)view.findViewById(R.id.txtpreco);
        DistanciaE = (TextView)view.findViewById(R.id.txtDistanciaE);
        EnderecoE = (TextView)view.findViewById(R.id.txtEnderecoE);
        CriadorE = (TextView)view.findViewById(R.id.txtCriadorE);
        networkImageView2=(NetworkImageView)view.findViewById(R.id.VollyEspecifico);
        //DescEvento.setTypeface(EasyFonts.freedom(getActivity()));


        mToolbar = (Toolbar) getActivity().findViewById(R.id.tb_main);
        mToolbar.setLogo(null);


        //mToolbar.setVisibility(view.VISIBLE);


        getData();




        ((ActionBarActivity)getActivity()).getSupportActionBar().show();

        return view;
    }

    private void getData()
    {


        loading = ProgressDialog.show(getActivity(), "Carregando informações do evento!", "Por favor, aguarde", false, false);

        String url = ConfigRetrieve.DATA_URL+String.valueOf(Static.getIdevento()).trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            loading.setCancelable(true);
                            e.printStackTrace();
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void getData1()
    {


       // loading = ProgressDialog.show(getActivity(), "Carregando informações do evento!", "Por favor, aguarde", false, false);

        String url = ConfigRetrieveCliente.DATA_URL+idcli;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON2(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                           // loading.setCancelable(true);
                            e.printStackTrace();
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response)
    {
        String name="";
        String address="";
        String vc = "";
        String desc = "";
        String hora = "";
        String preco = "";
        String endereco = "";
        String latitude = "";
        String longitude = "";
        String id = "";

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(ConfigRetrieve.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(ConfigRetrieve.KEY_NAME);
            address = collegeData.getString(ConfigRetrieve.KEY_ADDRESS);
            vc = collegeData.getString(ConfigRetrieve.KEY_VC);
            desc = collegeData.getString(ConfigRetrieve.KEY_DESC);
            hora = collegeData.getString(ConfigRetrieve.KEY_HORA);
            preco = collegeData.getString(ConfigRetrieve.KEY_PRECO);
            endereco = collegeData.getString(ConfigRetrieve.KEY_END);
            latitude = collegeData.getString(ConfigRetrieve.KEY_LATITUDE);
            longitude = collegeData.getString(ConfigRetrieve.KEY_LONGITUDE);
            id = collegeData.getString(ConfigRetrieve.KEY_IDCLI);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        imageLoader1 = ServerImageParseAdapter.getInstance(getContext()).getImageLoader();

        imageLoader1.get(vc,
                ImageLoader.getImageListener(
                        networkImageView2,//Server Image
                        R.drawable.image,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );
        networkImageView2.setImageUrl(vc, imageLoader1);

       // NomeEvento.setText(name);
        DataEvento.setText(address);
        DescEvento.setText(desc);
        Horaevento.setText(hora);
        PrecoEvento.setText(preco);
        EnderecoE.setText(" "+endereco);
        idcli =id;
        getData1();
       // new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+latitude.toString().replace(" ", "")+','+longitude.toString().replace(" ", "")+"&destination=-23.2349128,-45.8990308");
        new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + longitude.toString().replace(" ", "") + ',' + latitude.toString().replace(" ", "") + "&destination=" + Static.getLatitude() + "," + Static.getLongitude() + "");


        mToolbar.setTitle(name);
        mToolbar.setSubtitle("R$" + preco);
        //region Click da Toolbar + Ícone da Toolbar
       mToolbar.setNavigationIcon(R.drawable.back); //Icone de Voltar

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbar.setTitle("");
                mToolbar.setSubtitle("");
                mToolbar.setNavigationIcon(null);
                Tab1 frag = new Tab1();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left,0);
                ft.replace(R.id.realtabcontent, frag, "mainFrag");
                ft.commit();

            }
        });
        //endregion

    }

    private void showJSON2(String response)
    {
        String name="";
        String email="";
        String login = "";
        String senha = "";


        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result2 = jsonObject.getJSONArray(ConfigRetrieveCliente.JSON_ARRAY);
            JSONObject collegeData = result2.getJSONObject(0);
            name = collegeData.getString(ConfigRetrieveCliente.KEY_NAME);
            email = collegeData.getString(ConfigRetrieveCliente.KEY_EMAIL);
            login = collegeData.getString(ConfigRetrieveCliente.KEY_LOGIN);
            senha = collegeData.getString(ConfigRetrieveCliente.KEY_SENHA);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
      CriadorE.setText(" "+name);

    }

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

                JSONObject parentArray3 = finalObject2.getJSONObject("distance");




                String distance = parentArray3.getString("text");

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
                DistanciaE.setText(" "+result);
                //Viewholder.bar.setProgress(32);

            }
            else
            {
                DistanciaE.setText("Erro inesperado no calculo da distância");
            }


        }
    }

}



