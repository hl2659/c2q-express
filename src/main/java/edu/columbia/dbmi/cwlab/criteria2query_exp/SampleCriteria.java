package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class SampleCriteria {
	//static InformationExtractionServiceImpl ieService = new InformationExtractionServiceImpl();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String source_dir="/Users/cy2465/Documents/datasets/AllPublicXML-EC";
    	//String target_dir=args[1];//"/Users/cy2465/Documents/dataset/Allparsed"
    	List<String> nctids=allNCTs(source_dir); 
    	
    	//System.out.println(xid);
    	
    	for(int i=0;i<10;i++){
    		int xid=(int) (Math.random()*nctids.size());
    	String s=nctids.get(xid);
    	String folder = "/" + s.substring(0, 7) + "xxxx";
		String parent = source_dir + folder;
		
		
		String incpath = parent + "/" + s + ".txt.inc.txt";
		String excpath = parent + "/" + s + ".txt.exc.txt";

		String inc = FileUtil.readFile(incpath);
		String exc = FileUtil.readFile(excpath);
		
		StringBuffer sb=new StringBuffer();
		sb.append("Inclusion Criteria:\n"+inc);
		sb.append("Exclusion Criteria:\n"+exc);
		FileUtil.write2File("/Users/cy2465/Desktop/trials/"+s+".txt",sb.toString() );
//		if(exc.length()==0 || inc.length()==0){
//			continue;
//		}
//		String[] inc_lines=inc.split("\n");
//		int allcount=0;
//		int alllength=0;
//		for(String incr:inc_lines){
//			if(incr.contains(", or ")&&incr.contains(" and ")&&incr.contains("such as ")){
//				System.out.println(incr);
//			}
//			alllength=alllength+incr.length();
//			allcount++;
//		}
//		if( (alllength/allcount) < 50){
//			continue;
//		}
		

//		FileUtil.write2File("/Users/cy2465/Documents/sample_criteria/"+s+"_inc.txt",inc );
//		FileUtil.write2File("/Users/cy2465/Documents/sample_criteria/"+s+"_inc.ann","");
//		FileUtil.write2File("/Users/cy2465/Documents/sample_criteria/"+s+"_exc.txt",exc);
//		FileUtil.write2File("/Users/cy2465/Documents/sample_criteria/"+s+"_exc.ann","");
//		i++;
    	}
    	
	}

	public static List<String> allNCTs(String dir){
    	File f=new File(dir);
    	File[] dirs=f.listFiles();
    	List<String> allfilenames=new ArrayList<String>();
    	for(File d:dirs){
    		//System.out.println("d="+d.getAbsolutePath());
    		if(d.getAbsolutePath().contains("DS_Store")){
    			continue;
    		}
    		File[] subflist=d.listFiles();
    		for(File sf:subflist){
    			if(sf.getName().contains("inc")||sf.getName().contains("exc")){
    				allfilenames.add(sf.getAbsolutePath());
    			}
    		}
    	}
		List<String> nctlist=new ArrayList<String>();
		Set<String> nctset=new HashSet<String>();
		for(String s:allfilenames){
			int start_index=s.lastIndexOf("/");
			int end_index=s.indexOf(".");
			String nctid=s.substring(start_index+1,end_index);
			//System.out.println("nctid="+nctid);
			nctset.add(nctid);	
			
		}
		for(String k:nctset){
			nctlist.add(k);
		}
		return nctlist;
    }
 
}
