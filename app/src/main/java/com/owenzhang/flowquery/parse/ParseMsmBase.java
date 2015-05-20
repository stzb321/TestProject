package com.owenzhang.flowquery.parse;

/**
 * Created by OwenZhang on 2015/5/19.
 */
public abstract class ParseMsmBase{

    private IOnParseFinishListener listener;

    public abstract void parse(String[] message);
    ParseMsmBase(IOnParseFinishListener listener){
        this.listener = listener;
    }
}
