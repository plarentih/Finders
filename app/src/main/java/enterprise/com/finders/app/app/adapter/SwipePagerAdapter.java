package enterprise.com.finders.app.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import enterprise.com.finders.app.app.fragments.DescriptionFragment;
import enterprise.com.finders.app.app.fragments.PictureFragment;

public class SwipePagerAdapter extends FragmentPagerAdapter {

    public SwipePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return PictureFragment.newInstance();
            case 1:
                return DescriptionFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
