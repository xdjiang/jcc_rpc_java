package com.jccdex.rpc.token;

/**
 * 链通证数据对象
 * @author xdjiang
 */
public class Token {
    /**
     * 通证名称
     */
    private String name;

    /**
     * 发行银关
     */
    private String issuer;

    /**
     * 是否是链的记账通证
     */
    private boolean isNative;

    /**
     * 构造函数
     * @param name 通证名称
     * @param issuer 发行银关
     */
    public Token(String name, String issuer) {
        this.name = name.toUpperCase();
        this.issuer = issuer;
        this.isNative = false;
    }

    /**
     * 构造函数
     * @param name 通证名称
     */
    public Token(String name) {
        this.name = name;
        this.issuer = null;
        this.isNative = true;
    }

    /**
     * 获取通证名称
     * @return 通证名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取通证发行银关
     * @return 通证发行银关
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * 是否是记账通证标志
     * @return 记账通证标志
     */
    public boolean isNative() {
        return isNative;
    }

    @Override
    public String toString() {
        if (isNative) {
            return this.name.toLowerCase();
        }
        return this.name.toLowerCase() + "." + this.issuer;
    }

}
