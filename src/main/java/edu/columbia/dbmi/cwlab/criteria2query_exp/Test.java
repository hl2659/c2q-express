package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.ohdsi.usagi.apis.ConceptSearchAPI;

public class Test {
	ConceptSearchAPI cs = new ConceptSearchAPI("C:/Users/jaysh/OneDrive/Documents/RA/Usagi/");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test t = new Test();
		
		String entity = "severe or chronic neurological disease";
		String domain = "Condition";
	
//			List<ScoredConcept> lsc = t.cs.standarizeConcept(entity, domain);
//			System.out.println("first try:" + lsc.get(0).term + "\t" + lsc.get(0).matchScore + "\t"
//					+ lsc.get(0).concept.conceptName);
//			float score1=lsc.get(0).matchScore;
//			int sindex = entity.indexOf(" ");
//			if (sindex != -1 && lsc.get(0).matchScore < 1.0) {
//				String second = entity.substring(sindex + 1);
//				System.out.println("second=" + second);
//				List<ScoredConcept> lsc2 = t.cs.standarizeConcept(second, domain);
//				System.out.println("second try:" + lsc2.get(0).term + "\t" + lsc2.get(0).matchScore + "\t"+ lsc2.get(0).concept.conceptId);
//				float score2=lsc2.get(0).matchScore;
//				if(score2>score1){
//					
//				}else{
//					
//				}
//			}
		
		//NCT02561000
		
		String text="TIA )";
		System.out.println(text.substring(0, text.length()-1));
		
		/*
			File dir=new File("/Users/cy2465/Documents/CTresults/");
			File[] files=dir.listFiles();
			Set<String> set=new HashSet<String>();
			for(File f:files){
				if(f.getName().endsWith(".c2q")){
					System.out.println("~!#!@#!@#");
					set.add(f.getName().substring(0, 11));
				}
			}
			String content=FileUtil.Readfile("/Users/cy2465/Documents/filepath_inc_exc.txt");
			
			String[] rows=content.split("\n");
			for(String r:rows){
				
			}
			
			
			//System.out.println("content="+content);
			File direc=new File("/Users/cy2465/Documents/home/njust4060/allctfiles");
			
			int ccc=0;
			File[] ss=direc.listFiles();
			for(File s:ss){
				if(s.getName().endsWith(".txt")){
					String nctid=s.getName().substring(0, 11);
					System.out.println("nctid="+nctid);
					if(set.contains(nctid)==false){
						ccc++;
					}
				}
			}
			System.out.println("cc="+ccc);
			*/
		
	}

}
