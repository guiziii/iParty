package android.tabhost.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.tabhost.CustomDialogFragment;
import android.tabhost.MainActivity;
import android.tabhost.R;
import android.tabhost.Static;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;


/**
 * Created by JUNED on 6/16/2016.
 */
public class RecyclerViewAdapterEstado extends RecyclerView.Adapter<RecyclerViewAdapterEstado.ViewHolder> {

    //Context context;
    private Activity context;

    List<GetDataAdapterEstado> getDataAdapterEstado;
    ImageLoader imageLoader1;

    Object mContext;
    public RecyclerViewAdapterEstado(List<GetDataAdapterEstado> getDataAdapterEstado, Activity context){

        super();
        this.getDataAdapterEstado = getDataAdapterEstado;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_stateitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Viewholder, final int position) {

        final GetDataAdapterEstado getDataAdapter2 =  getDataAdapterEstado.get(position);





        Viewholder.txtEstado.setText(getDataAdapter2.getUf());
        Viewholder.txtIDEstado.setText(getDataAdapter2.getIdEst());




        Viewholder.itemView.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view)
            {
                // Toast.makeText(context, Viewholder.idEvento.getText(), Toast.LENGTH_SHORT).show();
//                Viewholder.cad.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
  //              Viewholder.cdll.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                //Viewholder.txtData.setBackgroundColor(android.graphics.Color.rgb(128,128,128));
                Static.setIdESTADO(Integer.parseInt(String.valueOf(Viewholder.txtIDEstado.getText())));
                Static.setUfESTADO(Viewholder.txtEstado.getText().toString());

                CustomDialogFragment frag = new CustomDialogFragment();

                MainActivity mainActivity = (MainActivity) context;

                mainActivity.switchContent(frag);





            }
        });


    }

    @Override
    public int getItemCount() {

        return getDataAdapterEstado.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEstado,txtIDEstado;
        public ProgressBar bar;
        CardView cad;
        RelativeLayout cdll;



        public ViewHolder(View itemView) {

            super(itemView);

            txtEstado = (TextView)itemView.findViewById(R.id.txtEstadoLista);
            txtIDEstado = (TextView)itemView.findViewById(R.id.txtIDEstado);




        }



    }


}