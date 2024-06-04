package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    List<LP> lpList = new ArrayList<>();

    public ListAdapter(List<LP> lpList){
        this.lpList.addAll(lpList);
    }

    @Override
    public int getCount() {
        return lpList.size();
    }

    @Override
    public Object getItem(int position) {
        return lpList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

            TextView title = convertView.findViewById(R.id.title);
            TextView value = convertView.findViewById(R.id.value);

            holder.title = title;
            holder.value = value;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        LP lp = this.lpList.get(position);
        holder.title.setText(lp.title);
        holder.value.setText(lp.value);

        return convertView;
    }

    static class ViewHolder{
        TextView title, value;
    }
}
