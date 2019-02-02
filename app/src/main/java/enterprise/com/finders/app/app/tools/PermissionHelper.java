package enterprise.com.finders.app.app.tools;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Ema on 1/30/2018.
 */

public class PermissionHelper {

    public static String[] permissionsRequired = { Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                                        Manifest.permission.ACCESS_FINE_LOCATION};

    private static String alertTitle = "Attention";
    private static String message = "You can't post a report if you don't give access for location and storage.";
    private static String buttonText = "OK";

    public static boolean checkForPermissions(Activity activity) {
        for (String permission : permissionsRequired) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void askForPermissions(final Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Important message");
        builder.setMessage("Application needs to have access to your location and storage. Please grant it.");
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity, permissionsRequired, 0);
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UIHelper.showDialog(activity, alertTitle, message, buttonText, null);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
