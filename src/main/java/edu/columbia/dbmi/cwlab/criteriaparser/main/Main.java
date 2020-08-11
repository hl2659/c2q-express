package edu.columbia.dbmi.cwlab.criteriaparser.main;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.columbia.dbmi.cwlab.criteriaparser.main.ParseThread;
import edu.columbia.dbmi.cwlab.criteriaparser.cli.Args;
import edu.columbia.dbmi.cwlab.criteriaparser.cli.Argument;
import edu.columbia.dbmi.cwlab.util.FileUtil;
import org.ohdsi.usagi.apis.ConceptSearchAPI;

import static java.lang.System.out;

public class Main
{
    private static class Option
    {
    	@Argument(description = "fetch task")
        boolean fetch;
    	
    	@Argument(description = "parse task")
        boolean parse;
    	
    	@Argument(description = "all pipeline task for normalization")
        boolean all;

		@Argument(description = "complete pipeline task from fetch to normalization")
		boolean complete;
    	
    	@Argument(description = "normalize task")
        boolean normalize;
    	
    	@Argument(description = "entity normalization")
        boolean entity;
    	
    	@Argument(description = "numeric normalization")
        boolean numeric;
    	
    	@Argument(description = "temporal normalization")
        boolean temporal;

    	@Argument(description = "Concept Mapping Method")
        String mapping_method;
    	
    	@Argument(description = "NCTID List")
        String nctid_path;

		@Argument(description = "NCTID List: Update ctgov_trial_condition and" +
				" ctgov_trial_intervention")
		String nctid_path2;
    	
    	@Argument(description = "Usagi Path")
        String usagi_path;
    	
    	@Argument(description = "Criterion text index")
        String criteria_path;
    	
    	@Argument(description = "Value Range index")
        String value_range_path;
    	
    	@Argument(description = "Temporal text")
        String temporal_path;

		@Argument(description = "Local database hostname")
		String host;

		@Argument(description = "Local Database port")
		String port;

		@Argument(description = "local Database name")
		String database_name;

		@Argument(description = "local Database username")
		String username1;

		@Argument(description = "local Database password")
		String pass1;

		@Argument(description = "AACT Database password")
		String pass2;

		@Argument(description = "AACT Database password")
		String username2;
    	
    	@Argument(description = "Entity text")
        String entity_path;
    	
        @Argument(description = "Input file dir")
        String input_dir;

        @Argument(description = "Result dir path")
        String result_dir;

		@Argument(description = "Result dir path for parsing step")
		String result_dir2;
        
        @Argument(description = "Result file path")
        String result_path;

		@Argument(description = " Call trial_condition")
		boolean cid;

		@Argument(description = "Call common condition")
		boolean ccd;

        @Argument(description = "The number of threads")
        Integer thread = Runtime.getRuntime().availableProcessors();
    }

    public static void main(String[] args)
    {

        //nohup time java -jar averaged-perceptron-segment-1.0.jar -train -model 2014_2w.bin -reference 2014_blank.txt -development 2014_1k.txt > log.txt
        Option option = new Option();
        try
        {
            Args.parse(option, args);
            if(option.fetch==true){
	            List<String> nctids=getNCTIDs(option.nctid_path);
//				System.out.println(option.nctid_path);
	            CriteriaFetcher cfetcher=new CriteriaFetcher();
//	            System.out.println(option.result_dir);
	            cfetcher.fetchEC(nctids, option.result_dir);
            }else if(option.parse==true){
            	String nctc=FileUtil.readFile(option.criteria_path);
        		String[] nctarry=nctc.split("\n");
        		int tobep_num=nctarry.length;
        		int tcount=option.thread;
        		for(int x=0;x<option.thread;x++){
        			new ParseThread(option.criteria_path,option.result_dir,((tobep_num/tcount)*x),((tobep_num/tcount)*(x+1)+1)).start();
        		}
            }else if(option.normalize==true) {
				if (option.numeric == true) {
					String range_value = FileUtil.readFile(option.value_range_path);
					out.println("1");
					String[] range_arr = range_value.split("\n");
					String output = MeasurementUtil.batchStandardizeValueRanges(range_arr);
					FileUtil.write2File(option.result_path, output);
				} else if (option.temporal == true) {
					String temporal = FileUtil.readFile(option.temporal_path);
					String[] temporal_arr = temporal.split("\n");
					TemporalNormalize tn = new TemporalNormalize();
					String output = tn.batchTemporalNormalize(temporal_arr);
					out.println(output);
					FileUtil.write2File(option.result_path, output);
				} else if (option.entity == true) {
					String entity = FileUtil.readFile(option.entity_path);
					String usagipath = option.usagi_path;
					out.println("3");
					out.printf("->%s", "Usagi path=" + usagipath + "\n");
					out.printf("->%s", "Entity path=" + option.entity_path + "\n");
					String[] tobemappedentities = entity.split("\n");
					List<String> entitylist = new ArrayList<String>();
					for (String r : tobemappedentities) {
						entitylist.add(r);
					}
					int tobep_num = entitylist.size();
					int tcount = option.thread;
					for (int x = 0; x < option.thread; x++) {
						new MappingThread(String.valueOf(x), ((tobep_num / tcount) * x), ((tobep_num / tcount) * (x + 1)), entitylist, usagipath, option.result_dir).start();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (option.all == true) {
					String range_value = FileUtil.readFile(option.value_range_path);
					String[] range_arr = range_value.split("\n");
					String output1 = MeasurementUtil.batchStandardizeValueRanges(range_arr);
					FileUtil.write2File(option.result_path, output1);
					String temporal = FileUtil.readFile(option.temporal_path);
					String[] temporal_arr = temporal.split("\n");
					TemporalNormalize tn = new TemporalNormalize();
					String output2 = tn.batchTemporalNormalize(temporal_arr);
					//System.out.println(output2);
					FileUtil.write2File(option.result_path, output2);
					String entity = FileUtil.readFile(option.entity_path);
					String usagipath = option.usagi_path;
					out.println("3");
					out.printf("->%s", "Usagi path=" + usagipath + "\n");
					out.printf("->%s", "Entity path=" + option.entity_path + "\n");
					String[] tobemappedentities = entity.split("\n");
					List<String> entitylist = new ArrayList<String>();
					for (String r : tobemappedentities) {
						entitylist.add(r);
					}
					int tobep_num = entitylist.size();
					int tcount = option.thread;
					for (int x = 0; x < option.thread; x++) {
						new MappingThread(String.valueOf(x), ((tobep_num / tcount) * x), ((tobep_num / tcount) * (x + 1)), entitylist, usagipath, option.result_dir).start();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
            else if(option.complete == true){
				List<String> nctids=getNCTIDs(option.nctid_path);
				CriteriaFetcher cfetcher=new CriteriaFetcher();
				cfetcher.fetchEC(nctids, option.result_dir);
				File directorypath = new File(option.result_dir);
				String contents[] = directorypath.list();
				String final_content ="";
				for (int j=0; j <contents.length;j++){
					contents[j] = contents[j].substring(0,contents[j].indexOf("."));
					contents[j] = contents[j] +"\t"+ option.result_dir + contents[j]+".txt";
					final_content += contents[j]+"\n";
				}
				FileUtil.write2File(option.criteria_path, final_content);
				String nctc=FileUtil.readFile(option.criteria_path);
				String[] nctarry=nctc.split("\n");
				int tobep_num=nctarry.length;
				int tcount=option.thread;
				for(int x=0;x<option.thread;x++) {

					ParseThread p1 = new ParseThread(option.criteria_path, option.result_dir2, ((tobep_num / tcount) * x), ((tobep_num / tcount) * (x + 1) + 1));
					p1.start();
					p1.join();

				}
//				System.out.println("2");
				File directorypath2 = new File(option.result_dir2);
				String contents2[] = directorypath2.list();
				for (int j=0; j <contents2.length;j++){
					contents2[j] = option.result_dir2 + contents2[j];
				}
				for (int k=0;k<contents2.length;k++) {
					//System.out.println(contents2[k]);
					//String range_value = FileUtil.readFile(option.value_range_path);
					String range_value = FileUtil.readFile(contents2[k]);
					String[] range_arr = range_value.split("\n");
					String output1 = MeasurementUtil.batchStandardizeValueRanges(range_arr);
					//FileUtil.write2File(option.result_path, output1);
					//FileUtil.write2File(contents2[k], output1);
					//String temporal = FileUtil.readFile(option.temporal_path);
					//String temporal = FileUtil.readFile(contents2[k]);
					String temporal = output1;
					String[] temporal_arr = temporal.split("\n");
					TemporalNormalize tn = new TemporalNormalize();
					String output2 = tn.batchTemporalNormalize(temporal_arr);
					//FileUtil.write2File(option.result_path, output2);
					//FileUtil.write2File(contents2[k], output2);
					//String entity = FileUtil.readFile(option.entity_path);
					//String entity = FileUtil.readFile(contents2[k]);
					String entity = output2;
					String usagipath = option.usagi_path;
					out.printf("->%s", "Usagi path=" + usagipath + "\n");
					out.printf("->%s", "Entity path=" + contents2[k] + "\n");
					String[] tobemappedentities = entity.split("\n");
					List<String> entitylist = new ArrayList<String>();
					for (String r : tobemappedentities) {
						entitylist.add(r);
					}
					int tobep_num1 = entitylist.size();
					int tcount1 = option.thread;
					for (int x = 0; x < option.thread; x++) {
						new MappingThread(String.valueOf(x), ((tobep_num1 / tcount1) * x), ((tobep_num1 / tcount1) * (x + 1)), entitylist, usagipath, option.result_dir2).start();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
            else if(option.cid == true){
				trial_condition trial=new trial_condition();
				trial.fetchid(option.nctid_path2,option.usagi_path,
						option.host,option.port,option.database_name,
						option.username1,option.pass1,option.username2,
						option.pass2);

			}
			else if(option.ccd == true){
				common_condition com =new common_condition();
				com.fetchCondition();

			}
        }
        catch (IllegalArgumentException e)
        {
            System.err.println(e.getMessage());
            Args.usage(option);
        }
        catch (Exception e)
        {
            System.err.println("IO Exception，Please check the file path");
            e.printStackTrace();
        }
    }
    
    public static List<String> getNCTIDs(String filepath){
    	String content=FileUtil.readFile(filepath);
    	String[] nctids=content.split("\n");
    	List<String> nctidlist=new ArrayList<String>();
    	for(String nctid:nctids){
    		nctidlist.add(nctid);
    	}
    	return nctidlist;
    }
}
