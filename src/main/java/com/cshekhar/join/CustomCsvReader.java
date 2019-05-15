package com.cshekhar.join;


import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * My custom csv reader, this uses regex(pattern matching) to extract columns
 *  * @author  cshekhar
 *  * 05-15-2019
 */

public class CustomCsvReader {

    private String header;

    private Map<String, Integer> columns = new LinkedHashMap<>();

    private String SEPARATOR = ",";

    private Pattern pattern = Pattern.compile(SEPARATOR) ;
    private Map<Integer, String> dataRecords;


    public Map<Integer, String> readFile(List<String> list, String path) throws Exception{

        Map<Integer, String> records = new LinkedHashMap<>();

        int count = -1;
        try {
        for(String line : list) {
                if(count == -1)
                    extractHeader(line);
                else{
                    checkRightNoOfColumns(line);
                    records.put(count, line);
                }

                count++;
            }
            System.out.println("---Read " + count + " rows from file path:" + path);
            this.dataRecords = records;
            return records;
        } catch (Exception e) {
            throw e;
        }
    }


    public Map<Integer, String> readFile(String path) throws Exception {
       List<String> list = readFromFile(path);
       return readFile(list, path);
    }


        private List<String> readFromFile(String path) throws Exception{

        List<String> list = new ArrayList<>();
        File file;
        file = new File(path);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                list.add(line);

            }
            return list;

        }
        catch (Exception e) {
            throw e;
        }

    }



    private void checkRightNoOfColumns(String line) throws Exception{
        int occurences = countOccurences(line);
        if(!(occurences + 1 ==columns.size()))
            throw new Exception("Invalid row, no of columns should match header columns. Line:" + line + "Actual"+ occurences + "expected"+ columns.size());

    }

    private int countOccurences(String str) {
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while(matcher.find())
            count++;
        return count;

    }

    public void extractHeader(String line) {
        this.header = line;
        String[] columnNames = line.split(SEPARATOR);
        for(int i = 0; i < columnNames.length; i++)
            columns.put(columnNames[i], i);

    }

    public String getHeader() {
        return header;
    }

    public Map<String, Integer> getColumns(){
        return this.columns;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getColumnAtIndex(String row, int index) throws Exception{
        Matcher matcher = pattern.matcher(row);
        int count = 0;
        int indexLastMatch = 0;
        if(index >= columns.size())
            throw new Exception("index can't be more than no of columns");
        while(matcher.find()){
            if(count == index){
                return row.substring(indexLastMatch, matcher.start());
            }
            indexLastMatch= matcher.end();
            count++;

        }
        if(index == columns.size() -1) // last element
            return row.substring(indexLastMatch);
        return null;


    }

    public String getDefaultRow(){
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        for(int i = 0; i < columns.size() -1; i++){
            builder.append(SEPARATOR);
            builder.append(" ");
        }

        return builder.toString();
    }

    public String getSEPARATOR(){
        return this.SEPARATOR;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public Map<Integer, String> getDataRecords() {
        return dataRecords;
    }

    public void setDataRecords(Map<Integer, String> dataRecords) {
        this.dataRecords = dataRecords;
    }
}
