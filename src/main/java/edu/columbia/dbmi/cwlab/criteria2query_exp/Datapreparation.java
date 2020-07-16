package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.util.HashSet;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class Datapreparation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = FileUtil.readFile("/Users/cy2465/Documents/alltresults_c3.txt");
		// System.out.println(str);
		Integer k=1;
		String[] arr = str.split("\n");
		StringBuffer sb = new StringBuffer();
		
		
		HashSet<String> sets=new HashSet<String>();
		for (String r : arr) {
			String[] elements = r.split("\t");
			if(elements[3].equals("Demographic")){
				continue;
			}
			sets.add(elements[2].toLowerCase()+"\t"+elements[3]);
		}
		for(String en:sets){
			sb.append(en+"\n");
		}
		
		FileUtil.write2File("/Users/cy2465/Documents/entities.txt", sb.toString());
	}

}
