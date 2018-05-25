package com.mamba.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.mamba.mambasdk.video.MVideoCacheWrapper;

/**
 * The type Real time play demo activity.
 */
public class VideoDemoActivity extends com.mamba.mambasdk.ui.BaseActivity {


    private VideoView videoView;

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected boolean isApplyButterKnife() {
        return false;
    }

    @Override
    protected int getInflateLayoutId() {
        return R.layout.layout_video_demo;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    @Override
    protected void getSavedBundleData(Bundle bundle) {

    }

    @Override
    protected void initViews() {
        RelativeLayout viewPlayVideo = findViewById(R.id.rl_play);
        viewPlayVideo.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        videoView = new VideoView(this);
        videoView.setLayoutParams(lp);
        String demoVideoPath = "http://f.us.sinaimg.cn/003hRvKNlx07kIODDD0Y01040201xOKX0k020.mp4?label=mp4_hd&template=852x480.28&Expires=1527230203&ssig=HfqOXIC5r7&KID=unistore,video";


        HttpProxyCacheServer proxy = MVideoCacheWrapper.getInstance().getProxy(this);
        videoView.setVideoURI(Uri.parse(proxy.getProxyUrl(demoVideoPath)));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        viewPlayVideo.addView(videoView);

    }

    @Override
    protected void bindEvents() {

    }

    @Override
    protected void initDatas() {

    }
    @Override
    public void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
