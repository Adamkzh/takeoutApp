package cmpe275eat.takeoutapp;
import java.text.SimpleDateFormat;
import org.json.*;


public class Order {

    private String uid, pickTime, OrderStartTime;
    private JSONObject items;
    private JSONArray  orderList;

    public Order(String uid, String pickTime, String orderStartTime,JSONObject items, JSONArray orderlist){
        this.uid = uid;
        this.pickTime = pickTime;
        this.OrderStartTime = orderStartTime;
        this.orderList = orderlist;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getOrderStartTime() {
        return OrderStartTime;
    }

    public void setOrderStartTime(String orderStartTime) {
        OrderStartTime = orderStartTime;
    }

    public JSONArray getOrderList() {
        return orderList;
    }

    public void setOrderList(JSONArray orderList) {
        this.orderList = orderList;
    }

    public JSONObject getItems() {
        return items;
    }

    public void setItems(JSONObject items) {
        this.items = items;
    }






}
