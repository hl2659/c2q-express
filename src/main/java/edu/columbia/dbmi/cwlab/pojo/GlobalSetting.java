package edu.columbia.dbmi.cwlab.pojo;

public class GlobalSetting {
	public final static String c2qversion="criteria2query v0.8.3.5";
	public final static String ohdsi_api_base_url="http://api.ohdsi.org/WebAPI/";//http://api.ohdsi.org/WebAPI/ http://localhost:8080/WebAPI/
	public final static String crf_model="edu/columbia/dbmi/cwlab/model/c2q_all_model_advanced.ser.gz";//all-c2q-model. //ec-ner-model.ser.gz
	public final static String relexmodel="C:\\Users\\jaysh\\OneDrive\\Documents\\RA\\re.model";//all-c2q-model. //ec-ner-model.ser.gz
	public final static String relExmodel="edu/columbia/dbmi/cwlab/model/RelEx.model";
	public final static String negatemodel="edu/columbia/dbmi/cwlab/model/negex_triggers.txt";//all-c2q-model. //ec-ner-model.ser.gz
	public final static String instancefile="edu/columbia/dbmi/cwlab/model/100trialsrels4weka.arff";//all-c2q-model. //ec-ner-model.ser.gz
	public final static String rule_base_model="C:\\Users\\jaysh\\OneDrive\\Documents\\RA\\rule_based_model_offline.ser.gz";///home/njust4060/models/rule_based_model_offline.ser.gz
	public final static String dependence_model="edu/columbia/dbmi/cwlab/model/wsjPCFG.ser.gz";//edu/columbia/dbmi/cwlab/model/wsjPCFG.ser.gz
	public final static String opennlp_model_dir="";
	public final static String[] alldomains={"Condition","Observation","Drug","Measurement","Demographic","Temporal","Value","Negation_cue","Procedure","Device"};
	public final static String[] conceptSetDomains={"Condition","Observation","Measurement","Drug","Procedure"};
	public final static String[] primaryEntities={"Condition","Observation","Measurement","Drug","Procedure","Demographic"};
	public final static String[] atrributes={"Temporal_measurement","Temporal","Value"};
	public final static String[] allclasses={"Condition","Observation","Measurement","Drug","Procedure","Demographic","Temporal_measurement","Temporal","Value"};
	public final static String[] combo={"Measurement_Value","Drug_Temporal","Demographic_Value","Observation_Value","Condition_Value","Condition_Temporal","Procedure_Temporal","Drug_Value","Procedure_Value","Observation_Temporal","Measurement_Temporal","Demographic_Temporal"};
	public final static String[] relations={"no_relation","has_value","has_temporal"};
	public final static String negateTag="Negation_cue";
	public final static String concepthub="http://127.0.0.1:8080/concepthub";
}
