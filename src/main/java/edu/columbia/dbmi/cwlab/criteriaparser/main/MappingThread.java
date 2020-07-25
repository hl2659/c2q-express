package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.util.List;

import org.ohdsi.usagi.UsagiSearchEngine.ScoredConcept;
import org.ohdsi.usagi.apis.ConceptSearchAPI;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class MappingThread extends Thread {
	Integer start_id;
	Integer end_id;
	List<String> entities;
	String name;
	String usagipath;
	String resultpath;
	ConceptSearchAPI cs;

	public MappingThread(String name, Integer start_id, Integer end_id, List<String> entities, String usagipath,
			String resultpath) {
		this.name = name;
		this.start_id = start_id;
		this.end_id = end_id;
		this.entities = entities;
		this.usagipath = usagipath;
		this.resultpath = resultpath;
		this.cs = new ConceptSearchAPI(usagipath);
	}
//Chi's Code
//	public void run() {
//		System.out.println(this.name +" start id ="+start_id+" end id "+end_id);
//		StringBuffer sb = new StringBuffer();
//		for (int i = start_id; i <end_id; i++) {
//			String s = entities.get(i);
//			String[] cells = s.split("\t");
//			String entity=cells[0];
//			System.out.println("Entity "+entity);
//			String domain=cells[1];
//			System.out.println("Domain "+domain);
//			try {
//				List<ScoredConcept> lsc = cs.standarizeConcept(entity, domain);
//				sb.append(entity+"\t"+domain+"\t"+lsc.get(0).concept.conceptName + "\t" + lsc.get(0).matchScore + "\t"+ lsc.get(0).concept.conceptId+"\n");
//			} catch (Exception ex) {
//				sb.append(entity+"\t"+domain+"\t"+"unmapped" + "\t" +"unmapped" + "\t"+"unmapped" +"\n");
//
//			}
//			if((i-start_id)%100==0||i==(end_id-1)||i==(entities.size()-1)){
//				System.out.println(this.name+" processed :"+(i-start_id));
//				FileUtil.add2File(resultpath+"mapping_"+name+".txt", sb.toString());
//				sb.setLength(0);
//			}
//		}
//	}

// // Jay's code
		public void run() {
		System.out.println(this.name +" start id ="+start_id+" end id "+end_id);
		StringBuffer sb = new StringBuffer();
		for (int i = start_id; i <end_id; i++) {
			String s = entities.get(i);
			String[] cells = s.split("\t");
			String nctid = cells[0];
			int include=0;
			int neg =0;
			if (cells[1].equals("INC")) {
				include = 1;
			}
			if (cells[5] == "true") {
				neg = 1;
			}
			String entity=cells[3];
			//System.out.println("Entity "+entity);
			String domain=cells[4];
			//System.out.println("Domain "+domain);
			try {
				List<ScoredConcept> lsc = cs.standarizeConcept(entity, domain);
				sb.append(nctid+"\t"+include+"\t"+cells[2]+"\t"+ lsc.get(0).concept.conceptId+"\t"+lsc.get(0).concept.conceptName + "\t"+domain+"\t"+ neg+"\t"+cells[6]+"\t"+cells[7]+"\t"+cells[8]+"\t"+entity+"\t"+ lsc.get(0).matchScore+"\t" +cells[12]+"\t"+"0"+"\t"+cells[9]+"\t"+cells[10]+"\t"+cells[11]+"\n");
				//sb.append(nctid+"\t"+include+"\t"+ lsc.get(0).concept.conceptId+"\t"+lsc.get(0).concept.conceptName + "\t"+domain+"\t" +entity+"\t"+ lsc.get(0).matchScore +"\n");
			} catch (Exception ex) {
				sb.append(nctid+"\t"+include+"\t"+cells[2]+"\t"+ "unmapped" +"\t"+"unmapped" + "\t"+domain+"\t"+ neg+"\t"+cells[6]+"\t"+cells[7]+"\t"+cells[8]+"\t"+entity+"\t"+ "unmapped"+"\t" +cells[12]+"\t"+"0"+"\t"+cells[9]+"\t"+cells[10]+"\t"+cells[11]+"\n");

			}
			if((i-start_id)%100==0||i==(end_id-1)||i==(entities.size()-1)){
				//System.out.println(this.name+" processed :"+(i-start_id));
				//System.out.println(sb.toString());
				FileUtil.add2File(resultpath+"mapping.txt", sb.toString());
				sb.setLength(0);
			}
		}
	}



}
