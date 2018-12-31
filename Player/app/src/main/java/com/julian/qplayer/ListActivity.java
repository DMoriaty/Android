package com.julian.qplayer;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    public static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
//    public static final String TAG = "ListActivity";
//    public static MediaPlayer mPlayer;
//    private MusicService mMusicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("曲库");
        init();
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SongListFragment());
        fragmentList.add(new SingerGridFragment());
        fragmentList.add(new AlbumListFragment());
//        fragmentList.add(new AlbumGridFragment());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Tools.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, this, REQUEST_READ_EXTERNAL_STORAGE);
//        }
//        MusicDB musicDB = MusicDB.getInstance(getApplicationContext());
//        bindMusicService();
        initWidget();
    }


    private void initWidget() {
        //桌面小插件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicWidget.CHANGE_STATE);
        intentFilter.addAction(MusicWidget.LAST_SONG);
        intentFilter.addAction(MusicWidget.NEXT_SONG);
//        registerReceiver(mReceiver, intentFilter);
    }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_songs) {
            // Handle the camera action
        } else if (id == R.id.nav_playlist) {

        } else if (id == R.id.nav_folder) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
