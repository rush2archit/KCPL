package kamalcotspin.kcpl;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AT on 3/28/2016.
 */
public class CountFile extends Activity {
    String countType;
    String path = Environment.getExternalStorageDirectory().getPath() + "//kcpl//";

    private LinearLayout main;
    private ScrollView scroll;
    private int id = 0;
    private List<EditText> editTexts = new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countType = getIntent().getExtras().getString("countType");

        ActionBar ab = getActionBar();
        ab.setTitle(countType);

        scroll = new ScrollView(this);


        main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(main);

        File folder = new File(path);
        if (!(folder.exists())) {
            folder.mkdir();
        }

        final String filename = countType + ".txt";


        Button addButton = new Button(this);
        addButton.setText("Add");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                addEditText("");
            }
        });

        Button submit = new Button(this);
        submit.setText("SUBMIT");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String collectData = "";
                for (EditText editText : editTexts) {
                    if (!(editText.getText().toString().equals(""))) {
                        collectData = collectData + editText.getText().toString() + "/";
                    }
                }
                FileWriter fw = null;
                try {
                    File cType = new File(path, filename);
                    if (cType.exists()) {
                        cType.delete();
                    }
                    fw = new FileWriter(path + countType + ".txt", true);
                    fw.write(collectData);
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        main.addView(addButton);
        main.addView(submit);
        setContentView(scroll);

        File coPrice = new File(path, filename);
        if (coPrice.exists()) {
            try {
                FileReader fr = new FileReader(path + filename);
                @SuppressWarnings("resource")
                BufferedReader br = new BufferedReader(fr);
                String co = br.readLine();
                if (coPrice.length() == 0) {
                    Toast.makeText(this, "empty File", Toast.LENGTH_SHORT).show();
                } else {
                    /*if (co != null || co != "") {*/
                    String[] count1 = co.split("/");
                    for (String s : count1) {
                        addEditText(s);
                    }
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void addEditText(String pair) {
        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);
        main.addView(editTextLayout);

        EditText editText1 = new EditText(this);
        editText1.setId(id++);
        editTextLayout.addView(editText1);

        editTexts.add(editText1);
        if (pair.equals("") || pair.equals(null)) {
            editText1.setHint("count:price");
        } else {
            editText1.setText(pair);
        }
    }
}
