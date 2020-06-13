package photo.photofilter.picturefilters.images.pictures.effects.croping;

import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;


import java.io.File;
import java.io.FileOutputStream;

import photo.photofilter.picturefilters.images.pictures.effects.FullscreenActivity;
import photo.photofilter.picturefilters.images.pictures.effects.R;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.CommonMethods;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.PhotoModel;

public class CropFragment extends Fragment {


    public CropFragment(){

    }


    CardView cropToolsCardView, cardLeftRotate, cardRightRotate, cardRefelect, flipImage;
    TextView saveButton;


    public static CropFragment newInstance() {
        return new CropFragment();
    }

    ImageView cropImageView,backCropFragment;
    CardView cropCard;
    Bitmap bitmapPhoto;
    private CommonMethods commonMethods;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.crop_fragment, container, false);
            cropImageView = view.findViewById(R.id.cropImageView);
            cropToolsCardView = view.findViewById(R.id.cropToolsCardView);
            backCropFragment = view.findViewById(R.id.backCropFragment);
            cropCard = view.findViewById(R.id.cropIcon);
            cardLeftRotate = view.findViewById(R.id.leftRotate);
            cardRightRotate = view.findViewById(R.id.righRotate);
            cardRefelect = view.findViewById(R.id.rotateicon);
            saveButton = view.findViewById(R.id.saveButton);
            flipImage = view.findViewById(R.id.flipImage);
            toolsAppearAnimation(cropToolsCardView);
            bitmapPhoto = PhotoModel.getInstance().getPhotoCopyBitmap();
            cropImageView.setImageBitmap(bitmapPhoto);
            commonMethods  = new CommonMethods();
           events();
        return view;
    }

    private void toolsAppearAnimation(CardView view){

        Animation aniFade = AnimationUtils.loadAnimation(getContext(),R.anim.crop_fragment_tools_appear);
        view.startAnimation(aniFade);

    }

    boolean saveStatus = false;
    public void events(){


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) cropImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                SaveImage(bitmap);
                PhotoModel.getInstance().setPhoto(bitmap);
                saveStatus = false;
            }
        });

        backCropFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FullscreenActivity.class);
                startActivity(intent);
            }
        });

        cropImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CommonMethods commonMethods = new CommonMethods();
                commonMethods.viewTransformation(v, event);
                return true;
            }
        });

        cropCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crop();
            }
        });

        final float[] rotateAngle = {0};
        cardLeftRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAngle[0] +=90;
                if(rotateAngle[0]==360)rotateAngle[0]=0;
                bitmapPhoto=rotate(bitmapPhoto, rotateAngle[0]);
                cropImageView.setImageBitmap(bitmapPhoto);
                saveStatus = true;
            }
        });
        cardRightRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAngle[0] -=90;
                if(rotateAngle[0]==360)rotateAngle[0]=0;
                bitmapPhoto=rotate(bitmapPhoto, rotateAngle[0]);
                cropImageView.setImageBitmap(bitmapPhoto);
                saveStatus = true;
            }
        });
        cardRefelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAngle[0] +=180;
                if(rotateAngle[0]==360)rotateAngle[0]=0;
                bitmapPhoto=rotate(bitmapPhoto, rotateAngle[0]);
                cropImageView.setImageBitmap(bitmapPhoto);
                saveStatus = true;
            }
        });

        flipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapPhoto = flip(bitmapPhoto,-1.0f, 1.0f);
                cropImageView.setImageBitmap(bitmapPhoto);
                saveStatus = true;
             }
        });
    }

    public static Bitmap rotate(Bitmap src, float angle){

        float degrees = angle;//rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return src;
    }
    public static Bitmap flip(Bitmap src , float x, float y) {
        Matrix matrix = new Matrix();
        matrix.preScale(x, y);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public  void SaveImage(Bitmap finalBitmap) {

        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Beauty Snaps");
        if(!myDir.exists())
            myDir.mkdirs();

        String fname = commonMethods.getUniqueFileName();
        File file = new File (myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_LONG).show();
            PhotoModel.getInstance().setPhoto(finalBitmap);
            PhotoModel.getInstance().setImage_Uri(Uri.parse(file.getPath()));
            //This code to display your new image into gallary
            MediaScannerConnection.scanFile(getContext(),
                    new String[] { file.getPath() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Not Saved. Error! Try Again", Toast.LENGTH_LONG).show();
        }
    }

    public void crop(){
      if(saveStatus == true){
          Toast.makeText(getContext(), "If you want to keep changes Please Save or Press Again to Crop", Toast.LENGTH_LONG).show();
          saveStatus = false;
          return;
      }
      CropImage.activity(Uri.fromFile(new File(PhotoModel.getInstance().getImage_Uri().getPath()))).start((Activity) getContext());
    }

}
