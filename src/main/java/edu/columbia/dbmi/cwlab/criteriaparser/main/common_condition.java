package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.ohdsi.usagi.UsagiSearchEngine;
import org.ohdsi.usagi.apis.ConceptSearchAPI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class common_condition {
//    String mapping;
//    String nctid_path;

    public void fetchCondition(){
//        this.nctid_path=nctid_path;
//        this.mapping = mapping;
        Connection con1 = null;
        try
        {
            String dbURL1 = "jdbc:mysql://localhost:3306/clinical_trails";
//            String dbURL1 = "jdbc:mysql://" +host+":"+port+"/"+database_name;
            String username1 = "root";
            String pass1 = "jay1397";
            con1 = DriverManager.getConnection(dbURL1, username1, pass1);
            if (con1 != null)
                System.out.println("Connected to database #1");


            TreeMap<String, Integer> tcount = new TreeMap<>();

            TreeMap<String, Integer> ccount = new TreeMap<>();
            Statement stmt = con1.createStatement();
            ResultSet rs = stmt.executeQuery("select nctid, concept_id, concept_name, domain, include from " +
                    "ec_all_criteria");
            while(rs.next()){
//            List<String> triple = new ArrayList<>();
//            triple.add(rs.getString(1));
//            triple.add(rs.getString(2));
//            triple.add(rs.getString(3));
//            triple.add(rs.getString(4));
//            triple.add(rs.getString(5));
            String triple=
                    rs.getString(1)+"#"+rs.getString(2)+"#"+rs.getString(3)+"#"+rs.getString(4)+"#"+rs.getString(5);
//            System.out.println(triple);
            tcount.put(triple,tcount.getOrDefault(triple,0)+1);
            ccount.put(triple,1);
            }

            System.out.println(tcount);
//            String jsonValue1 = JSONValue.toJSONString(tcount);
//            System.out.println(jsonValue1);
//            try (FileWriter file = new FileWriter("E:\\Ra\\totalcount.json")) {
//
//                file.write(jsonValue1);
//                file.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String jsonValue2 = JSONValue.toJSONString(ccount);
//            try (FileWriter file = new FileWriter("E:\\Ra\\conceptcount.json")) {
//
//                file.write(jsonValue2);
//                file.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            JSONParser jsonParser = new JSONParser();

//            try (FileReader reader = new FileReader("E:\\Ra\\totalcount.json"))
//            {
//                //Read JSON file
//                Object obj = jsonParser.parse(reader);
//
//                JSONObject employeeList = (JSONObject) obj;
//                System.out.println(employeeList);
//
//                //Iterate over employee array
////                employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            String id= null;
            Map<String,Integer> name_to_id_map = new HashMap<>();
            Map<String,List<String>> conditions = new HashMap<>();
            for(String k: tcount.keySet() ){
                id= k.split("#")[0];
                String sql1 =
                        "Select mesh_term,condition_concept_id FROM ctgov_trial_condition where nct_id = '"+ id+ "' " +
                                "and mesh_term = Covid-19;";
                ResultSet rs2 = stmt.executeQuery(sql1);
                List<String> c = new ArrayList<>();
                while(rs2.next()){
                    String mesh_term = null;
                    mesh_term = rs2.getString(1);
//                    System.out.println(mesh_term);
                    if(mesh_term!=null){
                        name_to_id_map.put(mesh_term,rs2.getInt(2));
                        c.add(mesh_term);
                    }
                }
                if(c.isEmpty()){
                    continue;
                }
                conditions.put(id,c);
            }


            List<String> condition_list = new ArrayList<>();
            Map<String, Integer> total_count =new TreeMap<>();
            Map<String, Integer> concept_count =new TreeMap<>();
            Integer value1= 0;
            Integer value2 = 0;
            for(String x: tcount.keySet()){
                condition_list = conditions.get(x.split("#")[0]);
                if(condition_list==null)
                    continue;
                value1 = tcount.get(x);
                value2 = ccount.get(x);
//                System.out.println(value1);
//                System.out.println(condition_list);
                for(String condition: condition_list){
                    try {
//                        System.out.println(condition);
                        String key =
                                name_to_id_map.get(condition) + "#" + condition + "#" + x.split("#")[1] + "#" + x.split("#")[2] + "#" + x.split(
                                        "#")[3] + "#" + x.split("#")[4];
                        total_count.put(key, total_count.getOrDefault(key, 0) + value1);
                        concept_count.put(key, concept_count.getOrDefault(key, 0) + value2);
                    }
                    catch(NullPointerException e){
                        System.out.println("Null pointer exception thrown");
                    }
                }
            }
            System.out.println(total_count);
            System.out.println(concept_count);
            if(total_count.equals(concept_count))
                System.out.print("Equal");
            else
                System.out.print("Unequal");


            Integer value3= 0;
            Integer value4 = 0;
            for(String combination: total_count.keySet()){
                value3 = total_count.get(combination);
                value4 = concept_count.get(combination);
                String[] record =combination.split("#");
                System.out.println("Combi : " +combination);
                String sql4 = "INSERT INTO test_ec_common_condition(condition_concept_id," +
                        "condition_concept_name,criteria_concept_id,criteria_concept_name," +
                        "criteria_domain,include,concept_count,total_count)"+"VALUES('"+record[0]+
                        "','"+record[1]+"','"+record[2]+ "','"+record[3]+ "','"+record[4]+
                        "', '"+record[5]+"','"+value4+"','"+value3+"');";
                stmt.executeUpdate(sql4);

            }
        con1.close();

        }

        catch(Exception e)
        {
            System.out.println(e);
        }



    }
}
