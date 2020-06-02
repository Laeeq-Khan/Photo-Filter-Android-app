package photo.photofilter.picturefilters.images.pictures.effects;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CropFragment extends Fragment {


    public CropFragment(){

    }

    String imagePath;
    Context context;
    CardView cropToolsCardView, cardLeftRotate, cardRightRotate, cardRefelect, flipImage;
    TextView saveButton;

    public CropFragment(String path , Context context){
        System.out.println("Path we received "+path);
        imagePath = path;
        this.context = context;
     }

    public static CropFragment newInstance() {
        return new CropFragment();
    }

    ImageView cropImageView,backCropFragment;
    CardView cropCard;
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


            if(imagePath!=null){
                Glide.with(this).load(imagePath).skipMemoryCache(true).into(cropImageView);
            }else{
                cropImageView.setImageResource(R.drawable.chooseimage);

            }
            events();
        return view;
    }

    private void toolsAppearAnimation(CardView view){

        Animation aniFade = AnimationUtils.loadAnimation(context,R.anim.crop_fragment_tools_appear);
        view.startAnimation(aniFade);

    }


    public void events(){


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) cropImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                SaveImage(bitmap);

            }
        });

        backCropFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullscreenActivity.class);
                startActivity(intent);
            }
        });

        cropImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewTransformation(v, event);
                return true;
            }
        });

        cropCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crop();
            }
        });

        final int[] rotateAngle = {0};
        cardLeftRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAngle[0] -=90;
                Picasso.get().load(new File(imagePath)).rotate(rotateAngle[0]).into(cropImageView);

            }
        });
        cardRightRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAngle[0] +=90;
                Picasso.get().load(new File(imagePath)).rotate(rotateAngle[0]).into(cropImageView);
            }
        });
        cardRefelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAngle[0] +=180;
                Picasso.get().load(new File(imagePath)).rotate(rotateAngle[0]).into(cropImageView);
            }
        });

        flipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) cropImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                cropImageView.setImageBitmap(flip(bitmap));
            }
        });

    }

    public static Bitmap flip(Bitmap src) {
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f);

        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public  void SaveImage(Bitmap finalBitmap) {

        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Beauty Snaps");
        if(!myDir.exists())
            myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "JPEG_" + timeStamp + "_laeeq.jpg";
        File file = new File (myDir, fname);
        imagePath = file.getPath();

//        if (file.exists ())
//            file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            out.close();
            Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show();
            //This code to display your new image into gallary
            MediaScannerConnection.scanFile(context,
                    new String[] { imagePath }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Not Saved. Error! Try Again", Toast.LENGTH_LONG).show();
        }
    }

    public void crop(){
//
//        Uri path = Uri.parse("android.resource://photo.photofilter.picturefilters.images.pictures.effects/" + R.drawable.homeactivityimage);
//        String imgPath = path.toString();
        Uri path2 =  Uri.fromFile(new File(imagePath));
        CropImage.activity(path2).start(getActivity());
    }





    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;
    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 5f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);

                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {

                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


}
