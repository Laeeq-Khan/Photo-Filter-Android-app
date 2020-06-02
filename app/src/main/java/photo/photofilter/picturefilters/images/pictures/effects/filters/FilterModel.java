package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;


public class FilterModel {

    String filterName;
    Bitmap bitmap;
    Filter filter;

    public FilterModel(String filterName, Bitmap bitmap, Filter filter) {
        this.filterName = filterName;
        this.bitmap = bitmap;
        this.filter = filter;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
