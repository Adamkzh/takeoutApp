package cmpe275eat.takeoutapp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.*;

import cmpe275eat.takeoutapp.cooker.Interval;


public class Order {
    private String userId;
    private double totalPrice;
    private String status;
    private String orderTime;
    private String orderId;
    private String pickupTime;
    private String readyTime;
    private String startTime;
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
        return totalPrice;
    }

    public void setTotalPirce(double totalPirce) {
        this.totalPrice = totalPirce;
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

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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