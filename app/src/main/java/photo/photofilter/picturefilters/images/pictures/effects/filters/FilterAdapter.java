package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zomato.photofilters.imageprocessors.Filter;

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
                .inflate(R.layout.filter_recycler_view_thumbnail_template, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textView = holder.textView;
        ImageView imageView = holder.imageView;


        Bitmap bitmap3 = list.get(position).getBitmap();
        Bitmap bitmap2 = bitmap3.copy(bitmap3.getConfig(), bitmap3.isMutable());
        textView.setText(list.get(position).getFilterName());
        final Filter filter = list.get(position).getFilter();

        Glide.with(context).load(list.get(position).getFilter().processFilter(bitmap2)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersActivity.filterApply(filter);
            }
        });
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
