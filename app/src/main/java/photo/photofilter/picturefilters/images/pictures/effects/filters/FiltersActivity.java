package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;

import photo.photofilter.picturefilters.images.pictures.effects.AdjustFragment;
import photo.photofilter.picturefilters.images.pictures.effects.GlitchFragment;
import photo.photofilter.picturefilters.images.pictures.effects.R;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FiltersActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    String imagePath;
    CardView bottomNavigation , filterTab, glitchTab, adjustTab;
    static ImageView filterImage;
    static  Bitmap bitmap;
    static int count=0;

    public static void filterApply(Filter filter){
        if(count==0){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)filterImage.getDrawable();
            bitmap = bitmapDrawable.getBitmap();
        }count++;
        Bitmap bitmcopy = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        filter.processFilter(bitmcopy);
        filterImage.setImageBitmap(bitmcopy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activty);
        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        imagePath = getIntent().getStringExtra("picturePath");
        bottomNavigation = findViewById(R.id.bottomnavigation);
        filterImage= findViewById(R.id.filterImage);
        filterTab = findViewById(R.id.filterTab);
        glitchTab = findViewById(R.id.glitchTab);
        adjustTab = findViewById(R.id.adjustTab);

        toolsAppearAnimation(bottomNavigation);

        if(imagePath!=null){
            Glide.with(this).load(imagePath).skipMemoryCache(true).into(filterImage);
        }else{
            filterImage.setImageResource(R.drawable.chooseimage);
        }

        events();

    }

    public void events(){
        filterTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filerFragmentDisplay();
            }
        });

        glitchTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glitchFragmentDisplay();
            }
        });

        adjustTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustmentFragmentDisplay();
            }
        });

    }

    public void filerFragmentDisplay(){
        if(count ==0){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)filterImage.getDrawable();
            bitmap = bitmapDrawable.getBitmap();
        }
        Bitmap bitmap1  = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        Bundle args = new Bundle();
        Filters_List_Fragment filters_list_fragment = new Filters_List_Fragment();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        args.putString("imagePath", imagePath);
        args.putByteArray("bitmap", byteArray);
        filters_list_fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.navigationFragmentsContainer, filters_list_fragment).commit();

    }
    public void glitchFragmentDisplay(){
        GlitchFragment glitchFragment = new GlitchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationFragmentsContainer, glitchFragment).commit();

    }
    public void adjustmentFragmentDisplay(){
        AdjustFragment adjustFragment = new AdjustFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationFragmentsContainer, adjustFragment).commit();

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

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
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
