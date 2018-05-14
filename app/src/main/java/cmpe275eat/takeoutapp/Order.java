package cmpe275eat.takeoutapp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.*;

import cmpe275eat.takeoutapp.cooker.Interval;


public class Order {
    private String userID;
    private int pickTime;
    private ArrayList<OrderItem> item;

    public Order(){

    }

    public String getUserID() {
        return userID;
    }


    public void setUserID(String userID) {
        this.userID = userID;

    }

    public int getPickTime() {
        return pickTime;
    }

    public void setPickTime(int pickTime) {
        this.pickTime = pickTime;
    }


    public ArrayList<OrderItem> getItem() {
        return item;
    }

    public void setItem(ArrayList<OrderItem> item) {
        this.item = item;
    }

}
