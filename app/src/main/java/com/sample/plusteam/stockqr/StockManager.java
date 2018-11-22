package com.sample.plusteam.stockqr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Internet Communication Importing
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//JSON Parse Importing
import org.json.JSONException;
import org.json.JSONObject;


public class StockManager extends AppCompatActivity {

    private Context context = null;

    private TextView storeTxt;
    private String storeNameStr;

    private Button scanQR;

    //EditText Component Array
    private EditText[][] editTexts = new EditText[10][4];
    //StockArray Containing Contents of EditText Array.
    private String[][] stockArray = new String[10][4];
    //Scanned Data from QRCode. Containing ProductID and Stocks' Number to be added.
    private String[][] scannedStockValue = new String[10][2];

    int saveCounter;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_manager);

        //Saved SharedPreferences as "pref". Contains StoreID(Store Name), saveCounter(check if saved one or more times).
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        storeNameStr = pref.getString("StoreID", null);
        saveCounter = pref.getInt("saveCounter",0);

        initializeText();

        storeTxt = (TextView)findViewById(R.id.storeNameTxt);
        scanQR = (Button)findViewById(R.id.scanQR);
        qrScan = new IntentIntegrator(this);
        storeTxt.setText(storeNameStr);

        if (saveCounter == 0){
            //if don't have saved history, save the current data.
            saveStockData();
        }
        else {
            //if saved one or more times get the saved data.
            getStockData();
            stockManagement();
        }

        scanQR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveStockData();
        this.finish();
        //If back button pressed do not go to the previous activity but rather exit the program.
        //And to prevent losing data, save stock data again.
    }

    public void btnSaveStockData(View taget){
        //If save button clicked save stock data.
        saveStockData();
        Toast.makeText(getApplicationContext(), "Stock Data Saved!!", Toast.LENGTH_LONG).show();

    }

    public void initializeText(){
        //Initialize EditText Array
        editTexts[0][0] = (EditText) findViewById(R.id.IDno1);
        editTexts[0][1] = (EditText) findViewById(R.id.Stockno1);
        editTexts[0][2] = (EditText) findViewById(R.id.SPDno1);
        editTexts[0][3] = (EditText) findViewById(R.id.Profitno1);

        editTexts[1][0] = (EditText) findViewById(R.id.IDno2);
        editTexts[1][1] = (EditText) findViewById(R.id.Stockno2);
        editTexts[1][2] = (EditText) findViewById(R.id.SPDno2);
        editTexts[1][3] = (EditText) findViewById(R.id.Profitno2);

        editTexts[2][0] = (EditText) findViewById(R.id.IDno3);
        editTexts[2][1] = (EditText) findViewById(R.id.Stockno3);
        editTexts[2][2] = (EditText) findViewById(R.id.SPDno3);
        editTexts[2][3] = (EditText) findViewById(R.id.Profitno3);

        editTexts[3][0] = (EditText) findViewById(R.id.IDno4);
        editTexts[3][1] = (EditText) findViewById(R.id.Stockno4);
        editTexts[3][2] = (EditText) findViewById(R.id.SPDno4);
        editTexts[3][3] = (EditText) findViewById(R.id.Profitno4);

        editTexts[4][0] = (EditText) findViewById(R.id.IDno5);
        editTexts[4][1] = (EditText) findViewById(R.id.Stockno5);
        editTexts[4][2] = (EditText) findViewById(R.id.SPDno5);
        editTexts[4][3] = (EditText) findViewById(R.id.Profitno5);

        editTexts[5][0] = (EditText) findViewById(R.id.IDno6);
        editTexts[5][1] = (EditText) findViewById(R.id.Stockno6);
        editTexts[5][2] = (EditText) findViewById(R.id.SPDno6);
        editTexts[5][3] = (EditText) findViewById(R.id.Profitno6);

        editTexts[6][0] = (EditText) findViewById(R.id.IDno7);
        editTexts[6][1] = (EditText) findViewById(R.id.Stockno7);
        editTexts[6][2] = (EditText) findViewById(R.id.SPDno7);
        editTexts[6][3] = (EditText) findViewById(R.id.Profitno7);

        editTexts[7][0] = (EditText) findViewById(R.id.IDno8);
        editTexts[7][1] = (EditText) findViewById(R.id.Stockno8);
        editTexts[7][2] = (EditText) findViewById(R.id.SPDno8);
        editTexts[7][3] = (EditText) findViewById(R.id.Profitno8);

        editTexts[8][0] = (EditText) findViewById(R.id.IDno9);
        editTexts[8][1] = (EditText) findViewById(R.id.Stockno9);
        editTexts[8][2] = (EditText) findViewById(R.id.SPDno9);
        editTexts[8][3] = (EditText) findViewById(R.id.Profitno9);

        editTexts[9][0] = (EditText) findViewById(R.id.IDno10);
        editTexts[9][1] = (EditText) findViewById(R.id.Stockno10);
        editTexts[9][2] = (EditText) findViewById(R.id.SPDno10);
        editTexts[9][3] = (EditText) findViewById(R.id.Profitno10);
    }

    public void saveStockData(){

        if (saveCounter == 0) {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("saveCounter", 1);
            editor.apply();
            //If no saved history, set savedCounter Parametre as 1.
        }

        SharedPreferences stockValue = getSharedPreferences("stockValue", MODE_PRIVATE);
        SharedPreferences.Editor valueEditor = stockValue.edit();
        //Get permission to edit SharedPreferences as 'Editor'.

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 4; k++) {
                stockArray[i][k] = editTexts[i][k].getText().toString();
                valueEditor.putString("stock_"+i+"_"+k ,stockArray[i][k]);
                //Make 'For Loop' to save all 40 data.
            }
        }
        //Apply changes.
        valueEditor.apply();

        stockManagement();
        Toast.makeText(StockManager.this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    public void getStockData(){
        //Get stock data from saved SharedPreferences and show it to EditText Array.
        SharedPreferences stockValue = getSharedPreferences("stockValue", MODE_PRIVATE);
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 4; k++) {
                stockArray[i][k] = stockValue.getString("stock_"+i+"_"+k ,null);
                editTexts[i][k].setText(stockArray[i][k]);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //If had no QRCode,
            if (result.getContents() == null) {
                Toast.makeText(StockManager.this, "Canceled!", Toast.LENGTH_SHORT).show();
            } else {
                //If have,
                Toast.makeText(StockManager.this, "Scanned!", Toast.LENGTH_SHORT).show();
                try {
                    //Convert data to JSON
                    JSONObject obj = new JSONObject(result.getContents());
                    scannedStockValue[0][0] = (obj.getString("id0"));
                    scannedStockValue[1][0] = (obj.getString("id1"));
                    scannedStockValue[2][0] = (obj.getString("id2"));
                    scannedStockValue[3][0] = (obj.getString("id3"));
                    scannedStockValue[4][0] = (obj.getString("id4"));
                    scannedStockValue[5][0] = (obj.getString("id5"));
                    scannedStockValue[6][0] = (obj.getString("id6"));
                    scannedStockValue[7][0] = (obj.getString("id7"));
                    scannedStockValue[8][0] = (obj.getString("id8"));
                    scannedStockValue[9][0] = (obj.getString("id9"));

                    scannedStockValue[0][1] = (obj.getString("addstock0"));
                    scannedStockValue[1][1] = (obj.getString("addstock1"));
                    scannedStockValue[2][1] = (obj.getString("addstock2"));
                    scannedStockValue[3][1] = (obj.getString("addstock3"));
                    scannedStockValue[4][1] = (obj.getString("addstock4"));
                    scannedStockValue[5][1] = (obj.getString("addstock5"));
                    scannedStockValue[6][1] = (obj.getString("addstock6"));
                    scannedStockValue[7][1] = (obj.getString("addstock7"));
                    scannedStockValue[8][1] = (obj.getString("addstock8"));
                    scannedStockValue[9][1] = (obj.getString("addstock9"));

                    inputStock();

                } catch (JSONException e) {
                    //Exception Try-Catch Statement.
                    //Currently Writing...
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void inputStock(){
        for (int i = 0; i < 10; i++){
            for (int k = 0; k < 10; k++) {
                int scannedProductID = Integer.parseInt(scannedStockValue[i][0]);
                int comparedProductID = Integer.parseInt(stockArray[k][0]);
                //Compare Scanned ProductID and the user's productID List.

                if (scannedProductID == comparedProductID) {
                    //If two data seems to be equal,
                    int stockData = Integer.parseInt(editTexts[k][1].getText().toString());
                    int addStockData = Integer.parseInt(scannedStockValue[i][1]);

                    int newStockData = stockData + addStockData;
                    //Add CurrentStock and number of stocks to be added.

                    String setStock = Integer.toString(newStockData);
                    editTexts[k][1].setText(setStock);
                }
            }
        }
        saveStockData();
    }

    public void stockManagement(){
        //Every statements to help user to get the information of his stock visually.
        for (int i = 0; i < 10; i++){
            int currentStock = Integer.parseInt(stockArray[i][1]);
            int salesPerDay = Integer.parseInt(stockArray[i][2]);

            //If stock is less then the triple times of SPD, set the text color to #RED
            if (currentStock <= salesPerDay*3){
                editTexts[i][1].setTextColor(Color.parseColor("#FE2E2E"));
            }
            else
                editTexts[i][1].setTextColor(Color.parseColor("#000000"));

        }
    }
}
