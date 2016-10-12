package com.kilogramm.mattermost.view.fragments;

import android.view.View;

import com.android.annotations.Nullable;
import com.kilogramm.mattermost.view.BaseActivity;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusFragment;

/**
 * Created by Evgeny on 19.08.2016.
 */
public abstract class BaseFragment<P extends Presenter> extends NucleusFragment<P> {

    protected void setupToolbar(String activityTitle,String channelName, View.OnClickListener listener1, View.OnClickListener listener2){
        ((BaseActivity) getActivity()).setupChannelToolbar(activityTitle,channelName,listener1,listener2);
    }
}
