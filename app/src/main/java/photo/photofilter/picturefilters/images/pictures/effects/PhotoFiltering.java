package photo.photofilter.picturefilters.images.pictures.effects;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;

import photo.photofilter.picturefilters.images.pictures.effects.croping.CropFragment;
import photo.photofilter.picturefilters.images.pictures.effects.filters.FiltersActivity;
import photo.photofilter.picturefilters.images.pictures.effects.filters.First_ImageContainerFragment;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.PhotoModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PhotoFiltering extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;


    FrameLayout mainLayouteContainer;
    private static final int AUTO_HIDE_DELAY_MILLIS = 10;
    ImageView imageDisplay;
    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    CardView cropCard, filterCard, adjustmentCard, effectCard,bottomnavigation;

    static{
        System.loadLibrary("NativeImageProcessor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_contain_tools_nav);
        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        mainLayouteContainer= findViewById(R.id.mainLayouteContainer);
        cropCard  = findViewById(R.id.cropIcon);
        filterCard = findViewById(R.id.filterIcon);
      //  adjustmentCard = findViewById(R.id.adjustmentIcon);
      //  effectCard = findViewById(R.id.effectIcon);
        events();
        onload();


    }


    private void onload(){
        First_ImageContainerFragment firstFrament = new First_ImageContainerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayouteContainer,firstFrament ).commit();
    }

    private void events(){

        cropCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropFragment cropFragment = new CropFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayouteContainer,cropFragment ).commit();
             }
        });

        filterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(PhotoFiltering.this, FiltersActivity.class);
               startActivity(intent);
            }
        });

    }





    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
     private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(100);
    }
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }




    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hide();
        CropFragment cropFragment = new CropFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayouteContainer,cropFragment ).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = result.getUri();
                ImageView img = new ImageView(this);
                img.setImageURI(uri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable)img.getDrawable();
                PhotoModel.getInstance().setPhoto(bitmapDrawable.getBitmap());
                CropFragment cropFragment = new CropFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayouteContainer,cropFragment ).commit();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }




}
