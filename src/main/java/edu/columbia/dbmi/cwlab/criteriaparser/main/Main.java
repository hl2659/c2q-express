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

import static java.lang.System.out;

public class Main
{
    private static class Option
    {
    	@Argument(description = "fetch task")
        boolean fetch;
    	
    	@Argument(description = "parse task")
        boolean parse;
    	
    	@Argument(description = "all pipeline task")
        boolean all;
    	
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
    	
    	@Argument(description = "Usagi Path")
        String usagi_path;
    	
    	@Argument(description = "Criterion text index")
        String criteria_path;
    	
    	@Argument(description = "Value Range index")
        String value_range_path;
    	
    	@Argument(description = "Temporal text")
        String temporal_path;
    	
    	@Argument(description = "Entity text")
        String entity_path;
    	
        @Argument(description = "Input file dir")
        String input_dir;

        @Argument(description = "Result dir path")
        String result_dir;
        
        @Argument(description = "Result file path")
        String result_path;

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
        			new ParseThread(option.criteria_path,option.result_dir,((tobep_num/tcount)*x),((tobep_num/tcount)*(x+1)+1)).start();;
        		}
            }else if(option.normalize==true){
            	if(option.numeric==true){
	            	String range_value=FileUtil.readFile(option.value_range_path); 
	            	String[] range_arr=range_value.split("\n");
	            	String output=MeasurementUtil.batchStandardizeValueRanges(range_arr);
	            	FileUtil.write2File(option.result_path, output);
            	}else if(option.temporal==true){
            		String temporal=FileUtil.readFile(option.temporal_path); 
	            	String[] temporal_arr=temporal.split("\n");
            		TemporalNormalize tn=new TemporalNormalize();
            		String output=tn.batchTemporalNormalize(temporal_arr);
            		FileUtil.write2File(option.result_path, output);
            	}else if(option.entity==true){
            		String entity=FileUtil.readFile(option.entity_path); 
            		String usagipath=option.usagi_path;
            		out.printf("->%s","Usagi path="+usagipath+"\n");
            		out.printf("->%s","Entity path="+option.entity_path+"\n");  
            		String[] tobemappedentities=entity.split("\n");	
            		List<String> entitylist=new ArrayList<String>();
                	for(String r:tobemappedentities){
                		entitylist.add(r);
                	}
            		int tobep_num=entitylist.size();
            		int tcount=option.thread;
            		for(int x=0;x<option.thread;x++){
            			new MappingThread(String.valueOf(x),((tobep_num/tcount)*x),((tobep_num/tcount)*(x+1)),entitylist,usagipath,option.result_dir).start();  
            			try {
            				Thread.sleep(1000);
            			} catch (InterruptedException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
            	}
            }else if(option.all==true){
            	
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
