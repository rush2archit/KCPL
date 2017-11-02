package kamalcotspin.kcpl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Archit on 30-10-2017.
 */

public class FormatSettings extends Activity implements CompoundButton.OnCheckedChangeListener{
    Switch timeStamp, header, footer;
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    ArrayList<HashMap<String, String>> flagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formatsettings);
        // initiate view's
        timeStamp = (Switch) findViewById(R.id.ids_timestamp);
        header = (Switch) findViewById(R.id.ids_header);
        footer = (Switch) findViewById(R.id.ids_footer);

        timeStamp.setOnCheckedChangeListener(this);
        header.setOnCheckedChangeListener(this);
        footer.setOnCheckedChangeListener(this);

        flagList = controller.getFlags();
        for (Map<String, String> entry : flagList) {
            /*addEditText(entry.get("countVariant") + ":" +entry.get("countPrice"));*/
            switch (entry.get("flagname")){
                case "timestamp":
                    if (entry.get("flagvalue").equals("0")) {
                        timeStamp.setChecked(false);
                    }else {
                        timeStamp.setChecked(true);
                    }
                    break;
                case "header":
                    if (entry.get("flagvalue").equals("0")) {
                        header.setChecked(false);
                    }else {
                        header.setChecked(true);
                    }
                    break;
                case "footer":
                    if (entry.get("flagvalue").equals("0")) {
                        footer.setChecked(false);
                    }else {
                        footer.setChecked(true);
                    }
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ids_timestamp:
                sendSwitchInfotoDB("timestamp",isChecked);
                break;
            case R.id.ids_header:
                sendSwitchInfotoDB("header",isChecked);
                break;
            case R.id.ids_footer:
                sendSwitchInfotoDB("footer",isChecked);
                break;
            default:
                break;
        }
    }

    public void sendSwitchInfotoDB(String flagname, boolean isChecked){
        if(isChecked){
            controller.updateFormatFlags(flagname,"1");
        }else{
            controller.updateFormatFlags(flagname,"0");
        }
    }
}
