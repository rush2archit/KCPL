package kamalcotspin.kcpl;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;

import java.util.HashMap;

/**
 * Created by Archit on 17-10-2017.
 */

public class Compare extends ListActivity{
    String countType;
    String prefix, suffix;
    String path = Environment.getExternalStorageDirectory().getPath() + "//kcpl//";
    String sharePath = path + "share//";
    String[] countVariant, amountRs, amount$, amountf$,countVType;

    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        countType = getIntent().getExtras().getString("count");
        ActionBar ab = getActionBar();
        ab.setTitle(countType);
    }
}
