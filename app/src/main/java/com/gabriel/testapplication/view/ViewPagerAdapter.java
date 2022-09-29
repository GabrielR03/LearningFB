package com.gabriel.testapplication.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gabriel.testapplication.view.fragments.ItemsFragment;
import com.gabriel.testapplication.view.fragments.UserFragment;
import com.google.firebase.firestore.auth.User;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position){
            case 1:
                return new ItemsFragment();
            default:
                return new UserFragment();
        }
    }

    @Override
    public int getItemCount(){
        return 2;
    }
}
