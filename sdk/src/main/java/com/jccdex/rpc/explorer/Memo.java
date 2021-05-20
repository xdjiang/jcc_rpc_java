package com.jccdex.rpc.explorer.memo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Memo {

    @JsonProperty(value = "Memo")
    private MemoItem memo;

    public MemoItem getMemo() {
        return memo;
    }
}
