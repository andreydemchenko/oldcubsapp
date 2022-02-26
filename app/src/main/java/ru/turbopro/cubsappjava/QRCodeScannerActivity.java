package ru.turbopro.cubsappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    private PreviewView codeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scanner);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        codeScanner = findViewById(R.id.scanner_view);

        if (!allPermissionsGranted(this))
            getRuntimePermissions(this);
        else startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication());
        cameraProviderFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                        Preview.Builder builder = new Preview.Builder();
                        Preview previewUseCase = builder.build();
                        previewUseCase.setSurfaceProvider(codeScanner.getSurfaceProvider());
                        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                        cameraProvider.unbindAll();
                        cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase);
                    } catch (ExecutionException | InterruptedException e) {
                        Log.e("TAG", "Unhandled exception", e);
                    }
                }, ContextCompat.getMainExecutor(getApplication()));
    }

    private static String[] getRequiredPermissions(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;

            if (ps != null && ps.length > 0)
                return ps;
            else
                return new String[0];

        } catch (Exception e) {
            return new String[0];
        }
    }

    public static boolean allPermissionsGranted(Context context) {
        for (String permission : getRequiredPermissions(context))
            if (!isPermissionGranted(context, permission)) return false;
        return true;
    }

    public static void getRuntimePermissions(Activity activity) {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions(activity))
            if (!isPermissionGranted(activity, permission))
                allNeededPermissions.add(permission);

        if (!allNeededPermissions.isEmpty())
            ActivityCompat.requestPermissions(activity, allNeededPermissions.toArray(new String[0]), CAMERA_REQUEST_CODE);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission granted: " + permission);
            return true;
        }
        Log.i("TAG", "Permission NOT granted: " + permission);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
       //mCodeScanner.releaseResources();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}