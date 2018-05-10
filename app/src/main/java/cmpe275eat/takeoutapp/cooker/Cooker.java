package cmpe275eat.takeoutapp.cooker;

import java.util.Arrays;

public class Cooker {
    Interval[] intervals ;
    //intervals = getdatafrom database

//    ["10:20", "10:40"],["10:20", "10:40"],["10:20", "10:40"]

    //startTime is new order startTime
    //check this startTime and endTime works?
    public boolean CheckCooker(int startTime, int endTime){


        int[] start = new int[intervals.length + 1];
        int[] end = new int[intervals.length + 1];

        for(int i=1; i<intervals.length; i++) {
            start[i] =  intervals[i].start;
            end[i] =  intervals[i].end;
        }
        start[0]= startTime;
        end[0] = endTime;
        Arrays.sort(start);
        Arrays.sort(end);

        for (int i = 1; i < start.length; i++){
            if ( start[i] < end[i-1])
                return false;
        }

        //add to new Interval
        Interval[] newInterval = new Interval[intervals.length + 1];
        newInterval[intervals.length] = new Interval(startTime,endTime);
        Arrays.sort(newInterval);

        //saveToDB

        return true;
    }

}
