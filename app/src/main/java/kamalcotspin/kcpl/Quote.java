package kamalcotspin.kcpl;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by AT on 3/28/2016.
 */
public class Quote extends ListActivity {
    String countType;
    String prefix, suffix,tStamp,shareString="";
    String path = Environment.getExternalStorageDirectory().getPath() + "//kcpl//";
    String sharePath = path + "share//";
    String[] countVariant, amountRs, amount$, amountf$,countVType;

    DBController controller = new DBController(this);
    ArrayList<HashMap<String, String>> flagList;
    public ArrayList<Integer> coloredItems = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        countType = getIntent().getExtras().getString("MA_Count");
        ActionBar ab = getActionBar();
        ab.setTitle(countType);


        File folder = new File(path);
        File folderShare = new File(sharePath);
        if (!(folder.exists())) {
            folder.mkdir();
        }
        if (!(folderShare.exists())) {
            folderShare.mkdir();
        }


        File Variables = new File(path, "parameters.txt");
        //File formatter = new File(path, "format.txt");

        flagList = controller.getFlags();
        for (Map<String, String> entry : flagList) {
            switch (entry.get("flagname")){
                case "timestamp":
                    if (entry.get("flagvalue").equals("0")) {
                        tStamp="";
                    }else {
                        String format = "EEE, d MMM yyyy HH:mm:ss ZZZZ";
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        tStamp = sdf.format(new Date());
                        shareString = tStamp + "\n\n";
                    }
                    break;
                case "header":
                    if (entry.get("flagvalue").equals("0")) {
                        prefix = "";
                    }else {
                        for (Map<String, String> entry1 : controller.getActivehorf("h")) {
                            prefix = entry1.get("content");
                        }
                        shareString = shareString + prefix + "\n\n";
                    }
                    break;
                case "footer":
                    if (entry.get("flagvalue").equals("0")) {
                        suffix = "";
                    }else {
                        for (Map<String, String> entry1 : controller.getActivehorf("f")) {
                            suffix = entry1.get("content");
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        /*if (formatter.exists()) {
            try {
                FileReader fr2 = new FileReader(path + "format.txt");
                @SuppressWarnings("resource")
                BufferedReader br2 = new BufferedReader(fr2);
                String reader, hf = "";
                while ((reader = br2.readLine()) != null) {
                    hf = hf + reader + "\n";
                }
                String[] hnf1 = hf.split("#");
                prefix = hnf1[0];
                suffix = hnf1[1];
                if (hnf1[0].equals("-")) {
                    prefix = "";
                }
                if (hnf1[1].equals("-")) {
                    suffix = "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            prefix = suffix = "";
        }*/

        if (Variables.exists()) {
            try {
                FileReader fr1 = new FileReader(path + "parameters.txt");
                BufferedReader br1 = new BufferedReader(fr1);
                String co1 = br1.readLine();
                String[] parameters = co1.split("#");

                float price = Float.parseFloat(parameters[0]);
                float transport = Float.parseFloat(parameters[1]);
                float transport_conversion = Float.parseFloat(parameters[2]);
                float ring_yarn = Float.parseFloat(parameters[3]);
                float open_yarn = Float.parseFloat(parameters[4]);
                float lc_interest = Float.parseFloat(parameters[5]);
                float lc_months = Float.parseFloat(parameters[6]);
                float commision = Float.parseFloat(parameters[7]);
                float insurance = Float.parseFloat(parameters[8]);
                float conversion = Float.parseFloat(parameters[9]);
                float dbk = Float.parseFloat(parameters[10]);
                float yarn;

                //System.out.println("yarn value" + yarn);
                ArrayList<HashMap<String, String>> userList = controller.getAllCounts(countType);

                /*FileReader fr = new FileReader(path + filename);
                BufferedReader br = new BufferedReader(fr);
                String co = br.readLine();
                */if (userList.size() == 0) {
                    Toast.makeText(this, "Enter Count & Price Details", Toast.LENGTH_SHORT).show();
                } else {

                    //String[] count1 = co.split("/");
                    countVariant = new String[userList.size()];
                    countVType = new String[userList.size()];
                    amountRs = new String[userList.size()];
                    amount$ = new String[userList.size()];
                    amountf$ = new String[userList.size()];
                    int i = 0;


                    for (Map<String, String> entry : userList) {
                        //System.out.println(entry.get("countPrice")+" : "+entry.get("countType")+" : "+entry.get("countVariant"));


                    /*for (String s : count1) {
                        String[] pair = s.split(":");
                        countVariant[i] = pair[0];
                        float countPrice = Float.parseFloat(pair[1]);
                        */
                        countVariant[i] = entry.get("countVariant");
                        countVType[i] = entry.get("countType");
                        float countPrice = Float.parseFloat(entry.get("countPrice"));

                        if (countVType[i].substring(0, 2).equalsIgnoreCase("OE")) {
                            yarn = open_yarn;
                        } else {
                            yarn = ring_yarn;
                        }


                        float calc1 = price / yarn;
                        float calc2 = transport * transport_conversion / yarn;
                        float calc3 = countPrice * commision;
                        float calc4 = calc3 / conversion;
                        float calc5 = countPrice + calc1 + calc2 + calc3;
                        float calc6 = calc5 * yarn;
                        float calc7 = ((calc6 * lc_interest) / 12) * lc_months;
                        float calc8 = calc7 / yarn;
                        float calc9 = calc5 + calc8;
                        float calc10 = calc6 + calc7;
                        float calc11 = (calc10 / 1000) * insurance;
                        float calc12 = calc11 / yarn;
                        float amtRs = calc9 + calc12;
                        float amt$ = amtRs / conversion;
                        float calc13 = countPrice * dbk;
                        float calc14 = amtRs - calc13;
                        float famt$ = calc14 / conversion;

                        amountRs[i] = String.format("%.2f", amtRs);
                        amount$[i] = String.format("%.2f", amt$);
                        amountf$[i] = String.format("%.2f", famt$);

                        i++;
/*
                        System.out.println(calc1);
                        System.out.println(calc2);
                        System.out.println(calc3);
                        System.out.println(calc4);
                        System.out.println(calc5);
                        System.out.println(calc6);
                        System.out.println(calc7);
                        System.out.println(calc8);
                        System.out.println(calc9);
                        System.out.println(calc10);
                        System.out.println(calc11);
                        System.out.println(calc12);
                        System.out.println(amtRs);
                        System.out.println(amt$);
                        System.out.println(calc13);
                        System.out.println(famt$);
                        System.out.println(pair[0] + "--" + amtRs + "--" + amt$);
*/
                    }



                    //prefix = tStamp + "\n\n" + prefix + "\n\n";
                    prefix = shareString;
                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    int j = 0;
                    HashMap<String, String> map;
                    for (String x : countVariant) {
                        map = new HashMap<String, String>();
                        map.put("Variant", countVariant[j] + " " + countVType[j]);
                        map.put("amtRs", getResources().getString(R.string.rs) + " " + amountRs[j]);
                        map.put("amt$", amount$[j] + "$");
                        map.put("famt$", amountf$[j] + "$");
                        mylist.add(map);
                        j++;
                    }

                    custQuoteAdapter quoteDisplay = new custQuoteAdapter(this, mylist, R.layout.quote_listview,
                            new String[]{"Variant", "amtRs", "amt$", "famt$"}, new int[]{R.id.id_countVariant, R.id.id_INR, R.id.id_$, R.id.id_f$},coloredItems);
                    setListAdapter(quoteDisplay);
                    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int position, long arg3) {

                            Intent openCompare  = new Intent("kamalcotspin.kcpl.COMPARE");
                            openCompare.putExtra("count",countVariant[position] + " "+countVType[position]);
                            startActivity(openCompare);
                            return true;
                        }
                    });
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        if (coloredItems.contains(position)){
        //if (!l.isItemChecked(position)){
            //l.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            //Toast.makeText(this,l.getChildAt(position),Toast.LENGTH_SHORT).show();
            coloredItems.remove(new Integer(position));
            v.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            coloredItems.add(position);
            //l.getChildAt(position).setBackgroundColor(Color.GRAY);
            v.setBackgroundColor(Color.GRAY);
        }
    }





@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.counts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idm_counts:
                Intent openCountPrice = new Intent("kamalcotspin.kcpl.COUNTFILE");
                openCountPrice.putExtra("countType", countType);
                startActivity(openCountPrice);
                finish();
                return true;

            case R.id.idm_$:
                ListView l = this.getListView();
                String shareSelectQuote = prefix;
                for (int i=0;i<l.getCheckedItemPositions().size();i++){
                    if(l.getCheckedItemPositions().valueAt(i)){
                        int a = l.getCheckedItemPositions().keyAt(i);
                        shareSelectQuote = shareSelectQuote+countVariant[a]+" " +countVType[a] + "\t" + amountf$[a]+ "$" + "\n" ;
                    }
                }
                shareSelectQuote = shareSelectQuote + "\n" +suffix;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareSelectQuote);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.idm_₹$:
                l = this.getListView();
                shareSelectQuote = prefix;
                for (int i=0;i<l.getCheckedItemPositions().size();i++){
                    if(l.getCheckedItemPositions().valueAt(i)){
                        int a = l.getCheckedItemPositions().keyAt(i);
                        shareSelectQuote = shareSelectQuote+countVariant[a]+ " " +countVType[a] + "\t" + "₹" +amountRs[a]+ "\t" + amountf$[a]+ "$" + "\n" ;
                    }
                }

                shareSelectQuote = shareSelectQuote + "\n" +suffix;
                shareSelectQuote = shareSelectQuote.replaceAll("([\\n\\r]+\\s*)*$", "");
                Intent sendIntent1 = new Intent();
                sendIntent1.setAction(Intent.ACTION_SEND);
                sendIntent1.putExtra(Intent.EXTRA_TEXT, shareSelectQuote);
                sendIntent1.setType("text/plain");
                startActivity(sendIntent1);
                return true;

            case R.id.idm_$image:
                    ListView l3 = this.getListView();
                    String ShareImageName = String.valueOf(System.currentTimeMillis())+".jpg";
                    shareSelectQuote = prefix;

                for (int i=0;i<l3.getCheckedItemPositions().size();i++){
                        if(l3.getCheckedItemPositions().valueAt(i)){
                            int a = l3.getCheckedItemPositions().keyAt(i);
                            shareSelectQuote = shareSelectQuote+countVariant[a]+" " +countVType[a] + "\t\t\t" + amountf$[a]+ "$" + "\n" ;
                        }
                    }
                shareSelectQuote = shareSelectQuote + "\n" +suffix;

                    Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.kcpllogo); // the original file yourimage.jpg i added in resources

                    String[] print = shareSelectQuote.split("\n");
                    int x = print.length;
                    int height = src.getHeight()*2;
                    if (x*50 > height)
                    {
                        height = x*50+50;
                    }
                    int width = src.getWidth()*2;


                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas cs = new Canvas(bitmap);

                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    cs.drawPaint(paint);
                    cs.drawBitmap(src,width/4,height/3,paint);

                    Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                    p2.setColor(Color.BLACK);
                    p2.setAntiAlias(true);
                    p2.setTextSize(40.f);
                    p2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    p2.setTextAlign(Paint.Align.LEFT);

                    float height1 = 50.f;
                    float width1 = 50.f;

                    for (String s : print){
                        cs.drawText(s, width1, height1, p2);
                        height1 = height1+50.f;
                    }

                    try {
                        File file = new File(sharePath);
                        String[] files;
                        files = file.list();
                        if(files!=null) {
                            for (int i = 0; i < files.length; i++) {
                                File myFile = new File(file, files[i]);
                                myFile.delete();
                            }
                        }

                        OutputStream outStream = null;
                        outStream = new FileOutputStream(sharePath+ShareImageName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        Intent sendIntent2 = new Intent();
                        sendIntent2.setAction(Intent.ACTION_SEND);
                        sendIntent2.setType("image/*");
                        Uri fileUri = FileProvider.getUriForFile(this, "kcpl.kamalcotspin.fileprovider", new File(sharePath+ShareImageName));
                        sendIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        sendIntent2.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        sendIntent2.putExtra(Intent.EXTRA_STREAM, fileUri);
                        this.startActivity(Intent.createChooser(sendIntent2, "Share Image"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


/*    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Sample Long Click", Toast.LENGTH_SHORT).show();
        return false;
    }*/
}