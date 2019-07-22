package com.example.androidstd;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ThreadActivity extends AppCompatActivity {

    private static final int UPDATE_TEXT=1;
    TextView text;
    Handler handler=new Handler(){
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_TEXT:
                    text.setText("改变了");
                    break;
                default:;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
       // initBtn();
        initBtn2();

    }

    private void initBtn2() {
        Button btn=(Button)findViewById(R.id.btn2_1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute();
            }
        });
    }

    private void initBtn() {
        Button btn=(Button)findViewById(R.id.btn2_1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        text=(TextView)findViewById(R.id.textcontent);
//                        tv.setText("changed");  子线程中无法更新UI
                        Message message=new Message();
                        message.what=UPDATE_TEXT;
                        handler.sendMessage(message );
                    }
                }).start();
            }
        });
    }

    class MyTask extends AsyncTask<Void,Integer,String> {

        static final String TAG="TAG";
        @Override
        protected void onPreExecute() {
            Toast.makeText(ThreadActivity.this, "begin", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            int i=0;
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
                if(i++>100){
                    break;
                }
            }
            return "1";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            text=(TextView)findViewById(R.id.textcontent);

            text.setText(values[0]+"");
        }

        @Override
        protected void onPostExecute(String Result) {
            if("1".equals(Result)){
                Toast.makeText(ThreadActivity.this, "success", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ThreadActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
