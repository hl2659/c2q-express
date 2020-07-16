package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.util.HashMap;
import java.util.List;

import edu.columbia.dbmi.cwlab.pojo.Document;
import edu.columbia.dbmi.cwlab.pojo.Paragraph;
import edu.columbia.dbmi.cwlab.serviceimpl.InformationExtractionServiceImpl;
import edu.columbia.dbmi.cwlab.util.FileUtil;
import edu.columbia.dbmi.cwlab.util.IOUtil;

public class ParseThread extends Thread {
	InformationExtractionServiceImpl ieService = new InformationExtractionServiceImpl();
	String index_file;
	String target_dir;
	int start_index;
	int end_index;

	public ParseThread(String index_file, String target_dir, int start_index,int end_index) {
		this.index_file = index_file;
		this.target_dir = target_dir;
		this.start_index=start_index;
		this.end_index=end_index;
	}

	public void run() {
		String indexc=FileUtil.readFile(this.index_file);
		String[] ecindex=indexc.split("\n");
		HashMap<String,String> pathmap=new HashMap<String,String>();
		for(String ecr:ecindex){
			String[] es=ecr.split("\t");
			pathmap.put(es[0], es[1]);
			
		}
		String targetdir = this.target_dir;
		for (int c = this.start_index; c < this.end_index;c++) {
			try {	
				String nctid =  ecindex[c].split("\t")[0];
				StringBuffer incsb = new StringBuffer();
				StringBuffer excsb = new StringBuffer();
				String content = FileUtil.readFile(pathmap.get(nctid));
				String[] rows = content.split("\n");
				boolean flag = true;
				if (rows[0].toLowerCase().contains("inclusion")) {

				} else {
					incsb.append(rows[0] + "\n");
				}
				for (int x = 1; x < rows.length; x++) {
					if (rows[x].toLowerCase().contains("exclusion")) {
						flag = false;
						continue;
					}
					if (flag == false) {
						excsb.append(rows[x] + "\n");
					} else {
						incsb.append(rows[x] + "\n");
					}
				}
				Document doc = ieService.translateByDoc("", incsb.toString(), excsb.toString());
				doc = ieService.patchIEResults(doc);
				List<Paragraph> incps = doc.getInclusion_criteria();
				List<Paragraph> excps = doc.getExclusion_criteria();
				String aaa = IOUtil.Pargraph2List(incps, "INC", nctid);
				String bbb = IOUtil.Pargraph2List(excps, "EXC", nctid);
				StringBuffer outsb = new StringBuffer();
				outsb.append(aaa);
				outsb.append(bbb);
				FileUtil.write2File(targetdir + nctid + ".c2q", outsb.toString());
				System.out.println("write to " + targetdir + nctid + ".c2q");
			} catch (Exception ex) {
				System.out.println("ex message="+ex.toString());
				continue;
			}

		}
	}

}
