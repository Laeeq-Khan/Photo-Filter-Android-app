package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.Random;

import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.PhotoModel;


public class CustomDiagram extends AppCompatImageView {

        private Paint currentPaint;
        float xStart, yStart, xStop, yStop;
        Bitmap bitmapImage;


    public CustomDiagram(Context context, AttributeSet attrs ) {
        super(context, attrs);
        bitmapImage = PhotoModel.getInstance().getPhoto();
        xStart = 10;
        yStart=50;
        xStop = 100;
        yStop=100;

    }

    Canvas canvas;

    @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
                this.canvas = canvas;
                currentPaint = new Paint();
                currentPaint.setStrokeWidth(5);
                currentPaint.setColor(Color.parseColor("#B6B6B6"));
                currentPaint.setStyle(Paint.Style.STROKE);
                currentPaint.setAlpha(90);
                drawLine();

        }

        public void drawLine( ){
            currentPaint.setAlpha(90);
            currentPaint.setStrokeWidth(8);
            currentPaint.setColor(Color.parseColor("#B6B6B6"));
            canvas.drawRect(5, getHeight()/2-(250), getWidth()-5, getHeight()/2+(250), currentPaint);
            RectF rectF = new RectF(5, getHeight()/2-(250), getWidth()-5, 5);
            canvas.drawLine(5, getHeight()/2+(240), getWidth()-5 , getHeight()/2-(250), currentPaint);

            currentPaint.setAlpha(100);
            currentPaint.setStrokeWidth(8);
            currentPaint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawLine(5, getHeight()/2+(240), getWidth()-5 , getHeight()/2-(250), currentPaint);

        }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        xStart = event.getX();
        yStop = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            currentPaint.setColor(Color.RED);
            currentPaint.setStyle(Paint.Style.FILL);

        }

        invalidate();
        return true;
    }




}


