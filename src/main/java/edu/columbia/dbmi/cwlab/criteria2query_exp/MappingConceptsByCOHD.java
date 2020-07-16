package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;

import edu.columbia.dbmi.cwlab.util.FileUtil;
import edu.columbia.dbmi.cwlab.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MappingConceptsByCOHD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		//http://athena.ohdsi.org/api/v1/concepts/22281/relationships?std=false
		File dir=new File("/Users/cy2465/Downloads/omop2codes/");
		
		for(File f:dir.listFiles()){
			if(f.getName().startsWith(".")){
				continue;
			}
			System.out.println(f.getAbsolutePath());
		String content=FileUtil.readFile(f.getAbsolutePath());
		String[] lines=content.split("\n");
//		Set<String> vocset=new HashSet<String>();
//		vocset.add("ICD10CM");
//		vocset.add("ICD9CM");
//		vocset.add("");
		int i=0;
		for(String l:lines){
			i++;
			System.out.println(i);
			if(i<=2){
				continue;
			}
			//System.out.println("=>"+l);
			String[] elements=l.split("\t");
			//sb.append(l);
			String ss=HttpUtil.doGet("http://cohd.nsides.io/api/omop/mapFromStandardConceptID?concept_id="+elements[0]);
			//System.out.println("http://cohd.nsides.io/api/omop/mapFromStandardConceptID?concept_id="+elements[0]);
			JSONObject jo=JSONObject.fromObject(ss);
			try{
			JSONArray ja=JSONArray.fromObject(jo.get("results"));
			for(int k=0;k<ja.size();k++){
				JSONObject jone=JSONObject.fromObject(ja.get(k));
				//System.out.println(jone.get("concept_code")+"\t"+jone.get("vocabulary_id")+"\t"+jone.get("concept_name"));
				sb.append(l+"\t"+jone.get("concept_code")+"\t"+jone.get("vocabulary_id")+"\t"+jone.get("concept_name")+"\n");
			}
			}catch(Exception ex){
				continue;
			}
//			JSONArray ja=JSONArray.fromObject(jo.get("items"));
//			for(int k=0;k<ja.size();k++){
//				JSONObject jone=JSONObject.fromObject(ja.get(k));
//				if(jone.get("relationshipName")!=null &&jone.get("relationshipName").equals("Standard to Non-standard map (OMOP)")){
//					System.out.println(jone);
//					System.out.println(jone.get("relationships"));
//					JSONArray jasub=JSONArray.fromObject(jone.get("relationships"));
//					for(int y=0;y<jasub.size();y++){
//						System.out.println("mapping size="+jasub.size());
//						JSONObject concept=JSONObject.fromObject(jasub.get(y));
//						System.out.println(concept.get("targetConceptId")+"\t"+concept.get("targetConceptName")+"\t"+concept.get("targetVocabularyId")+"\t"+getSourceCode(concept.get("targetConceptId").toString()));
//						//sb.append(l+"\t"+concept.get("targetConceptId")+"\t"+concept.get("targetConceptName")+"\t"+concept.get("targetVocabularyId")+"\t"+getSourceCode(concept.get("targetConceptId").toString())+"\n");
//						//System.out.println(concept.get("targetConceptId")+"\t"+concept.get("targetConceptName")+"\t"+concept.get("targetVocabularyId")+"\t"+getSourceCode(concept.get("targetConceptId").toString()));
//					}
//					//sb.append("\t"+jone.get("relationships"));
//				}
//			}
		}
		FileUtil.write2File(f.getAbsolutePath()+"_mapping2nonstandard.txt", sb.toString());
		}
	}
	
	public static String getSourceCode(String conceptId){
		//http://athena.ohdsi.org/api/v1/concepts/45542708
		String ss=HttpUtil.doGet("http://athena.ohdsi.org/api/v1/concepts/"+conceptId);
		JSONObject c=JSONObject.fromObject(ss);
		String code=c.get("conceptCode").toString();
		return code;
	}

}
