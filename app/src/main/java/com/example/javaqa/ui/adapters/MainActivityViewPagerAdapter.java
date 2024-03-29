package com.example.javaqa.ui.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewPagerAdapter extends FragmentPagerAdapter {

  private final List<Fragment> fragmentList = new ArrayList<>();
  private final List<String> fragmentTitleList = new ArrayList<>();

  public MainActivityViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    return fragmentList.get(position);
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return fragmentTitleList.get(position);
  }

  @Override
  public int getCount() {
    return fragmentTitleList.size();
  }

  public void addFragment(Fragment fr,String title){
    fragmentList.add(fr);
    fragmentTitleList.add(title);
  }
}
