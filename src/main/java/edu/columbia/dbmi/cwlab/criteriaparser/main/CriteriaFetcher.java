package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.columbia.dbmi.cwlab.util.FileUtil;




public class CriteriaFetcher {
	
	public static void main( String[] args )
    {
//        String spath=args[0];
//        String tpath=args[1];
//        String allnctids=FileUtil.readFile(spath);
//        String[] nctids=allnctids.split("\n");
//        List<String> ids=new ArrayList<String>();
//        for(String i:nctids){
//        	ids.add(i);
//        }
		
       // fetchEC(ids,"/Users/cy2465/Documents/");
		CriteriaFetcher cf=new CriteriaFetcher();
		System.out.println(cf.fetchEligibilityCriteriaByNCTID("NCT03105011"));
    }
	
	public String fetchEligibilityCriteriaByNCTID(String nctid) {
		StringBuffer sb=new StringBuffer();
		try {
			String url="https://clinicaltrials.gov/ct2/show/"+nctid+"/";
			Document doc = Jsoup.connect(url).get();
			Elements content = doc.getElementsByClass("ct-header3");
			for (Element link : content) {
				if(link.text().equals("Criteria")){
					Element se=link.nextElementSibling();
					Elements earray=se.getAllElements();
					for(Element ea:earray){
						if(ea.getAllElements().size()==1){
							sb.append(ea.text()+"\n");
						}
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

	public void fetchEC(List<String> nctidlist,String targetpath) {
		int cc=0;
		for(String nctid:nctidlist){
			cc = cc+1;
			System.out.println(cc +" of "+nctidlist.size()+" :"+nctid);
			StringBuffer sb=new StringBuffer();
			try {
				String url="https://clinicaltrials.gov/ct2/show/"+nctid;
//				System.out.println("HI");
				Document doc = Jsoup.connect(url).get();
//				System.out.println("Hello");
				Elements content = doc.getElementsByClass("ct-header3");
				for (Element link : content) {
					if(link.text().equals("Criteria")){
						//System.out.println("=====");
						Element se=link.nextElementSibling();
						Elements earray=se.getAllElements();
						for(Element ea:earray){
							if(ea.getAllElements().size()==1){
								sb.append(ea.text()+"\n");
//								sb.append(ea.text());
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			String b ="c" + nctid + "a";

			String finalpath = targetpath + b.substring(1,12) + ".txt";
			System.out.println(targetpath + b.substring(1,12) + ".txt");


//			String file_path = new String(nctid);
//
//			String extension = new String(".txt");
			FileUtil.write2File(finalpath, sb.toString());

		}
	}
}
