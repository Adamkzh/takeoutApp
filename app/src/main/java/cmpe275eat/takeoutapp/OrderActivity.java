package cmpe275eat.takeoutapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.flipboard.bottomsheet.BottomSheetLayout;
import cmpe275eat.takeoutapp.adapter.CatograyAdapter;
import cmpe275eat.takeoutapp.adapter.GoodsAdapter;
import cmpe275eat.takeoutapp.adapter.GoodsDetailAdapter;
import cmpe275eat.takeoutapp.adapter.ProductAdapter;
import cmpe275eat.takeoutapp.bean.CatograyBean;
import cmpe275eat.takeoutapp.bean.GoodsBean;
import cmpe275eat.takeoutapp.bean.ItemBean;
import cmpe275eat.takeoutapp.cooker.Interval;
import cmpe275eat.takeoutapp.view.MyListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import static android.content.ContentValues.TAG;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.String;

public class OrderActivity extends Activity{
    private ListView lv_catogary, lv_good;
    private ImageView iv_logo;
    private TextView tv_car;
    private  TextView tv_count,tv_totle_money;
    Double totleMoney = 0.00;
    private TextView bv_unm;
    private RelativeLayout rl_bottom;

    private List<CatograyBean> list = new ArrayList<CatograyBean>();
    private List<GoodsBean> list2 = new ArrayList<GoodsBean>();
    private MyApp myApp;
    private CatograyAdapter catograyAdapter;
    private GoodsAdapter goodsAdapter;
    ProductAdapter productAdapter;
    GoodsDetailAdapter goodsDetailAdapter;
    private static DecimalFormat df;
    private LinearLayout ll_shopcar;

    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private SparseArray<GoodsBean> selectedList;

    private View bottomDetailSheet;
    private List<GoodsBean> list3 = new ArrayList<GoodsBean>();
    private List<GoodsBean> list4 = new ArrayList<GoodsBean>();
    private List<GoodsBean> list5 = new ArrayList<GoodsBean>();
    private List<GoodsBean> list6 = new ArrayList<GoodsBean>();

    private Handler mHanlder;
    private ViewGroup anim_mask_layout;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        myApp = (MyApp) getApplicationContext();
        mHanlder = new Handler(getMainLooper());
        initFirebase();
        initView();
        initData();
        addListener();
        ll_shopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
    }

    private void initFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRference = mFirebaseDatabase.getReference("menu");

        Intent intent = getIntent();
        String sortType = intent.getStringExtra("sortType");


        Log.i("fuck", "fuck:" + sortType);

        if (sortType.equals("Sort By Price")) {
            mDatabaseRference.orderByChild("price").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                        //Loop 1 to go through all the child nodes of users
                        String itemskey = uniqueKeySnapshot.getKey();
                        GetMenu m = uniqueKeySnapshot.getValue(GetMenu.class);

                        if (m.isEnabled()) {
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setTitle(m.getName());
                            goodsBean.setCategory(m.getCategory());
                            goodsBean.setCooktime(m.getPreparation_time());
                            goodsBean.setProduct_id(Integer.parseInt(itemskey));
                            goodsBean.setIcon(m.getPicture());
                            goodsBean.setPrice(String.valueOf(m.getPrice()));
                            goodsBean.setCalories(m.getCalories());
                            goodsBean.setPopularity(m.getPopularity());
                            if (m.getCategory().equals("Appetizer")) {
                                list3.add(goodsBean);
                            } else if (m.getCategory().equals("Drink")) {
                                list5.add(goodsBean);
                            } else if (m.getCategory().equals("Main course")) {
                                list6.add(goodsBean);
                            } else if (m.getCategory().equals("Dessert")) {
                                list4.add(goodsBean);
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (sortType.equals("Sort By Popularity")) {
            mDatabaseRference.orderByChild("popularity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                        //Loop 1 to go through all the child nodes of users
                        String itemskey = uniqueKeySnapshot.getKey();
                        GetMenu m = uniqueKeySnapshot.getValue(GetMenu.class);

                        if (m.isEnabled()) {
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setTitle(m.getName());
                            goodsBean.setCategory(m.getCategory());
                            goodsBean.setCooktime(m.getPreparation_time());
                            goodsBean.setProduct_id(Integer.parseInt(itemskey));
//                        goodsBean.setIcon(m.getPicture());
                            goodsBean.setPrice(String.valueOf(m.getPrice()));
                            goodsBean.setCalories(m.getCalories());
                            goodsBean.setPopularity(m.getPopularity());
                            if (m.getCategory().equals("Appetizer")) {
                                list3.add(goodsBean);
                            } else if (m.getCategory().equals("Drink")) {
                                list5.add(goodsBean);
                            } else if (m.getCategory().equals("Main course")) {
                                list6.add(goodsBean);
                            } else if (m.getCategory().equals("Dessert")) {
                                list4.add(goodsBean);
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            mDatabaseRference.orderByChild("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                        //Loop 1 to go through all the child nodes of users
                        String itemskey = uniqueKeySnapshot.getKey();
                        GetMenu m = uniqueKeySnapshot.getValue(GetMenu.class);

                        if (m.isEnabled()) {
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setTitle(m.getName());
                            goodsBean.setCategory(m.getCategory());
                            goodsBean.setCooktime(m.getPreparation_time());
                            goodsBean.setProduct_id(Integer.parseInt(itemskey));
//                        goodsBean.setIcon(m.getPicture());
                            goodsBean.setPrice(String.valueOf(m.getPrice()));
                            goodsBean.setCalories(m.getCalories());
                            goodsBean.setPopularity(m.getPopularity());
                            if (m.getCategory().equals("Appetizer")) {
                                list3.add(goodsBean);
                            } else if (m.getCategory().equals("Drink")) {
                                list5.add(goodsBean);
                            } else if (m.getCategory().equals("Main course")) {
                                list6.add(goodsBean);
                            } else if (m.getCategory().equals("Dessert")) {
                                list4.add(goodsBean);
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }



    public void initView() {
        lv_catogary = (ListView) findViewById(R.id.lv_catogary);
        lv_good = (ListView) findViewById(R.id.lv_good);
        tv_car = (TextView) findViewById(R.id.tv_car);

        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        tv_count = (TextView) findViewById(R.id.tv_count);
        bv_unm = (TextView) findViewById(R.id.bv_unm);
        tv_totle_money= (TextView) findViewById(R.id.tv_totle_money);
        ll_shopcar= (LinearLayout) findViewById(R.id.ll_shopcar);
        selectedList = new SparseArray<>();
        df = new DecimalFormat("0.00");
    }

    public void toCheckOut(View v){
        Intent intent = new Intent(OrderActivity.this, Checkout.class);
        int size = selectedList.size();
        HashMap<String, Integer> qtymap = new HashMap<>();
        HashMap<String, String> pricemap = new HashMap<>();
        HashMap<String, Integer> timemap = new HashMap<>();
        HashMap<String, Integer> idmap = new HashMap<>();
        for(int i=0;i<size;i++){
            GoodsBean item = selectedList.valueAt(i);
            if (!qtymap.containsKey(item.getTitle())){
                qtymap.put(item.getTitle(), getSelectedItemCountById(item.getProduct_id()));
                pricemap.put(item.getTitle(), item.getPrice());
                timemap.put(item.getTitle(), item.getCooktime());
                idmap.put(item.getTitle(), item.getProduct_id());
            }
        }

        int newsize = qtymap.size();
        String[] goodlist = new String[newsize];
        String[] pricelist = new String[newsize];
        int[] timelist = new int[newsize];
        int[] qtylist = new int[newsize];
        int[] idlist = new int[newsize];

        int i = 0;
        for(String key: qtymap.keySet()) {
            if (i < newsize) {
                goodlist[i] = key;
                qtylist[i] = qtymap.get(key);
                pricelist[i] = pricemap.get(key);
                timelist[i] = timemap.get(key);
                idlist[i] = idmap.get(key);
                i++;
            }
        }

        intent.putExtra("itemlist", goodlist);
        intent.putExtra("pricelist", pricelist);
        intent.putExtra("timelist", timelist);
        intent.putExtra("qtylist", qtylist);
        intent.putExtra("idlist", idlist);
        intent.putExtra("totalqty", size);
        intent.putExtra("totalamount", totleMoney);
        startActivity(intent);
    }


    private void initData() {

//        GoodsBean goodsBean1 = new GoodsBean();
//        goodsBean1.setTitle("Steak");
//        goodsBean1.setCategory("Main course");
//        goodsBean1.setCooktime(10);
//        goodsBean1.setProduct_id(1);
//        goodsBean1.setIcon("https://3yis471nsv3u3cfv9924fumi-wpengine.netdna-ssl.com/wp-content/uploads/2013/11/Rump-Steak-Meal-Deal.jpg");
//        goodsBean1.setPrice("15");
//        goodsBean1.setCalories(300);
//        list5.add(goodsBean1);
//
//        GoodsBean goodsBean2 = new GoodsBean();
//        goodsBean2.setTitle("Cheese Burger");
//        goodsBean2.setCategory("Main course");
//        goodsBean2.setCooktime(10);
//        goodsBean2.setProduct_id(2);
//        goodsBean2.setIcon("https://upload.wikimedia.org/wikipedia/commons/4/4d/Cheeseburger.jpg");
//        goodsBean2.setPrice("12.5");
//        goodsBean2.setCalories(250);
//        list5.add(goodsBean2);
//
//        GoodsBean goodsBean3 = new GoodsBean();
//        goodsBean3.setTitle("Fries");
//        goodsBean3.setCategory("Appetizer");
//        goodsBean3.setCooktime(8);
//        goodsBean3.setProduct_id(3);
//        goodsBean3.setIcon("https://images.agoramedia.com/EHBlogImages/margaret-omalley-the-lunch-lady/2015/03/fries-daikon-400.jpg.jpg");
//        goodsBean3.setPrice("5");
//        goodsBean3.setCalories(150);
//        list3.add(goodsBean3);
//
//        GoodsBean goodsBean4 = new GoodsBean();
//        goodsBean4.setTitle("Pudding");
//        goodsBean4.setCategory("Desert");
//        goodsBean4.setCooktime(2);
//        goodsBean4.setProduct_id(4);
//        goodsBean4.setIcon("http://chocolatechipmuffins.net/wp-content/uploads/2017/08/hqdefault.jpg");
//        goodsBean4.setPrice("4.5");
//        goodsBean4.setCalories(120);
//        list6.add(goodsBean4);
//
//        GoodsBean goodsBean5 = new GoodsBean();
//        goodsBean5.setTitle("Milk Tea");
//        goodsBean5.setCategory("Drink");
//        goodsBean5.setCooktime(1);
//        goodsBean5.setProduct_id(5);
//        goodsBean5.setIcon("http://hojotea.com/jp/wp-content/uploads/IMG_6465.jpg");
//        goodsBean5.setPrice("3.5");
//        goodsBean5.setCalories(100);
//        list4.add(goodsBean5);
//
//        GoodsBean goodsBean6 = new GoodsBean();
//        goodsBean6.setTitle("Beer");
//        goodsBean6.setCategory("Drink");
//        goodsBean6.setCooktime(1);
//        goodsBean6.setProduct_id(6);
//        goodsBean6.setIcon("http://www.pierliquors.com/wp-content/uploads/2017/06/craft-beer-ri.jpg");
//        goodsBean6.setPrice("11");
//        goodsBean6.setCalories(11);
//        list4.add(goodsBean6);
//
//        GoodsBean goodsBean7 = new GoodsBean();
//        goodsBean7.setTitle("Ice Cream");
//        goodsBean7.setCategory("Desert");
//        goodsBean7.setCooktime(2);
//        goodsBean7.setProduct_id(7);
//        goodsBean7.setIcon("https://upload.wikimedia.org/wikipedia/commons/d/da/Strawberry_ice_cream_cone_%285076899310%29.jpg");
//        goodsBean7.setPrice("20");
//        goodsBean7.setCalories(1);
//        list6.add(goodsBean7);


        CatograyBean catograyBean3 = new CatograyBean();
        catograyBean3.setCount(3);
        catograyBean3.setKind("Appetizer");
        catograyBean3.setList(list3);
        list.add(catograyBean3);

        CatograyBean catograyBean4 = new CatograyBean();
        catograyBean4.setCount(4);
        catograyBean4.setKind("Dessert");
        catograyBean4.setList(list4);
        list.add(catograyBean4);

        CatograyBean catograyBean5 = new CatograyBean();
        catograyBean5.setCount(5);
        catograyBean5.setKind("Drink");
        catograyBean5.setList(list5);
        list.add(catograyBean5);

        CatograyBean catograyBean6 = new CatograyBean();
        catograyBean6.setCount(5);
        catograyBean6.setKind("Main course");
        catograyBean6.setList(list6);
        list.add(catograyBean6);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);


        list2.clear();
        list2.addAll(list.get(0).getList());


        catograyAdapter = new CatograyAdapter(this, list);
        lv_catogary.setAdapter(catograyAdapter);
        catograyAdapter.notifyDataSetChanged();

        goodsAdapter = new GoodsAdapter(this, list2, catograyAdapter);
        lv_good.setAdapter(goodsAdapter);
        goodsAdapter.notifyDataSetChanged();

    }

    private void addListener() {
        lv_catogary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("fyg","list.get(position).getList():"+list.get(position).getList());
                list2.clear();
                list2.addAll(list.get(position).getList());
                catograyAdapter.setSelection(position);
                catograyAdapter.notifyDataSetChanged();
                goodsAdapter.notifyDataSetChanged();


            }
        });
    }

    public void showDetailSheet(List<ItemBean> listItem, String mealName){
        bottomDetailSheet = createMealDetailView(listItem,mealName);
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(listItem.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomDetailSheet);
            }
        }
    }


    private View createMealDetailView(List<ItemBean> listItem, String mealName){
        View view = LayoutInflater.from(this).inflate(R.layout.activity_goods_detail,(ViewGroup) getWindow().getDecorView(),false);
        ListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView tv_meal = (TextView) view.findViewById(R.id.tv_meal);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
        int count=0;
        for(int i=0;i<listItem.size();i++){
            count = count+Integer.parseInt(listItem.get(i).getNote2());
        }
        tv_meal.setText(mealName);
        tv_num.setText("(共"+count+"件)");
        goodsDetailAdapter = new GoodsDetailAdapter(OrderActivity.this,listItem);
        lv_product.setAdapter(goodsDetailAdapter);
        goodsDetailAdapter.notifyDataSetChanged();
        return view;
    }


    private void showBottomSheet(){
        bottomSheet = createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(selectedList.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }


    private View createBottomSheetView(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet,(ViewGroup) getWindow().getDecorView(),false);
        MyListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView clear = (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
        productAdapter = new ProductAdapter(OrderActivity.this,goodsAdapter, selectedList);
        lv_product.setAdapter(productAdapter);
        return view;
    }


    public void clearCart(){
        selectedList.clear();
        list2.clear();
        if (list.size() > 0) {
            for (int j=0;j<list.size();j++){
                list.get(j).setCount(0);
                for(int i=0;i<list.get(j).getList().size();i++){
                    list.get(j).getList().get(i).setNum(0);
                }
            }
            list2.addAll(list.get(0).getList());
            catograyAdapter.setSelection(0);
            catograyAdapter.notifyDataSetChanged();
            goodsAdapter.notifyDataSetChanged();
        }
        update(true);
    }

    public int getSelectedItemCountById(int id){
        GoodsBean temp = selectedList.get(id);
        if(temp==null){
            return 0;
        }
        return temp.getNum();
    }

    public void handlerCarNum(int type, GoodsBean goodsBean, boolean refreshGoodList){
        if (type == 0) {
            GoodsBean temp = selectedList.get(goodsBean.getProduct_id());
            if(temp!=null){
                if(temp.getNum()<2){
                    goodsBean.setNum(0);
                    selectedList.remove(goodsBean.getProduct_id());

                }else{
                    int i =  goodsBean.getNum();
                    goodsBean.setNum(--i);
                }
            }
        } else if (type == 1) {
            GoodsBean temp = selectedList.get(goodsBean.getProduct_id());
            if(temp==null){
                goodsBean.setNum(1);
                selectedList.append(goodsBean.getProduct_id(), goodsBean);
            }else{
                int i= goodsBean.getNum();
                goodsBean.setNum(++i);
            }
        }

        update(refreshGoodList);

    }


    private void update(boolean refreshGoodList){
        int size = selectedList.size();
        int count =0;
        for(int i=0;i<size;i++){
            GoodsBean item = selectedList.valueAt(i);
            count += item.getNum();
            totleMoney += item.getNum()*Double.parseDouble(item.getPrice());
        }
        tv_totle_money.setText("$"+String.valueOf(df.format(totleMoney)));
        if(count<1){
            bv_unm.setVisibility(View.GONE);
        }else{
            bv_unm.setVisibility(View.VISIBLE);
        }

        bv_unm.setText(String.valueOf(count));

        if(productAdapter!=null){
            productAdapter.notifyDataSetChanged();
        }

        if(goodsAdapter!=null){
            goodsAdapter.notifyDataSetChanged();
        }

        if(catograyAdapter!=null){
            catograyAdapter.notifyDataSetChanged();
        }

        if(bottomSheetLayout.isSheetShowing() && selectedList.size()<1){
            bottomSheetLayout.dismissSheet();
        }
    }


    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];
        tv_car.getLocationInWindow(endLocation);

        int endX = 0 - startLocation[0] + 40;
        int endY = endLocation[1] - startLocation[1];

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);
        view.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });
    }
}