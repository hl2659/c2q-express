package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.columbia.dbmi.cwlab.util.FileUtil;


public class App 
{
    public static void main( String[] args )
    {
    	File dir=new File("/Users/cy2465/Documents/corpora/Shruti_Alzh_check_175");
    	File[] files=dir.listFiles();
    	Set<String> training_nctid=new HashSet<String>();
    	Set<String> training_nctid_2=new HashSet<String>();
    	StringBuffer sb=new StringBuffer();
    	for(File f:files){
    		if(f.getName().endsWith("txt")==true){
    			String c=FileUtil.readFile(f.getAbsolutePath());
    			String[] rows=c.split("\n");
    			for(String l:rows){
    				if(l.startsWith(">>")){
    					System.out.println("=>"+l);
    					System.out.println("=>"+l.substring(2));
    					training_nctid.add(l.substring(2));
    					sb.append(l.substring(2)+"\n");
    				}
    				if(l.contains("NCT")){
    					System.out.println("m2="+l);
    					training_nctid_2.add(l);
    				}
    			}
    		}
    	}
    	FileUtil.write2File("/Users/cy2465/Downloads/traningNCTIDs.txt", sb.toString());
    	String testfile=FileUtil.readFile("/Users/cy2465/Downloads/testNCTIDs.txt");
    	String[] lines=testfile.split("\n");
    	
    	StringBuffer testSb=new StringBuffer();
    	for(String r:lines){
    		if(r.length()>0){
    			//System.out.println("test==="+r);
    			//System.out.println("test==="+r.substring(0, 11));
    			String testnctid=r.substring(0, 11);
    			System.out.println("test nctid:"+testnctid);
    			testSb.append(testnctid+"\n");
    			if(training_nctid.contains(testnctid)){
    				System.out.println("Bingo!");
    			}
//    			System.out.println();
//    			training_nctid.add(r);
    		}
    	}
    	FileUtil.write2File("/Users/cy2465/Downloads/testingNCTIDs.txt", testSb.toString());
    	
//    	String source_dir="/Users/cy2465/Documents/datasets/AllPublicXML-EC";
//    	String target_dir="/Users/cy2465/Documents/dataset/Allparsed";
//    	List<String> nctids=allNCTs(source_dir);  
//        IInformationExtractionService ieService=new InformationExtractionServiceImpl();
//		for(String s:nctids){
//			String folder="/"+s.substring(0, 7)+"xxxx";
//			String parent=source_dir+folder;
//			String incpath=parent+"/"+s+".txt.inc.txt";
//			String excpath=parent+"/"+s+".txt.inc.txt";		
//			System.out.println("inc="+incpath);
//			String inc = FileUtil.Readfile(incpath);
//			String exc = FileUtil.Readfile(excpath);
//			Document doc = ieService.translateByDoc("", inc, exc);
//			doc = ieService.patchIEResults(doc);			
//			String targetfile=target_dir+folder+"/"+s+".c2q";
//			createFile(targetfile);
//			System.out.println("t="+targetfile);
//			FileUtil.write2File(targetfile, IOUtil.doc2Str(doc));
//		}
		
    }
    
    public static boolean createFile(String destFileName) {  
        File file = new File(destFileName);  
        if(file.exists()) {  
            return false;  
        }  
        if (destFileName.endsWith(File.separator)) {  
            return false;  
        }   
        if(!file.getParentFile().exists()) {  
            if(!file.getParentFile().mkdirs()) {                
                return false;  
            }  
        }  
        return true;
    }  
    
    public static List<String> allNCTs(String dir){
    	File f=new File("/Users/cy2465/Documents/datasets/AllPublicXML-EC");
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
		for(String s:allfilenames){
			int start_index=s.lastIndexOf("/");
			int end_index=s.indexOf(".");
			String nctid=s.substring(start_index+1,end_index);
			//System.out.println("nctid="+nctid);
			nctlist.add(nctid);
			
		}
		return nctlist;
    }
     
}
