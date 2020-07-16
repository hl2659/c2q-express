package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import edu.columbia.dbmi.cwlab.util.FileUtil;

public class Query2Criteria {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content=FileUtil.readFile("/Users/cy2465/Downloads/ncdtv_0806.csv");
		String[] lines=content.split("\n");
		HashMap<String,Integer> criteriaset=new HashMap<String,Integer>();
		HashMap<String,Integer> condition=new HashMap<String,Integer>();
		HashMap<String,Integer> drug=new HashMap<String,Integer>();
		HashMap<String,Integer> measurement=new HashMap<String,Integer>();
		HashMap<String,Integer> observation=new HashMap<String,Integer>();
		HashMap<String,Integer> procedure=new HashMap<String,Integer>();
		HashSet<String> trials=new HashSet<String>();
		for(int i=1;i<lines.length;i++){
			//System.out.println("r="+r);
			String[] elements=lines[i].split("\t");
			//criteriaset.add(elements[2]);
			trials.add(elements[0]);
			if(criteriaset.containsKey(elements[2])){
				Integer count=criteriaset.get(elements[2]);
				count++;
				criteriaset.put(elements[2], count);
			}else{
				criteriaset.put(elements[2], 1);
			}
			//System.out.println("=>"+lines[i]);
			if(elements[3].equals("condition")){
				if(condition.containsKey(elements[2])){
					Integer ccount=condition.get(elements[2]);
					ccount++;
					condition.put(elements[2], ccount);
				}else{
					condition.put(elements[2], 1);
				}
			}else if(elements[3].equals("drug")){
				if(drug.containsKey(elements[2])){
					Integer dcount=drug.get(elements[2]);
					dcount++;
					drug.put(elements[2], dcount);
				}else{
					drug.put(elements[2], 1);
				}
			}else if(elements[3].equals("measurement")){
				if(measurement.containsKey(elements[2])){
					Integer mcount=measurement.get(elements[2]);
					mcount++;
					measurement.put(elements[2], mcount);
				}else{
					measurement.put(elements[2], 1);
				}
			}else if(elements[3].equals("observation")){
				if(observation.containsKey(elements[2])){
					Integer ocount=observation.get(elements[2]);
					ocount++;
					observation.put(elements[2], ocount);
				}else{
					observation.put(elements[2], 1);
				}
			}else if(elements[3].equals("procedure")){
				if(procedure.containsKey(elements[2])){
					Integer pcount=procedure.get(elements[2]);
					pcount++;
					procedure.put(elements[2], pcount);
				}else{
					procedure.put(elements[2], 1);
				}
			}
		}
		int entity_count=0;
//		for (Entry<String, Integer> entry : criteriaset.entrySet()) {
//			if(entry.getValue()>5){
//				entity_count++;
//			}
//		}
		int ccondition=0;
		int cdrug=0;
		int cmeasurement=0;
		int cobservation=0;
		int cprocedure=0;
		for (Entry<String, Integer> entry : criteriaset.entrySet()) {
			if(entry.getValue()>5){
				entity_count++;
			}
		}
		for (Entry<String, Integer> entry : condition.entrySet()) {
			if(entry.getValue()>5){
				ccondition++;
			}		
		}
		for (Entry<String, Integer> entry : drug.entrySet()) {
			if(entry.getValue()>5){
				cdrug++;
			}	
		}
		for (Entry<String, Integer> entry : observation.entrySet()) {
			if(entry.getValue()>5){
				cobservation++;
			}	
		}
		for (Entry<String, Integer> entry : measurement.entrySet()) {
			if(entry.getValue()>5){
				cmeasurement++;
			}	
		}
		for (Entry<String, Integer> entry : procedure.entrySet()) {
			if(entry.getValue()>5){
				cprocedure++;
			}	
		}
		System.out.println("total size="+entity_count);
		System.out.println("condition size="+ccondition);
		System.out.println("drug size="+cdrug);
		System.out.println("measurement size="+cmeasurement);
		System.out.println("observation size="+cobservation);
		System.out.println("procedure size="+cprocedure);
		System.out.println("trial size="+trials.size());
	}

}
