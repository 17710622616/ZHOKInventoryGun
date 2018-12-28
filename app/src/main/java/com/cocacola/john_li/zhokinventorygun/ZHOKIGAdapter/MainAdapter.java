package com.cocacola.john_li.zhokinventorygun.ZHOKIGAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocacola.john_li.zhokinventorygun.R;

import java.util.List;

/**
 * Created by John_Li on 20/7/2018.
 */

public class MainAdapter extends BaseAdapter {
        private List<String> list;
        private LayoutInflater inflater;
        private Context mContext;

        public MainAdapter(Context context, List<String> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);
            mContext = context;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View convertView = view;
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_main_menu, null);
                holder.text = convertView.findViewById(R.id.item_main_tv);
                holder.warmingIv = convertView.findViewById(R.id.item_main_warming_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.warmingIv.setVisibility(View.GONE);

            holder.text.setText(list.get(i));
            return convertView;
        }

    class ViewHolder {
        private TextView text;
        private ImageView warmingIv;
    }

}
