package edu.columbia.dbmi.cwlab.criteria2query_exp;

import edu.columbia.dbmi.cwlab.util.FileUtil;
import org.ohdsi.usagi.UsagiSearchEngine.ScoredConcept;
import org.ohdsi.usagi.apis.ConceptSearchAPI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckDomain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConceptSearchAPI cs= new ConceptSearchAPI("C:/Users/jaysh/OneDrive/Documents/RA/Usagi/");
		String c=FileUtil.readFile("/Users/cy2465/Documents/top_200_commonused_crieria_import.txt");
		String[] rows=c.split("\n");
		StringBuffer sb=new StringBuffer();
		sb.append(rows[0]+"\n");
		Set<String> set=new HashSet<String>();
		
		String results=FileUtil.readFile("/Users/cy2465/Documents/tbterms_verify.txt");
		String[] rearr=results.split("\n");
		Set<String> readyconcept=new HashSet<String>();
		for(String rr:rearr){
			String[] entity=rr.split("\t");
			readyconcept.add(entity[0]);
		}
		
		for(int i=1;i<rows.length;i++){
			String[] e=rows[i].split("\t");
			if(readyconcept.contains(e[2])==false){
				set.add(e[2]);
			}
			/*
			List<ScoredConcept> lsc = cs.searchResults(e[2]);
			ScoredConcept sc=lsc.get(0);
			sb.append(sc.concept.standardConcept+"\t"+sc.concept.conceptId+"\t"+sc.concept.domainId+"\n");
			*/
		}
		System.out.println(set.size());
		
		//List<ScoredConcept> lsc2 = cs.searchResults("HbA1c Measurement");
		//ScoredConcept sc=lsc2.get(0);
		//System.out.println("HbA1c Measurement"+"\t"+sc.concept.conceptName+"\t"+sc.concept.conceptId+"\t"+sc.concept.domainId+"\n");
		int count=0;
		for(String s:set){
			count++;
			List<ScoredConcept> lsc = cs.searchResults(s);
			ScoredConcept sc=lsc.get(0);
			//System.out.println(s+"\t"+sc.concept.conceptName+"\t"+sc.concept.conceptId+"\t"+sc.concept.domainId+"\n");
			sb.append(s+"\t"+sc.concept.conceptName+"\t"+sc.concept.conceptId+"\t"+sc.concept.domainId+"\n");
			if(count%1000==0){
				System.out.println("count="+count);
				FileUtil.write2File("/Users/cy2465/Documents/tbterms_verify_1.txt", sb.toString());
			}
		}
		FileUtil.write2File("/Users/cy2465/Documents/tbterms_verify_1.txt", sb.toString());
		
	}

}
