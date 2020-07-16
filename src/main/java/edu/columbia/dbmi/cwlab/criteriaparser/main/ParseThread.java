package edu.columbia.dbmi.cwlab.criteriaparser.main;

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
//		int last_index_N = this.index_file.lastIndexOf("N");
//		int last_index_fullstop = this.index_file.lastIndexOf(".");
// 		String nctid = this.index_file.substring(last_index_N,last_index_fullstop);
		String[] ecindex=indexc.split("\r\n");
		HashMap<String,String> pathmap=new HashMap<String,String>();
//		pathmap.put(nctid,this.index_file);

	for(String ecr:ecindex){ 	//Chi's code
			String[] es=ecr.split("\t");
			pathmap.put(es[0], es[1]);
//			pathmap.put(nctid,es[0]);

		}
		String targetdir = this.target_dir;
		Integer error_id=0;
		for (int c = this.start_index; c < this.end_index;c++) {
			try {
				
				error_id=c;
				String nctid =  ecindex[c].split("\t")[0]; //Chi's code line
				StringBuffer incsb = new StringBuffer();
				StringBuffer excsb = new StringBuffer();
				String content = FileUtil.readFile(pathmap.get(nctid));
				String[] rows = content.split("\n");
				//System.out.println(nctid+"\t row length="+rows.length);
				boolean flag = true;
				//System.out.println("-------1-------");
				if (rows[0].toLowerCase().contains("inclusion")) {
					
				} else {
					incsb.append(rows[0] + "\n");
				}
				//System.out.println("-------2-------");
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
				//System.out.println("-------3-------");
				Document doc = ieService.translateByDoc("", incsb.toString(), excsb.toString());
				//System.out.println("-------4-------");
				doc = ieService.patchIEResults(doc);
				List<Paragraph> incps = doc.getInclusion_criteria();
				List<Paragraph> excps = doc.getExclusion_criteria();
				//System.out.println("-------5-------");
				String aaa = IOUtil.Pargraph2List(incps, "INC", nctid);
				String bbb = IOUtil.Pargraph2List(excps, "EXC", nctid);
				//System.out.println("-------6-------");
				StringBuffer outsb = new StringBuffer();
				outsb.append(aaa);
				outsb.append(bbb);
				FileUtil.write2File(targetdir + nctid + ".c2q", outsb.toString());
				System.out.println("write to " + targetdir + nctid + ".c2q");
			} catch (Exception ex) {
				System.out.println(error_id+"\t"+ecindex.length+" ["+this.start_index+"~"+ this.end_index+"]\t ex message="+ex.toString());
				continue;
			}

		}
	}

}
