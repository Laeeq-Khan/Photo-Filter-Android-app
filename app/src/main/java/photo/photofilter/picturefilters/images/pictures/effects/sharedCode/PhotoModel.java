package photo.photofilter.picturefilters.images.pictures.effects.sharedCode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

public class PhotoModel {

    private static PhotoModel instance;

   private static String title;
   private static Bitmap photo;
   private static Uri image_Uri;

    private PhotoModel(){

    }

    public static PhotoModel getInstance(){
        if(instance == null){
          instance=  new PhotoModel();
        }
        return instance;
    }

    public  Uri getImage_Uri() {
        return image_Uri;
    }

    public  void setImage_Uri(Uri image_Uri) {
         this.image_Uri = image_Uri;
    }

    public  String getTitle() {
        return title;
    }

    public  void setTitle(String title) {
        PhotoModel.title = title;
    }

    public  Bitmap getPhoto() {
        return photo;
    }

    public  void setPhoto(Bitmap photo) {
//        if((photo.getWidth()>1024 && photo.getHeight() > 720)  && (photo.getWidth() < 2048 && photo.getHeight() <1440)){
//            OutputStream stream = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG,80, stream);
//        }else if(photo.getWidth() > 2048 && photo.getHeight() >1440){
//            OutputStream stream = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG,65, stream);
//        }
        PhotoModel.photo = photo;
    }




    public  Bitmap getPhotoCopyBitmap(){
        return getPhoto().copy(getPhoto().getConfig(), getPhoto().isMutable());
    }



    public void textImageLoad(){
        String path = "/storage/emulated/0/Download/images.jpg";
        File imgFile = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath());
        setPhoto(bitmap);
        setImage_Uri(Uri.parse(path));
    }
}
