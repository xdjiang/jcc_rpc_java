//package com.jccdex.rpc.explorer;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.jccdex.core.utils.Utils;
//
//public class MemoItem {
//
//    @JsonProperty(value = "MemoData")
//    private String memoData;
//    @JsonProperty(value = "MemoType")
//    private String memoType;
//
//    public String getMemoData() {
//        return memoData;
//    }
//
//    public String getMemoType() {
//        return memoType;
//    }
//
//    @Override
//    public String toString() {
//        if (memoData == null) {
//            return "";
//        }
//        return Utils.hexStrToStr(memoData);
//    }
//
//}
