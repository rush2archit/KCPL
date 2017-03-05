package kamalcotspin.kcpl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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
public class Format extends Activity implements View.OnClickListener {

    EditText header,footer;
    Button update;
    String path = Environment.getExternalStorageDirectory().getPath()+"//kcpl//";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format);

        header = (EditText) findViewById(R.id.idet_header);
        footer = (EditText) findViewById(R.id.idet_footer);


        update = (Button) findViewById(R.id.idb_updatef);
        update.setOnClickListener(this);



        File setParameters = new File(path,"format.txt");
        if (setParameters.exists())
        {
            try {


            FileReader fr=new FileReader(path+"format.txt");
            @SuppressWarnings("resource")
            BufferedReader br=new BufferedReader(fr);


                String reader, hf = "";
                while ((reader = br.readLine()) != null) {
                    hf = hf + reader + "\n";
                }


            String[] count1=hf.split("#");
                header.setText(count1[0]);
                footer.setText(count1[1]);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this,"Enter the Share Format Details",Toast.LENGTH_LONG).show();
            File folder = new File(path);
            folder.mkdir();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idb_updatef:
                try {
                    String format,prefix,suffix;
                    if (header.getText().toString().equals(""))  {
                        Toast.makeText(this, "You have not entered any Header Information", Toast.LENGTH_SHORT).show();
                        prefix = "-";
                    }
                    if (footer.getText().toString().equals(""))
                    {
                        Toast.makeText(this, "You have not entered any Footer Information", Toast.LENGTH_SHORT).show();
                        suffix = "-";
                    }
                        format = header.getText().toString() + "#" + footer.getText().toString();
                        if (!format.equals("#"))
                        {
                            File formatter = new File(path, "format.txt");
                        if (formatter.exists()) {
                            formatter.delete();
                        }
                        FileWriter fw = new FileWriter(path + "format.txt", true);
                        fw.write(format);
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
