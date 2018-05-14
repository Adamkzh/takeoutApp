package cmpe275eat.takeoutapp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.*;

import cmpe275eat.takeoutapp.cooker.Interval;


public class Order {
    private String userId;
    private double totalPirce;
    private String status;
    private String orderTime;
    private String orderId;
    private String PickUpTime;
    private String ReadyTime;
    private String StartTime;
    private String customerEmail;
    private ArrayList<OrderItem> items;

    public Order(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalPirce() {
        return totalPirce;
    }

    public void setTotalPirce(double totalPirce) {
        this.totalPirce = totalPirce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPickUpTime() {
        return PickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        PickUpTime = pickUpTime;
    }

    public String getReadyTime() {
        return ReadyTime;
    }

    public void setReadyTime(String readyTime) {
        ReadyTime = readyTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }
}
