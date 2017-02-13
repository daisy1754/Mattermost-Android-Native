package com.kilogramm.mattermost.rxtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kilogramm.mattermost.MattermostApp;
import com.kilogramm.mattermost.R;
import com.kilogramm.mattermost.model.error.HttpError;

import java.io.IOException;
import java.net.UnknownHostException;

import icepick.Icepick;
import nucleus.presenter.RxPresenter;
import nucleus.presenter.delivery.Delivery;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Evgeny on 03.10.2016.
 */
public class BaseRxPresenter<ViewType> extends RxPresenter<ViewType> {
    public static final String CREATE_CHANNEL = "CREATE_CHANNEL";
    public static final String CHANNELS_MORE = "CHANNELS_MORE";
    public static final String LOGIN = "LOGIN";
    public static final String SAVE_PREFERENCES = "SAVE_PREFERENCES";
    public static final String UPLOAD_A_FILE = "UPLOAD_A_FILE";

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }

    protected <T> Observable<Delivery<ViewType, T>> createTemplateObservable(T obj) {
        return Observable.just(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(deliverFirst());
    }

    public String getError(Throwable e) {
        if (e instanceof HttpException) {
            try {
                HttpError error = new Gson().fromJson(((HttpException) e).response()
                        .errorBody()
                        .string(), HttpError.class);

                if (error != null && error.getStatusCode() != null
                        && error.getStatusCode() == 500) {
                    return "Internal server error, please try later";
                }

                return (error != null) ?
                        (error.getMessage() != null) ? error.getMessage() : error.getError() :
                        e.getMessage();
            } catch (IOException e1) {
                return e.getMessage();
            }
        } else if (e instanceof UnknownHostException) {
            return "Couldn't find existing team matching this URL";
        } else {
            return e.getMessage();
        }
    }

    /**
     * This method just call {@link #parseError(Throwable, String)} with null default message.
     * In such case will be shown message from the exception
     *
     * @param e error from the server. {@link HttpException} expected
     * @return error message
     */
    public String parseError(Throwable e) {
        return parseError(e, null);
    }

    /**
     * This method parses error from the server. At first we try to parse Json body from the response
     * {@link #parseServerError(HttpError)}. If there is now Json body or its structure was
     * modified, throw an exception and show default message, passed into the method.
     * If default message wasn't be passed, show error message directly from the exception (can be
     * replaced by {@link R.string.error_unknown_exception})
     *
     * @param e              error from the server. {@link HttpException} expected
     * @param defaultMessage message that will be shown if {@link HttpError} can't be parsed
     * @return error message
     */
    public String parseError(Throwable e, String defaultMessage) {
        try {
            return parseServerError(getErrorFromResponse(e));
        } catch (IOException | JsonSyntaxException e1) {
            e1.printStackTrace();
            if (!isNetworkAvailable()) {
                return MattermostApp.getSingleton().getString(R.string.error_network_connection);
            } else if (defaultMessage != null) {
                return defaultMessage;
            } else {
                return e.getLocalizedMessage();
            }
        }
    }

    /**
     * Get {@link HttpError} object from the error response body. Excepted Json body
     *
     * @param e exception, thrown on the server response
     * @return {@link HttpError} object
     * @throws IOException
     * @throws JsonSyntaxException if response body wasn't Json
     */
    public HttpError getErrorFromResponse(Throwable e) throws IOException, JsonSyntaxException {
        if(e instanceof HttpException) {
            return new Gson().fromJson(((HttpException) e)
                    .response()
                    .errorBody()
                    .string(), HttpError.class);
        } else {
            throw new JsonSyntaxException(MattermostApp.getSingleton().getString(R.string.error_json_parse));
        }
    }

    public String parseServerError(HttpError httpError) {
        if(httpError != null && httpError.getMessage() != null) {
            return httpError.getMessage();
        } else {
            return MattermostApp.getSingleton().getString(R.string.error_unknown_exception);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) MattermostApp.getSingleton()
                        .getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        return !(ni == null || !ni.isConnectedOrConnecting());
    }
}
