# LeftJoin
left joins Joins Two tables provided as CSV  programmatically 

-------------------Instructions------------

Prerequisite 
- java version 8 or above


- gradle version 4> 0 if you want to modify the code and check it in intellij


How to install
1. Execute on terminal git clone https://github.com/cshekhar1337/LeftJoin.git
2. cd LeftJoin
3. execute "./gradlew clean" -> builds
4. execute "./gradlew fatJar" -> generates fat jar

Above command generates a jar file in **build/libs** folder 

How to run jar file

Execute:
```sh
java -Dlpath=PATH_TO_LEFT_TABLE_CSV -Drpath=PATH_TO_RIGHT_TABLE_CSV -DlCol=COLUMN_NAME_USED_FOR_JOIN_IN_LEFT_TABLE -DrCol=COLUMN_NAME_USED_FOR_JOIN_IN_RIGHT_TABLE -jar LeftJoin-all-1.0.jar 
```
Example:
```sh
java -Dlpath=/Users/cshekhar/IdeaProjects/LeftJoin/employee_names.csv -Drpath=/Users/cshekhar/IdeaProjects/LeftJoin/employee_pay.csv -DlCol=id -DrCol=id -jar build/libs/LeftJoin-all-1.0.jar 
```
This will generate **output.csv**  in the same directory as the jar file.

-------------------------------
# Note:

Though the problem was simple if i had just build the solution for the given input.

#### I have made the solution generic, you can pass as many columns in each of the tables.

#### You can pass as parameter "COLUMN_NAME_USED_FOR_JOIN_IN_LEFT_TABLE" and "COLUMN_NAME_USED_FOR_JOIN_IN_RIGHT_TABLE", it can be any element. So you no restriction on just "id"

#### I have added 3 test cases too.

#### I am showing all the enteries in both the table including the common entry. If i want i can remove common entry, just let me know.

-------------------------------------------------------------
### Map reduce algorithm for large date
```sh

Fundamentally to achieve it, we will have two different Mapper class: leftMapper, rightMapper

and a common reducer class which will process data from both the mapper

Stage 1: Mapping 
 map each row of left table with key as id and value as completeRowString+"##--left--##" for left table: leftMapper
 map each row of right table with key as id and value as completeRowString+"##--right--##" for right table: rightMapper

This will map values to various partions 

Stage 2: Reducing

All keys with same value will be in same partition

run reduce -> return List<String>
 key -> [list of values]
     value will be either have  --left--## &&  ##--right--## at the end

     Iterate over all the values and filter them 
     to List<String> left and List<String> right

     Cross-product left and right to get result

     List<String> result

     for(int i = 0; i <left; i++){

     for(int j = 0; j < right; j++)
          result.add(left[i] + SEPERATOR_CHARACTER + right[j]);
     }

     return result // joined rows based on id

 Step3: write to location in hdfs : Note: There won't be a single file. Each partition will generate one part.00X file

```








