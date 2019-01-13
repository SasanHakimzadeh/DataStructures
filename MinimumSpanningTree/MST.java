package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList Lis = new PartialTreeList();
		for(int i = 0; i < graph.vertices.length; i++){
			Vertex v = graph.vertices[i];
			PartialTree Tis = new PartialTree(v);
			MinHeap<PartialTree.Arc> Pis;
			Pis = new MinHeap<PartialTree.Arc>();
			for(Vertex.Neighbor temporary = v.neighbors; temporary != null;){
				Pis.insert(new PartialTree.Arc(v, temporary.vertex, temporary.weight));
				temporary = temporary.next;
			}
			Tis.getArcs().merge(Pis);
			Lis.append(Tis);
		}
		return Lis;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		
		ArrayList<PartialTree.Arc> result = new ArrayList<PartialTree.Arc>();
		
		for(PartialTree added = null; ptlist.size() != 1; ptlist.append(added)){	
			added = ptlist.remove();
			PartialTree.Arc arcc = null;
			int i=0;
			while (i < added.getArcs().size()){
				boolean cont = true;
				arcc = added.getArcs().getMin();
				added.getArcs().deleteMin();
				MinHeap<PartialTree.Arc> d = new MinHeap<PartialTree.Arc>();
				
				if(!added.getArcs().isEmpty()){	
					for(;!added.getArcs().isEmpty();){
						PartialTree.Arc b = added.getArcs().getMin();
						added.getArcs().deleteMin();
						d.insert(b);
						if(arcc.v2.name.equals(b.v1.name)){
							cont = false;
							break;
						}
					}
				}
				added.getArcs().merge(d);
				if(cont == true)
					break;
				i++;
			}

			if(arcc == null){
				System.out.println("There are no arcs that correspond to this element, and thus can't be part of the MST.");
				continue;
			}
			
			result.add(arcc);
			PartialTree merged = ptlist.removeTreeContaining(arcc.v2);	
			added.merge(merged);
		}
		return result;
	}
}