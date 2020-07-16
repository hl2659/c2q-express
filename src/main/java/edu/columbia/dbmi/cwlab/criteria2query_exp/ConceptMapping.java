package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.List;

import org.ohdsi.usagi.UsagiSearchEngine.ScoredConcept;
import org.ohdsi.usagi.apis.ConceptSearchAPI;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class ConceptMapping {
	ConceptSearchAPI cs=new ConceptSearchAPI("C:/Users/jaysh/OneDrive/Documents/RA/Usagi/");// /home/cy2465/Usagi/

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		ConceptMapping cm=new ConceptMapping();
		File f=new File("C:/Users/jaysh/OneDrive/Documents/RA/dataset/Allparsed/NCT0118xxxx");
		File[] flist=f.listFiles();
		int i=0;
		boolean flag=true;
		for(File file:flist){
			if(file.getAbsolutePath().contains("DS_Store")){
				continue;
			}
			String str=FileUtil.readFile(file.getAbsolutePath());
			//System.out.println(str);
			String[] rows=str.split("\n");
			for(String r:rows){
				i++;
				//System.out.println("r="+r);
				if(r.length()<2){
					continue;
				}
				String[] cells=r.split("\t");
				
				List<ScoredConcept> lsc=cm.cs.standarizeConcept(cells[1],cells[2] );
				if(cells[2].equals("Demographic")==false){
					System.out.println(cells[1]+"\t"+cells[2]+"\t"+lsc.get(0).term+"\t"+Double.valueOf(lsc.get(0).matchScore)*100);
					if(i==1000){
						flag=false;
						break;
					
					}else{
					sb.append(cells[1]+"\t"+cells[2]+"\t"+lsc.get(0).term+"\t"+Double.valueOf(lsc.get(0).matchScore)*100+"\n");
					}
				}		
				//sb.append(+)		
			}
			if(flag==false){
				break;
			}
			
		}
		FileUtil.write2File("C:/Users/jaysh/OneDrive/Documents/RA/sample_mapping_1000.txt", sb.toString());
	}

}
