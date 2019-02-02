package enterprise.com.finders.app.app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import enterprise.com.finders.R;
import enterprise.com.finders.app.app.activities.ReportDetailActivity;

public class PictureFragment extends Fragment {

    private ImageView imageView;

    private Dialog dialog;

    public PictureFragment(){}

    public static PictureFragment newInstance(){
        PictureFragment pictureFragment = new PictureFragment();
        return pictureFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container,false);
        imageView = view.findViewById(R.id.imageView_picture);
        Glide.with(getContext()).load(ReportDetailActivity.urlPhoto).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_picture);
                dialog.setTitle("Picture of report");
                ImageView imageView = dialog.findViewById(R.id.image);
                Glide.with(getContext()).load(ReportDetailActivity.urlPhoto).into(imageView);
                dialog.show();
            }
        });
        return view;
    }
}
