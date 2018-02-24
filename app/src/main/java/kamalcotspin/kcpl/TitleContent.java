package kamalcotspin.kcpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class TitleContent extends Activity implements View.OnClickListener {

    EditText Title, Content;
    Button update;
    DBController controller = new DBController(this);
    //String path = Environment.getExternalStorageDirectory().getPath()+"//kcpl//";
    String horf = "";
    String oldTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format);

        Title = (EditText) findViewById(R.id.idet_title);
        Content = (EditText) findViewById(R.id.idet_content);

        oldTitle =  getIntent().getExtras().getString("horftitle");
        horf = getIntent().getExtras().getString("horf");
        //Toast.makeText(this, oldTitle, Toast.LENGTH_SHORT).show();

        update = (Button) findViewById(R.id.idb_updatef);
        update.setOnClickListener(this);
        if (!oldTitle.equals("")){
            Title.setText(oldTitle);
            Content.setText(controller.getContent(oldTitle)[0]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idb_updatef:

                    //String format,prefix,suffix;
                    if (Title.getText().toString().equals("")) {
                        //Toast.makeText(this, "You have not entered any Header Information", Toast.LENGTH_SHORT).show();
                    }
                    if (Content.getText().toString().equals("")) {
                        //Toast.makeText(this, "You have not entered any Footer Information", Toast.LENGTH_SHORT).show();
                    }
                    controller.updateContent(oldTitle,Title.getText().toString(),Content.getText().toString(),horf);
                    Intent openHeaders = new Intent("kamalcotspin.kcpl.HEADNFOOT");
                    openHeaders.putExtra("horf",horf);
                    startActivity(openHeaders);
                    finish();
                break;
        }

    }

    @Override
    public void onBackPressed(){
        finish();
        Intent openHeaders = new Intent("kamalcotspin.kcpl.HEADNFOOT");
        openHeaders.putExtra("horf",horf);
        startActivity(openHeaders);
    }
}
