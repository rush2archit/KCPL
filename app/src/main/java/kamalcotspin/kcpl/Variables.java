package kamalcotspin.kcpl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by AT on 3/27/2016.
 */
public class Variables extends Activity implements View.OnClickListener {
    TextView tprice, ttransport, ttransport_conversion, tring_yarn, topen_yarn, tlc_interest, tlc_months, tcommision, tinsurance, tconversion, tdbk;
    EditText price, transport, transport_conversion, ring_yarn, open_yarn, lc_interest, lc_months, commision, insurance, conversion, dbk;
    Button update;
    String path = Environment.getExternalStorageDirectory().getPath()+"//kcpl//";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variables);

        tprice = (TextView) findViewById(R.id.idtv_price);
        ttransport = (TextView) findViewById(R.id.idtv_transport);
        ttransport_conversion = (TextView) findViewById(R.id.idtv_transport_conversion);
        tring_yarn = (TextView) findViewById(R.id.idtv_ring_yarn);
        topen_yarn = (TextView) findViewById(R.id.idtv_open_yarn);
        tlc_interest = (TextView) findViewById(R.id.idtv_lc_interest);
        tlc_months = (TextView) findViewById(R.id.idtv_lc_months);
        tcommision = (TextView) findViewById(R.id.idtv_commision);
        tinsurance = (TextView) findViewById(R.id.idtv_insurance);
        tconversion = (TextView) findViewById(R.id.idtv_conversion);
        tdbk = (TextView) findViewById(R.id.idtv_dbk);

        price = (EditText) findViewById(R.id.idet_price);
        transport = (EditText) findViewById(R.id.idet_transport);
        transport_conversion = (EditText) findViewById(R.id.idet_transport_conversion);
        ring_yarn = (EditText) findViewById(R.id.idet_ring_yarn);
        open_yarn = (EditText) findViewById(R.id.idet_open_yarn);
        lc_interest = (EditText) findViewById(R.id.idet_lc_interest);
        lc_months = (EditText) findViewById(R.id.idet_lc_months);
        commision = (EditText) findViewById(R.id.idet_commision);
        insurance = (EditText) findViewById(R.id.idet_insurance);
        conversion = (EditText) findViewById(R.id.idet_conversion);
        dbk = (EditText) findViewById(R.id.idet_dbk);

        update = (Button) findViewById(R.id.idb_update);
        update.setOnClickListener(this);

        String txt;
        String url = "http://www.google.com/finance/converter?a=1&from=USD&to=INR";

/*        HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
        WebResponse response = request.GetResponse();
        using(Stream responseStream = response.GetResponseStream())
        {
            StreamReader reader = new StreamReader(responseStream, Xml.Encoding.UTF8);
            txt = reader.ReadToEnd();
        }*/


        File setParameters = new File(path,"parameters.txt");
        if (setParameters.exists())
        {
            try {


            FileReader fr=new FileReader(path+"parameters.txt");
            @SuppressWarnings("resource")
            BufferedReader br=new BufferedReader(fr);
            String co = br.readLine();
            String[] count1=co.split("#");
                price.setText(count1[0]);
                transport.setText(count1[1]);
                transport_conversion.setText(count1[2]);
                ring_yarn.setText(count1[3]);
                open_yarn.setText(count1[4]);
                lc_interest.setText(count1[5]);
                lc_months.setText(count1[6]);
                commision.setText(count1[7]);
                insurance.setText(count1[8]);
                conversion.setText(count1[9]);
                dbk.setText(count1[10]);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this,"Enter the Variables",Toast.LENGTH_SHORT).show();
            File folder = new File(path);
            folder.mkdir();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idb_update:
                try {
                    String variables;
                    if (price.getText().toString().equals("") || transport.getText().toString().equals("") || transport_conversion.getText().toString().equals("") || ring_yarn.getText().toString().equals("") || open_yarn.getText().toString().equals("") || lc_interest.getText().toString().equals("") || lc_months.getText().toString().equals("") || commision.getText().toString().equals("") || insurance.getText().toString().equals("") || conversion.getText().toString().equals("") || dbk.getText().toString().equals("")) {
                        Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                    } else {
                        variables = price.getText().toString() + "#" + transport.getText().toString() + "#" + transport_conversion.getText().toString() + "#" + ring_yarn.getText().toString() + "#" + open_yarn.getText().toString() + "#" + lc_interest.getText().toString() + "#" + lc_months.getText().toString() + "#" + commision.getText().toString() + "#" + insurance.getText().toString() + "#" + conversion.getText().toString() + "#" + dbk.getText().toString() ;

                        File parameters = new File(path, "parameters.txt");
                        if (parameters.exists()) {
                            parameters.delete();
                        }
                        FileWriter fw = new FileWriter(path + "parameters.txt", true);
                        fw.write(variables);
                        fw.close();

                        finish();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }

    }
}
