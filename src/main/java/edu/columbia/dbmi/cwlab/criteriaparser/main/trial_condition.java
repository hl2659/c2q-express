package edu.columbia.dbmi.cwlab.criteriaparser.main;

import org.ohdsi.usagi.UsagiSearchEngine;
import org.ohdsi.usagi.apis.ConceptSearchAPI;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class trial_condition {
    ConceptSearchAPI cs;
    String usagiapth;
    String nctid_path;
    String host;
    String port;
    String database_name;
    String username1;
    String username2;
    String pass1;
    String pass2;


    public void fetchid(String nctid_path, String usagipath, String host,
                        String port, String database_name, String username1,
                        String pass1, String username2, String pass2){
        this.cs =new ConceptSearchAPI(usagipath);
        this.usagiapth=usagipath;
        this.nctid_path=nctid_path;
        this.host =host;
        this.port =port;
        this.database_name =database_name;
        this.username1 =username1;
        this.username2 =username2;
        this.pass1 =pass1;
        this.pass2=pass2;
        Connection con1 = null;
        Connection con2 = null;

        try {
            String dbURL1 = "jdbc:mysql://" +host+":"+port+"/"+database_name;

            con1 = DriverManager.getConnection(dbURL1, username1, pass1);
            if (con1 != null)
                System.out.println("Connected to database #1");

            String dbURL2 = "jdbc:postgresql://aact-db.ctti-clinicaltrials.org/aact";

            con2 = DriverManager.getConnection(dbURL2, username2, pass2);
            if (con2 != null)
                System.out.println("Connected to database #2");

            Statement stmt = con1.createStatement();
            ResultSet rs = stmt.executeQuery("select nct_id from ctgov_trial_condition");
            List<String> original_nctid_list =new ArrayList<>();
            while(rs.next()){
                original_nctid_list.add(rs.getString(1));
            }
            ResultSet rs0 = stmt.executeQuery("select nct_id from ctgov_trial_intervention");
            while(rs0.next()){
                original_nctid_list.add(rs0.getString(1));
            }
            List<String> new_nctid_list = Files.readAllLines(Paths.get(nctid_path),
                    StandardCharsets.UTF_8);

            Set<String> new_list = new HashSet<String>(new_nctid_list);
            Set<String> original = new HashSet<String>(original_nctid_list);
            new_list.removeAll(original);
            List<String> nctid_list = new ArrayList<String>(new_list);
            Map<String,List<List<String>>> browse_conditions_mesh = new HashMap<>();
            Map<String,List<List<String>>> browse_interventions_mesh = new HashMap<>();
            Map<String, List<String>> condition_concept_ids = new HashMap<>();
            Map<String, List<String>> intervention_concept_ids = new HashMap<>();
            Statement stm1 = con1.createStatement();
            ResultSet r1 = stm1.executeQuery("select mesh_term, unique_id, condition_concept_id, map_method from " +
                    "ctgov_trial_condition;");
            while(r1.next()){
                List<String> combo1 = new ArrayList<>();
                combo1.add(r1.getString(2));
                combo1.add(r1.getString(3));
                combo1.add(r1.getString(4));
                condition_concept_ids.put(r1.getString(1),combo1);
            }
            ResultSet r2 = stm1.executeQuery("select mesh_term, unique_id, intervention_concept_id, map_method from " +
                    "ctgov_trial_intervention;");
            while(r2.next()){
                List<String> combo2 = new ArrayList<>();
                combo2.add(r2.getString(2));
                combo2.add(r2.getString(3));
                combo2.add(r2.getString(4));
                intervention_concept_ids.put(r2.getString(1),combo2);
            }
            System.out.println("Mesh term and id dictionaries done");

            for(int i = 0; i < nctid_list.size(); i++) {
                Statement stmt1 = con2.createStatement();
                ResultSet rs1 = stmt1.executeQuery("select mesh_term,downcase_mesh_term from browse_conditions where nct_id ='" + nctid_list.get(i) + "'");
                List<List<String>> condition_mesh = new ArrayList<>();
                System.out.println(nctid_list.get(i));
                while(rs1.next()){
                    List<String> mesh = new ArrayList<>();
                    String mesh_term =null;
                    String dmt=null;
                    String unique_id=null;
                    String condition_concept_id=null;
                    String map_method = null;
                    mesh_term =rs1.getString(1);
                    if(mesh_term!=null){
//                        System.out.println("1  "+mesh_term);
                        if(condition_concept_ids.containsKey(mesh_term)){
                            List<String> value =new ArrayList<>();
                            value = condition_concept_ids.get(mesh_term);
                            unique_id=value.get(0);
                            condition_concept_id = value.get(1);
                            map_method=value.get(2);
                        }
                        else{
                            List<UsagiSearchEngine.ScoredConcept> lsc = cs.standarizeConcept(mesh_term, "Condition");
                                unique_id = lsc.get(0).concept.conceptCode;
                                condition_concept_id = Integer.toString(lsc.get(0).concept.conceptId);
                                map_method = "USAGI";
                        }
                        mesh_term=mesh_term.replaceAll("'","''");
                        dmt = rs1.getString(2);
                        dmt=dmt.replaceAll("'","''");
                        mesh.add(mesh_term);
                        mesh.add(dmt);
                        mesh.add(unique_id);
                        mesh.add(condition_concept_id);
                        mesh.add(map_method);
                        condition_mesh.add(mesh);
                    }

                }
                if(condition_mesh.isEmpty()){
                    ResultSet rs2 = stmt1.executeQuery("select mesh_term,downcase_mesh_term from browse_interventions where nct_id ='" + nctid_list.get(i) + "'");
                    List<List<String>> intervention_mesh = new ArrayList<>();
                    while(rs2.next()) {
                        List<String> mesh2 = new ArrayList<>();
                        String mesh_term2 =null;
                        String dmt2=null;
                        String unique_id=null;
                        String intervention_concept_id=null;
                        String map_method = null;
                        mesh_term2 =rs2.getString(1);
                        if(mesh_term2!=null){
//                            System.out.println("2  "+mesh_term2);
                            if(intervention_concept_ids.containsKey(mesh_term2)){
                                List<String> value2 =new ArrayList<>();
                                value2 = intervention_concept_ids.get(mesh_term2);
                                unique_id=value2.get(0);
                                intervention_concept_id = value2.get(1);
                                map_method=value2.get(2);
                            }
                            else{
                                List<UsagiSearchEngine.ScoredConcept> lsc = cs.standarizeConcept(mesh_term2,
                                        "Condition");
                                unique_id = lsc.get(0).concept.conceptCode;
                                intervention_concept_id = Integer.toString(lsc.get(0).concept.conceptId);
                                map_method = "USAGI";
                            }
                            mesh_term2=mesh_term2.replaceAll("'","''");
                            dmt2=rs2.getString(2);
                            dmt2=dmt2.replaceAll("'","''");
                            mesh2.add(mesh_term2);
                            mesh2.add(dmt2);
                            mesh2.add(unique_id);
                            mesh2.add(intervention_concept_id);
                            mesh2.add(map_method);
                            intervention_mesh.add(mesh2);
                        }

                    }
                    if(intervention_mesh.isEmpty())
                        continue;
                    browse_interventions_mesh.put(nctid_list.get(i),intervention_mesh);

                }
                else{
                    browse_conditions_mesh.put(nctid_list.get(i),condition_mesh);
                }
            }
            System.out.println("Final Dictionaries made\n Updation start");
            Statement stmt3 = con1.createStatement();
            for(String id:browse_conditions_mesh.keySet()){
                List<List<String>> record_list = browse_conditions_mesh.get(id);
                System.out.println(id+" "+record_list);
                for(List<String> record:record_list){
                    String sql = "INSERT INTO ctgov_trial_condition(nct_id," +
                            "mesh_term,downcase_mesh_term,unique_id," +
                            "condition_concept_id,map_method)"+"VALUES('"+id+
                            "','"+record.get(0)+"','"+record.get(1)+ "','"+record.get(2)+ "','"+record.get(3)+ "','"+record.get(4)+"');";
                    stmt3.executeUpdate(sql);
                }
            }
            for(String id:browse_interventions_mesh.keySet()){
                List<List<String>> record_list = browse_interventions_mesh.get(id);
                System.out.println(id+" "+record_list);
                for(List<String> record:record_list){
                    String sql = "INSERT INTO ctgov_trial_intervention" +
                            "(nct_id," +
                            "mesh_term,downcase_mesh_term,unique_id," +
                            "intervention_concept_id,map_method)"+"VALUES('"+id+
                            "','"+record.get(0)+"','"+record.get(1)+ "','"+record.get(2)+ "','"+record.get(3)+ "','"+record.get(4)+"');";
                    stmt3.executeUpdate(sql);
                }
            }

            con1.close();
            con2.close();


        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
