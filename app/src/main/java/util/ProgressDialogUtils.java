package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.usecar.uescar.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ProgressDialogUtils extends AlertDialog {
    public static List<AlertDialog> alertdialogs = new ArrayList<AlertDialog>();
    private static String TAG = AlertDialog.class.getName();
    private RotateLoading rotateLoading;
    public ProgressDialogUtils(Context context) {
        super(context);
        show();

        getWindow().setContentView(R.layout.progress);

        //还要解决网络超时问题
        loadingNetworkTimeout();

        alertdialogs.add(this);
    }

    public void dismiss() {
        Context context = getContext();
        if (context != null && (context instanceof Activity) && ((Activity) context).isFinishing()) {
            return;
        } else {
            rotateLoading.stop();
            super.dismiss();
            return;
        }
    }



    public void show() {
        Object obj = getContext();
        if (obj != null && (!(obj instanceof Activity) || !((Activity) obj).isFinishing())) {
            try {
                super.show();
                return;
            } catch (Exception exception) {
                String str;
                if (exception != null)
                    str = String.valueOf(exception).concat(" is finishing");
                else
                    str = "context is null";

                return;
            }

        }
    }

    /**
     * 网络超时解决
     */
    private long mExitTime;
    public void loadingNetworkTimeout() {
        if ((System.currentTimeMillis() - mExitTime) > 10000) {
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
            rotateLoading.start();
            mExitTime = System.currentTimeMillis();
        } else {
            dismiss();
        }

    }

    /**
     * 无网络状态时候加载过程
     */
    public void loadingNoNetWork() {
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        rotateLoading.start();
        timer.schedule(task, 1000, 1000);

    }

    /**
     * 倒计时
     */
    private int recLen = 11;
    private Timer timer = new Timer();
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    if (recLen < 0) {
                        timer.cancel();
                        dismiss();
                        Toast.makeText(getContext(),"网络无连接，请检查网络后再试~",Toast.LENGTH_SHORT).show();
                        //ToastUtils.toastShort(getContext(), "网络无连接，请检查网络后再试~");
                        recLen = 11;
                    }
            }
        }
    };

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            recLen--;
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    public static void closeProgressAlert() {

        for (AlertDialog alertdialog : alertdialogs) {

            alertdialog.dismiss();

        }

    }

}
