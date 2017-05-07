package android.tabhost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

//                                             CADASTRO DE EVENTO
public class Tab3 extends Fragment //implements AdapterView.OnItemSelectedListener
{
    //region Toolbar   --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- ----
    TextView txtTitle, txtSubtitle;
    private Toolbar mToolbar;
    //endregion
    //region Variaveis

    int a =0,b=0;
    EditText txtNomeEvento,txtCidadeEvento,txtpreco,txtEnderecoEvento,txtDataEvento,txtDescricaoEvento,txtHoraEvento,txtFaceEvento;
    TextView lblLatitude,lblLongitude,lblID;
    Button btnCadEvento;
    String idest="1",estado="AC",cidade="Assis Brasil";
    String TodoEndereco;
    private Spinner spinner3,spinner4;
    private ArrayList<String> students3,students4;
    private JSONArray result3 = null,result4;

    private Toast toast;
    private long lastBackPressTime = 0;
    private int i=0;
    private  boolean continuar =false;
    Context context;
    private ImageView imageView;

    private ImageButton btnChoose;

    String estadoid="1";
    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://guiziii.esy.es/photos/EventPhoto.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private String KEY_IDCLI = "id_cli";
    private String KEY_NomeEvento = "nome_evento";
    private String KEY_EstadoEvento = "estado_evento";
    private String KEY_CidadeEvento = "cidade_evento";
    private String KEY_LatitudeEvento = "latitude_evento";
    private String KEY_LongitudeEvento = "longitude_evento";
    private String KEY_DataEvento = "data_evento";
    private String KEY_HoraEvento = "hora_evento";
    private String KEY_FaceEvento = "face_evento";
    private String KEY_DescEvento = "desc_evento";
    private String KEY_PrecoEvento = "preco_evento";
    private String KEY_EndEvento = "end_evento";
    Animation animFadein,blink,rotate;
    //endregion

    //region INUTIL
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab3() {
        // Required empty public constructor
    }

        // TODO: Rename and change types and number of parameters
    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //region Inutil2

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onAttach(Context context) {
        super.onAttach((Activity) context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);

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
//endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_tab3, container, false);

        if (getActivity()!=null) {

            Static.setId(8);
             context = view.getContext();
            // region LoadAnimation[Configurando a Animação em relação ao arquivo .xml]
            animFadein = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
            blink = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down);
            rotate = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate);
            //endregion
            btnCadEvento = (Button) view.findViewById(R.id.btnCadEvento);
            txtNomeEvento = (EditText) view.findViewById(R.id.txtNomeEvento);
            //region Toolbar  --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- ---------------------
            mToolbar = (Toolbar) getActivity().findViewById(R.id.tb_main);
            txtTitle = (TextView)getActivity().findViewById(R.id.txtTitle);
            txtSubtitle= (TextView)getActivity().findViewById(R.id.txtSubtitle);
            //region Toolbar  --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- --------------------- ---------------------
            try
            {
                txtTitle.setText("Cadastrar meu evento");
                //txtSubtitle.setVisibility(View.INVISIBLE);
                txtSubtitle.setText("Deixe todo mundo saber do seu evento!");
            }
            catch(Exception e){}
            mToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        ProgressDialog.show(getActivity(), "Carregando informações do evento!", "Por favor, aguarde", false, true);
                    }
                    catch (Exception e){}


                }
            });





            //endregion
            //endregion
            txtpreco = (EditText) view.findViewById(R.id.txtpreco);
            txtEnderecoEvento = (EditText) view.findViewById(R.id.txtEnderecoEvento);
            txtDataEvento = (EditText) view.findViewById(R.id.txtDataEvento);
            txtDescricaoEvento = (EditText) view.findViewById(R.id.txtDescricaoEvento);
            txtHoraEvento = (EditText) view.findViewById(R.id.txtHoraEvento);
            txtFaceEvento = (EditText) view.findViewById(R.id.txtFaceEvento);
            lblLatitude = (TextView) view.findViewById(R.id.lblLatitude);
            lblLongitude = (TextView) view.findViewById(R.id.lblLongitude);
            btnChoose = (ImageButton) view.findViewById(R.id.btnChoose1);
            lblID = (TextView) view.findViewById(R.id.lblID);
            imageView = (ImageView) view.findViewById(R.id.imageView6);
            students3 = new ArrayList<String>();
            students4 = new ArrayList<String>();
            spinner3 = (Spinner) view.findViewById(R.id.spinner3);
            spinner4 = (Spinner) view.findViewById(R.id.spinner4);
            //spinner3.setOnItemSelectedListener(this);
            //spinner4.setOnItemSelectedListener(this);
            //getData3();
            //getData4();
            TextWatcher tw = new TextWatcher() {
                private String current = "";
                private String ddmmyyyy = "DDMMYYYY";
                private Calendar cal = Calendar.getInstance();

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!s.toString().equals(current)) {
                        String clean = s.toString().replaceAll("[^\\d.]", "");
                        String cleanC = current.replaceAll("[^\\d.]", "");

                        int cl = clean.length();
                        int sel = cl;
                        for (int i = 2; i <= cl && i < 6; i += 2) {
                            sel++;
                        }
                        //Fix for pressing delete next to a forward slash
                        if (clean.equals(cleanC)) sel--;

                        if (clean.length() < 8) {
                            clean = clean + ddmmyyyy.substring(clean.length());
                        } else {
                            //This part makes sure that when we finish entering numbers
                            //the date is correct, fixing it otherwise
                            int day = Integer.parseInt(clean.substring(0, 2));
                            int mon = Integer.parseInt(clean.substring(2, 4));
                            int year = Integer.parseInt(clean.substring(4, 8));

                            if (mon > 12) mon = 12;
                            cal.set(Calendar.MONTH, mon - 1);
                            year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                            cal.set(Calendar.YEAR, year);
                            // ^ first set year for the line below to work correctly
                            //with leap years - otherwise, date e.g. 29/02/2012
                            //would be automatically corrected to 28/02/2012

                            day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                            clean = String.format("%02d%02d%02d", day, mon, year);
                        }

                        clean = String.format("%s/%s/%s", clean.substring(0, 2),
                                clean.substring(2, 4),
                                clean.substring(4, 8));

                        sel = sel < 0 ? 0 : sel;
                        current = clean;
                        txtDataEvento.setText(current);
                        txtDataEvento.setSelection(sel < current.length() ? sel : current.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }


            };

            txtDataEvento.addTextChangedListener(tw);
            btnCadEvento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploadImage();
                }
            });
            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                }
            });


        }

        return view;
    }









    public class JsonTask extends AsyncTask<String,String,String> {


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




                String distance = parentArray3.getString("lat");

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
                lblLongitude.setText(result);


            }
            else
            {
                lblLongitude.setText("Caculo indisponivel");
            }


        }
    }


    public class JsonTask2 extends AsyncTask<String,String,String> {


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
                lblLatitude.setText(result);


            }
            else
            {
                lblLatitude.setText("Caculo indisponivel");
            }


        }
    }




    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Cadastrando Evento","Por favor, aguarde...",false,false);
        new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + txtEnderecoEvento.getText().toString().replace(" ", "") + "&destination=-22.91199364,-43.23021412");
        new JsonTask2().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + txtEnderecoEvento.getText().toString().replace(" ", "") + "&destination=-22.91199364,-43.23021412");



        new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + txtEnderecoEvento.getText().toString().replace(" ", "") + "&destination=-22.91199364,-43.23021412");




        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                while (continuar == false) {if (lblLatitude.getText().toString().equals("0") || lblLongitude.getText().toString().equals("0")){continuar = false;} else {continuar = true;}}

                //Getting Image Name
                // String name = txtNomeEvento.getText().toString().trim();
                String Id_cli = String.valueOf(Static.getId()).trim();
                String nome_evento = txtNomeEvento.getText().toString().trim();
                String  estado_evento =Static.getUfESTADO() ;
                String  cidade_evento = Static.getNomeCIDADE();
                String  latitude_evento = lblLatitude.getText().toString().trim();
                String  longitude_evento = lblLongitude.getText().toString().trim();
                String  data_evento = txtDataEvento.getText().toString().trim();
                String  hora_evento = txtHoraEvento.getText().toString().trim();
                String  face_evento = txtFaceEvento.getText().toString().trim();
                String  desc_evento = txtDescricaoEvento.getText().toString().trim();
                String  preco_evento = txtpreco.getText().toString().trim();
                String end_evento=txtEnderecoEvento.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_IDCLI, Id_cli);
                params.put(KEY_NomeEvento, nome_evento);
                params.put(KEY_EstadoEvento, estado_evento);
                params.put(KEY_CidadeEvento, cidade_evento);
                params.put(KEY_LatitudeEvento, latitude_evento);
                params.put(KEY_LongitudeEvento, longitude_evento);
                params.put(KEY_DataEvento, data_evento);
                params.put(KEY_HoraEvento, hora_evento);
                params.put(KEY_FaceEvento, face_evento);
                params.put(KEY_DescEvento, desc_evento);
                params.put(KEY_PrecoEvento, preco_evento);
                params.put(KEY_EndEvento, end_evento);


                //returning parameters
                return params;


            }
        };

        //Creating a Request Queue
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }




    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                btnChoose.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







}


