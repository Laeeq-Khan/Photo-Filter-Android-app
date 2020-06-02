package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import photo.photofilter.picturefilters.images.pictures.effects.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {


    List<FilterModel> list;
    Context context;
    public FilterAdapter(Context context,List<FilterModel> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filtersdisplay_list_layout_recycler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textView = holder.textView;
        ImageView imageView = holder.imageView;

        textView.setText(list.get(position).getFilterName());
        imageView.setImageBitmap(list.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        Filter filter;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.filterName);
            imageView = itemView.findViewById(R.id.filterImage);
        }
        // each data item is just a string in this case

    }

}
