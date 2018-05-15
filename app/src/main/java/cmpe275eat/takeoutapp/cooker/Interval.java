package cmpe275eat.takeoutapp.cooker;


  public class Interval {
      public int start;
      public int end;
      public String orderID;

      public String getOrderID() {
          return orderID;
      }

      public void setOrderID(String orderID) {
          this.orderID = orderID;
      }

      public Interval() {

          start = 0; end = 0;
      }
      public Interval(int s, int e, String orderId) {
          orderID = orderId;
          start = s;
          end = e;
      }
  }
