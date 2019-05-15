package com.cshekhar.join;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static sun.nio.cs.Surrogate.is;

public class LeftJoinTest {

    @Test
    public void joinWhenBothTablesHasSameNumberRows() {
        String leftHeader = "id,first_name,last_name";
        String rightHeader= "id,salary,bonus";
        List<String> leftCSV = new ArrayList<>();
        List<String> rightCSV = new ArrayList<>();
        leftCSV.add(leftHeader);
        leftCSV.add("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray");
        rightCSV.add(rightHeader);
        rightCSV.add("59ea7840fc13ae1f6d0000a4,$60600.37,");
        CustomCsvReader left = new CustomCsvReader();
        CustomCsvReader right = new CustomCsvReader();
        try {
            left.readFile(leftCSV,"test1");
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            right.readFile(rightCSV,"test2");
        } catch (Exception e) {
            e.printStackTrace();
        }


        LeftJoin leftJoin = new LeftJoin();
        List<String> output = leftJoin.execute(left, right, "id", "id");
        assert(output.size()==1);
        assert(output.get(0).equals("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray,59ea7840fc13ae1f6d0000a4,$60600.37,"));

    }

    @Test
    public void joinWhenLeftTableHasMoreRows() {
        String leftHeader = "id,first_name,last_name";
        String rightHeader= "id,salary,bonus";
        List<String> leftCSV = new ArrayList<>();
        List<String> rightCSV = new ArrayList<>();
        leftCSV.add(leftHeader);
        leftCSV.add("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray");
        leftCSV.add("59ea7840fc13ae1f6dTest,test,test");
        rightCSV.add(rightHeader);
        rightCSV.add("59ea7840fc13ae1f6d0000a4,$60600.37,");
        CustomCsvReader left = new CustomCsvReader();
        CustomCsvReader right = new CustomCsvReader();
        LeftJoin leftJoin = new LeftJoin();
        try {
            left.readFile(leftCSV,"test1");
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            right.readFile(rightCSV,"test2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> output = leftJoin.execute(left, right, "id", "id");
        assert(output.size()==2);
        assert(output.get(0).equals("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray,59ea7840fc13ae1f6d0000a4,$60600.37,"));
        assert(output.get(1).equals("59ea7840fc13ae1f6dTest,test,test, , , "));




    }

    @Test
    // Testing one many relationship
    public void joinWhenRightTableHasMoreRows() {
        String leftHeader = "id,first_name,last_name";
        String rightHeader= "id,salary,bonus";
        List<String> leftCSV = new ArrayList<>();
        List<String> rightCSV = new ArrayList<>();
        leftCSV.add(leftHeader);
        leftCSV.add("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray");
        rightCSV.add(rightHeader);
        rightCSV.add("59ea7840fc13ae1f6d0000a4,$60600.37,");
        rightCSV.add("59ea7840fc13ae1f6d0000a4,$9999,");
        CustomCsvReader left = new CustomCsvReader();
        CustomCsvReader right = new CustomCsvReader();
        LeftJoin leftJoin = new LeftJoin();
        try {
            left.readFile(leftCSV,"test1");
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            right.readFile(rightCSV,"test2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> output = leftJoin.execute(left, right, "id", "id");
        assert(output.size()==2);
        assert(output.get(0).equals("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray,59ea7840fc13ae1f6d0000a4,$60600.37,"));
        assert(output.get(1).equals("59ea7840fc13ae1f6d0000a4,Cordelia,Aubray,59ea7840fc13ae1f6d0000a4,$9999,"));

    }
}