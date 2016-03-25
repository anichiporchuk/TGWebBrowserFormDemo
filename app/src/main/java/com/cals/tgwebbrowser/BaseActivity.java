package com.cals.tgwebbrowser;

        import android.app.Activity;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Bundle;

/************************Вспомогательный класс для убийства всех activity**********************/
public class BaseActivity extends Activity {
    private KillReceiver mKillReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKillReceiver = new KillReceiver();
        registerReceiver(mKillReceiver,
                IntentFilter.create("context://closeall", "context://closeallact"));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mKillReceiver);
    }
    private final class KillReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}