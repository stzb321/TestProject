package com.owenzhang.flowquery.parse;

/**
 * Created by OwenZhang on 2015/5/20.
 */
public class LocalParser extends ParseMsmBase{
    @Override
    public void parse(String[] message) {
        StringBuffer sb = new StringBuffer();
        for(String s : message){
            sb.append(s);
        }

    }

    public LocalParser(IOnParseFinishListener listener) {
        super(listener);
    }
}
