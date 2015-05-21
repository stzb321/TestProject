package com.owenzhang.flowquery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        okBtn = (Button)findViewById(R.id.okButton);
        SharedPreferences setting = this.getSharedPreferences("SETTING", MODE_PRIVATE);
        String num = setting.getString("sendNum", "");
        String content = setting.getString("sendContent","");
        QueryManager.getInstance().saveQuerySetting(num,content);
        QueryManager.getInstance().saveContext(this);
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
            View view = this.getLayoutInflater().inflate(R.layout.sample_setting_dialog, null);
            final SharedPreferences setting = this.getSharedPreferences("SETTING", MODE_PRIVATE);
            final EditText sendNum = (EditText)findViewById(R.id.sendNum);
            final EditText sendContent = (EditText)findViewById(R.id.sendContent);
            sendNum.setText(setting.getString("sendNum",""));
            sendContent.setText(setting.getString("sendContent",""));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("流量查询").setView(view).setPositiveButton("确定",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences.Editor editor = setting.edit();
                    String num = sendNum.getText().toString();
                    String content = sendContent.getText().toString();
                    editor.putString("sendNum",num);
                    editor.putString("sendContent",content);
                    editor.apply();
                    QueryManager.getInstance().saveQuerySetting(num,content);
                }
            }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okButton:

                break;
        }
    }
}
