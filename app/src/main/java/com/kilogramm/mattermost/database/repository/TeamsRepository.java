package com.kilogramm.mattermost.database.repository;

import android.content.ContentValues;

import com.kilogramm.mattermost.MattermostApp;
import com.kilogramm.mattermost.database.DBHelper;
import com.kilogramm.mattermost.database.MattermostContentProvider;
import com.kilogramm.mattermost.model.entity.team.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kepar on 06.02.17.
 */

public class TeamsRepository {

    public static final String TABLE_NAME_TEAMS = "Teams";

    public static final String FIELD_NAME = "name";

    public static void addTeam(List<Team> teams) {
        List<ContentValues> list = new ArrayList<>();
        for (Team team : teams) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.FIELD_COMMON_ID, team.getId());
            values.put(FIELD_NAME, team.getDisplayName());
            list.add(values);
        }
        ContentValues[] contentValues = new ContentValues[teams.size()];
        list.toArray(contentValues);
        MattermostApp.getSingleton()
                .getApplicationContext()
                .getContentResolver()
                .bulkInsert(MattermostContentProvider.CONTENT_URI_TEAMS, contentValues);
    }

}