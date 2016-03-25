package com.cals.tgwebbrowser;


        import java.util.ArrayList;
        import android.app.Activity;
        import android.content.Context;
        import android.content.res.Resources;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

/*********************Класс для кастомизации выпадающего списка серверов*********************/
public class ServerSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    public Resources res;
    private LayoutInflater inflater;
    private ArrayList<String> Server1;
    private ArrayList<String> Server2;

    public ServerSpinnerAdapter(final Activity a, final int textViewResourceId, ArrayList<String> server1, ArrayList<String> server2) {
        super(a, textViewResourceId, server1);
        this.Server1 = server1;
        this.Server2 = server2;
        activity = a;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // Метод для обновления выпадающего списка в интерфейсе (вызывается для каждого элемента списка)
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.server_spinner, parent, false);

        //инициализация разметки
        TextView label        = (TextView)row.findViewById(R.id.textView1);
        TextView sub          = (TextView)row.findViewById(R.id.textView2);

        //задание значений для конкретного элемента списка
        label.setText(Server1.get(position));
        sub.setText(Server2.get(position));
        return row;
    }
}