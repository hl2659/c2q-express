package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.util.List;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class MeasurementUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content = FileUtil.readFile("/Users/cy2465/Documents/measurements.txt");
		
		String[] rows = content.split("\n");
		
		
		StringBuffer sb = new StringBuffer();
		int id=0;
		for (String r : rows) {
			String[] elements = r.split("\t");
			// System.out.println(elements[9]);
			String re = elements[9].toLowerCase();
			
			//System.out.println("=>"+re);
			//System.out.println("min="+min+"\t"+"max="+max+"\t"+"times="+times);
			String neg="0";
			if(elements[5].equals("true")){
				neg="1";
			}
			//sb.append(id+"\t"+elements[0]+"\t"+elements[1]+"\t"+elements[2]+"\t"+elements[3]+"\t"+elements[4]+"\t"+neg+"\t"+elements[6]+"\t"+elements[7]+"\t"+elements[8]+"\t"+elements[9]+"\t"+min+"\t"+max+"\t"+times+"\t"+unit+"\n");
		}
		
		FileUtil.write2File("/Users/cy2465/Documents/measurements_standard.txt", sb.toString());
	}
	
	public static String batchStandardizeValueRanges(String[] ranges){
		StringBuffer sb=new StringBuffer();
		for(String range:ranges){
			Double[] rangenum=standardValueRange(range);
			sb.append(range+"\t"+rangenum[0]+"\t"+rangenum[1]+"\n");
		}
		return sb.toString();
	}
	
	public static Double[] standardValueRange(String re){
		Double[] range=new Double[2];
		re = re.replace("no less than or equal to", ">");
		re = re.replace("less than or equal to", "≤");
		re = re.replace("no greater than or equal to", "<");
		re = re.replace("greater than or equal to", "≥");
		re = re.replace("no greater than ", "≤");
		re = re.replace("no less than ", "≥");
		re = re.replace("<=", "≤");
		re = re.replace("< =", "≤");
		re = re.replace("> =", "≥");
		re = re.replace(">=", "≥");
		re = re.replace(",", "");
		re=re.replace("m3", "");
		re=re.replace("m2", "");//mm ^ 3
		re=re.replace("mm ^ 3", "");//m ^ 2
		re=re.replace("m ^ 2", "");
		Double min = Double.NEGATIVE_INFINITY;
		Double max = Double.POSITIVE_INFINITY;
		Integer inorout = 1;
		Integer eq = 1;
		Integer times=0;
		String unit="tobeadded";
		String mvalue = re;
		List<Double> m = NumericConvert.recognizeNumbersAdvanced(re);
		
		if (m != null) {
			if (m.size() >= 2) {
				if (mvalue.toLowerCase().contains("or")) {
					min = m.get(0);
					max = m.get(1);
					inorout = 0;

				} else {
					min = m.get(0);
					max = m.get(1);
					inorout = 1;
				}
			} else if (m.size() == 1) {
				// System.out.println("1 number");
				if ((mvalue.indexOf(">") != -1) || (mvalue.indexOf("≥") != -1) || (mvalue.indexOf("more") != -1)
						|| (mvalue.indexOf("greater") != -1) || (mvalue.indexOf("larger") != -1)
						|| (mvalue.indexOf("above") != -1) || (mvalue.indexOf("at least") != -1)
						|| (mvalue.indexOf("higher") != -1)|| (mvalue.indexOf("over") != -1)
						|| (mvalue.indexOf("excess") != -1)|| (mvalue.indexOf("exceeding") != -1)
						|| (mvalue.indexOf("exceed") != -1)|| (mvalue.indexOf("＞") != -1)
						|| (mvalue.indexOf("≧") != -1)) {
					// System.out.println("> ≥");
					if(mvalue.indexOf("not")==-1 &&mvalue.indexOf("no")==-1  ){
						min = m.get(0);
					}else{
						max=m.get(0);
					}
				} else if ((mvalue.indexOf("<") != -1) || (mvalue.indexOf("≤") != -1 )|| (mvalue.indexOf("≦") != -1)
						|| (mvalue.indexOf("less") != -1) || (mvalue.indexOf("lower") != -1)
						|| (mvalue.indexOf("smaller") != -1) || (mvalue.indexOf("within") != -1)
						|| (mvalue.indexOf("up to") != -1)|| (mvalue.indexOf("below") != -1)
						||(mvalue.indexOf("＜") != -1)||(mvalue.indexOf("under") != -1)) {
					// System.out.println("<");
					if(mvalue.indexOf("not")==-1 &&mvalue.indexOf("no")==-1  ){
						max = m.get(0);
					}else{
						min=m.get(0);
					}
				} else {
					if(mvalue.toLowerCase().contains("uln")){
						max=m.get(0);
					}else{
						min=m.get(0);
					}
				}
			}
		}
		if(mvalue.toLowerCase().contains("uln")||mvalue.toLowerCase().contains("upper limit of normal")){
			times=1;
		}
		range[0]=max;
		range[1]=min;
		return range;
	}
}
