package com.semihbg.gorun.setting;

import com.semihbg.gorun.core.AppContext;
import com.semihbg.gorun.util.StateUtils;

import java.util.function.Consumer;

public class AppSetting {

    public static final AppSetting instance;

    static {
        instance = new AppSetting();
    }

    public final AppState appState;

    private AppSetting() {
        this.appState = new AppState();
    }

    public void updateAllState() {
        updateInternetConnection();
        updateServerState();
    }

    public void updateInternetConnection() {
        AppContext.instance().listenedThreadPoolWrapper.listenedExecute(StateUtils::hasInternetConnection, appState::setInternetConnection);
    }

    public void updateInternetConnection(Consumer<? super Boolean> callback) {
        AppContext.instance().listenedThreadPoolWrapper.listenedExecute(StateUtils::hasInternetConnection,
                (Boolean internetConnection) -> {
                    appState.setInternetConnection(internetConnection);
                    callback.accept(internetConnection);
                });
    }

    public void updateServerState() {
        AppContext.instance().listenedThreadPoolWrapper.listenedExecute(StateUtils::serverStateType, appState::setServerStateType);
    }

    public void updateServerState(Consumer<? super ServerStateType> callback) {
        AppContext.instance().listenedThreadPoolWrapper.listenedExecute(StateUtils::serverStateType,
                (ServerStateType serverStateType) -> {
                    appState.setServerStateType(serverStateType);
                    callback.accept(serverStateType);
                });
    }





}
