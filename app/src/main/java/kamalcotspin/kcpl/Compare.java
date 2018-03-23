package kamalcotspin.kcpl;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Archit on 17-10-2017.
 */

public class Compare extends Activity {
    GridView gridView;
    String countType, countVariant;
    String path = Environment.getExternalStorageDirectory().getPath() + "//kcpl//";

    // DB Class to perform DB related operations
    //DBController controller = new DBController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        gridView = (GridView) findViewById(R.id.gridView1);




        countVariant = getIntent().getExtras().getString("countVariant");
        countType = getIntent().getExtras().getString("countType");
        float countPrice = Float.parseFloat(getIntent().getExtras().getString("countPrice"));
        ActionBar ab = getActionBar();
        ab.setTitle(countVariant + " " + countType);

        File Variables = new File(path, "parameters.txt");

        if (Variables.exists()) {
            try {
                FileReader fr1 = new FileReader(path + "parameters.txt");
                BufferedReader br1 = new BufferedReader(fr1);
                String co1 = br1.readLine();
                String[] parameters = co1.split("#");

                float price = Float.parseFloat(parameters[0]);
                float transport = Float.parseFloat(parameters[1]);
                float transport_conversion = Float.parseFloat(parameters[2]);
                float ring_yarn = Float.parseFloat(parameters[3]);
                float open_yarn = Float.parseFloat(parameters[4]);
                float lc_interest = Float.parseFloat(parameters[5]);
                float lc_months = Float.parseFloat(parameters[6]);
                float commision = Float.parseFloat(parameters[7]);
                float insurance = Float.parseFloat(parameters[8]);
                float conversion = Float.parseFloat(parameters[9]);
                float dbk = Float.parseFloat(parameters[10]);
                float yarn;

                if (countType.substring(0, 2).equalsIgnoreCase("OE")) {
                    yarn = open_yarn;
                } else {
                    yarn = ring_yarn;
                }

                float calc1 = price / yarn;
                float calc2 = transport * transport_conversion / yarn;
                float calc3 = countPrice * commision;
                float calc4 = calc3 / conversion;
                float calc5 = countPrice + calc1 + calc2 + calc3;
                float calc6 = calc5 * yarn;
                float calc7 = ((calc6 * lc_interest) / 12) * lc_months;
                float calc8 = calc7 / yarn;
                float calc9 = calc5 + calc8;
                float calc10 = calc6 + calc7;
                float calc11 = (calc10 / 1000) * insurance;
                float calc12 = calc11 / yarn;
                float amtRs = calc9 + calc12;
                float amt$ = amtRs / conversion;
                float calc13 = countPrice * dbk;
                float calc14 = amtRs - calc13;
                float famt$ = calc14 / conversion;

                String display[]=new String[32];
                display[0]=getString(R.string.price);
                display[1]=Float.toString(calc1);
                display[2]=getString(R.string.transport);
                display[3]=Float.toString(calc2);
                display[4]=getString(R.string.commissioncrateinr);
                display[5]=Float.toString(calc3);
                display[6]=getString(R.string.commissioncrateusd);
                display[7]=Float.toString(calc4);
                display[8]=getString(R.string.totamtb4commissioninkg);
                display[9]=Float.toString(calc5);
                display[10]=getString(R.string.total);
                display[11]=Float.toString(calc6);
                display[12]=getString(R.string.bankcommission);
                display[13]=Float.toString(calc7);
                display[14]=getString(R.string.bankcommissionkg);
                display[15]=Float.toString(calc8);
                display[16]=getString(R.string.totamtb4insurance);
                display[17]=Float.toString(calc9);
                display[18]=getString(R.string.total);
                display[19]=Float.toString(calc10);
                display[20]=getString(R.string.insurance1);
                display[21]=Float.toString(calc12);
                display[22]=getString(R.string.finalamount₹);
                display[23]=Float.toString(amtRs);
                display[24]=getString(R.string.finalamount$);
                display[25]=Float.toString(amt$);
                display[26]=getString(R.string.dbksimple);
                display[27]=Float.toString(calc13);
                display[28]=getString(R.string.lessdbk);
                display[29]=Float.toString(calc14);
                display[30]=getString(R.string.finalamount);
                display[31]=Float.toString(famt$);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, display);

                gridView.setAdapter(adapter);
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}