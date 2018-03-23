package kamalcotspin.kcpl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;


    //String Count[] = {"KW","KH","CW","CH","CCW","CCH","OE 1700","OE 1850","OE 1900","OE others","Others","ALL"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String c1[] = controller.getCountTypes();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.count_listview, c1);

        ListView count_list = (ListView) findViewById(R.id.idlv_MainActivity);
        count_list.setAdapter(adapter);
        count_list.setOnItemClickListener(this);

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transferring Data from Server. Please wait...");
        prgDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idm_variables:
                Intent openVariables = new Intent("kamalcotspin.kcpl.VARIABLES");
                startActivity(openVariables);
                return true;

            /*case R.id.idm_titlecontent:
                Intent openFormat = new Intent("kamalcotspin.kcpl.TITLECONTENT");
                startActivity(openFormat);
                return true;*/

            case R.id.idm_refresh:
                String json = controller.getStringToUpdate();
                syncSQLiteMySQLDB();
                updateMySQLCountDetails(json);
                return true;

            case R.id.idm_categories:
                Intent openCategories = new Intent("kamalcotspin.kcpl.CATEGORIES");
                startActivity(openCategories);
                return true;

            case R.id.idm_header:
                Intent openHeaders = new Intent("kamalcotspin.kcpl.HEADNFOOT");
                openHeaders.putExtra("horf","h");
                startActivity(openHeaders);
                return true;

            case R.id.idm_footer:
                Intent openFooters = new Intent("kamalcotspin.kcpl.HEADNFOOT");
                openFooters.putExtra("horf","f");
                startActivity(openFooters);
                return true;

            case R.id.idm_formatsettings:
                Intent openFormatSettings = new Intent("kamalcotspin.kcpl.FORMATSETTINGS");
                //openFormatSettings.putExtra("horf","f");
                startActivity(openFormatSettings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
    }
    public void onBackPressed() {
            finish();
            System.exit(0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent openQuote = new Intent("kamalcotspin.kcpl.QUOTE");
        openQuote.putExtra("MA_Count",parent.getItemAtPosition(position).toString());
        startActivity(openQuote);
    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();

        // Make Http call to getusers.php
        client.post("https://kcpl.000webhostapp.com/mysqlsqlitesync/getCount.php?lastsynctime="+controller.getLastSyncTime(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLite(response);
            }
            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Device might not be connected to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void updateSQLite(String response){
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has count details
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB QueryValues Object to insert into SQLite
                    /*queryValues = new HashMap<String, String>();
                    queryValues.put("countType", obj.get("cT").toString());
                    queryValues.put("countVariant", obj.get("cV").toString());
                    queryValues.put("countPrice", obj.get("cP").toString());*/
                    // Insert User into SQLite DB
                    //controller.insertCount(queryValues);
                    controller.insertChangedCount(obj.get("cT").toString(),obj.get("cV").toString(),obj.get("cP").toString(),obj.get("cTm").toString(),"server");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateMySQLCountDetails(String json) {
        System.out.println("----------->"+json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("changedLocalData", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post("https://kcpl.000webhostapp.com/mysqlsqlitesync/updatePhoneCount.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                controller.updateLastSyncTime();
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }
}