package edu.columbia.dbmi.cwlab.criteriaparser.main;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class MParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		String source_dir="/Users/cy2465/Documents/ct_gov_ec_data/ct_gov_ec/";
		String target_dir="/Users/cy2465/Documents/CTresults_0630/";
		String indexfile="/Users/cy2465/Documents/tobenctidlist.txt";
		
		//all ctfiles
		File ctsource_dir=new File(source_dir);	
		List<String> source_set=new ArrayList<String>();
		HashMap<String,String> path_map=new HashMap<String,String>();
		for(File f:ctsource_dir.listFiles()){
			if(f.isDirectory()){
				File ctsubdir=new File(f.getAbsolutePath());
				for(File ctfile:ctsubdir.listFiles()){
					source_set.add(getNCTID(ctfile.getName()));
					path_map.put(getNCTID(ctfile.getName()),ctfile.getAbsolutePath());
				}
				
			}
			//source_set.add(getNCTID(f.getName()));
		}
		
		String content=FileUtil.readFile(indexfile);
		String[] crows=content.split("\n");
		
		StringBuffer tobedonesb=new StringBuffer();
		for(String cr:crows){
			//System.out.println("=>"+cr+"\t"+path_map.get(cr));
			tobedonesb.append(cr+"\t"+path_map.get(cr)+"\n");
		}

		FileUtil.write2File("/Users/cy2465/Documents/tobedonepath.txt", tobedonesb.toString());
		*/
		
		String target_dir="/Users/cy2465/Documents/CTresults_0630/";
		String indexfile="/Users/cy2465/Documents/tobedonepath.txt";
		
		
		String nctc=FileUtil.readFile(indexfile);
//		
		String[] nctarry=nctc.split("\n");
		
		//System.out.println("total length="+source_set.size());
		
		
		System.out.println("to be processed length="+nctarry.length);		
		int tobep_num=nctarry.length;
		
		int tcount=6;
		
		for(int x=0;x<tcount;x++){
			//System.out.println("Thread - "+x+"\t"+((nctarry.length/tcount)*x)+"\t"+((nctarry.length/tcount)*(x+1)+1));
			new ParseThread(indexfile,target_dir,((tobep_num/tcount)*x),((tobep_num/tcount)*(x+1)+1)).start();;
		}
		
		
	}
	
	public static String getNCTID(String str){
		int nctidindex = str.indexOf("NCT");
		String nctid = str.substring(nctidindex, nctidindex + 11);
		return nctid;
	}
}
