package cmpe275eat.takeoutapp.cooker;


public class Interval {
    public int start;
    public int end;
    public String orderID;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String date;



    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Interval() {

        start = 0; end = 0;
    }
    public Interval(int s, int e, String orderId, int year, int month, int day) {
        orderID = orderId;
        start = s;
        end = e;
        date = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
    }
}
