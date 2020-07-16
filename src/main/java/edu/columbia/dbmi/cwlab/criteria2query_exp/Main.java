package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String source_dir=args[0];//"/Users/cy2465/Documents/datasets/AllPublicXML-EC"
    	String target_dir=args[1];//"/Users/cy2465/Documents/dataset/Allparsed"
    	List<String> nctids=allNCTs(source_dir); 
    	List<String> done_nctids=allDoneNCTs(target_dir);
    	System.out.println("nct="+nctids.size());
    	System.out.println("done="+done_nctids.size());
    	nctids=merge(nctids,done_nctids);
    	System.out.println("after remove="+nctids.size());
    	
    	Integer all=nctids.size();
    	int c=20;
    	RunParse mTh0=new RunParse("0",(all/c)*0,(all/c)*1,nctids,source_dir,target_dir);  
		RunParse mTh1=new RunParse("1",(all/c)*1+1,(all/c)*2,nctids,source_dir,target_dir);  
		RunParse mTh2=new RunParse("2",(all/c)*2+1,(all/c)*3,nctids,source_dir,target_dir);  
		RunParse mTh3=new RunParse("3",(all/c)*3+1,(all/c)*4,nctids,source_dir,target_dir); 
		RunParse mTh4=new RunParse("4",(all/c)*4+1,(all/c)*5,nctids,source_dir,target_dir);  
		RunParse mTh5=new RunParse("5",(all/c)*5+1,(all/c)*6,nctids,source_dir,target_dir); 
		RunParse mTh6=new RunParse("6",(all/c)*6+1,(all/c)*7,nctids,source_dir,target_dir); 
		RunParse mTh7=new RunParse("7",(all/c)*7+1,(all/c)*8,nctids,source_dir,target_dir); 
		RunParse mTh8=new RunParse("8",(all/c)*8+1,(all/c)*9,nctids,source_dir,target_dir); 
		RunParse mTh9=new RunParse("9",(all/c)*9+1,(all/c)*10,nctids,source_dir,target_dir); 
		RunParse mTh10=new RunParse("10",(all/c)*10+1,(all/c)*11,nctids,source_dir,target_dir); 
		RunParse mTh11=new RunParse("11",(all/c)*11+1,(all/c)*12,nctids,source_dir,target_dir); 
		RunParse mTh12=new RunParse("12",(all/c)*12+1,(all/c)*13,nctids,source_dir,target_dir); 
		RunParse mTh13=new RunParse("13",(all/c)*13+1,(all/c)*14,nctids,source_dir,target_dir); 
		RunParse mTh14=new RunParse("14",(all/c)*14+1,(all/c)*15,nctids,source_dir,target_dir); 
		RunParse mTh15=new RunParse("15",(all/c)*15+1,(all/c)*16,nctids,source_dir,target_dir); 
		RunParse mTh16=new RunParse("16",(all/c)*16+1,(all/c)*17,nctids,source_dir,target_dir); 
		RunParse mTh17=new RunParse("17",(all/c)*17+1,(all/c)*18,nctids,source_dir,target_dir);
		RunParse mTh18=new RunParse("18",(all/c)*18+1,(all/c)*19,nctids,source_dir,target_dir);
		RunParse mTh19=new RunParse("19",(all/c)*19+1,(all/c)*20,nctids,source_dir,target_dir);
		mTh0.start();
		
		mTh1.start(); 
		
        mTh2.start();  
        mTh3.start(); 
        mTh4.start(); 
        mTh5.start(); 
        mTh6.start(); 
        mTh7.start(); 
        mTh8.start(); 
        mTh9.start();
        mTh10.start();
        mTh11.start();
        mTh12.start();
        mTh13.start();
        mTh14.start();
        mTh15.start();
        mTh16.start();
        mTh17.start();
        mTh18.start();
        mTh19.start();
        
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
	 
	 
	 public static List<String> allDoneNCTs(String dir){
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
	    			if(sf.getName().contains(".c2q")){
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
	 public static List<String> merge(List<String> s,List<String> t){
		 s.removeAll(t);
		 return s;
	 }
}
