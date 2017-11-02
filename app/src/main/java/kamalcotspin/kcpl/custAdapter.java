package kamalcotspin.kcpl;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Archit on 01-11-2017.
 */

public class custAdapter extends ArrayAdapter {
int some;
    public custAdapter(@NonNull Context context, @LayoutRes int resource, String[] c1, int xz) {
        super(context, resource,c1);
        some = xz;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position == some) {
            view.setBackgroundColor(Color.GRAY);
        }
        else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

}
