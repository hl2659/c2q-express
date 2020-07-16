package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

import edu.columbia.dbmi.cwlab.pojo.Document;
import edu.columbia.dbmi.cwlab.serviceimpl.InformationExtractionServiceImpl;
import edu.columbia.dbmi.cwlab.util.FileUtil;

public class RunParse extends Thread {
	InformationExtractionServiceImpl ieService = new InformationExtractionServiceImpl();
	String targetdir;
	Integer start_id;
	Integer end_id;
	List<String> nctids;
	String name;
	String sourcedir;

	public RunParse(String name,Integer start_id, Integer end_id, List<String> nctids,String sourcedir, String targetdir) {
		this.start_id = start_id;
		this.end_id = end_id;
		this.nctids = nctids;
		this.targetdir = targetdir;
		this.sourcedir=sourcedir;
		this.name=name;
	}

	public void run() {
		for (int i = start_id; i <= end_id; i++) {
			String s = nctids.get(i);
			//System.out.println(this.name+"is working on:"+s);
			String folder = "/" + s.substring(0, 7) + "xxxx";
			String parent = this.sourcedir + folder;
			String incpath = parent + "/" + s + ".txt.inc.txt";
			String excpath = parent + "/" + s + ".txt.exc.txt";
			//System.out.println("inc=" + incpath);
			String inc = FileUtil.readFile(incpath);
			String exc = FileUtil.readFile(excpath);
			Document doc = ieService.translateByDoc("", inc, exc);
			doc = ieService.patchIEResults(doc);
			String targetfile = targetdir + folder + "/" + s + ".c2q";
			File file = new File(targetfile);
			if (!file.getParentFile().exists()) {
				if (!file.getParentFile().mkdirs()) {
				}
			}
			//System.out.println("t=" + targetfile);
			FileUtil.write2File(targetfile, JSON.toJSONString(doc));
			if((i-start_id)%1000==0){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(df.format(new Date()));
				System.out.println(this.name+" complete "+(i-start_id)+"/"+(end_id-start_id));
			}
		}
		

	}
}
