package kamalcotspin.kcpl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by AT on 3/28/2016.
 */
public class CountFile extends Activity {
    String countType;
    String path = Environment.getExternalStorageDirectory().getPath() + "//kcpl//";
    DBController controller = new DBController(this);
    HashMap<String, String> queryValues;

    private LinearLayout main;
    private ScrollView scroll;
    private int id = 0;
    private List<EditText> editTexts = new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countType = getIntent().getExtras().getString("countType");

        if (countType.equalsIgnoreCase("ALL")){
            Toast.makeText(this, "Edit prices in their own Window", Toast.LENGTH_SHORT).show();
            finish();
        }


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
                queryValues = new HashMap<String, String>();
                for (EditText editText : editTexts) {
                    if (editText.getText().toString().matches("([0-9]*)\\:([0-9]*)(\\.\\d+)?")) {
                        collectData = editText.getText().toString();
                        queryValues.put("countVariant", collectData.split(":")[0]);
                        queryValues.put("countType", countType);
                        queryValues.put("countPrice", collectData.split(":")[1]);
                        // Insert User into SQLite DB
                        //controller.insertCount(queryValues);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        try {
                            controller.insertChangedCount(countType,collectData.split(":")[0],collectData.split(":")[1], sdf.format(timestamp),"local");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        Toast.makeText(CountFile.this, "Enter the data in proper format \n for eg - 30:120.50", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent openQuote = new Intent("kamalcotspin.kcpl.QUOTE");
                openQuote.putExtra("MA_Count",countType);
                startActivity(openQuote);
                finish();
            }
        });

        main.addView(addButton);
        main.addView(submit);
        setContentView(scroll);

        ArrayList<HashMap<String, String>> userList = controller.getAllCounts(countType);
        for (Map<String, String> entry : userList) {
            addEditText(entry.get("countVariant") + ":" +entry.get("countPrice"));
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