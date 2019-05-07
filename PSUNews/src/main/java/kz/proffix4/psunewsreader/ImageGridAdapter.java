package kz.proffix4.psunewsreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridItemViewHolder> {

    private List<String> imageList;
    private Context c;

    class GridItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        GridItemViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv);
        }
    }

    ImageGridAdapter(Context c, List<String> imageList) {
        this.c = c;
        this.imageList = imageList;
    }

    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);

        return new GridItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        final String path = imageList.get(position);
        Picasso.with(c).load(path).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

}
