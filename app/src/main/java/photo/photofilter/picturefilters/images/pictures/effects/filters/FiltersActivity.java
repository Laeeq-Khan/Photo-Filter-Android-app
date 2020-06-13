package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import photo.photofilter.picturefilters.images.pictures.effects.R;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.CommonMethods;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.PhotoModel;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FiltersActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 10;
    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    String imagePath;
    CardView bottomNavigation , saveTab, glitchTab, adjustTab;
    static ImageView filterImage;
    CommonMethods commonMethods;
    private InterstitialAd mInterstitialAd;
    static  Bitmap bitmap;



    public static void filterApply(Filter filter){

        bitmap = PhotoModel.getInstance().getPhotoCopyBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()-2, bitmap.getHeight()-2, true);
        filter.processFilter(scaledBitmap);
        filterImage.setImageBitmap(scaledBitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activty);
        commonMethods = new CommonMethods();
        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        bottomNavigation = findViewById(R.id.bottomnavigation);
        filterImage= findViewById(R.id.filterImage);
        saveTab = findViewById(R.id.saveTab);
        mInterstitialAd = new InterstitialAd(this);
        try{
            if(commonMethods.getAdLoadTime() <=2){

                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }catch (Exception e){
        }
        toolsAppearAnimation(bottomNavigation);
        filterImage.setImageBitmap(PhotoModel.getInstance().getPhoto());
        events();
         filerFragmentDisplay();
    }



    public void events(){
        saveTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                filerFragmentDisplay();

                BitmapDrawable bitmapDrawable = (BitmapDrawable)filterImage.getDrawable();
                SaveImage(bitmapDrawable.getBitmap());
                PhotoModel.getInstance().setPhoto(bitmapDrawable.getBitmap());
            }
        });


//        filterImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                commonMethods.viewTransformation(v,event);
//                return true;
//            }
//        });

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails..
                if(commonMethods.getAdLoadTime() <=2){
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }

        });

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
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
            PhotoModel.getInstance().setPhoto(finalBitmap);
            PhotoModel.getInstance().setImage_Uri(Uri.parse(file.getPath()));
            //This code to display your new image into gallary
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[] { file.getPath() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Not Saved. Error! Try Again", Toast.LENGTH_LONG).show();
        }
    }

    public void filerFragmentDisplay(){
        Filters_List_Fragment filters_list_fragment = new Filters_List_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationFragmentsContainer, filters_list_fragment).commit();

    }
    public void glitchFragmentDisplay(){
//        GlitchFragment glitchFragment = new GlitchFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.navigationFragmentsContainer, glitchFragment).commit();

    }


    private void toolsAppearAnimation(CardView view){
        Animation aniFade = AnimationUtils.loadAnimation(this,R.anim.crop_fragment_tools_appear);
        view.startAnimation(aniFade);

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
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
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

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
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
    protected void onResume() {
        super.onResume();
        hide();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hide();
    }


}
