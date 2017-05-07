package android.tabhost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private ImageView profile_pic = null;
    private TextView tv = null,txtemail=null;
    private Button logoutButton = null;
    private Profile profile = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        tv = (TextView) view.findViewById(R.id.tv_name);
        txtemail = (TextView) view.findViewById(R.id.txtemail42);
        logoutButton = (Button) view.findViewById(R.id.logout_button);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            profile = (Profile) bundle.getParcelable(Login.PARCEL_KEY);
        } else {
            profile = Profile.getCurrentProfile();
        }


        tv.setText("Welcome \n\n" + profile.getName());
        //txtemail.setText(profile);
        Picasso.with(getActivity())
                .load(profile.getProfilePictureUri(400, 400).toString())
                .into(profile_pic);
        GraphRequestAsyncTask request2 = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse response) {
                if (user != null) {
                    // set the profile picture using their Facebook ID
                    // txtemail.setText(user.optString("id"));

                }
            }
        }).executeAsync();



        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            txtemail.setText(object.getString("email") + ("\n" + object.getString("gender")) + ("\n" + object.getString("id")) + ("\n" + object.getString("birthday")));
                            Static.setFace(true);
                            Thread timerThread = new Thread() {
                                public void run() {
                                    try {
                                        sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } finally {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            };
                            timerThread.start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender,birthday,location");
        request.setParameters(parameters);
        request.executeAsync();




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        getActivity().finish();
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new LoginFragment());
        fragmentTransaction.commit();
    }


}
