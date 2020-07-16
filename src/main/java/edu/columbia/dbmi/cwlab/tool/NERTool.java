package edu.columbia.dbmi.cwlab.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.ohdsi.usagi.UsagiSearchEngine.ScoredConcept;
import org.ohdsi.usagi.apis.ConceptSearchAPI;

import edu.columbia.dbmi.cwlab.pojo.GlobalSetting;
import edu.columbia.dbmi.cwlab.pojo.RuleBasedModels;
import edu.columbia.dbmi.cwlab.pojo.Sentence;
import edu.columbia.dbmi.cwlab.pojo.Term;
import edu.columbia.dbmi.cwlab.tool.AhoCorasickDoubleArrayTrie.Hit;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import weka.core.SerializationHelper;

public class NERTool {
	AbstractSequenceClassifier<CoreLabel> ner=CRFClassifier.getClassifierNoExceptions(GlobalSetting.crf_model);
	public static final String grammars = GlobalSetting.dependence_model;
	private final static String diclookup = GlobalSetting.concepthub+"/omop/searchOneEntityByTerm";
	private final static String rule_based_model = GlobalSetting.rule_base_model;
	ConceptSearchAPI cs=new ConceptSearchAPI("/Users/cy2465/Documents/git/Usagi");
	public RuleBasedModels rbm=new RuleBasedModels();
	
	public NERTool(){
		try {   
	    	InputStream redis = null;	   
	    	File f = new File(rule_based_model);
	    	redis = new FileInputStream(f);
			this.rbm =(RuleBasedModels) SerializationHelper.read(new GZIPInputStream(redis));	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception{
		NERTool ner=new NERTool();
		String text="Weigh >300 lbs ";
		Sentence sent = new Sentence(text);
		String crf_results=sent.getText();
		crf_results = ner.nerByCrf(sent.getText());
		System.out.println("crf_results="+crf_results);
		List<Term> terms = ner.formulateNerResult(sent.getText(), crf_results);
		List<Term> newterms=ner.nerEnhancedByACAlgorithm(text, terms);
		
	}
	
	
	
	public static void train(String traindatapath,String targetpath){
		long startTime = System.nanoTime();
        /* Step 1: learn the classifier from the training data */
        String trainFile = traindatapath; 
        /* Learn the classifier from the training data */
        String serializeFileLoc =targetpath;
        // properties: https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/ie/NERFeatureFactory.html
        Properties props = new Properties();
        props.put("trainFile", trainFile); // To train with multiple files, a comma separated list
        props.put("map", "word=0,answer=1");
        props.put("useClassFeature", "true");
        props.put("useNGrams", "true");
        props.put("noMidNGrams", "true");
        props.put("maxNGramLeng", "6");
        props.put("useDisjunctive", "true");
        props.put("usePrev", "true");
        props.put("useNext", "true");
        props.put("useSequences", "true");
        props.put("usePrevSequences", "true");
        props.put("maxLeft", "1");
        props.put("useTypeSeqs", "true");
        props.put("useTypeSeqs2", "true");
        props.put("useTypeySequences", "true");
        props.put("wordShape", "chris2useLC");
        // props.put("printFeatures", "true");
        // This feature can be turned off in recent versions with the flag -useKnownLCWords false
        // https://nlp.stanford.edu/software/crf-faq.html question 13

        SeqClassifierFlags flags = new SeqClassifierFlags(props);
        CRFClassifier<CoreLabel> crf = new CRFClassifier<CoreLabel>(flags);
        crf.train();
        crf.serializeClassifier(serializeFileLoc);
        
	}
	
	
	/**
	 * Word Dependency Author:chi Date:2017-3-22
	 * 
	 */
	public Collection<TypedDependency> outputDependency(Tree t) {
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		// tlp.setGenerateOriginalDependencies(true); Standford Dependency
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(t);

		Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

		int countforitem = 0;
		int source = 0;
		int target = 0;
		/*
		for (TypedDependency item : tdl) {
			System.out.println(item);
		}
		*/
		return tdl;

	}

	public List<Term> formulateNerResult(String orignialstr, String result) {
		List<String[]> listmap = new ArrayList<String[]>();
		List<Term> terms=new ArrayList<Term>();	
		String pattern = "<(Value|Demographic|Condition|Qualifier|Measurement|Observation|Drug|Procedure|Temporal|Negation_cue)>([\\s\\S]*?)</(Value|Demographic|Condition|Qualifier|Measurement|Observation|Drug|Procedure|Temporal|Negation_cue)>";
		String s = result;
		Pattern pat = Pattern.compile(pattern);
		Matcher mat = pat.matcher(s);
		int count = 0;
		while (mat.find()) {
			count = count + 1;
			String[] arrs = new String[2];
			arrs[0] = mat.group(1);
			arrs[1] = mat.group(2);
			listmap.add(arrs);
		}
		int relativepos=0;
		int termindex=0;
		for(int i=0;i<listmap.size();i++){
			int start=orignialstr.indexOf(listmap.get(i)[1]);			
			int entitylength=listmap.get(i)[1].length();				
			Term term=new Term();
			term.setTermId(termindex);
			term.setCategorey(listmap.get(i)[0]);
			term.setText(listmap.get(i)[1]);
			term.setStart_index(relativepos+start);
			term.setEnd_index(relativepos+start+entitylength);
			terms.add(term);
			termindex++;
			relativepos=relativepos+start+listmap.get(i)[1].length();
			orignialstr=orignialstr.substring(start+listmap.get(i)[1].length());		
		}
		return terms;
	}
	
	
	
	public List<Term> nerEnhancedByACAlgorithm(String orignialstr,List<Term> terms) throws Exception{
			
		List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = this.rbm.getAcdat().parseText(orignialstr.toLowerCase());
		Integer last_start = 0;
		Integer last_end = 0;
		List<AhoCorasickDoubleArrayTrie.Hit<String>> longest = new ArrayList<AhoCorasickDoubleArrayTrie.Hit<String>>();
		for (Hit<String> s : wordList) {
			//System.out.println(s.begin +"\t"+ s.end+"\t"+s.value);
			if ((s.begin <= last_start) && (s.end >= last_end)) {
				if (longest.size() > 0) {
					longest.remove(longest.size() - 1);
				}
			} else if (s.begin >= last_start && s.end <= last_end) {
				continue;
			}
			longest.add(s);
			last_start = s.begin;
			last_end = s.end;
		}
		List<Term> enhancedNERResults=new ArrayList<Term>();
		
		List<Term> dicResults=new ArrayList<Term>();
		int temporalId=terms.size();
		for(Hit<String> s : longest){
			Term t=new Term();
			t.setTermId(temporalId++);
			t.setText(orignialstr.substring(s.begin+1,s.end-1));//t.setText(s.value.trim())
			t.setStart_index(s.begin+1);
			t.setEnd_index(s.end-1);
			t.setCategorey(this.rbm.getDir().get(s.value.toLowerCase().trim()));//look up dic for the Category
			t.setNeg(false);
			dicResults.add(t);
			//System.out.println("rule based results = "+s.value+"\t"+s.begin+","+s.end+"\t"+this.rbm.getDir().get(s.value.trim().toLowerCase()));
		}
		enhancedNERResults=mergeResultsfromRuleAndML(orignialstr,dicResults,terms);
		return enhancedNERResults;
	}
	
	/**
	 * Note: Author only implemented a over-simple method, it could be optimized by Segment tree 
	 * 
	 * */
	public List<Term> mergeResultsfromRuleAndML(String text, List<Term> ruleresults,List<Term> mlresults){
		List<Term> termlist=new ArrayList<Term>();	
		if(ruleresults!=null){
			termlist.addAll(ruleresults);
		}
		if(mlresults!=null){
			termlist.addAll(mlresults);
		}
		Collections.sort(termlist, new Comparator<Term>() {
			public int compare(Term t1, Term t2) {
				if (t1.getStart_index() < t2.getStart_index()) {
					return -1;
				}
				if (t1.getStart_index() == t2.getStart_index()) {
					return 0;
				}
				return 1;
			}
		});
		
		
		
		for(int k=0;k<termlist.size();k++){
			if(termlist.get(k).getTermId()<mlresults.size()){
				if((k+1)<termlist.size()){
					if(termlist.get(k).getEnd_index()>=termlist.get(k+1).getEnd_index()){
						termlist.remove(k+1);
					}else {
						if(termlist.get(k+1).getStart_index()<=termlist.get(k).getEnd_index()){
							Term t=new Term();
							t.setText(text.substring(termlist.get(k).getStart_index(), termlist.get(k+1).getEnd_index()));
							t.setStart_index(termlist.get(k).getStart_index());
							t.setEnd_index(termlist.get(k+1).getEnd_index());
							t.setCategorey(termlist.get(k+1).getCategorey());
							t.setNeg(false);
							termlist.remove(k);
							termlist.remove(k);
							termlist.add(k,t);
						}else{
							continue;
						}
					}
				}
			}else{
				if((k)<termlist.size()&&(k-1)>=0){
					if(termlist.get(k-1).getEnd_index()>=termlist.get(k).getEnd_index()){
						termlist.remove(k);
					}else{
						if(termlist.get(k).getStart_index()<=termlist.get(k-1).getEnd_index()){
							termlist.remove(k);
						}
					}
				}
				if((k+1)<termlist.size()){
					if(termlist.get(k).getEnd_index()>=termlist.get(k+1).getEnd_index()){
						termlist.remove(k+1);
					}else{
						if(termlist.get(k+1).getStart_index()<=termlist.get(k).getEnd_index()){
							termlist.remove(k+1);
						}
					}
				}
				
			}
		}	
		int tId=0;
		for(Term t:termlist){
			t.setTermId(tId++);
		}
		List<Term> newtermlist=new ArrayList<Term>();	
		
		int newtId=0;
		if(termlist.size()>0){
			termlist.get(0).setTermId(newtId++);
			newtermlist.add(termlist.get(0));
		}
		for(int k=1;k<termlist.size();k++){
			if((termlist.get(k).getStart_index()>=termlist.get(k-1).getStart_index())
				&&(termlist.get(k).getEnd_index()<=termlist.get(k-1).getEnd_index())){
				continue;
			}
			termlist.get(k).setTermId(newtId++);
			newtermlist.add(termlist.get(k));
		}
		return newtermlist;
	}
	
	public String nerByCrf(String str) {
		String results= ner.classifyWithInlineXML(str);
		results=results.replace("<0>", "");
		results=results.replace("</0>", "");
		return results;
	}
	
	public String nerByDicLookUp(String str){
		//System.out.println("======nerByDicLookUp=====");
		String res=str;
		if(str.trim().toLowerCase().equals("male")||str.trim().toLowerCase().equals("female")||str.trim().toLowerCase().equals("women")||str.trim().toLowerCase().equals("men")||str.trim().toLowerCase().equals("man")||str.trim().toLowerCase().equals("woman")){
			res="<"+"Demographic"+">"+str+"</"+"Demographic"+">";
			return res;
		}
		
		/*
		JSONObject jo=new JSONObject();
		jo.accumulate("term", str);
		
		String result=HttpUtil.doPost(diclookup, jo.toString());
		System.out.println("result="+result);
		JSONObject bestconcept=JSONObject.fromObject(result);
		try{
			//System.out.println("matchScore="+bestconcept.getDouble("matchScore"));
			if(bestconcept.getDouble("matchScore")>0.75)
			{
				JSONObject concept_jo=bestconcept.getJSONObject("concept");
				String domain=concept_jo.getString("domainId");
				res="<"+domain+">"+str+"</"+domain+">";
			}
				
		}catch(Exception ex){
			
		}
		*/
		
		String term=str.trim();
		//System.out.println("term="+term);
		List<ScoredConcept> lsc=cs.searchResults(term);//||sc.concept.domainId.equals("Observation")
		if(lsc.size()>0){
		ScoredConcept sc=lsc.get(0);;
		for(int i=0;i<10;i++){
			if(i<4 && lsc.get(i).concept.domainId.equals("Condition")
						||lsc.get(i).concept.domainId.equals("Drug")
						||lsc.get(i).concept.domainId.equals("Procedure")
						||lsc.get(i).concept.domainId.equals("Measurement")){
				sc=lsc.get(i);
					//return lsc.get(i);
			}else if(lsc.get(i).concept.domainId.equals("Condition")
					||lsc.get(i).concept.domainId.equals("Drug")
					||lsc.get(i).concept.domainId.equals("Observation")
					||lsc.get(i).concept.domainId.equals("Procedure")
					||lsc.get(i).concept.domainId.equals("Measurement")){
				sc=lsc.get(i);
			}
				
		}
		//System.out.println("dic result======>"+str+"\t"+sc.concept.conceptName+"\t"+sc.concept.domainId+"\t"+sc.concept.conceptCode);
		if(sc.matchScore>0.75){
			res="<"+sc.concept.domainId+">"+str+"</"+sc.concept.domainId+">";
		}
		}
		//System.out.println("dic_result="+res);
		return res;
	}
	
	
	
	public String trans2Html(String result){
		result=result.replace("<Condition>", "<mark data-entity=\"condition\">");
		result=result.replace("</Condition>", "</mark>");
		result=result.replace("<Drug>", "<mark data-entity=\"drug\">");
		result=result.replace("</Drug>", "</mark>");
		result=result.replace("<Procedure>", "<mark data-entity=\"procedure\">");
		result=result.replace("</Procedure>", "</mark>");
		result=result.replace("<Observation>", "<mark data-entity=\"observation\">");
		result=result.replace("</Observation>", "</mark>");
		result=result.replace("<Measurement>", "<mark data-entity=\"measurement\">");
		result=result.replace("</Measurement>", "</mark>");
		result=result.replace("<Temporal>", "<mark data-entity=\"temporal\">");
		result=result.replace("</Temporal>", "</mark>");
		result=result.replace("<Negation_cue>", "<mark data-entity=\"negation_cue\">");
		result=result.replace("</Negation_cue>", "</mark>");
		result=result.replace("<Demographic>", "<mark data-entity=\"demographic\">");
		result=result.replace("</Demographic>", "</mark>");
		result=result.replace("<Value>", "<mark data-entity=\"value\">");
		result=result.replace("</Value>", "</mark>");	
		return result;
	}
	
	public String trans4display(String text,List<Term> terms){
		String sent=text;
		StringBuffer sb=new StringBuffer();
		int endindex=0;
		//System.out.println("text="+text);
		for(int i=0;i<terms.size();i++){
			//System.out.println("i="+i);
			String s2="<mark data-entity=\""+terms.get(i).getCategorey().toLowerCase()+"\">"+terms.get(i).getText()+"</mark>";
			//sent=sent.replace(.getText(), s2);
			//System.out.println("s2="+s2);
			if(i==0){
				sb.append(sent.substring(0,terms.get(i).getEnd_index()).replace(terms.get(i).getText(), s2));
				endindex =terms.get(i).getEnd_index();
			}else if(i>0){
				int sstrstart=terms.get(i-1).getEnd_index()+1;
				int sstrend=terms.get(i).getEnd_index();
				String ssss=sent.substring(sstrstart,sstrend).replace(terms.get(i).getText(), s2);
				sb.append(ssss);
				endindex =terms.get(i).getEnd_index();
			}
		}
		if(terms.size()==0){
			sb.append(text);
		}else if(endindex!=text.length()){
			sb.append(sent.substring(endindex+1));
		}
		return sb.toString();
	}
	
	
	public String nerByCrf4Dispaly(String str) {
		String results= ner.classifyWithInlineXML(str);
		String displaystr=trans2Html(results);
		return displaystr;
	}
	
	/**
	 * parseSentence Author:chi Date:2017-3-22
	 * 
	 */
	public Tree parseSentence(String input) {
		LexicalizedParser lp = LexicalizedParser.loadModel(grammars);
		Tree tree = lp.parse(input);
		return tree;
	}
	
	public ArrayList<TaggedWord> tagWords(Tree t) {
		ArrayList<TaggedWord> twlist = t.taggedYield();
		return twlist;
	}
}
