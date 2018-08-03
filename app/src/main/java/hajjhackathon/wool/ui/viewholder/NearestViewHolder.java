package hajjhackathon.wool.ui.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hajjhackathon.wool.R;

/**
 * Created by Mina on 2/15/2017.
 */

public class NearestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.map_image_view)
    public ImageView imaMap;
    @BindView(R.id.distance_text_view)
    public AppCompatTextView tvDistance;

    public NearestViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
