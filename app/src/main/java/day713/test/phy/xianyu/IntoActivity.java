package day713.test.phy.xianyu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.PublicWay;

public class IntoActivity extends AppCompatActivity {
    private Message msg;
    final String TAG = getClass().getSimpleName();
    private boolean resn=true;
    private int time =3;

    @Bind(R.id.btn_timeGo)
    Button btnTimeGo;
    //收到消息更新倒计时
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int n = msg.what;
            if (n==1){
                btnTimeGo.setText("跳过\n"+time+"秒");
                if (time>0){
                   time--;
                    }else {
                    finish();
                    start();
                   }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        PublicWay.activityList.add(this);

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 5; i >= 0; i--) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msg = new Message();
                    msg.what = 1;
                    if (resn){
                    handler.sendMessage(msg);
                    }
                }

            }
        }).start();
    }
    //进入APP启动页
    private void start() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }
    @OnClick(R.id.btn_timeGo)
    public void onClick() {
        resn = false;
        time=0;
        msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);

    }
}
