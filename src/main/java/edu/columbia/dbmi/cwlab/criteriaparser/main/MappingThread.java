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

	public void run() {
		System.out.println(this.name +"start id ="+start_id+"end id "+end_id);
		StringBuffer sb = new StringBuffer();
		for (int i = start_id; i <end_id; i++) {
			String s = entities.get(i);
			String[] cells = s.split("\t");
			String entity=cells[0];
			String domain=cells[1];
			try {
				List<ScoredConcept> lsc = cs.standarizeConcept(entity, domain);
				sb.append(entity+"\t"+domain+"\t"+lsc.get(0).concept.conceptName + "\t" + lsc.get(0).matchScore + "\t"+ lsc.get(0).concept.conceptId+"\n");
			} catch (Exception ex) {
				sb.append(entity+"\t"+domain+"\t"+"unmapped" + "\t" +"unmapped" + "\t"+"unmapped" +"\n");
				
			}
			if((i-start_id)%100==0||i==(end_id-1)||i==(entities.size()-1)){
				System.out.println(this.name+" processed :"+(i-start_id));
				FileUtil.add2File(resultpath+"mapping_"+name+".txt", sb.toString());
				sb.setLength(0);
			}
		}
	}
}
