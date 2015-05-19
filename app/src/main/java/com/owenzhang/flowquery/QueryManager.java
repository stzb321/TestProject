package com.owenzhang.flowquery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by OwenZhang on 2015/5/18.
 */
public class QueryManager {

    private boolean isQueryIng = false;
    private boolean isSendingMsg = false;
    private boolean isSendSuccess = false;
    private boolean isWaitingMsg = false;
    private boolean isParsingMsg = false;
    private boolean isParsedMsg = false;
    private String queryNum;
    private String queryContent;
    private Context context;
    public final static int NOTIFY_STATE_CHANGE = 1;

    public QueryManager(){
        smsManager = SmsManager.getDefault();
    }

    private static class Singleton{
        private static QueryManager INSTANCE = new QueryManager();
    }

    public static QueryManager getInstance(){
        return Singleton.INSTANCE;
    }

    public boolean isQueryIng(){
        return isQueryIng;
    }

    //是否正在发送查询短信
    public boolean isSendingMsg(){
        return isSendingMsg;
    }

    //是否正在等待运营商回短信
    public boolean isWaitingMsg(){
        return isWaitingMsg;
    }

    //是否正在解析短信
    public boolean isParsingMsg(){
        return isParsingMsg;
    }

    //是否成功解析短信
    public boolean isParsedMsg(){
        return isParsedMsg;
    }

    public void saveQuerySetting(String num,String content){
        queryNum = num;
        queryContent = content;
    }

    public void saveContext(Context c){
        context = c;
    }

    public Context getContext(){
        return context;
    }


    public String getDetailString(){
        StringBuffer sb = new StringBuffer();
        if(isQueryIng()){
            if(isSendingMsg()){
                sb.append("正在发送查询短信\n");
            }
            if(isWaitingMsg()){
                sb.append("正在等待运营商发送短信\n");
            }
            if(isParsingMsg()){
                sb.append("正在解析短信\n");
            }
            if(isParsedMsg()){
                sb.append("解析短信成功\n");
            }else{
                sb.append("解析短信失败\n");
            }
        }
        return sb.toString();
    }


    public void startQuery(){
        if(!isQueryIng()){
            isQueryIng = true;
        }
        sendMessage(queryNum,queryContent);
    }

    //事件分发，通知界面更新
    public void dispatch(){
        ProcessActivity.getHandler().sendEmptyMessage(NOTIFY_STATE_CHANGE);
    }


    /*********************************发送短信相关  START****************************************/

    public String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private SmsManager smsManager;

    public void sendMessage(String num, final String content){
        isSendingMsg = true;
        smsManager.sendTextMessage(num,null,content,null,null);
        QueryManager.getInstance().getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        isSendingMsg = false;
                        isWaitingMsg = true;
                        isSendSuccess = true;
                        receiveMessage();
                        context.unregisterReceiver(this);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        isSendingMsg = false;
                        isSendSuccess = false;
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        isSendingMsg = false;
                        isSendSuccess = false;
                        break;
                    default:
                        isSendingMsg = false;
                        isSendSuccess = false;
                        break;
                }
                dispatch();
            }
        }, new IntentFilter(SENT_SMS_ACTION));
    }


    /*********************************发送短信相关 END**********************************/


    /*********************************接受短信相关 StART**********************************/
    public static final String RECEIVE_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private void receiveMessage(){
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                StringBuffer SMSAddress = new StringBuffer();
                StringBuffer SMSContent = new StringBuffer();
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    Object[] pdusObjects = (Object[]) bundle.get("pdus");
                    SmsMessage[] messages = new SmsMessage[pdusObjects.length];
                    for (int i = 0; i < pdusObjects.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObjects[i]);
                    }
                    for (SmsMessage message : messages) {
                        SMSAddress.append(message.getDisplayOriginatingAddress());
                        SMSContent.append(message.getDisplayMessageBody());
                        System.out.println("来信号码：" + SMSAddress + "\n短信内容：" + SMSContent);
                    }
                    //停止该广播继续传递
                    abortBroadcast();
                    context.unregisterReceiver(this);
                }
            }
        },new IntentFilter(RECEIVE_SMS_ACTION));
    }
    /*********************************接受短信相关 END**********************************/

}
