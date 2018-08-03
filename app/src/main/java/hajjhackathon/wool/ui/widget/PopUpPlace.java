package hajjhackathon.wool.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hajjhackathon.wool.R;

/**
 * Created by mina on 02/04/18.
 */

public class PopUpPlace {

    private Unbinder unbinder;
    private Context context;
    private AlertDialog dialog;
    private CallbackResult callbackResult;

    public PopUpPlace(Context context, CallbackResult callbackResult) {
        this.context = context;
        this.callbackResult = callbackResult;
        buildDialog();
    }

    private void buildDialog() {
        LayoutInflater factory = LayoutInflater.from(context);
        View popupView = factory.inflate(R.layout.popup_place, null);
        unbinder = ButterKnife.bind(this, popupView);
        dialog = new AlertDialog.Builder(context).create();
        dialog.setView(popupView);
    }

    public void showDialog() {
        dialog.show();
    }


    public void closeDialog() {
        unbinder.unbind();
        dialog.dismiss();
    }


    @OnClick(R.id.free_button)
    void freeClicked(View view) {
        callbackResult.freeClicked();
        closeDialog();
    }


    @OnClick(R.id.busy_button)
    void busyClicked(View view) {
        callbackResult.busyClicked();
        closeDialog();
    }


    public interface CallbackResult {
        void busyClicked();

        void freeClicked();
    }


}
