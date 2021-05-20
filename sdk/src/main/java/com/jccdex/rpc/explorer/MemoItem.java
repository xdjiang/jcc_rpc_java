package com.jccdex.rpc.explorer.memo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemoItem {

    @JsonProperty(value = "MemoData")
    private String memoData;
    @JsonProperty(value = "MemoType")
    private String memoType;

    public String getMemoData() {
        return memoData;
    }

    public String getMemoType() {
        return memoType;
    }

    @Override
    public String toString() {
        if (memoData == null) {
            return "";
        }
        return Utils.hexStrToStr(memoData);
    }

}
