package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.util.List;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CleaningThread extends Thread{
	String targetdir;
	Integer start_id;
	Integer end_id;
	List<String> nctids;
	String name;
	String sourcedir;

	public CleaningThread(String name,Integer start_id, Integer end_id, List<String> nctids,String sourcedir, String targetdir){
		
	}
	
	public void run() {
		
	}

}
