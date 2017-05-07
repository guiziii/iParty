package android.tabhost.adapters;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.tabhost.MainActivity;
import android.tabhost.R;
import android.tabhost.Static;
import android.tabhost.evento;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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
import java.util.List;


/**
 * Created by JUNED on 6/16/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //Context context;
    private Activity context;

    List<GetDataAdapter> getDataAdapter;
    ImageLoader imageLoader1;

    Object mContext;
    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Activity context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Viewholder, final int position) {

        final GetDataAdapter getDataAdapter1 =  getDataAdapter.get(position);
        final String distancia ;
        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader1.get(getDataAdapter1.getImageServerUrl(),
                ImageLoader.getImageListener(
                        Viewholder.networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        Viewholder.networkImageView.setImageUrl(getDataAdapter1.getImageServerUrl(), imageLoader1);
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
                    Viewholder.txtDistancia.setText(result);
                    Viewholder.bar.setProgress(32);

                }
                else
                {
                    Viewholder.txtDistancia.setText(result);
                }


            }
        }

        Viewholder.ImageTitleNameView.setText(getDataAdapter1.getImageTitleName());
        Viewholder.txtData.setText(getDataAdapter1.getDataImage());
        Viewholder.txtHora.setText(getDataAdapter1.getHoraImage());
        Viewholder.idEvento.setText(getDataAdapter1.getId_evento());
        Viewholder.txtDistancia.setText(getDataAdapter1.getDistancia());
        Viewholder.txtPreco.setText(getDataAdapter1.getPreco());
        //new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + getDataAdapter1.getLongitudeEvento().toString().replace(" ", "") + ',' + getDataAdapter1.getLatitudeEvento().toString().replace(" ", "") + "&destination=-23.2349128,-45.8990308");
        new JsonTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + getDataAdapter1.getLongitudeEvento().toString().replace(" ", "") + ',' + getDataAdapter1.getLatitudeEvento().toString().replace(" ", "") + "&destination="+ Static.getLatitude()+","+Static.getLongitude()+"");
        Viewholder.itemView.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view)
            {
               // Toast.makeText(context, Viewholder.idEvento.getText(), Toast.LENGTH_SHORT).show();
                Viewholder.cad.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                Viewholder.cdll.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                Viewholder.txtData.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                Static.setIdevento(Integer.parseInt(String.valueOf(Viewholder.idEvento.getText())));

                evento frag = new evento();

                MainActivity mainActivity = (MainActivity) context;

                mainActivity.switchContent(frag);





            }
        });


    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ImageTitleNameView, txtData,txtHora,idEvento,txtDistancia,txtPreco;
        public NetworkImageView networkImageView ;
        public ProgressBar bar;
        CardView cad;
        RelativeLayout cdll;



        public ViewHolder(View itemView) {

            super(itemView);

            txtDistancia = (TextView)itemView.findViewById(R.id.txtDistancia);
            ImageTitleNameView = (TextView) itemView.findViewById(R.id.textView_item) ;
            txtData = (TextView) itemView.findViewById(R.id.txtData) ;
            txtHora= (TextView) itemView.findViewById(R.id.txtHora) ;
            txtPreco= (TextView) itemView.findViewById(R.id.txtPrecoE) ;
            idEvento= (TextView) itemView.findViewById(R.id.idEvento) ;
            bar=(ProgressBar)itemView.findViewById(R.id.progressBar);
            cad = (CardView)itemView.findViewById(R.id.cardview1);
            //ImageTitleNameView.setTypeface(EasyFonts.freedom(context));
            cdll= (RelativeLayout) itemView.findViewById(R.id.cdll2);

            networkImageView = (NetworkImageView) itemView.findViewById(R.id.VollyNetworkImageView1) ;

        }



    }


}