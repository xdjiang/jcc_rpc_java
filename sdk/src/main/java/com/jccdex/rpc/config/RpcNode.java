package com.jccdex.rpc.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;

public class ExplorerNode extends AServerNode {

    /**
     *
     * @param expNodes 浏览器服务地址
     */
    public ExplorerNode(ArrayList<String> expNodes)
    {
         super(expNodes);
    }
}
