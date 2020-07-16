package edu.columbia.dbmi.cwlab.criteriaparser.main;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class AttributeNormalization {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String allrows=FileUtil.readFile("/Users/cy2465/Documents/all_parsing_results_w_standard_id.txt");
		String[] rows=allrows.split("\n");
		TemporalNormalize tn=new TemporalNormalize();
		StringBuffer sb=new StringBuffer();
		for(String r:rows){
			//System.out.println("~"+r);
			String[] er=r.split("\t");
			Integer temporal=tn.temporalNormalizeforNumberUnit(er[8]);
			Double[] rangenum=MeasurementUtil.standardValueRange(er[9]);
			String numeric=rangenum[0]+"\t"+rangenum[1];
			sb.append(er[0]+"\t"+temporal+"\t"+numeric+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Documents/all_parsing_results_standardized.txt", sb.toString());
	}

}
