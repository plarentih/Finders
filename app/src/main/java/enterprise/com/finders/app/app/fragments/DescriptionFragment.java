package enterprise.com.finders.app.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import enterprise.com.finders.R;
import enterprise.com.finders.app.app.activities.ReportDetailActivity;
import enterprise.com.finders.app.app.model.Report;

public class DescriptionFragment extends Fragment {

    private TextView textView;

    public DescriptionFragment(){}

    public static DescriptionFragment newInstance(){
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        return descriptionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container,false);
        textView = view.findViewById(R.id.txt_description);
        textView.setText(ReportDetailActivity.description);

        return view;
    }
}
