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
//            String dbURL1 = "jdbc:mysql://localhost:3306/clinical_trails";
            String dbURL1 = "jdbc:mysql://" +host+":"+port+"/"+database_name;
//            String username1 = "root";
//            String pass1 = "jay1397";
            con1 = DriverManager.getConnection(dbURL1, username1, pass1);
            if (con1 != null)
                System.out.println("Connected to database #1");

            String dbURL2 = "jdbc:postgresql://aact-db.ctti-clinicaltrials.org/aact";
//            String username2 = "jayshah";
//            String pass2 = "Verna@hetvi1";

            con2 = DriverManager.getConnection(dbURL2, username2, pass2);
            if (con2 != null)
                System.out.println("Connected to database #2");

            Statement stmt = con1.createStatement();
            ResultSet rs = stmt.executeQuery("select nct_id from ctgov_trial_condition");
            List<String> original_nctid_list =new ArrayList<>();
            while(rs.next()){
                original_nctid_list.add(rs.getString(1));
            }

            List<String> new_nctid_list = Files.readAllLines(Paths.get(nctid_path),
                    StandardCharsets.UTF_8);

            Set<String> original = new HashSet<String>(new_nctid_list);
            Set<String> new_list = new HashSet<String>(original_nctid_list);
            new_list.removeAll(original);
            List<String> nctid_list = new ArrayList<String>(new_list);
            Map<String,List<List<String>>> browse_conditions_mesh = new HashMap<>();
            Map<String,List<List<String>>> browse_interventions_mesh = new HashMap<>();

            for (int i = 0; i < nctid_list.size(); i++) {
                Statement stmt1 = con2.createStatement();
                ResultSet rs1 = stmt1.executeQuery("select mesh_term,downcase_mesh_term from browse_conditions where nct_id ='" + nctid_list.get(i) + "'");
                List<List<String>> condition_mesh = new ArrayList<>();
                while(rs1.next()) {
                    List<String> mesh = new ArrayList<>();
                    String unique_id=null;
                    String condition_concept_id=null;
                    String mesh_term=null;
                    String map_method = null;
                    String concept_id;
                    mesh_term =rs1.getString(1);
                    ResultSet rs3 = null;
                    if(mesh_term!=null) {
                        Statement stmt2 = con1.createStatement();
//                        System.out.println("1  " + mesh_term);
                        mesh_term=mesh_term.replaceAll("'","''");
                        System.out.println("1  " + mesh_term);
                        rs3 = stmt2.executeQuery("select unique_id, condition_concept_id,map_method from ctgov_trial_condition where mesh_term ='" + mesh_term + "'limit 1");
                        while (rs3.next()) {
                            unique_id = rs3.getString(1);
                            condition_concept_id = rs3.getString(2);
                            map_method = rs3.getString(3);
                            if (condition_concept_id == null) {
                                List<UsagiSearchEngine.ScoredConcept> lsc = cs.standarizeConcept(mesh_term, "Condition");
                                unique_id = lsc.get(0).concept.conceptCode;
                                condition_concept_id = Integer.toString(lsc.get(0).concept.conceptId);
                                map_method = "USAGI";
                            }

                        }

                    }
                    mesh.add(rs1.getString(1));
                    mesh.add(rs1.getString(2));
                    mesh.add(unique_id);
                    mesh.add(condition_concept_id);
                    mesh.add(map_method);
                    condition_mesh.add(mesh);
                }
                if(condition_mesh.isEmpty()){

                    ResultSet rs2 = stmt1.executeQuery("select mesh_term,downcase_mesh_term from browse_interventions where nct_id ='" + nctid_list.get(i) + "'");
                    List<List<String>> intervention_mesh = new ArrayList<>();
                    while(rs2.next()) {
                        List<String> mesh2 = new ArrayList<>();
                        String unique_id=null;
                        String condition_concept_id=null;
                        String mesh_term2=null;
                        String map_method=null;
                        String concept_id;
                        ResultSet rs4;
                        mesh_term2 = rs2.getString(1);
                        if(mesh_term2!=null) {
                            Statement stmt2 = con1.createStatement();

                            mesh_term2 = mesh_term2.replaceAll("'","''");
                            System.out.println("2 "+mesh_term2);
                            rs4 = stmt2.executeQuery("select unique_id, intervention_concept_id, map_method from ctgov_trial_intervention where mesh_term ='" + mesh_term2 + "'limit 1");
                            while (rs4.next()) {
                                unique_id = rs4.getString(1);
                                condition_concept_id = rs4.getString(2);
                                map_method=rs4.getString(3);
                                if (condition_concept_id == null) {
                                    List<UsagiSearchEngine.ScoredConcept> lsc = cs.standarizeConcept(mesh_term2, "Condition");
                                    unique_id = lsc.get(0).concept.conceptCode;
                                    condition_concept_id = Integer.toString(lsc.get(0).concept.conceptId);
                                    map_method="USAGI";
                                }

                            }

                        }

                        mesh2.add(rs2.getString(1));
                        mesh2.add(rs2.getString(2));
                        mesh2.add(unique_id);
                        mesh2.add(condition_concept_id);
                        mesh2.add(map_method);
                        intervention_mesh.add(mesh2);
                    }
                    if(intervention_mesh.isEmpty()){
                        continue;
                    }
                    browse_interventions_mesh.put(nctid_list.get(i),intervention_mesh);
//                    System.out.print("2   ");
//                    System.out.println(browse_interventions_mesh);
                    continue;
                }
                browse_conditions_mesh.put(nctid_list.get(i),condition_mesh);
//                System.out.print("1   ");
//                System.out.println(browse_conditions_mesh);

            }
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

//        try {
//            List<UsagiSearchEngine.ScoredConcept> lsc = cs.standarizeConcept("COVID-19", "Condition");
//            System.out.println(lsc.get(0).concept.conceptCode+" "+ lsc.get(0).concept.conceptName);

//            sb.append(nctid+"\t"+include+"\t"+cells[2]+"\t"+ lsc.get(0).concept.conceptId+"\t"+lsc.get(0).concept.conceptName + "\t"+domain+"\t"+ neg+"\t"+cells[6]+"\t"+cells[7]+"\t"+cells[8]+"\t"+entity+"\t"+ lsc.get(0).matchScore+"\t" +cells[12]+"\t"+"0"+"\t"+cells[9]+"\t"+cells[10]+"\t"+cells[11]+"\n");
            //sb.append(nctid+"\t"+include+"\t"+ lsc.get(0).concept.conceptId+"\t"+lsc.get(0).concept.conceptName + "\t"+domain+"\t" +entity+"\t"+ lsc.get(0).matchScore +"\n");
//        } catch (Exception ex) { }
//            sb.append(nctid+"\t"+include+"\t"+cells[2]+"\t"+ "unmapped" +"\t"+"unmapped" + "\t"+domain+"\t"+ neg+"\t"+cells[6]+"\t"+cells[7]+"\t"+cells[8]+"\t"+entity+"\t"+ "unmapped"+"\t" +cells[12]+"\t"+"0"+"\t"+cells[9]+"\t"+cells[10]+"\t"+cells[11]+"\n");
//
//        }
    }
}
