package edu.columbia.dbmi.cwlab.enhancement;

import java.util.HashMap;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

public class RuleVariable {
	private static final long serialVersionUID = 901126911124L;
	AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
	HashMap<String,String> dir=new HashMap<String,String>();
}
