/**
 * Copyright 2020 javakam
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ando.player.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IGestureComponent;
import com.dueeeke.videoplayer.player.VideoView;

import java.util.Locale;

import ando.player.R;

import static com.dueeeke.videoplayer.util.PlayerUtils.stringForTime;

/**
 * 手势控制
 */
public class GestureView extends FrameLayout implements IGestureComponent {

    public GestureView(@NonNull Context context) {
        super(context);
    }

    public GestureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ControlWrapper mControlWrapper;

    private final ImageView mIcon;
    private final ProgressBar mProgressPercent;
    private final TextView mTextPercent;
    private final LinearLayout mCenterContainer;

    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.player_layout_gesture_control, this, true);
        mIcon = findViewById(R.id.iv_icon);
        mProgressPercent = findViewById(R.id.pro_percent);
        mTextPercent = findViewById(R.id.tv_percent);
        mCenterContainer = findViewById(R.id.center_container);
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
    }

    @Override
    public void onStartSlide() {
        mControlWrapper.hide();
        mCenterContainer.setVisibility(VISIBLE);
        mCenterContainer.setAlpha(1f);
    }

    @Override
    public void onStopSlide() {
        mCenterContainer.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mCenterContainer.setVisibility(GONE);
                    }
                })
                .start();
    }

    @Override
    public void onPositionChange(int slidePosition, int currentPosition, int duration) {
        mProgressPercent.setVisibility(GONE);
        if (slidePosition > currentPosition) {
            mIcon.setImageResource(R.drawable.ic_player_fast_forward);
        } else {
            mIcon.setImageResource(R.drawable.ic_player_fast_rewind);
        }
        mTextPercent.setText(String.format("%s/%s", stringForTime(slidePosition), stringForTime(duration)));
    }

    @Override
    public void onBrightnessChange(int percent) {
        mProgressPercent.setVisibility(VISIBLE);
        mIcon.setImageResource(R.drawable.ic_player_brightness);
        mTextPercent.setText(String.format(Locale.getDefault(), "%d%%", percent));
        mProgressPercent.setProgress(percent);
    }

    @Override
    public void onVolumeChange(int percent) {

        mProgressPercent.setVisibility(VISIBLE);
        if (percent <= 0) {
            mIcon.setImageResource(R.drawable.ic_player_volume_off);
        } else {
            mIcon.setImageResource(R.drawable.ic_player_volume_on);
        }
        mTextPercent.setText(String.format(Locale.getDefault(), "%d%%", percent));
        mProgressPercent.setProgress(percent);
    }

    @Override
    public void onPlayStateChanged(int playState) {
        if (playState == VideoView.STATE_IDLE
                || playState == VideoView.STATE_START_ABORT
                || playState == VideoView.STATE_PREPARING
                || playState == VideoView.STATE_PREPARED
                || playState == VideoView.STATE_ERROR
                || playState == VideoView.STATE_PLAYBACK_COMPLETED) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void setProgress(int duration, int position) {
    }

    @Override
    public void onLockStateChanged(boolean isLock) {
    }

}
