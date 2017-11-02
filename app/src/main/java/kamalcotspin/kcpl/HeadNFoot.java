package kamalcotspin.kcpl;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Archit on 19-10-2017.
 */

public class HeadNFoot extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    ArrayList<HashMap<String, String>> data;
    String horf = "";
    ListView count_list;
    int pos = 0;
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horf = getIntent().getExtras().getString("horf");
        String title = "Footers";
        if (horf.equals("h")){
            title = "Headers";
        }

        ActionBar ab = getActionBar();
        ab.setTitle(title);

        final String c1[] = controller.getTitle(horf);
        data = controller.getActivehorf(horf);
        for (Map<String, String> entry : data) {
           x = Arrays.asList(c1).indexOf(entry.get("title"));
        }




        //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.count_listview, c1);

        custAdapter adapter = new custAdapter(this,R.layout.count_listview,c1,x);

        count_list = (ListView) findViewById(R.id.idlv_MainActivity);
        count_list.setAdapter(adapter);
        count_list.setOnItemClickListener(this);


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Default " + title);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                controller.updateActivehorf(c1[pos],horf);
                Intent openHeaders = new Intent("kamalcotspin.kcpl.HEADNFOOT");
                openHeaders.putExtra("horf",horf);
                startActivity(openHeaders);
                finish();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });


        count_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                pos = position;
                if (c1.length>0){
                    alertDialog.setMessage("Set " + c1[pos] +" as default");
                }
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.newhorf, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idm_new:
                Intent openFormat = new Intent("kamalcotspin.kcpl.TITLECONTENT");
                openFormat.putExtra("horftitle","");
                openFormat.putExtra("horf",horf);
                startActivity(openFormat);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent openFormat = new Intent("kamalcotspin.kcpl.TITLECONTENT");
        openFormat.putExtra("horftitle",parent.getItemAtPosition(position).toString());
        openFormat.putExtra("horf",horf);
        startActivity(openFormat);
        finish();
    }

    @Override
    public void onClick(View v) {
    }
}
