package com.cals.tgwebbrowser.helper;

        import java.util.ArrayList;
        import java.util.List;

        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.cookie.Cookie;
        import org.apache.http.impl.client.AbstractHttpClient;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicHttpResponse;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.params.BasicHttpParams;
        import org.apache.http.params.HttpConnectionParams;
        import org.apache.http.params.HttpParams;

        import android.content.Context;
        import android.os.AsyncTask;

/**********************Класс для осуществления post-запросов к серверу**************************/
public class LoginTask extends AsyncTask<String, Void, ArrayList<Object>>  {

    Context context;
    public LoginTask(Context context) {
        super();
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected ArrayList<Object> doInBackground(String... arg0) {
        ArrayList<Object> resp = new ArrayList<Object>();
        resp = postData(arg0[0],arg0[1],arg0[2]);
        return resp;
    }
    @Override
    protected void onPostExecute(ArrayList<Object> s){
        super.onPostExecute(null);
    }
    public ArrayList<Object> postData(String url, String logText, String passHashText) {
        HttpResponse response = null;
        String cookie_value = null;
        final DefaultHttpClient httpclient;
        // Создадим HttpClient и PostHandler

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3*1000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(url);
        ArrayList<Object> resp = new ArrayList<Object>();

        try {
            // Добавим данные (пара - "название - значение")
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("login", logText));
            System.out.println("logintask " + logText);
            if (passHashText.equals(""))
                nameValuePairs.add(new BasicNameValuePair("password", passHashText));
            else
                nameValuePairs.add(new BasicNameValuePair("passhash", passHashText));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Выполним запрос
            response = (BasicHttpResponse) httpclient.execute(httppost);
            if (response != null){
                List<Cookie> cookies = ((AbstractHttpClient) httpclient).getCookieStore().getCookies();
                if (!cookies.isEmpty()) {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                        if (cookies.get(i).getName().equals("apl_web_session"))
                            cookie_value = cookies.get(i).getValue();
                    }
                }

            }
        } catch (Exception e2) {
            System.out.println("ct");
        }
        resp.add(response);
        resp.add(cookie_value);
        return resp;
    }
}
