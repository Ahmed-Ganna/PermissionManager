/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Rurio Luca
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package permission.auron.com.marshmallowpermissionhelper;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by luca on 2/29/16.
 */
@SuppressWarnings({"MissingPermission"})
public class FragmentManagePermission extends Fragment {


    private int KEY_PERMISSION = 0;
    private PermissionResult permissionResult;
    private String permissionsAsk[];


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRetainInstance(false);
    }

    @SuppressWarnings({"MissingPermission"})
    public boolean isPermissionGranted(Context context, String permission) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }


    @SuppressWarnings({"MissingPermission"})
    public boolean isPermissionsGranted(Context context, String permissions[]) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        boolean granted = true;

        for (String permission : permissions) {
            if (!(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }

        return granted;
    }

    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();


        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(getActivity(), permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }


        if (permissionsNotGranted.isEmpty()) {
            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {
            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            requestPermissions(arrayPermissionNotGranted, KEY_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode != KEY_PERMISSION) {
            return;
        }
        boolean granted = true;
        List<String> permissionDienid = new LinkedList<>();

        for (int grantResult : grantResults) {
            if (!(grantResults.length > 0 && grantResult == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }
        if (permissionResult != null) {
            if (granted) {
                permissionResult.permissionGranted();
            } else {

                for (String s : permissionDienid) {
                    if (!shouldShowRequestPermissionRationale(s)) {
                        permissionResult.permissionForeverDienid();
                        return;
                    }
                }
                permissionResult.permissionDenied();
            }

        }

    }

    public void askCompactPermission(String permission, PermissionResult permissionResult) {
        KEY_PERMISSION = 200;
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }

    public void askCompactPermissions(String permissions[], PermissionResult permissionResult) {
        KEY_PERMISSION = 200;
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }

}