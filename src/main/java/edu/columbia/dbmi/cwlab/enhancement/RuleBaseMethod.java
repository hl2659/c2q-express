package edu.columbia.dbmi.cwlab.enhancement;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class RuleBaseMethod {

	static AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
	static HashMap<String,String> dir=new HashMap<String,String>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
//		HashMap<String,String> dir=new HashMap<String,String>();
//		TreeMap<String, String> map = new TreeMap<String, String>();
//		String cleandic = "/Users/cy2465/Downloads/1.txt";
//		String content = FileUtil.Readfile(cleandic);
//		String[] keyArray = content.split("\n");
//		for (String key : keyArray) {
//			String[] t = key.split("\t");
//			map.put(t[0].toLowerCase(), t[0].toLowerCase());
//		}
//		acdat.build(map);
//		
//		
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/Users/cy2465/Downloads/acdat_vocabulary"));
//        oos.writeObject(acdat);
//        oos.close();
        //反序列化
		
		
//        File file = new File("/Users/cy2465/Downloads/acdat_vocabulary");
//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
//        AhoCorasickDoubleArrayTrie<String> acdat = (AhoCorasickDoubleArrayTrie<String>)ois.readObject();
//        System.out.println(acdat.parseText("Patient is having patanol"));
        
        //序列化
		
		
//		Map<String,String> maps=new HashMap<String,String>();
//		String dic=FileUtil.Readfile("/Users/cy2465/Downloads/map_dic.txt");
//		String[] records=dic.split("\n");
//		for(String r:records){
//			String[] es=r.split("\t");
//			maps.put(es[0], es[1]);
//		}
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/Users/cy2465/Downloads/rule_base_dict_model.ser"));
//		oos.writeObject(maps);
//		oos.close();
		
		//StringBuffer sb=new StringBuffer();
		//String content=;
//		Map<String,String> map=new HashMap<String,String>();
//		String dict=FileUtil.Readfile("/Users/cy2465/Downloads/concepts_CY_20181120.csv");
//		String[] rs=dict.split("\n");
//		for(String r:rs){
//			map.put(r.split(",")[1], r.split(",")[2].substring(0, 1));
//		}
//		
//		
//		String content=FileUtil.Readfile("/Users/cy2465/Downloads/allcriteria.txt");
//		String[] rows=content.split("\n");
//		for(String r:rows){
//			System.out.println("r="+r+"\t"+map.get(r));
//		}
		generateAcdataModel();
//		File file = new File("/Users/cy2465/Downloads/rule_base_acdat_model.ser.gz");
//        ObjectInputStream ois;
//        ois = new ObjectInputStream( new java.util.zip.GZIPInputStream(new FileInputStream(file)));
//		acdat =(AhoCorasickDoubleArrayTrie<String>)ois.readObject();
//		ois.close();
//		System.out.println(acdat.parseText("Patients have a history of renal transplant"));
//		
//		Map<String,String> orginial_maps=new HashMap<String,String>();
//		String csvc=FileUtil.Readfile("/Users/cy2465/Downloads/concepts_CY_20181120.csv");
//		String[] lines=csvc.split("\n");
//		for(String c:lines){
//			String[] es=c.split(",");
//			orginial_maps.put(es[1].toLowerCase(), es[2]);
//		}
//		
//		Map<String,String> final_maps=new HashMap<String,String>();
//		File dir=new File("/Users/cy2465/Dropbox/EHR_concepts");
//		File[] files=dir.listFiles();
//		for(File f:files){
//			String content=FileUtil.Readfile(f.getAbsolutePath());
//			String[] rows=content.split("\n");
//			for(String r:rows){
//				System.out.println(">"+r);
//				System.out.println(r.toLowerCase()+"\t"+ orginial_maps.get(r.toLowerCase()));
//				final_maps.put(r.toLowerCase(), orginial_maps.get(r.toLowerCase()));
//			}
//		}
//		ObjectOutputStream oos = new ObjectOutputStream( new java.util.zip.GZIPOutputStream(new FileOutputStream("/Users/cy2465/Downloads/rule_base_dict_model.ser.gz")));
//		oos.writeObject(final_maps);
//		oos.close();
	}

	public static void generateAcdataModel() throws IOException, FileNotFoundException {
		
		TreeMap<String, String> map = new TreeMap<String, String>();
		Map<String,String> dicmap=new HashMap<String,String>();
		String content=FileUtil.readFile("/Users/cy2465/Dropbox/allEHRconcept.txt");
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
		
		ObjectOutputStream oos = new ObjectOutputStream( new java.util.zip.GZIPOutputStream(new FileOutputStream("/Users/cy2465/Downloads/rule_based_model.ser.gz")));
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
	
	public void serialized(String path,Object obj) throws FileNotFoundException, IOException{
		ObjectOutputStream oos = new ObjectOutputStream( new java.util.zip.GZIPOutputStream(new FileOutputStream(path)));
		oos.writeObject(obj);
		oos.close();
	}

}
