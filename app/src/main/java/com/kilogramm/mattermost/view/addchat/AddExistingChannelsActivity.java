package com.kilogramm.mattermost.view.addchat;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.kilogramm.mattermost.R;
import com.kilogramm.mattermost.databinding.ActivityAllChatsBinding;
import com.kilogramm.mattermost.model.entity.channel.ChannelsDontBelong;
import com.kilogramm.mattermost.presenter.AddExistingChannelsPresenter;
import com.kilogramm.mattermost.view.BaseActivity;

import io.realm.Realm;
import io.realm.RealmResults;
import nucleus.factory.RequiresPresenter;

/**
 * Created by melkshake on 18.10.16.
 */

@RequiresPresenter(AddExistingChannelsPresenter.class)
public class AddExistingChannelsActivity
        extends BaseActivity<AddExistingChannelsPresenter>
        implements AddExistingChannelsAdapter.OnChannelItemClickListener {

    public static final String CHANNEL_ID = "channelId";
    public static final String TYPE = "type";
    public static final String CHANNEL_NAME = "channelName";

    private ActivityAllChatsBinding binding;
    private AddExistingChannelsAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_chats);
        init();
        setRecycleView();
    }

    private void setRecycleView() {
        RealmResults<ChannelsDontBelong> moreChannels = realm.where(ChannelsDontBelong.class).findAll();
        adapter = new AddExistingChannelsAdapter(this, moreChannels, true, this);
        binding.recViewMoreChannels.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        binding.recViewMoreChannels.setLayoutManager(manager);
    }

    private void init() {
        setupToolbar(getString(R.string.title_existing_channels), true);
        setColorScheme(R.color.colorPrimary, R.color.colorPrimaryDark);
        getPresenter().requestChannelsMore();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChannelItemClick(String joinChannelId, String channelName, String type) {
        Intent intent = new Intent(this, AddExistingChannelsActivity.class)
                .putExtra(CHANNEL_ID, joinChannelId)
                .putExtra(CHANNEL_NAME, channelName)
                .putExtra(TYPE, type);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}