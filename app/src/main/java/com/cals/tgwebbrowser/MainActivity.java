package com.cals.tgwebbrowser;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.zip.ZipEntry;
        import java.util.zip.ZipFile;

        import android.app.ActionBar;
        import android.app.AlertDialog;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.SharedPreferences.Editor;
        import android.content.pm.ApplicationInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Environment;
        import android.preference.PreferenceManager;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemSelectedListener;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.cals.tgwebbrowser.helper.DataKeeper;
        import com.cals.tgwebbrowser.helper.LoginTask;
        import com.cals.tgwebbrowser.helper.ServerResponse;
        import com.cals.tgwebbrowser.helper.SessionKeeper;
        import com.cals.tgwebbrowser.serverlist.ServerItem;

/********************Класс вызывается при запуске программы**********************/
public class MainActivity extends BaseActivity {

    Button btn;
    TextView logField;
    TextView passField;
    Spinner ServSpin;
    final String LOG_TAG = "myLogs";
    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    protected String passText;
    protected String logText;
    protected ServerResponse tmpresponse = null;
    protected String passHashText;
    final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    final int DIALOG_EXIT = 1;
    private String log;

    private ArrayList<ServerItem> servers;
    protected String path = null;
    private ServerSpinnerAdapter adapter;
    protected String StrUrl;
    private Button btns;
    private ArrayList<String> Server1 = new ArrayList<String>();
    private ArrayList<String> Server2 = new ArrayList<String>();
    public static int flag_stop = 0; //флаг для остановки всех активити
    public static String Version = "2.0.0.0";
    public boolean authFlag = true;
    private DataKeeper Keeper;
    private DataKeeper Data;
    private SharedPreferences sp;
    protected SessionKeeper SKeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Keeper = DataKeeper.getInstance();
            Keeper.setPath(Environment.getExternalStorageDirectory());
        } else {
            Keeper = DataKeeper.getInstance();
            Keeper.setPath(Environment.getExternalStorageDirectory());
            Keeper.setPath(getExternalFilesDir(null));
        }
        flag_stop = 0;
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);//hide title
        //кнопка Подключиться
        btn = (Button) findViewById(R.id.do_it);
        btn.setOnClickListener(oclBtnOk);
        btn.setFocusableInTouchMode(false);

        //кнопка Редактор серверов
        btns = (Button) findViewById(R.id.button2);
        btns.setBackgroundColor(getResources().getColor(R.color.grey));
        btns.setEnabled(false);
        logField = (TextView) findViewById(R.id.logField); //поле Логин
        passField = (TextView) findViewById(R.id.passField); //поле Пароль
        ServSpin = (Spinner) findViewById(R.id.spinner_1);//выпадающий список серверов
        //для демо-версии список заполняется тут
        Server2.add("tgb2.cals.ru:8110");
        Server1.add("tgb2.cals.ru:8110");
        servers = new ArrayList<ServerItem>();
        servers.add(new ServerItem("tgb2.cals.ru", "8110", "", "tgb2.cals.ru:8110", ""));
        SKeep = SessionKeeper.getInstance();
        SKeep.setServer("http://tgb2.cals.ru:8110");

        adapter = new ServerSpinnerAdapter(this, R.layout.server_spinner, Server1, Server2);
        ServSpin.setAdapter(adapter);
        passField.setText("");
        logField.requestFocus();

        //ищем по ключу сервер в списке и запоминаем его логин
        if (log != null) {
            logField.post(new Runnable() {
                @Override
                public void run() {
                    logField.setText(log);
                    logField.requestFocus();
                    if (log != null && !log.equals(""))
                        passField.requestFocus();
                }
            });
        }
        //обработчик выбора пункта выпадающего списка
        ServSpin.setOnItemSelectedListener(servSpinSelected);
    }

    OnItemSelectedListener servSpinSelected = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
            btn.setFocusableInTouchMode(false);
            //получаем из пункта списка адрес выбранного сервера и порт
            sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Editor ed = sp.edit();
            ed.putInt("servPos", position);
            ed.commit();
            passField.setText("");
            logField.requestFocus();
            String s1 = ((TextView) v.findViewById(R.id.textView1)).getText().toString();
            String s2 = ((TextView) v.findViewById(R.id.textView2)).getText().toString();
            if (s2.equals(""))
                StrUrl = s1;
            else
                StrUrl = s2;
            SKeep = SessionKeeper.getInstance();
            SKeep.setServer(StrUrl);

            if (log != null) {
                logField.post(new Runnable() {
                    @Override
                    public void run() {
                        logField.setText(log);
                        if (!log.equals(""))
                            passField.requestFocus();
                    }
                });
            }

            authFlag = false;
            Toast.makeText(MainActivity.this, "Авторизация не требуется", Toast.LENGTH_SHORT).show();
            logField.setHint("Логин не требуется");
            passField.setHint("Пароль не требуется");
            btn.setFocusableInTouchMode(true);
            btn.post(new Runnable() {
                @Override
                public void run() {
                    btn.requestFocus();
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    };


    //обработчик нажатия на кнопку Подключиться
    OnClickListener oclBtnOk = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //прошли проверку - считываем логин и пароль, отправляем в другую активити адрес сервера
         //   Intent intent;
            logField = (TextView) findViewById(R.id.logField);
            //ищем по ключу сервер, в который надо записать логин
            logText = logField.getText().toString();
            passText = passField.getText().toString();
         //   intent = new Intent(MainActivity.this, ListViewUrlActivity.class);
            //если был дополнительно базовый путь - его тоже отправляем
            if (path != null)
      //          intent.putExtra("path", path);
            try {
                logText = logField.getText().toString().trim();
                //если логин и пароль не заданы - вместо них пишем пустую строку
                if (logText == null)
                    logText = "";
                passText = passField.getText().toString().trim();
                if (passText == null)
                    passHashText = "";
                    //считаем хэш от пароля по формуле
                else {
                    byte[] byte1 = encryptPassword(logText.getBytes("UTF-8"));
                    byte[] byte2 = passText.getBytes("UTF-8");
                    byte[] pass = new byte[byte1.length + byte2.length];
                    System.arraycopy(byte2, 0, pass, 0, byte2.length);
                    System.arraycopy(byte1, 0, pass, byte2.length, byte1.length);
                    byte[] passHash = encryptPassword(pass);
                    passHashText = byteArrayToHexString(passHash).toUpperCase();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //подключаемся к серверу с логином и хэшэм пароля
            SKeep = SessionKeeper.getInstance();
            SKeep.setServer("http://tgb2.cals.ru:8110");
            AsyncTask<String, Void, ArrayList<Object>> tmptask = new LoginTask(MainActivity.this).execute("http://tgb2.cals.ru:8110" + "/LoginInt/", "", "");
            ArrayList<Object> tmp;
            try {
                tmp = (tmptask.get());
                tmpresponse = new ServerResponse(tmp);
                //записываем код ответа сервера
                int answer = tmpresponse.getServerAnswer();
                //реагируем на код ответа. Все, кроме 200 - подключение не прошло
                switch (answer) {
                    case -1:
                        Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        break;
                    case 200:
                        SKeep = SessionKeeper.getInstance();
                        SKeep.setCookie(tmpresponse.getCookie());
                       // startActivity(intent);
                        break;
                    case 401:
                        if (authFlag) {
                            Toast.makeText(MainActivity.this, "Need Authorization", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            SKeep = SessionKeeper.getInstance();
                            SKeep.setCookie(tmpresponse.getCookie());
                           // startActivity(intent);
                            break;
                        }
                    case 450:
                        if (authFlag) {
                            Toast.makeText(MainActivity.this, "Wrong Login/Password", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            SKeep = SessionKeeper.getInstance();
                            SKeep.setCookie(tmpresponse.getCookie());
                          //  startActivity(intent);
                            break;
                        }
                    case 451:
                        if (authFlag) {
                            Toast.makeText(MainActivity.this, "Can't Process", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            SKeep = SessionKeeper.getInstance();
                            SKeep.setCookie(tmpresponse.getCookie());
                          //  startActivity(intent);
                            break;
                        }
                    case 452:
                        Toast.makeText(MainActivity.this, "Истекла лицензия сервера", Toast.LENGTH_SHORT).show();
                        break;
                    case 503:
                        Toast.makeText(MainActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    //конвертер байтового массива в шестнадцатиричную строку
    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    //хэширование байтового массива
    private static byte[] encryptPassword(byte[] password) {
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return crypt.digest();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }




    /****************Инициализация меню******************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prog:
                Toast.makeText(MainActivity.this, "О программе", Toast.LENGTH_SHORT).show();
                try{
                    ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
                    ZipFile zf = new ZipFile(ai.sourceDir);
                    ZipEntry ze = zf.getEntry("classes.dex");
                    long time = ze.getTime();
                    String s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
                    zf.close();
                    AlertDialog ad = new AlertDialog.Builder(this)
                            .setMessage(s)
                            .setTitle("О программе")
                            .setCancelable(true)
                            .create();
                    ad.show();
                } catch(Exception e){
                    e.printStackTrace();
                }
                return true;

            case R.id.menu_exit:
                onBackPressed();
                Intent intent = new Intent("context://closeall");
                intent.setType("context://closeallact");
                sendBroadcast(intent);
                intent.putExtra("flag", true);
                setResult(RESULT_OK, intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}