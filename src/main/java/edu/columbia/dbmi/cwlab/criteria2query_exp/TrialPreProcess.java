package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class TrialPreProcess {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// findSimilarCriteria();
		//cleanAllEC();
		String ecfilename="/Users/cy2465/Documents/datasets/AllPublicXML-EC/NCT0303xxxx/NCT03030027.txt";
		String eccontent = FileUtil.readFile(ecfilename);
		
		//System.out.println(eccontent);
		File file = new File(ecfilename);
		
		String[] lines = eccontent.split("\n");
		StringBuffer sb = new StringBuffer();
		for (int x = 1; x < lines.length; x++) {
			if (lines[x - 1].trim().length() == 0) {
				// System.out.println("iiiiin=" +
				// removeMultiSpace(lines[x]));
				String res = removeMultiSpace(lines[x]);
				if (res.startsWith("-  ")) {
					res = res.substring(3);
				}
				sb.append("\n" + res);
			} else {
				// System.out.println("add=" + removeMultiSpace(lines[x]));
				sb.append(" " + removeMultiSpace(lines[x]));
			}
		}
		//System.out.println("____________");
		System.out.println(sb.toString());
		String ecstr = sb.toString();
		String[] inc_exc=separateIncExc(ecstr);
		System.out.println("----inc---");
		System.out.println(inc_exc[0]);
		System.out.println("----exc---");
		System.out.println(inc_exc[1]);
	}

	public static void cleanAllEC() {
		List<String> files = preprocessAll();
		StringBuffer incexcfilelist=new StringBuffer();
		///Users/cy2465/Documents/datasets/AllPublicXML-EC/NCT0303xxxx/NCT03030027.txt
		for (int i = 0; i < files.size(); i++) {
			String ecfilename=files.get(i);
			String eccontent = FileUtil.readFile(ecfilename);
			System.out.println("file=" + files.get(i));
			//System.out.println(eccontent);
			File file = new File(files.get(i));
			if (!file.exists()) {
				continue;
			}
			String[] lines = eccontent.split("\n");
			StringBuffer sb = new StringBuffer();
			for (int x = 1; x < lines.length; x++) {
				if (lines[x - 1].trim().length() == 0) {
					// System.out.println("iiiiin=" +
					// removeMultiSpace(lines[x]));
					String res = removeMultiSpace(lines[x]);
					if (res.startsWith("-  ")) {
						res = res.substring(3);
					}
					sb.append("\n" + res);
				} else {
					// System.out.println("add=" + removeMultiSpace(lines[x]));
					sb.append(" " + removeMultiSpace(lines[x]));
				}
			}
			//System.out.println("____________");
			//System.out.println(sb.toString());
			String ecstr = sb.toString();
			String[] inc_exc=separateIncExc(ecstr);
//			System.out.println(files.get(i)+"----inc---");
//			System.out.println(inc_exc[0]);
//			System.out.println(files.get(i)+"----exc---");
//			System.out.println(inc_exc[1]);
			FileUtil.write2File(files.get(i)+".inc.txt", inc_exc[0]);
			incexcfilelist.append(files.get(i)+".inc.txt"+"\n");
			FileUtil.write2File(files.get(i)+".exc.txt", inc_exc[1]);
			incexcfilelist.append(files.get(i)+".exc.txt"+"\n");
		}
		FileUtil.write2File("/Users/cy2465/Documents/allincexc.txt", incexcfilelist.toString());
	}

	public static String[] separateIncExc(String ecstr) {
		int incindex = ecstr.toLowerCase().indexOf("inclusion criteria");
		int excindex = ecstr.toLowerCase().indexOf("exclusion criteria");
		String[] eclines = ecstr.split("\n");
		String[] inc_exc = new String[2];
		StringBuffer incsb = new StringBuffer();
		StringBuffer excsb = new StringBuffer();
		boolean incflag = false;
		boolean excflag = false;
		if (incindex > -1 && excindex > -1) {
			for (String r : eclines) {
				if (r.toLowerCase().contains("inclusion criteria:")) {
					incflag = true;
					excflag = false;
					continue;
				}
				if (r.toLowerCase().contains("exclusion criteria:")) {
					excflag = true;
					incflag = false;
					continue;
				}
				if (incflag == true && excflag == false) {
					incsb.append(r + "\n");
				}
				if (excflag == true && incflag == false) {
					excsb.append(r + "\n");
				}
			}

		} else if (incindex != -1 && excindex == -1) {
			for (String r : eclines) {
				if (r.toLowerCase().contains("inclusion criteria")) {
					incflag = true;
					continue;
				}
				incsb.append(r + "\n");
			}

		} else if (incindex == -1 && excindex != -1) {
			for (String r : eclines) {
				if (r.toLowerCase().contains("exclusion criteria")) {
					continue;
				}
				excsb.append(r + "\n");
			}
		} else {
			incsb.append(ecstr);
		}
		inc_exc[0]=incsb.toString();
		inc_exc[1]=excsb.toString();
		return inc_exc;
	}

	public static String removeMultiSpace(String s) {
		String[] str = s.split("\\s+ ");
		// System.out.println("~~"+str.length);
		// if (str.length > 1) {
		// return str[1];
		// } else {
		// return s;
		// }
		int index = 0;
		for (int i = 0; i < s.length() - 1; i++) {
			if (s.charAt(i) != ' ') {
				index = i;
				break;
			}
		}
		//System.out.println(s.substring(index));
		return s.substring(index);
	}

	public static void randomSeletct() {
		String base_dir = "/Users/cy2465/Documents/project/1_Criteria2query/exptrials_new_20/";
		String content = FileUtil
				.readFile("/Users/cy2465/Documents/project/1_Criteria2query/trialsamples/allsamples.txt");
		String[] rows = content.split("\n");

		String[] trials = getRandomArray(rows, 20);

		StringBuffer allnctid = new StringBuffer();
		String existingtrial = FileUtil.readFile(base_dir + "alltrials.txt");
		for (String r : trials) {
			// System.out.println("=>"+r);
			if (existingtrial.contains(r)) {
				continue;
			}
			String[] line = r.split("\t");
			if (line.length > 2) {
				System.out.println(line[0]);
				System.out.println(line[1]);
				String incstr = line[1].substring(20);
				System.out.println(incstr);
				System.out.println(line[2]);
				String excstr = "";
				if (line[2].length() > 20) {
					excstr = line[2].substring(20);
				}
				System.out.println(excstr);
				allnctid.append(line[0] + "\n");
				FileUtil.write2File(base_dir + line[0] + "_inc.txt", incstr);
				FileUtil.write2File(base_dir + line[0] + "_exc.txt", excstr);
			}
		}
		FileUtil.write2File(base_dir + "alltrials.txt", allnctid.toString());
		System.out.println(trials.length);
	}

	public static String[] getRandomArray(String[] paramArray, int count) {
		if (paramArray.length < count) {
			return paramArray;
		}
		String[] newArray = new String[count];
		Random random = new Random();
		int temp = 0;// 接收产生的随机数
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= count; i++) {
			temp = random.nextInt(paramArray.length);// 将产生的随机数作为被抽数组的索引
			if (!(list.contains(temp))) {
				newArray[i - 1] = paramArray[temp];
				list.add(temp);
			} else {
				i--;
			}
		}
		return newArray;
	}

	public static List<String> preprocessAll() {
		List<String> flist = new ArrayList<String>();
		String content = FileUtil.readFile("/Users/cy2465/Documents/datasets/All_trial_files.txt");
		// System.out.println(content);
		String[] lines = content.split("\n");
		for (String r : lines) {
			// System.out.println("r="+r);
			r = r.replace("Downloads", "Documents/datasets");
			r = r.replace("AllPublicXML", "AllPublicXML-EC");
			r = r.replace("xml", "txt");
			// System.out.println("after="+r);
			// System.out.println(FileUtil.Readfile(r));
			flist.add(r);
		}
		return flist;
	}

	public static void findSimilarCriteria() {
		File fs = new File("/Users/cy2465/Downloads/trials");
		File[] flist = fs.listFiles();
		StringBuffer sb = new StringBuffer();
		for (File f : flist) {
			if (f.getAbsolutePath().endsWith(".txt")) {
				System.out.println(f.getAbsolutePath());
				String s = FileUtil.readFile(f.getAbsolutePath());
				String[] arr = s.split("\n");
				int total = 0;
				int count = 0;
				for (String a : arr) {
					total = total + a.length();
					count++;
				}
				int avg = total / count;
				System.out.println("avg=" + avg);
				if (avg > 10) {
					sb.append(f.getAbsolutePath() + "\n");
					sb.append(s + "\n");
					sb.append("-----------------------\n");
				}
			}
		}
		System.out.println(sb.toString());
		// FileUtil.write2File("/Users/cy2465/Documents/project/1_Criteria2query/expdata/validate_set.txt",
		// sb.toString());
	}

}
