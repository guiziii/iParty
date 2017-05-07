package android.tabhost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MeuPerfil extends Fragment
{
    String resposta;
    EditText txtemail,txtusuario,txtsenha;
    TextView txtnome;


    Button btnfundo1,btnfundo2,btnfundo3,btnfundo4,btnfundo5,btnmeusair,btnMeuseventos;
    public static final String PREFS_NAME = "0";
    int a =0,b=0;

    //region TALVEZ INUTIL
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MeuPerfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeuPerfil.
     */
    // TODO: Rename and change types and number of parameters
    public static MeuPerfil newInstance(String param1, String param2) {
        MeuPerfil fragment = new MeuPerfil();
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
//endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_meu_perfil, container, false);
        //region FindViews
        btnMeuseventos=(Button)view.findViewById(R.id.btnMeuseventos);
        btnmeusair=(Button) view.findViewById(R.id.btnMeusair);
        txtnome = (TextView)view.findViewById(R.id.txtnomep);
        txtemail=(EditText)view.findViewById(R.id.txtemailp);
        txtsenha=(EditText)view.findViewById(R.id.txtsenhap);
        txtusuario=(EditText)view.findViewById(R.id.txtusuariop);
        //endregion
        //region BOTÃO DE SAÍDA
        btnmeusair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Static.setFace(false);
                SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("0", "");

                //Confirma a gravação dos dados
                editor.commit();
                Static.setId(0);
                updateDetail();


            }
        });


//endregion
        //region Execução de pesquisas
        if(Static.getId() != 0)
        {
            PesqToDatabase(String.valueOf(Static.getId()));
            PesqToDatabaseEmail(String.valueOf(Static.getId()));
            PesqToDatabaseLogin(String.valueOf(Static.getId()));
            PesqToDatabaseSenha(String.valueOf(Static.getId()));
        }
        else
        {
            txtemail.setText("Facebook");
            txtusuario.setEnabled(false);
            txtsenha.setEnabled(false);
            txtnome.setText("Facebook");

        }
        //endregion
        //region  Meus eventos
        btnMeuseventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment MeusEventos = new MeusEventos();

                ft.replace(R.id.realtabcontent, MeusEventos, "MeusEventos");
                ft.commit();

            }
        });
        //endregion
        return view;
    }
    public void updateDetail() {
        Intent intent = new Intent(getActivity(), LoginA.class);
        startActivity(intent);
    }

    //region Metodos de Pesquisas
    private void PesqToDatabase(String id_cli){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                //InputStream is = null;

                String id_cli = String.valueOf(Static.getId());


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id_cli", id_cli));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://guiziii.esy.es/SelectNome.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    resposta = sb.toString();
                    return sb.toString();

                    //is = entity.getContent();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";


            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                // TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Inserted");
                txtnome.setText(resposta);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_cli);

    }


    private void PesqToDatabaseEmail(String id_cli){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                //InputStream is = null;

                String id_cli = String.valueOf(Static.getId());


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id_cli", id_cli));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://guiziii.esy.es/SelectEmail.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    resposta = sb.toString();
                    return sb.toString();

                    //is = entity.getContent();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";


            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                // TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Inserted");
                txtemail.setText(resposta);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_cli);

    }


    private void PesqToDatabaseLogin(String id_cli){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                //InputStream is = null;

                String id_cli = String.valueOf(Static.getId());


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id_cli", id_cli));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://guiziii.esy.es/SelectLogin.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    resposta = sb.toString();
                    return sb.toString();

                    //is = entity.getContent();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";


            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                // TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Inserted");
                txtusuario.setText(resposta);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_cli);

    }

    private void PesqToDatabaseSenha(String id_cli){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                //InputStream is = null;

                String id_cli = String.valueOf(Static.getId());


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id_cli", id_cli));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://guiziii.esy.es/SelectMeuSenha.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    resposta = sb.toString();
                    return sb.toString();

                    //is = entity.getContent();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";


            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                // TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Inserted");
                txtsenha.setText(resposta);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_cli);

    }
//endregion

    //region TALVEZ INUTIL 2
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onAttach(Context context) {
        super.onAttach((Activity) context);

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

}
