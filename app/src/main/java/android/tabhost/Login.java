package android.tabhost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    private OnFragmentInteractionListener mListener;
     //region inutil
    public Login() {
        // Required empty public constructor
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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




    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //endregion
    ImageView img1,img2,img3,img4,img5;
    EditText txtSenha,txtLogin;   String resposta;public static final String PREFS_NAME = "0";
    Button btnCadLogin,btnLogin;

    private CallbackManager callbackManager = null;
    private AccessTokenTracker mtracker = null;
    private ProfileTracker mprofileTracker = null;

    public static final String PARCEL_KEY = "parcel_key";

    private LoginButton loginButton;

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    View Rd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();



        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                Log.v("AccessTokenTracker", "oldAccessToken=" + oldAccessToken + "||" + "CurrentAccessToken" + currentAccessToken);
            }
        };


        mprofileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                Log.v("Session Tracker", "oldProfile=" + oldProfile + "||" + "currentProfile" + currentProfile);
                homeFragment(currentProfile);

            }
        };

        mtracker.startTracking();
        mprofileTracker.startTracking();
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void homeFragment(Profile profile) {

        if (profile != null) {


            Bundle mBundle = new Bundle();
            mBundle.putParcelable(PARCEL_KEY, profile);
            HomeFragment hf = new HomeFragment();
            hf.setArguments(mBundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
            fragmentTransaction.commit();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        String login = settings.getString("0", "");

        if(login != "")
        {
            Static.setId(Integer.parseInt(login));
        }
        if(Static.getId() == 0)
        {

        }
        else
        {
            startActivity(new Intent(getActivity(), MainActivity.class));

        }
        Rd = view.findViewById(R.id.parentLayout);

        txtLogin = (EditText)view.findViewById(R.id.txtLogin);
        txtSenha =(EditText) view.findViewById(R.id.txtSenha);
        btnCadLogin = (Button)view.findViewById(R.id.btnCadLogin);
        btnLogin = (Button)view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logpes();
            }
        });

        btnCadLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment CadCli = new CadCli();

                ft.replace(R.id.mainContainer, CadCli, "CadCli");
                ft.commit();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.setReadPermissions(Arrays.asList("user_location"));
        loginButton.setReadPermissions(Arrays.asList("user_birthday"));


        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        mtracker.stopTracking();
        mprofileTracker.stopTracking();
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isLoggedIn())
        {
            Rd.setVisibility(getView().INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }

    }

//region Login normal
    public void Logpes()
    {
        String login = txtLogin.getText().toString();
        String senha = txtSenha.getText().toString();

        LogpesToDatabase(login, senha);
    }
    private void LogpesToDatabase(String login, String senha){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramAddress = params[1];


                //InputStream is = null;

                String login = txtLogin.getText().toString();
                String senha = txtSenha.getText().toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("login_cli", login));
                nameValuePairs.add(new BasicNameValuePair("senha_cli", senha));


                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://guiziii.esy.es/LoginPon.php");
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
                // AlertDialog.Builder dialogo = new AlertDialog.Builder(zLogin.this);
                //dialogo.setTitle("Carregando");
                //dialogo.setMessage("Aguarde...");
                //dialogo.setNeutralButton("OK", null);
                //dialogo.show();
                final ProgressDialog dialog =
                        new ProgressDialog(getActivity());
                dialog.setMessage("Enviando dados... aguarde");
                dialog.setIndeterminate(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();

                if(Integer.parseInt(result) != 0) {

                    //Intent tela = new Intent(LoginA.this, Testefundo1.class);startActivity(tela);
                    Toast.makeText(getActivity(), "Logado com sucesso", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    // finish();
                    //ID.setId(Integer.parseInt(result));
                    Static.setId(Integer.parseInt(resposta));

                    startActivity(new Intent(getActivity(), MainActivity.class));





                    SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("0", resposta);

                    //Confirma a gravação dos dados
                    editor.commit();

                }
                else {Toast.makeText(getActivity(), "LoginA e/ou senha incorretos", Toast.LENGTH_LONG).show();dialog.cancel();}






            }


        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login, senha);

    }

//endregion















}
