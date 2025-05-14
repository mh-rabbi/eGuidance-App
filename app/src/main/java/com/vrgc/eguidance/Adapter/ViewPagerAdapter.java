package com.vrgc.eguidance.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vrgc.eguidance.Fragments.SignInTabFragment;
import com.vrgc.eguidance.Fragments.SignUpTabFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SignInTabFragment();
            case 1:
                return new SignUpTabFragment();
                default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
