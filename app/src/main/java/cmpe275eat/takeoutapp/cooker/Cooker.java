package cmpe275eat.takeoutapp.cooker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Cooker {

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }

    public ArrayList<Interval> intervals = new ArrayList<>();

    public Cooker(){
        intervals.add(new Interval(2100,2400));
        intervals.add(new Interval(0,5));
    }

    //intervals = getdatafrom database

//    ["10:20", "10:40"],["10:20", "10:40"],["10:20", "10:40"]

    //startTime is new order startTime
    //check this startTime and endTime works?
    public boolean CheckCooker(int startTime, int endTime){


        ArrayList<Integer> start = new ArrayList<>();
        ArrayList<Integer> end = new ArrayList<>();

        for(int i=1; i<intervals.size(); i++) {
            start.add(intervals.get(i).start);
            end.add(intervals.get(i).end);
        }
        start.add(startTime);
        end.add(endTime);
        Collections.sort(start);
        Collections.sort(end);

        for (int i = 1; i < start.size(); i++){
            if ( start.get(i) < end.get(i-1))
                return false;
        }

        //add to new Interval
        intervals.add(new Interval(startTime,endTime));
        //saveToDB


        return true;
    }

}
