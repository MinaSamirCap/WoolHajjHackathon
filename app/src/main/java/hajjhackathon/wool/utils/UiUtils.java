package hajjhackathon.wool.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import hajjhackathon.wool.R;

public class UiUtils {

    public static void loadSnackBar(String message, Activity activity) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_LONG).setAction(
                activity.getString(R.string.done),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.accent));
        /*View snackBarView = snackbar.getView();
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(Color.parseColor("#323232"));*/
        snackbar.show();
    }

    public static Toast displayToast(Context context, Toast currentToast, String text) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        return Toast.makeText(context, text, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
