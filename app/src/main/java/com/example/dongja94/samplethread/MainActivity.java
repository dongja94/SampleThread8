package com.example.dongja94.samplethread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView messageView;
    ProgressBar progressDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        messageView = (TextView)findViewById(R.id.text_message);
        progressDownload = (ProgressBar)findViewById(R.id.progress_download);

        progressDownload.setMax(100);

        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });

        btn = (Button)findViewById(R.id.btn_runnable);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRunnable();
            }
        });
    }

    public static final int MESSAGE_PROGRESS = 1;
    public static final int MESSAGE_DONE = 2;

    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PROGRESS :
                    int progress = msg.arg1;
                    messageView.setText("progress : " + progress);
                    progressDownload.setProgress(progress);
                    break;
                case MESSAGE_DONE :
                    messageView.setText("progress done");
                    break;
            }
        }
    };

    private void startDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while(count <= 100) {
                    Message msg = mHandler.obtainMessage(MESSAGE_PROGRESS, count, 0);
                    mHandler.sendMessage(msg);
                    count+= 5;
//                    messageView.setText("download : " + count);
//                    progressDownload.setProgress(count);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                messageView.setText("progress done");
                mHandler.sendEmptyMessage(MESSAGE_DONE);
            }
        }).start();
    }

    private void startRunnable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while(count <= 100) {
                    mHandler.post(new ProgressRunnable(count));
                    count+= 5;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mHandler.post(new DoneRunnable());
            }
        }).start();
    }

    class ProgressRunnable implements Runnable {
        int progress;
        public ProgressRunnable(int progress) {
            this.progress = progress;
        }

        @Override
        public void run() {
            messageView.setText("progress : " + progress);
            progressDownload.setProgress(progress);
        }
    }

    class DoneRunnable implements Runnable {
        @Override
        public void run() {
            messageView.setText("progress done");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
