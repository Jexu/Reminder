package com.tt.reminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tt.sharedutils.DeviceUtil;

public class MainActivity extends AppCompatActivity {

    private TextView mTextContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextContent = (TextView) findViewById(R.id.tv_content);
        mTextContent.setText(DeviceUtil.getDeviceId(this));
    }
}
