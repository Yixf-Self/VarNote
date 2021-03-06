package main.java.vanno.stack;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import main.java.vanno.bean.node.Node;
import main.java.vanno.process.ProcessResult;

public class IntervalStack extends AbstractReaderStack {
	protected List<Node> st;
	protected Node tempNode;
	
	public IntervalStack(final ProcessResult resultProcessor) {
		super(resultProcessor);
		if(st == null) {
			st = new LinkedList<Node>();
		}
		tempNode = null;
	}

	public void clearST() {
		st = null;
		st = new LinkedList<Node>();
		tempNode = null;
	}
	
	public void findOverlaps(List<Node> list) {
		for (Node intvNode : list) {
			findOverlap(intvNode);
		}
	}
	
	public boolean findOverlapInST(Node query) {

		for(int k=0; k<st.size(); k++) { 
			if(st.get(k).end <= query.beg) {
				st.remove(k);
				k--;
			} else {
				if(st.get(k).beg < query.end) {
					resultProcessor.doProcess(st.get(k)); 
				} else {
					return false;
				}
			}
		}
		
		if(tempNode != null) { 
			if( tempNode.beg >= query.end) {	
				return false;
			} else if(tempNode.end > query.beg) {
				resultProcessor.doProcess(tempNode); 			
				st.add(tempNode.clone());

				tempNode = null;
			}
		}
		return true;
	}
	
	public void findOverlap(Node query) {

		if(it == null) return;
		if(!findOverlapInST(query)) return;
		try {
			Node curNode = null;
			while((curNode = it.nextNode()) != null) {

				if(curNode.end <= query.beg) {
					continue;
				} else {
					if(curNode.beg < query.end) {
						resultProcessor.doProcess(curNode); 
						st.add(curNode.clone());
					} else {
						tempNode = curNode.clone();
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
