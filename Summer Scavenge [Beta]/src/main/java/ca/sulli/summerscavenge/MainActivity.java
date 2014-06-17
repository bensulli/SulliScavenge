package ca.sulli.summerscavenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.EditText;
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

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    ImageScanner scanner;
    FrameLayout preview;

    TextView clue1Number;
    TextView clue2Number;
    TextView clue3Number;

    static int clueMax = 3;
    Clue[] assignedClues = new Clue[3];

    private boolean codeValid;

    private boolean barcodeScanned = false;
    private boolean previewing = false;

    ImageButton scanButton;

    private String resultString = "";


    static {
        System.loadLibrary("iconv");
    }

    //TODO: Save gamestate to local file
    //TODO: Populate clues for beta
    //TODO: Randomly assign clues at first launch


    // ************ CREATE TEMPORARY BETA CODES ************

    Clue[] betaClueSet = {
            new Clue(1,60660775,"This is the clue for #1."),
            new Clue(2,31858634,"This is the clue for #2."),
            new Clue(3,51013409,"This is the clue for #3."),
            new Clue(4,55720161,"This is the clue for #4."),
            new Clue(5,98828642,"This is the clue for #5."),
            new Clue(6,94046376,"This is the clue for #6."),
            new Clue(7,27381797,"This is the clue for #7."),
            new Clue(8,89370771,"This is the clue for #8."),
            new Clue(9,60581440,"This is the clue for #9."),
            new Clue(10,15206641,"This is the clue for #10."),
            new Clue(11,67828057,"This is the clue for #11."),
            new Clue(12,31632278,"This is the clue for #12.")
    };



    // ****************************************************

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
        //TODO: Fix preview aspect ratio

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        preview = (FrameLayout)findViewById(R.id.cameraPreview);


        scanButton = (ImageButton)findViewById(R.id.scanButton);

        clue1Number = (TextView)findViewById(R.id.clue1NumText);
        clue2Number = (TextView)findViewById(R.id.clue2NumText);
        clue3Number = (TextView)findViewById(R.id.clue3NumText);


        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(previewing == false)
                {
                    preview.addView(mPreview);
                    previewing = true;
                    scanButton.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    preview.removeAllViews();
                    previewing = false;
                    scanButton.setBackgroundResource(android.R.drawable.btn_default);
                }

            }
        });


        int randomPick;
        int count = 0;

        // Get initial clues
        while(CluesAssigned() < 3)
        {
            randomPick = randInt(0,11);
            if(betaClueSet[randomPick].picked == false)
            {
                assignedClues[count] = betaClueSet[randomPick];
                count++;
            }
        }

        Refresh();





        //TODO: Get game state at launch

    }

    public void Refresh()
    {
        clue1Number.setText(Integer.toString(assignedClues[0].id));
        clue2Number.setText(Integer.toString(assignedClues[1].id));
        clue3Number.setText(Integer.toString(assignedClues[2].id));
    }


    public int CluesAssigned()
    {

        int count = 0;

        for(Clue x : assignedClues)
        {

            try
            {
                if(x.id >= 0)
                    count++;
            }
            catch(Exception e)
            {
                // Nothing, ignore if null
            }


        }

        return count;

    }

    public static int randInt(int min, int max) {

        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

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
                    Toast.makeText(getApplicationContext(), "Code found: " + sym.getData(), Toast.LENGTH_LONG).show();
                    Log.e(null,"barcode result " + sym.getData());
                    resultString = "barcode result " + sym.getData();
                    barcodeScanned = true;
                    preview.removeAllViews();
                    previewing = false;
                    scanButton.setBackgroundResource(android.R.drawable.btn_default);
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

        //TODO: Make this point to pre-populated assigned clues

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        switch(view.getId())
        {
//            case R.id.Clue1Bronze:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");
//
//            case R.id.Clue1Silver:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");

            case R.id.Clue1Gold:
                builder.setMessage("A helpful hint for where to find the next code!")
                        .setTitle("Clue!");


//            case R.id.Clue2Bronze:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");
//
//            case R.id.Clue2Silver:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");

            case R.id.Clue2Gold:
                builder.setMessage("A helpful hint for where to find the next code!")
                        .setTitle("Clue!");


//            case R.id.Clue3Bronze:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");
//
//            case R.id.Clue3Silver:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");

            case R.id.Clue3Gold:
                builder.setMessage("A helpful hint for where to find the next code!")
                        .setTitle("Clue!");
        }


        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        AlertDialog clueDialogue = builder.create();
        clueDialogue.show();
    }


    public void EnterID(View view)
    {
        //TODO Enter ID popup!!!
        AlertDialog.Builder enterID = new AlertDialog.Builder(this);
        enterID.setTitle("Enter found code!");
        enterID.setMessage("Submit the 8-digit code you found...");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        enterID.setView(input);

        enterID.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.toString();
                //TODO: Submit this value to server
            }
        });

        enterID.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        enterID.show();

    }

    public void PostCode(int code)
    {

        int serverResult = 0;

        //TODO: Ensure the found code is an int and matches the game's format (6 numbers)

        codeValid = true;

        if(codeValid == true)
        {
            Toast.makeText(getApplicationContext(), "TODO: Upload to Server", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "TODO: Server checks code, returns result", Toast.LENGTH_SHORT).show();

            //TODO: Upload code to server and get a return result
            //TODO: Ensure the returned code is a valid int

            switch (serverResult)
            {
                case 0: Toast.makeText(getApplicationContext(), "Success! Code recorded. (0)", Toast.LENGTH_SHORT).show();
                    //TODO: Get new clue
                    break;
                case 1: Toast.makeText(getApplicationContext(), "You've already found this code. (1)", Toast.LENGTH_SHORT).show();
                    break;
                case 2: Toast.makeText(getApplicationContext(), "Code doesn't exist. What game are you playing? (2)", Toast.LENGTH_SHORT).show();
                    break;
                case 3: Toast.makeText(getApplicationContext(), "Server error. Call 604-561-3036 and yell at Ben. (3)", Toast.LENGTH_SHORT).show();
                    //TODO: Report error code to server
                    break;
                default: Toast.makeText(getApplicationContext(), "EXTREME BADNESS - no valid server result found. (X)", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void SetProfile()
    {
        //TODO: Pop up profile setter
    }

}