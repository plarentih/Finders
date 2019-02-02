package enterprise.com.finders.app.app.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Ema on 1/30/2018.
 */

public class UIHelper {

    public static void showDialog(Activity activity, String title, String message, String buttonText,
                                  DialogInterface.OnClickListener buttonListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonText, buttonListener);
        builder.setCancelable(false);
        builder.show();
    }
}
