package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import edu.columbia.dbmi.cwlab.util.FileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MappingEva {
	private final static String vocubularyurl ="http://api.ohdsi.org/WebAPI/vocabulary/1PCT/search";
	
	
	public static void main(String[] args) throws UnsupportedEncodingException, ClientProtocolException, IOException {
		// TODO Auto-generated method stub
//		String content=FileUtil.Readfile("/Users/cy2465/Documents/project/1_Criteria2query/exptrials_new_20/mapping.txt");
//		String[] lines=content.split("\n");
//		for(String l:lines){
//			System.out.println("=>"+l);
//		}
		
		
		String content=FileUtil.readFile("/Users/cy2465/Documents/project/1_Criteria2query/tempids.csv");
		String[] rows=content.split("\r\n");
		StringBuffer sb=new StringBuffer();
		for(String r:rows){
			System.out.println("r="+r);
			if(r.equals("0")){
				System.out.println("!!!!!");
				sb.append(r+"\t"+"None"+"\n");
			}else if(r.contains("/")){
				System.out.println("????");
				String[] arr=r.split("/");
				String c1=arr[0].trim();
				String c2=arr[1].trim();
				sb.append(r+"\t"+searchForTerm(c1)+"#"+searchForTerm(c2)+"\n");
				
			}else if(r.length()<3){
				sb.append(r+"\t"+"None"+"\n");
			}
			else{
				System.out.println("____");
				String c1=r.trim();
				sb.append(r+"\t"+searchForTerm(c1)+"\n");
			}
		}
		FileUtil.write2File("/Users/cy2465/Documents/project/1_Criteria2query/tempid_term.txt", sb.toString());
	}
	
	
	public static String searchForTerm(String id){
		String result="none";
		try{
		JSONObject queryjson = new JSONObject();
		queryjson.accumulate("QUERY", id);
		queryjson.accumulate("STANDARD_CONCEPT","S");//only standard concept
//		queryjson.accumulate("VOCABULARY_ID", "['SNOMED']");
		System.out.println("queryjson:" + queryjson);
		String vocabularyresult = getConcept(queryjson);
		System.out.println("voc="+vocabularyresult);
		JSONArray ja=JSONArray.fromObject(vocabularyresult);
		JSONObject jo=(JSONObject) ja.get(0);
			result=(String) jo.get("CONCEPT_NAME");
		}catch(Exception ex){
			
		}
		return result;
		
	}
	public static String getConcept(JSONObject jjj)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		HttpPost httppost = new HttpPost(vocubularyurl);
		// httppost.setHeader("X-GWT-Permutation",
		// "3DE824138FE65400740EC1816A73CACC");
		httppost.setHeader("Content-Type", "application/json");
		StringEntity se = new StringEntity(jjj.toString());
		httppost.setEntity(se);
		
		HttpResponse httpresponse = new DefaultHttpClient().execute(httppost);
		
		System.out.println("statusCode:" + httpresponse.getStatusLine().getStatusCode());
		//System.out.println("Call API time (unit:millisecond)：" + (endTime - startTime));

		if (httpresponse.getStatusLine().getStatusCode() == 200) {
			// System.out.println("succeed!");
			String strResult = EntityUtils.toString(httpresponse.getEntity());
			return strResult;
			// httppost.
		} else {
			return null;
		}
	}

}
