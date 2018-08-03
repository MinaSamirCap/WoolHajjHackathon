package hajjhackathon.wool.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hajjhackathon.wool.R;
import hajjhackathon.wool.models.LocationModel;
import hajjhackathon.wool.ui.viewholder.NearestViewHolder;

/**
 * Created by Mina on 2/15/2017.
 */

public class NearestAdapter extends RecyclerView.Adapter<NearestViewHolder> {

    private List<LocationModel> data;
    private NearestCallback callback;

    public NearestAdapter(List<LocationModel> data, NearestCallback callback) {
        this.data = data;
        this.callback = callback;
    }

    @NonNull
    @Override
    public NearestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearest_location, null);
        layoutView.setLayoutParams((new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)));
        return new NearestViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NearestViewHolder holder, int position) {
        holder.tvDistance.setText("Samah the leader.. <3");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.mapClicked(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface NearestCallback {
        void mapClicked(int position);
    }

}
