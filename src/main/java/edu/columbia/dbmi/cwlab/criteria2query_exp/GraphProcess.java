package edu.columbia.dbmi.cwlab.criteria2query_exp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

public class GraphProcess {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		//Value_1, Receptor_host_1, modified_by
		//Receptor_host_1, Estrogen_1, observed_in
		//Value_2, Receptor_host2, modified_by
		//Receptor_host2, Progesterone_1, observed_in
		
		List<String> entities=new ArrayList<String>();
		entities.add("Value_1");
		entities.add("Receptor_host_1");
		entities.add("Estrogen_1");
		entities.add("Value_2");
		entities.add("Receptor_host_2");
		entities.add("Progesterone_1");
		
		Graph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		
		//You can easily use a loop to add Entities as Vertexes.  
		for(String s:entities){
			g.addVertex(s);
		}
		
		////You can easily use a loop to add Relations as Edges.  
		g.addEdge("Value_1", "Receptor_host_1");
		g.addEdge("Receptor_host_1", "Estrogen_1");
		g.addEdge("Value_2", "Receptor_host_2");
		g.addEdge("Receptor_host_2", "Progesterone_1");
		
		
		//------------
		DijkstraShortestPath dijk = new DijkstraShortestPath(g);
		GraphPath<Integer, DefaultWeightedEdge> shortestPath1 =dijk.getPath("Value_1", "Estrogen_1");
		GraphPath<Integer, DefaultWeightedEdge> shortestPath2 =dijk.getPath("Value_1", "Receptor_host_1");
		GraphPath<Integer, DefaultWeightedEdge> shortestPath3 =dijk.getPath("Value_1", "Receptor_host_2");
		System.out.println("shortest path from Value_1 to Estrogen_1 ===>"+shortestPath1);
		System.out.println("shortest path from Value_1 to Receptor_host_1 ===>"+shortestPath2);
		System.out.println("shortest path from Value_1 to Receptor_host_2 ===>"+shortestPath3);//null means not connented
		
		
		List<HashSet<String>> yc=new ArrayList<HashSet<String>>();
		boolean flag=false;
		for(int i=0;i<entities.size();i++){
			flag=false;
			for(int j=i+1;j<entities.size();j++){
				System.out.println(entities.get(i)+"~"+ entities.get(j));
				if(dijk.getPath(entities.get(i), entities.get(j))!=null){
					System.out.println("!connected!");
					for(int k=0;k<yc.size();k++){
						if(yc.get(k).contains(entities.get(i))){
							System.out.println("Set index ="+k);
							System.out.println("!existing subgraph!");
							HashSet<String> tempset=yc.get(k);
							tempset.add(entities.get(j));
							System.out.println("!modifier subgraph!");
							yc.set(k, tempset);
							System.out.println("list size="+yc.size());
							flag=true;
							break;
						}
					}
					if(flag==false){
						System.out.println("New Sub graph ="+entities.get(i));
						HashSet<String> set=new HashSet<String>();
						set.add(entities.get(i));
						set.add(entities.get(j));
						yc.add(set);
					}
					
				}
			}
		}
		
		//Results
		int subgraph=0;
		for(HashSet<String> set:yc){
			System.out.println("====Subgraph "+(subgraph++)+"======");
			for(String s:set){
				System.out.println("element="+s);
			}
		}
		
		//I am sure you can find a better algorithm than this. 
	}

}
