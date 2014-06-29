package ca.sulli.summerscavenge;

import android.app.AlertDialog;
import android.content.Context;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import java.io.Serializable;


public class MainActivity extends ActionBarActivity {

    FrameLayout preview;
    TextView clue1Number;
    TextView clue2Number;
    TextView clue3Number;
    TextView txtTeamName;
    TextView txtTeamScore;
    TextView txtYourScore;
    TextView txtTopTeam;
    TextView txtTopTeamName;

    ImageButton scanButton;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    ImageScanner scanner;

    public boolean firstRun = false;



    static int clueMax = 3;

    private boolean codeValid;

    private boolean barcodeScanned = false;
    private boolean previewing = false;

    private String resultString = "";

    public GameState gameState;
    public String saveFileName = "saveState";
    public File saveStateFile;

    static {
        System.loadLibrary("iconv");
    }

    //TODO: Save gamestate to local file
    //TODO: Populate clues for beta
    //TODO: Randomly assign clues at first launch



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
        txtTeamName = (TextView)findViewById(R.id.txtTeamName);
        txtTeamScore = (TextView)findViewById(R.id.txtTeamScore);
        txtYourScore = (TextView)findViewById(R.id.txtYourScore);
        txtTopTeam = (TextView)findViewById(R.id.txtTopTeam);
        txtTopTeamName = (TextView)findViewById(R.id.txtTopTeamName);


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


        // Determine whether this is firstrun or not
        saveStateFile = new File(getApplicationContext().getFilesDir(), saveFileName);
        if(saveStateFile.exists())
        {
            // Load File Contents
            firstRun = false;
        }
        else
        {
            try {
                saveStateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameState = new GameState();
            firstRun = true;
        }


        //TODO: Get game state at launch

        if(firstRun) {
            SetProfile(getWindow().getDecorView().getRootView());
            AssignClues();
        }

        Refresh();

    }



// ****************** CLUE MANAGEMENT ******************

    public int CluesAssigned()
    {

        int count = 0;

        for(Clue x : gameState.assignedClues)
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

    public static int randInt(int min, int max)
    {

        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void AssignClues()
    {
        int randomPick;
        int count = 0;

        while(CluesAssigned() < 3) {
            randomPick = randInt(0, 11);
            if (gameState.betaClueSet[randomPick].picked == false) {
                gameState.assignedClues[count] = gameState.betaClueSet[randomPick];
                gameState.betaClueSet[randomPick].picked = true;
                count++;
            }
        }
    }

// *******************



// ****************** UI REFRESH ******************

    public void Refresh()
    {
        clue1Number.setText("#" + Integer.toString(gameState.assignedClues[0].id));
        clue2Number.setText("#" + Integer.toString(gameState.assignedClues[1].id));
        clue3Number.setText("#" + Integer.toString(gameState.assignedClues[2].id));

        txtTeamName.setText(gameState.teamName);
        //txtTeamScore.setText(teamScore);
        //txtYourScore.setText(yourScore);
        //txtTopTeam.setText(topTeamScore);
        //txtTopTeamName.setText(topTeamName);

        SaveState();

    }
// *******************


// ****************** PLM ******************

    public void onPause()
    {
        super.onPause();
        releaseCamera();
    }

// *******************


// ****************** CAMERA ******************

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

                private Runnable doAutoFocus = new Runnable()
            {
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

// *******************


// ****************** BUTTONS ******************

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
                builder.setMessage(gameState.assignedClues[0].mediumClue)
                        .setTitle("Clue" + String.valueOf(gameState.assignedClues[0].id) + "!");
                break;


//            case R.id.Clue2Bronze:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");
//
//            case R.id.Clue2Silver:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");

            case R.id.Clue2Gold:
                builder.setMessage(gameState.assignedClues[1].mediumClue)
                        .setTitle("Clue" + String.valueOf(gameState.assignedClues[1].id) + "!");
                break;


//            case R.id.Clue3Bronze:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");
//
//            case R.id.Clue3Silver:
//                builder.setMessage("Clue!")
//                        .setTitle("Clue!");

            case R.id.Clue3Gold:
                builder.setMessage(gameState.assignedClues[2].mediumClue)
                        .setTitle("Clue" + String.valueOf(gameState.assignedClues[2].id) + "!");
                break;
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

    public void SetProfile(View view)
    {
        final AlertDialog.Builder enterName = new AlertDialog.Builder(this);
        enterName.setTitle("Name");
        enterName.setMessage("Enter your name...");

        final EditText inputName = new EditText(this);
        enterName.setView(inputName);

        enterName.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = inputName.getText().toString();
                //TODO: Submit this value to server
                if(value != "")
                {
                    gameState.name = value;
                }
                else
                {
                    enterName.show();
                }
                Refresh();
            }
        });

        final AlertDialog.Builder enterTeam = new AlertDialog.Builder(this);
        enterTeam.setTitle("Team");
        enterTeam.setMessage("Enter your team name...");

        final EditText inputTeam = new EditText(this);
        enterTeam.setView(inputTeam);

        enterTeam.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = inputTeam.getText().toString();
                //TODO: Submit this value to server
                gameState.teamName = value;
                Refresh();
            }
        });
        enterTeam.show();
        enterName.show();

    }

// *******************


// ****************** INTERFACE STUB ******************

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

// *******************


    public void SaveState()
    {
        try {
            FileOutputStream outputStream = new FileOutputStream(saveStateFile);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(gameState);
            out.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(null, e.toString());
        }

    }

    public void LoadState()
    {

    }


}