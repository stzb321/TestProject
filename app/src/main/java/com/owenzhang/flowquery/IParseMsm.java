package com.owenzhang.flowquery;

/**
 * Created by OwenZhang on 2015/5/19.
 */
public interface IParseMsm {
    void parse(String[] message);

    void onParseFinish();
}
