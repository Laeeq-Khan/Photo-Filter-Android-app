package photo.photofilter.picturefilters.images.pictures.effects.filters;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import photo.photofilter.picturefilters.images.pictures.effects.FullscreenActivity;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.CommonMethods;
import photo.photofilter.picturefilters.images.pictures.effects.sharedCode.PhotoModel;
import photo.photofilter.picturefilters.images.pictures.effects.R;


public class First_ImageContainerFragment extends Fragment {



    public First_ImageContainerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageView cropImageView,backCropFragment;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_first__image_container, container, false);
        cropImageView = view.findViewById(R.id.cropImageView);

        backCropFragment = view.findViewById(R.id.backCropFragment);
        cropImageView.setImageBitmap(PhotoModel.getInstance().getPhoto());
        events();

        return view;
    }

    public void events(){

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
                new CommonMethods().viewTransformation(v, event);
                return true;
            }
        });

    }


}
