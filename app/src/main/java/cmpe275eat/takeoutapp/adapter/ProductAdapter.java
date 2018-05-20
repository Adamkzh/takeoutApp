package cmpe275eat.takeoutapp.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cmpe275eat.takeoutapp.OrderActivity;
import cmpe275eat.takeoutapp.R;
import cmpe275eat.takeoutapp.bean.GoodsBean;
import cmpe275eat.takeoutapp.util.StringUtils;


public class ProductAdapter extends BaseAdapter {
    GoodsAdapter goodsAdapter;
    private OrderActivity activity;
    private SparseArray<GoodsBean> dataList;
    public ProductAdapter(OrderActivity activity, GoodsAdapter goodsAdapter, SparseArray<GoodsBean> dataList) {
        this.goodsAdapter =goodsAdapter;
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.valueAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.product_item, null);
            viewholder = new Viewholder();
            viewholder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewholder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewholder.iv_add= (ImageView) view.findViewById(R.id.iv_add);
            viewholder.iv_remove= (ImageView) view.findViewById(R.id.iv_remove);
            viewholder.tv_count= (TextView) view.findViewById(R.id.tv_count);

            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }


            StringUtils.filtNull(viewholder.tv_name,dataList.valueAt(position).getTitle());
            StringUtils.filtNull(viewholder.tv_price,"￥"+dataList.valueAt(position).getPrice());
            viewholder.tv_count.setText(String.valueOf(dataList.valueAt(position).getNum()));

            viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.handlerCarNum(1,dataList.valueAt(position),true);
                    goodsAdapter.notifyDataSetChanged();

                }
            });
            viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.handlerCarNum(0,dataList.valueAt(position),true);
                    goodsAdapter.notifyDataSetChanged();
                }
            });

        return view;
    }

    class Viewholder {
        TextView tv_price;
        TextView tv_name;
        ImageView iv_add,iv_remove;
        TextView tv_count;
    }

}