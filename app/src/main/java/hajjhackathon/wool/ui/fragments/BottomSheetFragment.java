package hajjhackathon.wool.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hajjhackathon.wool.R;
import hajjhackathon.wool.models.LocationModel;
import hajjhackathon.wool.ui.adapter.NearestAdapter;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private static final String FREE_LOCATION_REFERENCE = "arfatZone/freeLocation";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private List<LocationModel> data = new ArrayList<>();

    public static BottomSheetFragment newInstance() {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);

        getNearestLocation();

        return view;

    }

    private void getNearestLocation() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        DatabaseReference main = databaseReference.child(FREE_LOCATION_REFERENCE);

        main.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                data.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child != null) {
                        LocationModel model = child.getValue(LocationModel.class);
                        data.add(model);

                    }
                }

                NearestAdapter adapter = new NearestAdapter(data, new NearestAdapter.NearestCallback() {
                    @Override
                    public void mapClicked(int position) {
                        openBrowserWithMap(getActivity(), data.get(position).lat, data.get(position).lng);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void openBrowser(Activity activity, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            activity.startActivity(i);
        } catch (Exception ignored) {

        }
    }

    public static void openBrowserWithMap(Activity activity, double lat, double lng) {
        openBrowser(activity, "http://maps.google.com/maps?daddr=" + lat + "," + lng);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
