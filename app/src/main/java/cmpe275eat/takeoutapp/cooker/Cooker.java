package cmpe275eat.takeoutapp.cooker;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cooker {

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }

    public ArrayList<Interval> intervals = new ArrayList<>();
    public HashMap<String,ArrayList<Interval>> store = new HashMap<>();


    public boolean CheckCooker(int startTime, int endTime, String orderId , int year, int month, int day){

        Interval newInterval = new Interval(startTime,endTime,orderId,year,month,day);

        for(int i = 0; i<intervals.size(); i++) {
            if(store.containsKey( intervals.get(i).getDate())){
                store.get(intervals.get(i).getDate()).add(intervals.get(i));
            }else{
                store.put(intervals.get(i).getDate(), new ArrayList<Interval>());
                store.get(intervals.get(i).getDate()).add(intervals.get(i));
            }
        }

        if(store.containsKey(Integer.toString(year) +  Integer.toString(month) + Integer.toString(day))){
            store.get(Integer.toString(year) +  Integer.toString(month) + Integer.toString(day)).add(newInterval);
        }else{
            intervals.add(newInterval);
            return true;
        }


        //traverse test each day if is OK
        for (Map.Entry<String,ArrayList<Interval>> pair : store.entrySet()){
            if(!testConflict(pair.getValue())){
                return false;
            }
        }

        //if its ok add to new intervals
        intervals.add(new Interval(startTime,endTime, orderId, year, month, day));

        return true;
    }


    public boolean testConflict(ArrayList<Interval> eachDayIntervals){
            ArrayList<Integer> start = new ArrayList<>();
            ArrayList<Integer> end = new ArrayList<>();
        for (int i = 0; i < eachDayIntervals.size(); i++){
            start.add(eachDayIntervals.get(i).start);
            end.add(eachDayIntervals.get(i).end);
        }

        Collections.sort(start);
        Collections.sort(end);

        for (int j = 1; j < start.size(); j++){
            if ( start.get(j) < end.get(j-1))
                return false;
        }
        return true;
    }

}