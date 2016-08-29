package com.kilogramm.mattermost.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by Evgeny on 03.08.2016.
 */
public class Channel extends RealmObject {

    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String REFRESH = "refresh";
    public static final String AWAY = "away";


    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("create_at")
    @Expose
    private Long createAt;
    @SerializedName("update_at")
    @Expose
    private Long updateAt;
    @SerializedName("delete_at")
    @Expose
    private Long deleteAt;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("last_post_at")
    @Expose
    private Long lastPostAt;
    @SerializedName("total_msg_count")
    @Expose
    private Integer totalMsgCount;
    @SerializedName("extra_update_at")
    @Expose
    private Long extraUpdateAt;
    @SerializedName("creator_id")
    @Expose
    private String creatorId;
    @SerializedName("username")
    @Expose
    private String username;

    private String status = "offline";
    private Integer unreadedMessage = 0;


    public Integer getUnreadedMessage() {
        return unreadedMessage;
    }

    public void setUnreadedMessage(Integer unreadedMessage) {
        this.unreadedMessage = unreadedMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The createAt
     */
    public Long getCreateAt() {
        return createAt;
    }

    /**
     *
     * @param createAt
     * The create_at
     */
    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    /**
     *
     * @return
     * The updateAt
     */
    public Long getUpdateAt() {
        return updateAt;
    }

    /**
     *
     * @param updateAt
     * The update_at
     */
    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    /**
     *
     * @return
     * The deleteAt
     */
    public Long getDeleteAt() {
        return deleteAt;
    }

    /**
     *
     * @param deleteAt
     * The delete_at
     */
    public void setDeleteAt(Long deleteAt) {
        this.deleteAt = deleteAt;
    }

    /**
     *
     * @return
     * The teamId
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     *
     * @param teamId
     * The team_id
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     * The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The header
     */
    public String getHeader() {
        return header;
    }

    /**
     *
     * @param header
     * The header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     *
     * @return
     * The purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     *
     * @param purpose
     * The purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     *
     * @return
     * The lastPostAt
     */
    public Long getLastPostAt() {
        return lastPostAt;
    }

    /**
     *
     * @param lastPostAt
     * The last_post_at
     */
    public void setLastPostAt(Long lastPostAt) {
        this.lastPostAt = lastPostAt;
    }

    /**
     *
     * @return
     * The totalMsgCount
     */
    public Integer getTotalMsgCount() {
        return totalMsgCount;
    }

    /**
     *
     * @param totalMsgCount
     * The total_msg_count
     */
    public void setTotalMsgCount(Integer totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
    }

    /**
     *
     * @return
     * The extraUpdateAt
     */
    public Long getExtraUpdateAt() {
        return extraUpdateAt;
    }

    /**
     *
     * @param extraUpdateAt
     * The extra_update_at
     */
    public void setExtraUpdateAt(Long extraUpdateAt) {
        this.extraUpdateAt = extraUpdateAt;
    }

    /**
     *
     * @return
     * The creatorId
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     *
     * @param creatorId
     * The creator_id
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}