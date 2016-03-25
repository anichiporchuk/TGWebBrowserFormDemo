package com.cals.tgwebbrowser.helper;


/*******************************Класс для хранения данных, не меняющихся во время сессии************************************/
public class SessionKeeper {
    private static volatile SessionKeeper instance;
    private String Server;
    private String Cookie;

    public static SessionKeeper getInstance() {
        SessionKeeper localInstance = instance;
        if (localInstance == null) {
            synchronized (SessionKeeper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SessionKeeper();
                }
            }
        }
        return localInstance;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getCookie() {
        return Cookie;
    }

    public void setCookie(String cookie) {
        Cookie = cookie;
    }

}