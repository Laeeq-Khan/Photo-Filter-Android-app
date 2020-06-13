package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import photo.photofilter.picturefilters.images.pictures.effects.R;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.PhotoModel;


public class Filters_List_Fragment extends Fragment {


    RecyclerView recyclerView;
    Context context;

    public Filters_List_Fragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.filter_fragment_list_view_bottom, container, false);
        recyclerView = view.findViewById(R.id.filters_recycler_view);

        List<FilterModel> filtersList = new ArrayList<>();
        List<Filter> filters = FilterPack.getFilterPack(getActivity());

        Bitmap bitmap= Bitmap.createScaledBitmap(PhotoModel.getInstance().getPhotoCopyBitmap(), 60, 60, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream );
        for (Filter filter : filters) {
            filtersList.add(new FilterModel(filter.getName(), bitmap,filter ));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        FilterAdapter adapter  = new FilterAdapter(context, filtersList);
        recyclerView.setAdapter(adapter);

        return view;
    }




}
