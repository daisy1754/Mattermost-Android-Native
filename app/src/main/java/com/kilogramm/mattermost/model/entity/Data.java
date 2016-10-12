package com.kilogramm.mattermost.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kilogramm.mattermost.model.entity.post.Post;

import java.util.Map;

/**
 * Created by Evgeny on 19.09.2016.
 */
public class Data implements Parcelable {
    @SerializedName("channel_display_name")
    @Expose
    private String channelDisplayName;
    @SerializedName("channel_type")
    @Expose
    private String channelType;
    @SerializedName("mentions")
    @Expose
    private String mentions;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("status")
    @Expose
    private String status;

    private Map<String, String> statusMap;

    public Data(String channelDisplayName,
                 String channelType,
                 String mentions,
                 Post post,
                 String senderName,
                 String teamId,
                 String status,
                Map<String, String> statusMap) {
        this.channelDisplayName = channelDisplayName;
        this.channelType = channelType;
        this.mentions = mentions;
        this.post = post;
        this.senderName = senderName;
        this.teamId = teamId;
        this.status = status;
        this.statusMap = statusMap;
    }

    public Map<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    public String getChannelDisplayName() {
        return channelDisplayName;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getMentions() {
        return mentions;
    }

    public Post getPost() {
        return post;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelDisplayName);
        dest.writeString(this.channelType);
        dest.writeString(this.mentions);
        dest.writeParcelable(this.post, flags);
        dest.writeString(this.senderName);
        dest.writeString(this.parentId);
        dest.writeString(this.state);
        dest.writeString(this.teamId);
        dest.writeString(this.status);
    }

    protected Data(Parcel in) {
        this.channelDisplayName = in.readString();
        this.channelType = in.readString();
        this.mentions = in.readString();
        this.post = in.readParcelable(Post.class.getClassLoader());
        this.senderName = in.readString();
        this.parentId = in.readString();
        this.state = in.readString();
        this.teamId = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}