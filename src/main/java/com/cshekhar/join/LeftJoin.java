package com.cshekhar.join;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class has functions to perform leftJoin, uses Customcsv reader to read files and extract columns
 *  * @author  cshekhar
 *  * 05-15-2019
 *  Approach:  Read csv to generate mapping of row number to Row String
 *  generate mapping of (id in right table)  -> List of rows numbers in the right table [as it can in one to many relationship
 *  Iterate over left csv data for each row -> get Id -> check this exists in map generated in last step. generate entry, add default values if not present.
 *
 */
public class LeftJoin {

    private static String OUTPUT_FILENAME="output.csv";



    public static void main(String[] args) throws Exception {


        String lpath = System.getProperty("lpath") == null ? "./employee_names.csv" : System.getProperty("lpath");
        String rpath = System.getProperty("rpath") == null ? "./employee_pay.csv" : System.getProperty("rpath");
        String leftJoinColumnName = System.getProperty("lCol") == null ? "id" : System.getProperty("lCol");
        String rightJoinColumnName = System.getProperty("rCol") == null ? "salary" : System.getProperty("rCol");
        LeftJoin leftJoin = new LeftJoin();
        leftJoin.execute(lpath,rpath,leftJoinColumnName,rightJoinColumnName);


    }

        public List<String> execute(CustomCsvReader leftCsv, CustomCsvReader rightCsv, String leftJoinColumnName, String rightJoinColumnName){

            try {
                Map<Integer, String> leftData = leftCsv.getDataRecords();
                Map<Integer, String> rightData = rightCsv.getDataRecords();
                Pattern seperatorPattern = leftCsv.getPattern();
                Integer indexLeft = leftCsv.getColumns().get(leftJoinColumnName);
                Integer indexRight = rightCsv.getColumns().get(rightJoinColumnName);


                Map<String, List<Integer>> rightTableIDMappingWithRowNumber = new HashMap<>();

                for(Map.Entry<Integer, String> entry : rightData.entrySet()){

                    String row = entry.getValue();
                    String id = rightCsv.getColumnAtIndex(row, indexRight);
                    List<Integer> rowNumber = rightTableIDMappingWithRowNumber.getOrDefault(id, new ArrayList<>());
                    rowNumber.add(entry.getKey());
                    rightTableIDMappingWithRowNumber.put(id, rowNumber);
                }

                List<String> resultRows = new ArrayList<>();

                for(String row : leftData.values()){

                    String id = leftCsv.getColumnAtIndex(row, indexLeft);
                    if(!rightTableIDMappingWithRowNumber.containsKey(id)){
                        resultRows.add(row + leftCsv.getSEPARATOR() + rightCsv.getDefaultRow());
                    }
                    else
                    {
                        List<Integer> rowNumbers = rightTableIDMappingWithRowNumber.get(id);
                        for(Integer rowNumber : rowNumbers) {
                            resultRows.add(row + leftCsv.getSEPARATOR() + rightData.get(rowNumber));
                        }
                    }

                }

               return resultRows;

            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
            return null;
        }

        // lpath represents file path of the left table csv
        public void execute(String lpath, String rpath, String leftJoinColumnName, String rightJoinColumnName) throws Exception{
            CustomCsvReader leftCsv = new CustomCsvReader();
            CustomCsvReader rightCsv = new CustomCsvReader();
            try {
                 leftCsv.readFile(lpath);
                 rightCsv.readFile(rpath);
                List<String> result = execute(leftCsv, rightCsv,leftJoinColumnName,rightJoinColumnName);
                writeToCsv(result, leftCsv.getHeader() + leftCsv.getSEPARATOR() + rightCsv.getHeader());
            }
            catch (Exception e) {
                System.err.println("error while reading file" + e.getLocalizedMessage());
                throw e;
            }

        }





    // writes list of string, every string in list is appended with new line in the resulting output file
    private  static void writeToCsv(List<String> list, String header) {

        String result = "";
        System.out.println("No of rows in Output table: " + list.size());

        result += header + "\n";
        for(String str: list)
            result += str + "\n";

        writeUsingFileWriter(OUTPUT_FILENAME, result);


    }


    private static void writeUsingFileWriter(String filename, String data) {
        try {

            File file = new File("." + "/" + filename);
            FileWriter fr = null;
            try {
                fr = new FileWriter(file);
                fr.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //close resources
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }



}
