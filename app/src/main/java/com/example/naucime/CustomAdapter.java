package com.example.naucime;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;

public class CustomAdapter extends BaseAdapter {
    Activity activity;
    List<LessonModel> lessons;
    LayoutInflater inflater;

    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    public CustomAdapter(Activity activity, List<LessonModel> lessons) {
        this.activity = activity;
        this.lessons = lessons;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);

            holder = new ViewHolder();

            holder.read = (ImageView)view.findViewById(R.id.read);
            holder.lessonName = (TextView)view.findViewById(R.id.lessonName);

            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        LessonModel model = lessons.get(i);

        holder.lessonName.setText(model.getLessonName());

        if(model.isRead() == 0){
            holder.read.setBackgroundResource(R.drawable.notread);
        }else {
            holder.read.setBackgroundResource(R.drawable.read);
        }

        return view;
    }

    public void updateRecords(List<LessonModel> lessons){
        this.lessons = lessons;
    }

    class ViewHolder{

        ImageView read;
        TextView lessonName;
    }
}


