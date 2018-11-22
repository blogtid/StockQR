package com.sample.plusteam.stockqr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText storeName;
    public String storeNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storeName = (EditText) findViewById(R.id.storeName);

        final SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        storeNameStr = pref.getString("StoreID", null);

        if (storeNameStr != null){ //Check if the user had set up his store name.
            Intent intent = new Intent(this, StockManager.class);//If had, get to the next activity.
            startActivity(intent);
            finish();
        }
        else { //It new user, make him to set his store name.
            Toast.makeText(getApplicationContext(), "Welcome to StockQR!!", Toast.LENGTH_LONG).show();
        }
    }

    public void setStoreName(View target){
        storeNameStr = storeName.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Store Name Setup Manager");
        builder.setMessage("Store name: \"" + storeNameStr + "\"\nClick \"Confirm\" to set this name as your store name.");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"CONFIRMED!!",Toast.LENGTH_LONG).show();

                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("StoreID",storeNameStr);
                        editor.apply();
                        getToStockManager();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"CANCELED!!",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();

    }

    public void getToStockManager(){
        Intent intent = new Intent(this, StockManager.class);
        startActivity(intent);
        finish();
    }
}
