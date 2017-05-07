package android.tabhost;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CadCli.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CadCli#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadCli extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText cadnome, cademail, cadlogin,cadsenha;
    Button btnCad;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CadCli() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CadCli.
     */
    // TODO: Rename and change types and number of parameters
    public static CadCli newInstance(String param1, String param2) {
        CadCli fragment = new CadCli();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }





    public void insert(){

        String nome_cli = cadnome.getText().toString();
        String  email_cli = cademail.getText().toString();
        String  login_cli = cadlogin.getText().toString();
        String  senha_cli = cadsenha.getText().toString();

        insertToDatabase(nome_cli, email_cli, login_cli, senha_cli);

    }

    private void insertToDatabase(String nome, String email,String login,String senha){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramNome = params[0];
                String paramEmail = params[1];
                String paramLogin = params[2];
                String paramSenha = params[3];

                //InputStream is = null;

                String nome_cli = cadnome.getText().toString();
                String  email_cli = cademail.getText().toString();
                String  login_cli = cadlogin.getText().toString();
                String  senha_cli = cadsenha.getText().toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("nome_cli", nome_cli));
                nameValuePairs.add(new BasicNameValuePair("email_cli", email_cli));
                nameValuePairs.add(new BasicNameValuePair("login_cli", login_cli));
                nameValuePairs.add(new BasicNameValuePair("senha_cli", senha_cli));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://guiziii.esy.es/InsertCli.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    //is = entity.getContent();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                //TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                // textViewResult.setText("Inserted");
            }



        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(nome, email, login, senha);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    { final View view = inflater.inflate(R.layout.fragment_cad_cli, container, false);
        // Inflate the layout for this fragment

        cadnome = (EditText) view.findViewById(R.id.txtCadnome);
        cademail = (EditText) view.findViewById(R.id.txtCademail);
        cadlogin = (EditText) view.findViewById(R.id.txtCadlogin);
        cadsenha = (EditText) view.findViewById(R.id.txtCadsenha);
        btnCad = (Button) view.findViewById(R.id.btnCadastrar);
        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                insert();
                Toast.makeText(getActivity(), "onClickListener(): ", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onAttach(Context context) {
        super.onAttach((Activity) context);
        /*
        if (context instanceof OnFragmentInteractionListener) { mListener = (OnFragmentInteractionListener) context; } else {throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }*/
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
}
