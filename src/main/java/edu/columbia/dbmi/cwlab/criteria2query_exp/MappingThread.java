package edu.columbia.dbmi.cwlab.criteria2query_exp;

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

	public void run() {
		//System.out.println("-"+usagipath);
		//System.out.println("+/Users/cy2465/Documents/git/Usagi/");
		System.out.println(this.name + " start for "+(end_id-start_id));
		StringBuffer sb = new StringBuffer();
		for (int i = start_id; i <= end_id; i++) {
			String s = entities.get(i);
			String[] cells = s.split("\t");
			String entity=cells[0];
			String domain=cells[1];
			//System.out.println("entity="+entity);
			//System.out.println("domain="+domain);
			try {
				List<ScoredConcept> lsc = cs.standarizeConcept(entity, domain);
				
				//float score1=lsc.get(0).matchScore;
				/*
				int sindex = entity.indexOf(" ");
				if (sindex != -1 && lsc.get(0).matchScore < 1.0) {
					String second = entity.substring(sindex + 1);
					List<ScoredConcept> lsc2 = cs.standarizeConcept(second, domain);
					float score2=lsc2.get(0).matchScore;
					if(score2>score1){
						sb.append(entity+"\t"+lsc2.get(0).term + "\t" + lsc2.get(0).matchScore + "\t"+ lsc2.get(0).concept.conceptId+"\n");
					}else{
						sb.append(entity+"\t"+lsc.get(0).term + "\t" + lsc.get(0).matchScore + "\t"+ lsc.get(0).concept.conceptId+"\n");
					}
				}else{
				*/
					sb.append(entity+"\t"+lsc.get(0).term + "\t" + lsc.get(0).matchScore + "\t"+ lsc.get(0).concept.conceptId+"\n");
				//}
				
			} catch (Exception ex) {
				
			}
			if((i-start_id)%100==0||i==end_id){
				System.out.println(this.name+" processed :"+(i-start_id));
				FileUtil.add2File(resultpath+"mapping_"+name+".txt", sb.toString());
				sb.setLength(0);
			}
			// System.out.println(this.name+"is working on:"+s);
		}
	}
}
