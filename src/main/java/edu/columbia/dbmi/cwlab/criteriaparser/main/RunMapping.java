package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.util.ArrayList;
import java.util.List;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class RunMapping extends Thread {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String entitypath="/Users/cy2465/Documents/new_tobemapped_terms.txt";//"/Users/cy2465/Documents/tobemapped.txt";
		String usagipath="/Users/cy2465/Documents/git/Usagi/";
		String resultpath="/Users/cy2465/Documents/new_added_mapping_results/";//"/Users/cy2465/Documents/kbomopmap/";
    	String content=FileUtil.readFile(entitypath);
    	String[] rows=content.split("\n");
    	List<String> nctids=new ArrayList<String>();
    	for(String r:rows){
    		//System.out.println(r.substring(1, r.length()-1));
    		nctids.add(r);
    	}
    	Integer all=nctids.size();
    	
    	int c=7;
    	MappingThread mTh0=new MappingThread("0",(all/c)*0,(all/c)*1,nctids,usagipath,resultpath);  
		MappingThread mTh1=new MappingThread("1",(all/c)*1+1,(all/c)*2,nctids,usagipath,resultpath);  
		MappingThread mTh2=new MappingThread("2",(all/c)*2+1,(all/c)*3,nctids,usagipath,resultpath);  
		MappingThread mTh3=new MappingThread("3",(all/c)*3+1,(all/c)*4,nctids,usagipath,resultpath); 
		MappingThread mTh4=new MappingThread("4",(all/c)*4+1,(all/c)*5,nctids,usagipath,resultpath);  
		MappingThread mTh5=new MappingThread("5",(all/c)*5+1,(all/c)*6,nctids,usagipath,resultpath);
		MappingThread mTh6=new MappingThread("6",(all/c)*6+1,(all/c)*7,nctids,usagipath,resultpath);  
		mTh0.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh1.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh2.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh3.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh4.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh5.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh6.start();
	}
}
