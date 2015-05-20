package com.owenzhang.flowquery.parse;

/**
 * Created by OwenZhang on 2015/5/20.
 */
public interface IOnParseFinishListener {
    void onParseSuccess(FlowBean flowBean);

    void onParseFailed();
}
