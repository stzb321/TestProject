package com.owenzhang.flowquery;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class ProcessActivity extends ActionBarActivity {

    public static Handler handler;
    public TextView detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        detail = (TextView)findViewById(R.id.detail);
        handler = new UpdateHandler(this);

    }

    public static class UpdateHandler extends Handler{
        private final WeakReference<ProcessActivity> mActivity;

        public UpdateHandler(ProcessActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case QueryManager.NOTIFY_STATE_CHANGE:
                    String str = QueryManager.getInstance().getDetailString();
                    ProcessActivity activity = mActivity.get();
                    activity.detail.setText(str);
                    break;
            }
        }
    }

    public static Handler getHandler(){
        return handler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_process, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
