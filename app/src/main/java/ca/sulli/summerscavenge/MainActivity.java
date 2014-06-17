package ca.sulli.summerscavenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Button;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.ImageFormat;
import android.widget.Toast;

import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;


public class MainActivity extends ActionBarActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    ImageButton scanButton;

    static {
        System.loadLibrary("iconv");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (ImageButton)findViewById(R.id.scanButton);

        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    Toast.makeText(getApplicationContext(),"Scanning...", Toast.LENGTH_SHORT);
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    Toast.makeText(getApplicationContext(), "barcode result " + sym.getData(), Toast.LENGTH_LONG);
                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    public void ClueButtonPress(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        switch(view.getId())
        {
            case R.id.Clue1Bronze:
                builder.setMessage("Test Message")
                        .setTitle("Clue 1 - Easy");

            case R.id.Clue1Silver:
                builder.setMessage("Test Message")
                        .setTitle("Clue 1 - Medium");

            case R.id.Clue1Gold:
                builder.setMessage("Test Message")
                        .setTitle("Clue 1 - Hard");


            case R.id.Clue2Bronze:
                builder.setMessage("Test Message")
                        .setTitle("Test Title");

            case R.id.Clue2Silver:
                builder.setMessage("Test Message")
                        .setTitle("Test Title");

            case R.id.Clue2Gold:
                builder.setMessage("Test Message")
                        .setTitle("Test Title");


            case R.id.Clue3Bronze:
                builder.setMessage("Test Message")
                        .setTitle("Test Title");

            case R.id.Clue3Silver:
                builder.setMessage("Test Message")
                        .setTitle("Test Title");

            case R.id.Clue3Gold:
                builder.setMessage("Test Message")
                        .setTitle("Test Title");
        }


        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        AlertDialog clueDialogue = builder.create();
        clueDialogue.show();
    }

    public void ScanQR(View view)
    {
        // Activate QR scanner!!!
    }

    public void EnterID(View view)
    {
        // Enter ID popup!!!
    }

    public void ViewMap(View view)
    {
        // Open Map!!!
    }

}
