package com.kilogramm.mattermost;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.kilogramm.mattermost.model.entity.InitObject;
import com.kilogramm.mattermost.model.entity.LicenseCfg;
import com.kilogramm.mattermost.model.entity.Preference.Preferences;
import com.kilogramm.mattermost.model.entity.RealmString;
import com.kilogramm.mattermost.model.entity.ThemeProps;
import com.kilogramm.mattermost.model.entity.channel.Channel;
import com.kilogramm.mattermost.model.entity.notifyProps.NotifyProps;
import com.kilogramm.mattermost.model.entity.post.Post;
import com.kilogramm.mattermost.model.entity.team.Team;
import com.kilogramm.mattermost.model.entity.user.User;
import com.kilogramm.mattermost.model.fromnet.LogoutData;
import com.kilogramm.mattermost.network.ApiMethod;
import com.kilogramm.mattermost.network.MattermostRetrofitService;
import com.kilogramm.mattermost.network.PicassoService;
import com.kilogramm.mattermost.network.ServerMethod;
import com.kilogramm.mattermost.rxtest.MainRxActivity;
import com.kilogramm.mattermost.tools.FileUtil;
import com.kilogramm.mattermost.ui.FilesView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.log.AndroidLogger;
import io.realm.log.RealmLog;
import rx.schedulers.Schedulers;

/**
 * Created by Evgeny on 25.07.2016.
 */
public class MattermostApp extends MultiDexApplication {

    public static final String URL_WEB_SOCKET = "wss://mattermost.kilograpp.com/api/v3/users/websocket";

    private static MattermostApp singleton = null;

    private ApiMethod mattermostRetrofitService;

    public static MattermostApp get(Context context) {
        return (MattermostApp) context.getApplicationContext();
    }

    public static MattermostApp getSingleton() {
        return singleton;
    }

    public void refreshMattermostRetrofitService() throws IllegalArgumentException {
        mattermostRetrofitService = MattermostRetrofitService.create();
        ServerMethod.buildServerMethod(mattermostRetrofitService);
    }

    public ApiMethod getMattermostRetrofitService() throws IllegalArgumentException {
        if (mattermostRetrofitService == null) {
            mattermostRetrofitService = MattermostRetrofitService.create();
        }
        return mattermostRetrofitService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        singleton = this;
        FileUtil.createInstance(getApplicationContext());

        PicassoService.create(getApplicationContext());
        MattermostPreference.createInstance(getApplicationContext());
        setupRealm();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .build());


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheExtraOptions(300, 300, bitmap -> null)
                .memoryCacheExtraOptions(300, 300)
                .memoryCacheSize(1024 * 1024 * 20)
                .imageDownloader(new FilesView.AuthDownloader(this))
                .build();
        ImageLoader.getInstance().init(config);
        ServerMethod.buildServerMethod(getMattermostRetrofitService());
        disableSSLCertificateChecking();
    }

    private void setupRealm(){
        // Realm.init(getApplicationContext());
        Realm.init(getApplicationContext());
        RealmLog.add(new AndroidLogger(Log.WARN));
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("mattermostDb.realm")
                .migration((realm, oldVersion, newVersion) -> realm.deleteAll())
                .build();
        Realm.compactRealm(configuration);
        Realm.removeDefaultConfiguration();
        Realm.setDefaultConfiguration(configuration);
    }

    public static rx.Observable<LogoutData> logout() {
        return MattermostApp.getSingleton()
                .getMattermostRetrofitService()
                .logout(new Object())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static void clearPreference() {
        MattermostPreference.getInstance().setAuthToken(null);
        MattermostPreference.getInstance().setLastChannelId(null);
    }

    public static void clearDataBaseAfterLogout() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            realm1.delete(Post.class);
            realm1.delete(Channel.class);
            realm1.delete(InitObject.class);
            realm1.delete(Preferences.class);
            realm1.delete(LicenseCfg.class);
            realm1.delete(NotifyProps.class);
            realm1.delete(RealmString.class);
            realm1.delete(Team.class);
            realm1.delete(InitObject.class);
            realm1.delete(ThemeProps.class);
            realm1.delete(User.class);
        });
    }

    /**
     * Disables the SSL certificate checking for new instances of {@link HttpsURLConnection}
     * This has been created to aid testing on a local box, not for use on production.
     */
    private void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }
        };

        try {
            HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void showMainRxActivity() {
        MainRxActivity.start(MattermostApp.getSingleton().getApplicationContext(),
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
