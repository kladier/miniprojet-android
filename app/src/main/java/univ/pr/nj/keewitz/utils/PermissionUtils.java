package univ.pr.nj.keewitz.utils;

import android.content.pm.PackageManager;

public final class PermissionUtils {

    private PermissionUtils () {}

    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }

        return false;
    }
}
