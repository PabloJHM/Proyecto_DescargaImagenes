package com.example.pablo.proyecto_descargaimagenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adaptador extends ArrayAdapter<String> {
    private Context ctx;
    private LayoutInflater i;
    private int res;
    private List<String> aux;

    public Adaptador(Context context, List<String> lista) {
        super(context,R.layout.item_list, lista);
        this.aux = lista;
        this.res = R.layout.item_list;
        this.ctx=context;
        i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder {
        public TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();

        if (convertView == null) {
            convertView = i.inflate(res,null);
            TextView tv = (TextView) convertView.findViewById(R.id.tvEnlace);
            vh.tv = tv;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv.setText(aux.get(position));
        return convertView;
    }
}