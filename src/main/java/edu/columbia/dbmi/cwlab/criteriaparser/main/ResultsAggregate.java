package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class ResultsAggregate {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		/*
		String source_dir="/Users/cy2465/Downloads/omop_mapping/";
		String target_path="/Users/cy2465/Downloads/old_mapping_results.txt";
		//aggregateParsingResults(source_dir,target_path);
		aggregateMappingResults(source_dir,target_path);
		
		*/
		/*
		String interventions=FileUtil.readFile("/Users/cy2465/Documents/aact_export/browse_interventions.txt");
		String[] introws=interventions.split("\n");
		for(String ir:introws){
			System.out.println(ir);
		}
		
		String conditions=FileUtil.readFile("/Users/cy2465/Documents/aact_export/browse_conditions.txt");
		String[] conrows=conditions.split("\n");
		for(String cr:conrows){
			System.out.println(cr);
		}
		*/
		/*
		String parsing_path="/Users/cy2465/Documents/all_parsing_results_w_standard_id.txt";
		String content=FileUtil.readFile(parsing_path);
		String[] crows=content.split("\n");
		StringBuffer sb=new StringBuffer();
		for(String cr:crows){
			//System.out.println(cr);
			String[] es=cr.split("\t");
			if(es.length>14){
		
				//System.out.println(es[13]);
				if(es[13].equals("unmapped")==false){
					Double score=Double.valueOf(es[13]);
					if(score>0.7){
						sb.append(es[0]+"\t"+es[1]+"\t"+es[5]+"\t"+es[12]+"\t"+es[11]+"\t"+es[14]+"\n");
					}
				}
			}
		
		}
		
		FileUtil.write2File("/Users/cy2465/Documents/parseing_results_matchsocregt70.txt", sb.toString());
		*/
		/*
		String content=FileUtil.readFile("/Users/cy2465/Documents/0830_all_interventions_criteria_socre_gt_70.txt");
		String[] crows=content.split("\n");
		StringBuffer csb=new StringBuffer();
		for(String cr:crows){
			String[] ecr=cr.split("\t");
			if((ecr[2].equals("INC")&&ecr[3].equals("false"))||ecr[2].equals("EXC")&&ecr[3].equals("true")){
				csb.append(ecr[0]+"\t"+ecr[1]+"\t"+"INC"+"\t"+ecr[4]+"\t"+ecr[5]+"\t"+ecr[6]+"\n");
			}else{
				csb.append(ecr[0]+"\t"+ecr[1]+"\t"+"EXC"+"\t"+ecr[4]+"\t"+ecr[5]+"\t"+ecr[6]+"\n");
			}
		}
		FileUtil.write2File("/Users/cy2465/Documents/0830_all_interventions_criteria_socre_gt_70_s.txt", csb.toString());
		*/
		//calculateFrequency();
		//linkStartDate();
		/*
		try {
			changeOverTime("/Users/cy2465/Documents/0830_breast_cancer_w_start_date.txt","INC","/Users/cy2465/Documents/0830_breast_cancer_w_change_over_time_");
			changeOverTime("/Users/cy2465/Documents/0830_breast_cancer_w_start_date.txt","EXC","/Users/cy2465/Documents/0830_breast_cancer_w_change_over_time_");
			
			
			changeOverTime("/Users/cy2465/Documents/0830_t2dm_w_start_date.txt","INC","/Users/cy2465/Documents/0830_t2dm_w_change_over_time_");
			changeOverTime("/Users/cy2465/Documents/0830_t2dm_w_start_date.txt","EXC","/Users/cy2465/Documents/0830_t2dm_w_change_over_time_");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//calculateFrequency();
		changeOverTime("/Users/cy2465/Documents/0830_t2dm_w_start_date.txt","INC","/Users/cy2465/Documents/0830_t2dm_w_change_over_time_");
		changeOverTime("/Users/cy2465/Documents/0830_t2dm_w_start_date.txt","EXC","/Users/cy2465/Documents/0830_t2dm_w_change_over_time_");
		
	}
	
	
	public static void changeOverTime(String source,String inc,String target) throws ParseException{
		String trialinfos=FileUtil.readFile(source);
		String[] trs=trialinfos.split("\n");
		
		for(int y=2000; y<=2020; y++){
			System.out.println("YEAR"+y);
			Map<String,Set<String>> con_cri=new HashMap<String,Set<String>>();
			Map<String,Set<String>> con_nct=new HashMap<String,Set<String>>();
			String yearstr=String.valueOf(y);
			Date date=new SimpleDateFormat("dd/MM/yyyy").parse("01/01/"+yearstr);
			for(String tr:trs){
				String[] etrs=tr.split("\t");
				String start_date=etrs[0];
				Date sdate=new SimpleDateFormat("dd/MM/yyyy").parse(start_date);
				String cckeystr=etrs[1]+"\t"+etrs[4]+"\t"+etrs[5]+"\t"+etrs[6];
				String constr=etrs[1];
				if (etrs[3].equals(inc) && sdate.before(date)) {
					// condition-criteria pair
					if (con_cri.containsKey(cckeystr)) {
						Set<String> nctids = con_cri.get(cckeystr);
						nctids.add(etrs[2]);
						con_cri.put(cckeystr, nctids);
					} else {
						Set<String> nctids = new HashSet<String>();
						nctids.add(etrs[2]);
						con_cri.put(cckeystr, nctids);
					}

					// condition the number of nctids
					if (con_nct.containsKey(constr)) {
						Set<String> ncts = con_nct.get(constr);
						ncts.add(etrs[2]);
						con_nct.put(constr, ncts);
					} else {
						Set<String> ncts = new HashSet<String>();
						ncts.add(etrs[2]);
						con_nct.put(constr, ncts);
					}
				}
				HashMap<String,Integer> con_cri_count=new HashMap<String,Integer>();
				for (Entry<String, Set<String>> entry : con_cri.entrySet()) {
					
					con_cri_count.put(entry.getKey(), entry.getValue().size());
				}
				
				List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(con_cri_count.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						return o2.getValue().compareTo(o1.getValue());
					}
				});
				StringBuffer sb = new StringBuffer();
				for (Map.Entry<String, Integer> mapping : list) {
					//System.out.println(mapping.getKey() + ":" + mapping.getValue());
					String str_con_cri=mapping.getKey();
					String[] str_con_cri_es=str_con_cri.split("\t");
					String str_con=str_con_cri_es[0];
					//System.out.println("str_con="+str_con);
					if(mapping.getValue()>=10){
						sb.append(mapping.getKey() + "\t" + mapping.getValue() +"\t"+con_nct.get(str_con).size()+"\t"+yearstr+ "\n");
					}
				}
				FileUtil.write2File(target+inc+"_"+yearstr+".txt",sb.toString());
				
			}
			
		}
		
		
		
	}
	
	
	public static void linkStartDate(){
		String trialinfos=FileUtil.readFile("/Users/cy2465/Documents/aact_export/studies_20190528.txt");
		String[] tinfoarr=trialinfos.split("\n");
		HashMap<String,String> start_date_map=new HashMap<String,String>();
		for(String ti:tinfoarr){
			//System.out.println(ti);
			String[] eti=ti.split("\t");
			start_date_map.put(eti[0], eti[1]);
		}
		
		String content=FileUtil.readFile("/Users/cy2465/Documents/0830_all_conditions_criteria_socre_gt_70_s.txt");
		//System.out.println(content);
		String[] crows=content.split("\n");
		StringBuffer sb=new StringBuffer();
		for(String c:crows){
			String[] esc=c.split("\t");
			//System.out.println(start_date_map.get(esc[0])+"\t"+c);
			sb.append(start_date_map.get(esc[1])+"\t"+c+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Documents/0830_all_conditions_criteria_socre_gt_70_w_start_date.txt", sb.toString());
		
	}
	
	public static void calculateFrequency(){
		String content=FileUtil.readFile("/Users/cy2465/Documents/0830_all_interventions_criteria_socre_gt_70_s.txt");
		String[] rows=content.split("\n");
		Map<String,Set<String>> con_cri=new HashMap<String,Set<String>>();
		Map<String,Set<String>> con_nct=new HashMap<String,Set<String>>();
		
		for(String r:rows){
			String[] es=r.split("\t");
			String cckeystr=es[0]+"\t"+es[3]+"\t"+es[4]+"\t"+es[5];
			String constr=es[0];
			if(es[2].equals("INC")){
				//condition-criteria pair
				if(con_cri.containsKey(cckeystr)){
					Set<String> nctids=con_cri.get(cckeystr);
					nctids.add(es[1]);
					con_cri.put(cckeystr, nctids);
				}else{
					Set<String> nctids=new HashSet<String>();
					nctids.add(es[1]);
					con_cri.put(cckeystr, nctids);
				}
				
				//condition the number of nctids 
				if(con_nct.containsKey(constr)){
					Set<String> ncts=con_nct.get(constr);
					ncts.add(es[1]);
					con_nct.put(constr, ncts);
				}else{
					Set<String> ncts=new HashSet<String>();
					ncts.add(es[1]);
					con_nct.put(constr, ncts);
				}
			}
		}
		
		HashMap<String,Integer> con_cri_count=new HashMap<String,Integer>();
		for (Entry<String, Set<String>> entry : con_cri.entrySet()) {
			
			con_cri_count.put(entry.getKey(), entry.getValue().size());
		}
		
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(con_cri_count.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Integer> mapping : list) {
			//System.out.println(mapping.getKey() + ":" + mapping.getValue());
			String str_con_cri=mapping.getKey();
			String[] str_con_cri_es=str_con_cri.split("\t");
			String str_con=str_con_cri_es[0];
			//System.out.println("str_con="+str_con);
			sb.append(mapping.getKey() + "\t" + mapping.getValue() +"\t"+con_nct.get(str_con).size()+ "\n");
		}
		FileUtil.write2File("/Users/cy2465/Documents/0830_all_interventions_inclusion_criteria_w_domain_frequency_total.txt", sb.toString());
	}
	
	public static void commonCriteriaByCondition(String allcriteria_path) {
		String content=FileUtil.readFile("/Users/cy2465/Documents/aact_export/browse_conditions.txt");
		String[] cs=content.split("\n");
		HashMap<String,Set<String>> con_mesh_terms=new HashMap<String,Set<String>>();
		for(String c:cs){
			//System.out.println("c="+c);
			String[] es=c.split("\t");
			String ms=es[2];
			String nctid=es[1];
			//System.out.println(ms);
			if(con_mesh_terms.containsKey(ms)==false){
				Set<String> nctids=new HashSet<String>();
				nctids.add(nctid);
				con_mesh_terms.put(ms, nctids);
			}else{
				Set<String> nctids=con_mesh_terms.get(ms);
				nctids.add(nctid);
				con_mesh_terms.put(ms, nctids);
			}
		}
		
		String allcriteria=FileUtil.readFile(allcriteria_path);
		String[] criteria_set=allcriteria.split("\n");
		HashMap<String,Set<String>> nctid_criteria=new HashMap<String,Set<String>>();
		for(String c:criteria_set){
			String[] e=c.split("\t");
			String nctid=e[0];
			//String cstr=c;
			//System.out.println(ms);
			if(nctid_criteria.containsKey(nctid)==false){
				Set<String> csset=new HashSet<String>();
				csset.add(c);
				nctid_criteria.put(nctid, csset);
			}else{
				Set<String> csset=nctid_criteria.get(nctid);
				csset.add(c);
				nctid_criteria.put(nctid, csset);
			}

		}
		StringBuffer sbout=new StringBuffer();
		for (Entry<String, Set<String>> entry : con_mesh_terms.entrySet()) {
			String conditionstr=entry.getKey();
			Set<String> nctid_set=entry.getValue();
			for(String id:nctid_set){
				Set<String> csset=nctid_criteria.get(id);
				if(csset!=null){
					for(String ccc:csset){
						sbout.append(conditionstr+"\t"+ccc+"\n");
					}
				}
			}
			
		}
		FileUtil.write2File("/Users/cy2465/Documents/0830_all_conditions_criteria_matchsocre_gt_70.txt", sbout.toString());
	}

	public static void linkMappingResults(String mapping_result_path, String parsing_result_path, String output_path) {
		String allmap=FileUtil.readFile(mapping_result_path);
		String[] allmaprows=allmap.split("\n");
		Map<String,String> map=new HashMap<String,String>();
		for(String ar:allmaprows){
			//System.out.println(ar);
			String[] ares=ar.split("\t");
			map.put(ares[0], ares[1]+"\t"+ares[2]+"\t"+ares[3]);
		}
		
		
		String str=FileUtil.readFile(parsing_result_path);
		String[] prows=str.split("\n");
		StringBuffer writer=new StringBuffer();
		for(String pr:prows){
			//System.out.println("~"+pr);
			String[] es=pr.split("\t");
			if(es[4].equals("Drug")||es[4].equals("Condition")||es[4].equals("Procedure")||es[4].equals("Measurement")
					||es[4].equals("Observation")){
				String term=es[3];
				writer.append(pr+"\t"+es[3]+"\t"+es[4]+"\t"+map.get(term)+"\n");
			}
		}
		FileUtil.write2File(output_path, writer.toString());
	}
	
	public static void aggregateParsingResults(String source_dir,String target_path){
		File dir=new File(source_dir);
		File[] files=dir.listFiles();
		StringBuffer sb=new StringBuffer();
		for(File f:files){
			if(f.getName().endsWith(".c2q")){
				sb.append(FileUtil.readFile(f.getAbsolutePath()));
			}
		}
		FileUtil.write2File(target_path, sb.toString());
	}

	public static void aggregateMappingResults(String source_dir,String target_path) {
		File dir=new File(source_dir);
		File[] files=dir.listFiles();
		StringBuffer sb=new StringBuffer();
		Set<String> set=new HashSet<String>();
		for(File f:files){
			if(f.getName().endsWith(".txt")){
				String content=FileUtil.readFile(f.getAbsolutePath());
				String[] maprows=content.split("\n");
				for(String mr:maprows){
					set.add(mr);
				}
			}
		}
		for(String s:set){
			sb.append(s+"\n");
		}
		FileUtil.write2File(target_path, sb.toString());
	}

}
