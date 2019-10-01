package com.example.javaqa.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.javaqa.ActivityUtils.LaunchActivityHelper;
import com.example.javaqa.R;
import com.example.javaqa.ui.adapters.MainActivityViewPagerAdapter;
import com.example.javaqa.ui.fragments.ConversationFragment;
import com.example.javaqa.ui.fragments.GamesStatusFragment;
import com.example.javaqa.ui.fragments.LearnFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_tabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.main_nested_scroll_view) NestedScrollView mNestedScrollView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private MainActivityViewPagerAdapter mViewPagerAdapter;
    private LaunchActivityHelper launchActivityHelper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        launchActivityHelper = new LaunchActivityHelper();

        setUpAppBar();
        setUpViewPagerAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.main_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.invite_friends:

                return true;
            case R.id.notifications:

                return true;
            case R.id.profile:
                    launchActivityHelper.launchActivity(this,ProfileActivity.class,Intent.FLAG_ACTIVITY_NO_ANIMATION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpViewPagerAdapter() {
        mNestedScrollView.setFillViewport(true);

        mViewPagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());

        mViewPagerAdapter.addFragment(new LearnFragment(),"");
        mViewPagerAdapter.addFragment(new GamesStatusFragment(),"");
        mViewPagerAdapter.addFragment(new ConversationFragment(),"");

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(R.drawable.student_96px);
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setIcon(R.drawable.controller_96px);
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setIcon(R.drawable.comments_96px);

        mTabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String title;
                switch (position){
                    case 0:
                        title = "Обучение";
                        break;
                    case 1:
                        title = "Играть";
                        break;
                    case 2:
                        title = "Обсуждения";
                        break;
                    default:
                        title = "Q&A";
                }
                Objects.requireNonNull(getSupportActionBar()).setTitle(title);
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    fab.hide();
                } else if(position == 1) {
                    fab.hide();
                    fab.setImageResource(R.drawable.plus_math_16px);
                    fab.show();
                    fab.setOnClickListener(view -> launchActivity(NewGameActivity.class));
                } else if(position == 2) {
                    fab.hide();
                    fab.setImageResource(R.drawable.pencil_16px);
                    fab.show();
                    fab.setOnClickListener(view -> launchActivity(CreateConversationPostActivity.class));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void launchActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            sendToStart();
        }
    }

    private void sendToStart() {
        launchActivityHelper.launchActivity(this, StartActivity.class, Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }
}