package com.dovar.dplayer;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.bean.VideoListBean;
import com.dovar.dplayer.common.adapter.RCommonAdapter;
import com.dovar.dplayer.common.adapter.RCommonViewHolder;
import com.dovar.dplayer.common.base.StatusBarTintActivity;
import com.dovar.dplayer.common.utils.ToastUtil;
import com.dovar.dplayer.module.music.ui.fragment.LocalMusicFragment;
import com.dovar.dplayer.module.music.ui.fragment.MusicFragment;
import com.dovar.dplayer.module.music.ui.fragment.MusicListFragment;
import com.dovar.dplayer.module.video.ui.VideoListActivity;
import com.lantouzi.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends StatusBarTintActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeContract.IView, MusicFragment.Callback {

    private ImageView navImageView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initUI();
        initData();

    }

    private void setupDrawerHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navImageView = (ImageView) header.findViewById(R.id.nav_image_view);
        if (navImageView != null) {
            navImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify mSurface parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSleepDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_playlists:
                MusicListFragment mMusicListFragment = MusicListFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragContainer, mMusicListFragment, "musicList");
                ft.addToBackStack("musicList");
                ft.commit();
                break;
            case R.id.nav_local:
                LocalMusicFragment mLocalMusicFragment = LocalMusicFragment.newInstance();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.fragContainer, mLocalMusicFragment, "localMusicList");
                ft2.addToBackStack("localMusicList");
                ft2.commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void invalidateDrawerImg() {
//        try {
//            Bitmap croppedBmp = Bitmap.createBitmap(playerFragment.mVisualizerView.bmp, 0, (int) (75 * ratio), screen_width, screen_width);
//            navImageView.setImageBitmap(croppedBmp);
//        } catch (Exception | OutOfMemoryError dropVideoFrames) {
//            dropVideoFrames.printStackTrace();
//        }
    }

    boolean isSleepTimerEnabled = false;
    boolean isSleepTimerTimeout = false;
    long timerSetTime = 0;
    int timerTimeOutDuration = 0;
    Handler sleepHandler;

    public void showSleepDialog() {
        List<String> minuteList = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            minuteList.add(String.valueOf(i * 5));
        }

        sleepHandler = new Handler();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sleep_timer_dialog);

        final WheelView wheelPicker = (WheelView) dialog.findViewById(R.id.wheelPicker);
        wheelPicker.setItems(minuteList);

        Button setBtn = (Button) dialog.findViewById(R.id.set_button);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);
        final Button removerBtn = (Button) dialog.findViewById(R.id.remove_timer_button);

        final LinearLayout buttonWrapper = (LinearLayout) dialog.findViewById(R.id.button_wrapper);

        final TextView timerSetText = (TextView) dialog.findViewById(R.id.timer_set_text);

        setBtn.setBackgroundResource(R.color.b24242);
        removerBtn.setBackgroundResource(R.color.b24242);
        cancelBtn.setBackgroundColor(Color.WHITE);

        if (isSleepTimerEnabled) {
            wheelPicker.setVisibility(View.GONE);
            buttonWrapper.setVisibility(View.GONE);
            removerBtn.setVisibility(View.VISIBLE);
            timerSetText.setVisibility(View.VISIBLE);

            long currentTime = System.currentTimeMillis();
            long difference = currentTime - timerSetTime;

            int minutesLeft = (int) (timerTimeOutDuration - ((difference / 1000) / 60));
            if (minutesLeft > 1) {
                timerSetText.setText("Timer set for " + minutesLeft + " minutes from now.");
            } else if (minutesLeft == 1) {
                timerSetText.setText("Timer set for " + 1 + " minute from now.");
            } else {
                timerSetText.setText("Music will init after completion of current song");
            }

        } else {
            wheelPicker.setVisibility(View.VISIBLE);
            buttonWrapper.setVisibility(View.VISIBLE);
            removerBtn.setVisibility(View.GONE);
            timerSetText.setVisibility(View.GONE);
        }

        removerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSleepTimerEnabled = false;
                isSleepTimerTimeout = false;
                timerTimeOutDuration = 0;
                timerSetTime = 0;
                sleepHandler.removeCallbacksAndMessages(null);
                Toast.makeText(getApplicationContext(), "Timer removed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSleepTimerEnabled = true;
                int minutes = Integer.parseInt(wheelPicker.getItems().get(wheelPicker.getSelectedPosition()));
                timerTimeOutDuration = minutes;
                timerSetTime = System.currentTimeMillis();
                sleepHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isSleepTimerTimeout = true;
//                        if (playerFragment.mMediaPlayer == null || !playerFragment.mMediaPlayer.isPlaying()) {
//                            main.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(ctx, "Sleep timer timed out, closing app", Toast.LENGTH_SHORT).show();
//                                    if (playerFragment != null && playerFragment.timer != null)
//                                        playerFragment.timer.cancel();
//                                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                    try {
//                                        notificationManager.cancel(1);
//                                    } catch (Exception dropVideoFrames) {
//                                        dropVideoFrames.printStackTrace();
//                                    } finally {
//                                        finish();
//                                    }
//                                }
//                            });
//                        }
                    }
                }, minutes * 60 * 1000);
                Toast.makeText(getApplicationContext(), "Timer set for " + minutes + " minutes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSleepTimerEnabled = false;
                isSleepTimerTimeout = false;
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("多媒体");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMusicPlayer(0, null);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerHeader();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupMusicList();
        setupVideoList();
    }

    private HomePresenter mPresenter;

    @Override
    protected void initData() {
        mPresenter = new HomePresenter(this);
        mPresenter.getMusicList();
        mPresenter.getVideoList();
    }

    @BindView(R.id.rv_musicList)
    RecyclerView rv_musicList;
    @BindView(R.id.rv_videoList)
    RecyclerView rv_videoList;

    private RCommonAdapter<Music> adapter_music;
    private RCommonAdapter<VideoListBean.IssueListBean.ItemListBean> adapter_video;

    private void setupMusicList() {
        rv_musicList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter_music = new RCommonAdapter<Music>(this, R.layout.item_card_layout2) {
            @Override
            public void convert(RCommonViewHolder vh, int position) {
                Music bean = getItem(position);
                if (bean == null) return;
                vh.setText(R.id.tv_title, bean.getName());
                vh.setText(R.id.tv_artist, bean.getSinger());
                vh.setImageUrl(R.id.iv_cover, bean.getPic_small());
            }
        };
        adapter_music.setOnItemClickListener(new RCommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showMusicPlayer(position, (ArrayList<Music>) adapter_music.getDatas());
            }
        });
        rv_musicList.setAdapter(adapter_music);
    }

    private void setupVideoList() {
        rv_videoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter_video = new RCommonAdapter<VideoListBean.IssueListBean.ItemListBean>(this, R.layout.item_card_layout2) {
            @Override
            public void convert(RCommonViewHolder vh, int position) {
                VideoListBean.IssueListBean.ItemListBean bean = getItem(position);
                if (bean == null) return;
                vh.setText(R.id.tv_title, bean.getData().getTitle());
                vh.setText(R.id.tv_artist, bean.getData().getDescription());
                vh.setImageUrl(R.id.iv_cover, bean.getData().getCover().getDetail());
            }
        };
        rv_videoList.setAdapter(adapter_video);
    }

    @Override
    public void getMusicListSuccess(List<Music> mMusicList, boolean isLoadMore) {
        adapter_music.addDatas(mMusicList, !isLoadMore);
    }

    @Override
    public void getMusicListFail(boolean isLoadMore) {
        ToastUtil.show("获取歌曲失败");
    }

    @Override
    public void getVideoListSuccess(List<VideoListBean.IssueListBean.ItemListBean> mVideoList, boolean isLoadMore) {
        adapter_video.addDatas(mVideoList, !isLoadMore);
    }

    @Override
    public void getVideoListFail(boolean isLoadMore) {
        ToastUtil.show("获取视频失败");
    }

    public void showMusicPlayer(int index, ArrayList<Music> mMusicList) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment musicFragment = fm.findFragmentByTag("musicPlayer");
        if (musicFragment == null) {
            musicFragment = MusicFragment.newInstance(index, mMusicList);
        } else {
            if (musicFragment instanceof MusicFragment) {
                ((MusicFragment) musicFragment).updateMusics(index, mMusicList);
            }
        }
        ft.replace(R.id.fragContainer, musicFragment, "musicPlayer");
        ft.addToBackStack("musicPlayer");
        ft.commit();
    }

    @Override
    public void onPlayerShowOrHide(boolean isShow) {
        if (fab == null) return;
        if (isShow) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_video)
    void videoViewAll(View v) {
        VideoListActivity.jump(MainActivity.this);
    }

    @OnClick(R.id.tv_music)
    void musicViewAll(View v) {
        MusicListFragment mMusicListFragment = MusicListFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragContainer, mMusicListFragment, "musicList");
        ft.addToBackStack("musicList");
        ft.commit();
    }
}
