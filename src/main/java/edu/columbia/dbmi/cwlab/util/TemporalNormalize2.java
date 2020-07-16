package edu.columbia.dbmi.cwlab.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.util.CoreMap;

public class TemporalNormalize2 {

	public static void main(String[] args) {
	TemporalNormalize2 sd = new TemporalNormalize2();
//		
//		String content=FileUtil.Readfile("/Users/cy2465/Downloads/03ClampTimeResult_RawTextOnly.txt");
//		String[] rows=content.split("\n");
//		StringBuffer sb=new StringBuffer();
//		for(String s:rows){
//			String text = s;// surgery
//			//sd.temporalNormalize(text);
//			String result=sd.temporalNormalizeforNumberUnit(text);
//			sb.append(text+"\t"+result+"\n");
//		}
//		FileUtil.write2File("/Users/cy2465/Downloads/03ClampTimeResult_RawTextOnly_SUTime.txt", sb.toString());
		
		String text = "next 2 weeks";// surgery
		//sd.temporalNormalize(text);
		String result=sd.temporalNormalizeforNumberUnit(text);
		System.out.println(result);
		
	}

	AnnotationPipeline pipeline;

	public TemporalNormalize2() {
		pipeline = new AnnotationPipeline();
		//pipeline.addAnnotator(new PTBTokenizerAnnotator(false));
		pipeline.addAnnotator(new TokenizerAnnotator(false));
		pipeline.addAnnotator(new WordsToSentencesAnnotator(false));	
		String sutimeRules="edu/columbia/dbmi/ohdsims/model/defs.sutime.txt,"+"edu/columbia/dbmi/ohdsims/model/english.holidays.sutime.txt,"+"edu/columbia/dbmi/ohdsims/model/english.sutime.txt";
		Properties props = new Properties();
		props.setProperty("sutime.rules", sutimeRules);
		props.setProperty("sutime.binders", "0");
		pipeline.addAnnotator(new TimeAnnotator("sutime", props));

	}

	public String temporalNormalizeforNumberUnit(String text) {
		Annotation annotation = new Annotation(text);
		annotation.set(CoreAnnotations.DocDateAnnotation.class, SUTime.getCurrentTime().toString());
		pipeline.annotate(annotation);

		System.out.println(annotation.get(CoreAnnotations.TextAnnotation.class));

		List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
		Integer days=0;
		String str="";
		for (CoreMap cm : timexAnnsAll) {

			List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
			 System.out.println(cm + " [from char offset " + tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) + " to "
			 + tokens.get(tokens.size() -1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']'
			 + " --> " + cm.get(TimeExpression.Annotation.class).getTemporal());
			 System.out.println("!!!!-->" +cm.get(TimeExpression.Annotation.class).getValue()+"\tlable:"+cm.get(TimeExpression.Annotation.class).getTemporal().getTimeLabel());
			 System.out.println("---final result---");
			System.out.println(cm.get(TimeExpression.Annotation.class).getValue().getType());
			str=cm.get(TimeExpression.Annotation.class).getTemporal().toString();
			
		}
		return str;
		
	}
	


}
