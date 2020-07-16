package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class Paper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String source_dir="/Users/cy2465/Documents/datasets/AllPublicXML-EC";
		List<String> nctids=allNCTs(source_dir); 
		for (String s:nctids) {
			//System.out.println(this.name+"is working on:"+s);
			String folder = "/" + s.substring(0, 7) + "xxxx";
			String parent = source_dir + folder;
			String incpath = parent + "/" + s + ".txt.inc.txt";
			String excpath = parent + "/" + s + ".txt.exc.txt";
			//System.out.println("inc=" + incpath);
			String inc = FileUtil.readFile(incpath);
			String exc = FileUtil.readFile(excpath);
			
			//System.out.println("inc="+inc);
			//System.out.println("exc="+exc);
			String[] sents=inc.split("\n");
			StringBuffer sb=new StringBuffer();
			for(String l:sents){
				if(l.contains(" and ")&&l.contains(" or ")&& l.split(", ").length>2){
					System.out.println("sample="+l);
					sb.append(l+"\n");
				}
			}
//			sents=exc.split("\n");
//			for(String l:sents){
//				if(l.contains(" and ")&&l.contains(" or ")){
//					System.out.println("sample="+l);
//					sb.append(l+"\n");
//				}
//			}
			FileUtil.write2File("/Users/cy2465/Documents/paper_material/"+s+".txt", sb.toString());
			
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
