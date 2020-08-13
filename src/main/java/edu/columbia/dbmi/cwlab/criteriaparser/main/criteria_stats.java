package edu.columbia.dbmi.cwlab.criteriaparser.main;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class criteria_stats {
    String host;
    String port;
    String database_name;
    String username1;
    String pass1;

    public void stats( String host,String port, String database_name, String username1,
                                String pass1){

        this.host =host;
        this.port =port;
        this.database_name =database_name;
        this.username1 =username1;
        this.pass1=pass1;

        Connection con1 = null;

        try {
//            String dbURL1 = "jdbc:mysql://localhost:3306/clinical_trails";
            String dbURL1 = "jdbc:mysql://" + host + ":" + port + "/" + database_name;

            con1 = DriverManager.getConnection(dbURL1, username1, pass1);
            if (con1 != null)
                System.out.println("Connected to database #1");
            Map<String,List<String>> phase_map =new HashMap<>();
            Statement stmt = con1.createStatement();
            ResultSet rs = stmt.executeQuery("select nct_id,phase from ctgov_trial_info_v2");
            while(rs.next()){
                List<String> phases= new ArrayList<>();
                if (rs.getString(2)!=null){
                    String[] phase = rs.getString(2).split("/");
                    for(String c:phase)
                        phases.add(c);
                    phase_map.put(rs.getString(1),phases);
                }
                else
                    phase_map.put(rs.getString(1),null);

            }
//            System.out.println(phase_map);

            Map<String,List<String>> condition_map =new HashMap<>();
            ResultSet rs1 = stmt.executeQuery("select nct_id,condition_concept_id from ctgov_trial_condition");
            while(rs1.next()){
                List<String> conditions= new ArrayList<>();

                if(condition_map.containsKey(rs1.getString(1))){
                    conditions = condition_map.get(rs1.getString(1));
                    conditions.add(rs1.getString(2));
                }
                else
                    conditions.add(rs1.getString(2));
                condition_map.put(rs1.getString(1),conditions);

            }

            ResultSet rs2 = stmt.executeQuery("select nct_id,intervention_concept_id from ctgov_trial_intervention ");
            while(rs2.next()){
                List<String> interventions= new ArrayList<>();

                if(condition_map.containsKey(rs2.getString(1))){
                    interventions = condition_map.get(rs2.getString(1));
                    interventions.add(rs2.getString(2));
                }
                else
                    interventions.add(rs2.getString(2));
                condition_map.put(rs2.getString(1),interventions);
            }
//            System.out.println(condition_map);

            Map<String, List<List<String>>> criteria =new HashMap<>();
            ResultSet rs3= stmt.executeQuery("select nctid, concept_id, include from ec_all_criteria");
            while(rs3.next()){
                if(rs3.getString(2).equals("unmapped"))
                    continue;


                List<List<String>> combination = new ArrayList<>();
                List<String> combo =new ArrayList<>();
                combo.add(rs3.getString(2));
                combo.add(rs3.getString(3));
                if(criteria.containsKey(rs3.getString(1))){
                    combination = criteria.get(rs3.getString(1));

                }
                combination.add(combo);
                criteria.put(rs3.getString(1),combination);
            }
//            System.out.println(criteria);

            Map<List<String>,int[]> stats =new HashMap<>();

            for(String nctid: criteria.keySet()){
                if(condition_map.containsKey(nctid)){
                    List<List<String>> crit;
                    crit =criteria.get(nctid);
                    for(List<String> c :crit){
                        List<String> cond;
                        cond=condition_map.get(nctid);
                        for(String elem :cond){
                            if(c.size()==3)
                                c.set(0,elem);
                            else
                                c.add(0,elem);
                            int [] counts = new int[5];
                            List<String> phases = phase_map.get(nctid);
//                            System.out.println(phases);
                            if(phases==null && !stats.containsKey(c))
                                stats.put(c,new int[5]);
                            else if(phases!=null) {
                                for (String p : phases) {
                                    if(p.equals("Phase 1")) {
                                        counts[0]++;
                                    } else if (p.equals("Phase 2")) {
                                        counts[1]++;
                                    } else if (p.equals("Phase 3")) {
                                        counts[2]++;
                                    } else if (p.equals("Phase 4")) {
                                        counts[3]++;

                                    }
                                    else if(p.contains("Early")){
                                        counts[4]++;
                                    }
                                    else
                                        counts[4]++;
                                }
                                if(stats.containsKey(c)){
                                    int[] a =stats.get(c);
                                    for(int j=0;j<5;j++){
                                        a[j]=a[j]+counts[j];
                                        stats.put(c,a);
                                    }
                                }
                                else{
                                    stats.put(c,counts);
                                }
                            }
//                            System.out.println(stats);
                        }
                    }

                }
            }
//            System.out.println(stats);

//                for (Map.Entry mapElement : stats.entrySet()) {
//                    List<String> key = (List<String>)mapElement.getKey();
//                    int[] value = (int[]) mapElement.getValue();
//
//                    System.out.println(key + " : " + value[0]+value[1]+value[2]+value[3]+value[4]);
//
//                }

            for(List<String> com:stats.keySet()){
                int[] count = stats.get(com);
                System.out.println(com+" "+ Arrays.toString(count));
                String sql = "INSERT INTO test_ec_common_criteria_stats" +
                            "(condition_concept_id," +
                            "criteria_concept_id,include,p1_count," +
                            "p2_count,p3_count,p4_count,pe_count)"+"VALUES('"+com.get(0)+
                            "','"+com.get(1)+"','"+com.get(2)+ "','"+count[0]+ "','"+count[1]+ "','"+count[2]+"','"+count[3]+"','"+count[4]+
                            "');";
                stmt.executeUpdate(sql);
            }


        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
