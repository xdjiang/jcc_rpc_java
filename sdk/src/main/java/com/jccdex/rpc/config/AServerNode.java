package com.jccdex.rpc.config;

import java.util.ArrayList;

public abstract class AServerNode {
    /**
     * 服务器地址
     */
    public ArrayList<String> urls;

    public abstract String getUrls();
}
