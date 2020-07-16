package edu.columbia.dbmi.cwlab.pojo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.columbia.dbmi.cwlab.tool.AhoCorasickDoubleArrayTrie;
import edu.columbia.dbmi.cwlab.util.FileUtil;



public class TestSerial {
	AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
	HashMap<String,String> dir=new HashMap<String,String>();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		TestSerial ts=new TestSerial();
		ts.generateAcdataModel();
	}
	public void generateAcdataModel() throws IOException, FileNotFoundException {
		
		TreeMap<String, String> map = new TreeMap<String, String>();
		Map<String,String> dicmap=new HashMap<String,String>();
		String content=FileUtil.readFile("/Users/cy2465/Dropbox/0212allConcepts.txt");
		String[] rows=content.split("\n");
		
		for(String r:rows){
			System.out.println(r);
			String[] elements=r.split("\t");
			map.put(elements[0],elements[0]);
			dicmap.put(elements[0].trim(), elements[1]);
		}
		acdat.build(map);
		
		RuleBasedModels rbm=new RuleBasedModels();
		
		rbm.setAcdat(acdat);
		rbm.setDir(dicmap);
		
		//ObjectOutputStream oos = new ObjectOutputStream( new java.util.zip.GZIPOutputStream(new FileOutputStream("/Users/cy2465/Downloads/rule_based_model.ser.gz")));
//		oos.writeObject(dicmap);
//		oos.close();
//		
		
		ObjectOutputStream oos = new ObjectOutputStream( new java.util.zip.GZIPOutputStream(new FileOutputStream("/Users/cy2465/Downloads/rule_based_model_offline.ser.gz")));
		oos.writeObject(rbm);
		oos.close();
		
		
//		for(File f:files){
//			String content=FileUtil.Readfile(f.getAbsolutePath());
//			String[] rows=content.split("\n");
//			for(String r:rows){
//				System.out.println(">"+r);
//				map.put(" "+r.toLowerCase()+" ", " "+r.toLowerCase()+" ");
//			}
//		}

	}
	
}
