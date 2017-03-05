package kamalcotspin.kcpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    String Count[] = {"KW","KH","CW","CH","CCW","CCH","OE 1700","OE 1850","OE 1900","OE others","Others"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().show();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.count_listview, Count);

        ListView count_list = (ListView) findViewById(R.id.idlv_MainActivity);
        count_list.setAdapter(adapter);
        count_list.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.variables, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idm_variables:
                Intent openVariables = new Intent("kamalcotspin.kcpl.VARIABLES");
                startActivity(openVariables);
                return true;

            case R.id.idm_format:
                Intent openFormat = new Intent("kamalcotspin.kcpl.FORMAT");
                startActivity(openFormat);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {

    }

    public void onBackPressed()
    {
            finish();
            System.exit(0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
        Intent openQuote = new Intent("kamalcotspin.kcpl.QUOTE");
        openQuote.putExtra("MA_Count",parent.getItemAtPosition(position).toString());
        startActivity(openQuote);
    }
}
