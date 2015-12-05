package com.digitallight.gorescue.go_rescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by F on 12/5/2015.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] valuestitle;
    private final String[] valuessubtitle;
    private final String[] valuesimage;

    public ListAdapter(Context context, String[] valuestitle,
                       String[] valuessubtitle, String[] valuesimage) {
        super(context, R.layout.listview_layout, valuestitle);
        this.context = context;
        this.valuestitle = valuestitle;
        this.valuessubtitle = valuessubtitle;
        this.valuesimage = valuesimage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // tampilan listview di atur dalam listview_layout.xml yang berada dalam
        // res/layout
        View rowView = inflater
                .inflate(R.layout.listview_layout, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView subtitle = (TextView) rowView.findViewById(R.id.subtitle);
//		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        title.setText(valuestitle[position]);
        subtitle.setText(valuessubtitle[position]);
        new DownloadImageTask((ImageView) rowView.findViewById(R.id.icon)).execute(valuesimage[position]);
        // imageView.setImageResource(valuesimage[position]);

        return rowView;
    }
}
