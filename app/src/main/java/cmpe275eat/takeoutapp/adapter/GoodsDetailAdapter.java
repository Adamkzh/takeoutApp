package cmpe275eat.takeoutapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cmpe275eat.takeoutapp.OrderActivity;
import cmpe275eat.takeoutapp.R;
import cmpe275eat.takeoutapp.bean.ItemBean;

import java.util.List;


public class GoodsDetailAdapter extends BaseAdapter {
    private List<ItemBean> list;
    private Activity activity;
    public GoodsDetailAdapter(OrderActivity activity, List<ItemBean> list2) {
        this.activity=activity;
        this.list=list2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Viewholder viewholder;
        if (convertView==null){
            convertView= LayoutInflater.from(activity).inflate(R.layout.activity_goods_detail_item,null);
            viewholder=new Viewholder();
            viewholder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(viewholder);
        }else {
            viewholder = (Viewholder) convertView.getTag();

        }
        viewholder.tv_name.setText(list.get(position).getValue()+"*"+list.get(position).getNote2());
        viewholder.tv_price.setText("$"+list.get(position).getNote1());
        return convertView;
    }
    class Viewholder{
        TextView tv_name,tv_price;

    }

}
