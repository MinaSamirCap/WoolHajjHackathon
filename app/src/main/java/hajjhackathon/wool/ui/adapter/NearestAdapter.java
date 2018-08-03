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
    private double lat = 21.3546629;
    private double lng = 39.9836817;

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
        holder.tvDistance.setText(Math.round(distance(lat, data.get(position).lat, lng, data.get(position).lng,"M")) + " M");
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

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
