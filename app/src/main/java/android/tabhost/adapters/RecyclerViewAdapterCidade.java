package android.tabhost.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.tabhost.MainActivity;
import android.tabhost.R;
import android.tabhost.Static;
import android.tabhost.Tab1;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Guilherme on 29/01/2017.
 */
public class RecyclerViewAdapterCidade extends RecyclerView.Adapter<RecyclerViewAdapterCidade.ViewHolder>
        {
    //Context context;
    private Activity context;

    List<GetDataAdapterCidade> getDataAdapterCidade;
    ImageLoader imageLoader1;

    Object mContext;
    public RecyclerViewAdapterCidade(List<GetDataAdapterCidade> getDataAdapterCidade, Activity context){

        super();
        this.getDataAdapterCidade = getDataAdapterCidade;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cityitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Viewholder, final int position) {

        final GetDataAdapterCidade getDataAdapter2 =  getDataAdapterCidade.get(position);





        Viewholder.txtEstado.setText(getDataAdapter2.getNome());



        Viewholder.itemView.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view)
            {
                // Toast.makeText(context, Viewholder.idEvento.getText(), Toast.LENGTH_SHORT).show();
                //Viewholder.cad.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                //Viewholder.cdll.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                //Viewholder.txtData.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                Static.setNomeCIDADE(Viewholder.txtEstado.getText().toString());

                Tab1 frag = new Tab1();

                MainActivity mainActivity = (MainActivity) context;

                mainActivity.switchContent(frag);





            }
        });


    }

    @Override
    public int getItemCount() {

        return getDataAdapterCidade.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEstado;
        public ProgressBar bar;
        CardView cad;
        RelativeLayout cdll;



        public ViewHolder(View itemView) {

            super(itemView);

            txtEstado = (TextView)itemView.findViewById(R.id.txtCidadeLista);




        }



    }
}
