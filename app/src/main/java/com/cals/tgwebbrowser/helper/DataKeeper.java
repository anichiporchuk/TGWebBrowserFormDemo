package com.cals.tgwebbrowser.helper;

        import java.io.File;
        import java.util.ArrayList;

        import org.w3c.dom.Node;
        import android.content.Intent;
        import android.os.Bundle;

public class DataKeeper {
    private static volatile DataKeeper instance;
    private ArrayList<Node> groups;
    private ArrayList<Node> pms;
    private String args;
    Bundle web;
    boolean FlagShow;
    private String caption;
    private File Path;
    private String cxref;
    private String textLight;
    private int Response;
    private String app;
    private ArrayList<String> spin;
    private int pos;


    public static DataKeeper getInstance() {
        DataKeeper localInstance = instance;
        if (localInstance == null) {
            synchronized (DataKeeper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataKeeper();
                }
            }
        }
        return localInstance;
    }


    public void setPath(File a) {
        Path = a;
    }
    public File getPath() {
        return Path;
    }


}
