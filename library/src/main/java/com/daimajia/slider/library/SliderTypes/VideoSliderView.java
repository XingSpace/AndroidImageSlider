package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.VideoView;

import com.daimajia.slider.library.R;

import java.io.File;
import java.io.FileNotFoundException;

public class VideoSliderView extends BaseSliderView {

    private File mFile;
    private Uri uri;

    public VideoSliderView(Context context) {
        super(context);
    }

    public VideoSliderView video(String uri) {
        if (uri == null || uri.trim().length() == 0)
            throw new NullPointerException("VideoSliderView video url is null!!!");
        this.uri = Uri.parse(uri);
        return this;
    }

    public VideoSliderView video(File file) {
        if (!file.exists()) {
            try {
                throw new FileNotFoundException("VideoSliderView video file can not found!!!");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.mFile = file;
        return this;
    }

    protected void bindEventAndShow(final View root, VideoView videoView) {
        final BaseSliderView me = this;

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSliderClickListener != null){
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        if (videoView == null)
            return;

        if (mFile != null) {
            videoView.setVideoPath(mFile.getAbsolutePath());
        } else if (uri != null) {
            videoView.setVideoURI(uri);
        }
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (root.findViewById(R.id.loading_bar) != null) {
                    root.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
                mp.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("VideoSliderView","onError what->"+what+" extra->"+extra);
                return false;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//            mp.release();
                mp.seekTo(0);
                mp.start();
            }
        });

    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_video,null);
        VideoView target = (VideoView) v.findViewById(R.id.video);
        bindEventAndShow(v, target);
        return v;
    }
}
