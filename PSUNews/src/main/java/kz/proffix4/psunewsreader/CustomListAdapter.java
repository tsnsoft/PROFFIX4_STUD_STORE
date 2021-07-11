package kz.proffix4.psunewsreader;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomListAdapter extends BaseAdapter {

    private float fSize;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list;
    private Context context;
    private HashMap<String, String> result = new HashMap<String, String>();

    public CustomListAdapter(Context context, @NonNull ArrayList<HashMap<String, String>> list, float size) {
        this.list = list;
        this.context = context;
        this.fSize = size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview, parent, false);
        result = list.get(position);
        TextView title = itemView.findViewById(R.id.title);
        title.setText(result.get("title"));
        title.setTextSize(fSize);
        ImageView image = itemView.findViewById(R.id.image);
        Picasso.with(context).load(result.get("image")).into(image);
        return itemView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
