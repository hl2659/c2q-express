package edu.columbia.dbmi.cwlab.criteria2query_exp;

import edu.columbia.dbmi.cwlab.pojo.GlobalSetting;
import edu.columbia.dbmi.cwlab.util.FileUtil;
import edu.columbia.dbmi.cwlab.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ConceptMappingEval {
	
	private final static String conceptseturl = GlobalSetting.ohdsi_api_base_url+"conceptset/";
	private final static String usagi = GlobalSetting.concepthub+"/omop/searchOneEntityByTermAndDomain";
	private final static String umlsurl = GlobalSetting.concepthub+"/umls/searchUMLS";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String c=FileUtil.readFile("/Users/cy2465/Documents/mapped_results.txt");
		System.out.println("c="+c);
		String[] arr=c.split("\n");
		//getConceptListByUsagi("type 2 diabetes","Condition");
		StringBuffer sb=new StringBuffer();
		for(String r:arr){
			//System.out.println("r="+r);
			String[] es=r.split("\t");
			String term=es[0].substring(5).replace(" ", "%20");
			//String domain=es[2].replace(" ", "%20");
			String url="http://athena.ohdsi.org/api/v1/concepts?pageSize=5&page=1&query="+term;
			String response=HttpUtil.doGet(url);
			//System.out.println(response);
			JSONObject jo=JSONObject.fromObject(response);
			System.out.println("res="+jo);
			JSONArray ja=JSONArray.fromObject(jo.get("content"));
			if(ja.size()>0){
				JSONObject concept=JSONObject.fromObject(ja.get(0));
				System.out.println(concept.get("name"));
				sb.append(term+"\t"+concept.get("name")+"["+concept.get("id")+"]"+"["+concept.get("vocabulary")+"]"+"\n");
			}else{
				sb.append(term+"\t"+"unmapped"+"\n");
			}
			//System.out.println("re="+response);
		}
		FileUtil.write2File("/Users/cy2465/Documents/athena_mapped_term.txt", sb.toString());
		
	}



	public static void metamapRes() {
		String c=FileUtil.readFile("/Users/cy2465/Documents/mapped_results.txt");
		String[] arr=c.split("\n");
		
		String s=FileUtil.readFile("/Users/cy2465/Documents/metamap_res.txt");
		System.out.println("s="+s);
		String[] rows=s.split("\n");
		int count=0;
		boolean f=true;
		boolean mf=true;
		StringBuffer subsb=new StringBuffer();
		StringBuffer sboutput=new StringBuffer();
		for(int i=0;i<rows.length;i++){
			
			//System.out.println("r="+r);
			if(rows[i].startsWith("Processing 00000000.tx.1:")){
				//System.out.println(subsb.toString());
				String temp=subsb.toString();
				subsb.setLength(0);
				
				f=false;
				mf=false;
				count++;
				
				//System.out.println("!!!!!"+arr[count-2].split("\t")[0].substring(5));
				//System.out.println(temp+"\n");
				sboutput.append(temp+"\n");
				//System.out.println("->"+rows[i]+"\n");
				
			}
			if(rows[i].startsWith("Meta Mapping")&&f==true){
				mf=true;
			}
			if(rows[i].startsWith("Meta Mapping")&&f==false){
				f=true;
				subsb.append(rows[i]+"=>");
				//System.out.println("ssss===>"+rows[i]);
				//subsb.setLength(0);
			}
			if(rows[i].startsWith("  ")&&f==true&&mf==false){
				subsb.append(rows[i]+"  &&");
				//subsb.append(rows[i]+"\n");
			}
			
		}
		System.out.println("count="+count);
		FileUtil.write2File("/Users/cy2465/Documents/mapped_results_metamap.txt", sboutput.toString());
	}
	
	
	
	public static String[] getConceptListByUsagi(String term,String domain){
		String[] res=new String[2];
		JSONObject jo=new JSONObject();
		jo.accumulate("term", term);
		jo.accumulate("domain", domain);
		String result=HttpUtil.doPost(usagi, jo.toString());
		System.out.println("result="+result);
		JSONObject bestconcept=JSONObject.fromObject(result);
		try{
		System.out.println("matchScore="+bestconcept.getDouble("matchScore"));
		String matchs=String.format("%.2f", bestconcept.getDouble("matchScore")*100);
		//System.out.println("==>"+matches);
		JSONObject concept_jo=bestconcept.getJSONObject("concept");
		Integer cId=concept_jo.getInt("conceptId");
		System.out.println(concept_jo);
		//System.out.println("conceptset_json"+res[0]);
		res[1]=matchs;
		}catch(Exception ex){
			JSONArray conceptSet=new JSONArray();
			res[0]=conceptSet.toString();
			res[1]="0";
		}
		return res;
	}

}
