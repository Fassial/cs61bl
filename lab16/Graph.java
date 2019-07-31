import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
		Iterator<Edge> adjIterator = adjLists[v1].iterator();
        while (adjIterator.hasNext()) {
            Edge curEdge = adjIterator.next();
            if (curEdge.getTo() == v2) {
                curEdge.setWeight(weight);
				return;
            }
        }
		adjLists[v1].add(new Edge(v1, v2, weight));
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
		addEdge(v1, v2, weight);
		addEdge(v2, v1, weight);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        // TODO: YOUR CODE HERE
        // return false;
		Iterator<Edge> adjIterator = adjLists[from].iterator();
        while (adjIterator.hasNext()) {
            Edge curEdge = adjIterator.next();
            if (curEdge.getTo() == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        // TODO: YOUR CODE HERE
        // return null;
		List result = new LinkedList<>();
        Iterator<Edge> neiIterator = adjLists[v].iterator();
        while (neiIterator.hasNext()) {
            Edge curEdge = neiIterator.next();
            result.add(curEdge.getTo());
        }
        return result;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        // TODO: YOUR CODE HERE
        // return 0;
		int result = 0;
		for (int i = 0; i < vertexCount; i++) {
            if (isAdjacent(i, v)) {
                result++;
            }
        }
        return result;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /* A class that iterates through the vertices of this graph, starting with
       vertex START. If the iteration from START has no path from START to some
       vertex v, then the iteration will not include v. */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        DFSIterator(int start) {
            // TODO: YOUR CODE HERE
			this.fringe = new Stack<Integer>();
			this.fringe.push(start);
			this.visited = new HashSet<Integer>();
			this.visited.add(start);
        }

        public boolean hasNext() {
            // TODO: YOUR CODE HERE
            // return false;
			return !this.fringe.isEmpty();
        }

        public Integer next() {
            // TODO: YOUR CODE HERE
            // return null;
			Integer result = this.fringe.pop();
			Iterator<Edge> dfsIterator = adjLists[result].iterator();
			while (dfsIterator.hasNext()) {
				Edge edge = dfsIterator.next();
				if (!this.visited.contains(edge.getTo())) {
					this.fringe.push(edge.getTo());
					this.visited.add(edge.getTo());
				}
			}
			return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }
	
	public ArrayList<Integer> visitAll(int start) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		Iterator<Integer> dfsIterator = new DFSIterator(start);
		while (dfsIterator.hasNext()) {
			result.add(dfsIterator.next());
		}
		return result;
	}


    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        // TODO: YOUR CODE HERE
        // return false;
		if (start == stop) {
			return true;
		} else {
			ArrayList<Integer> result = visitAll(start);
			for (int i = 0; i < result.size(); i++) {
				if (result.get(i) == stop) {
					return true;
				}
			}
			return false;
		}
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        // TODO: YOUR CODE HERE
        // return null;
		ArrayList<Integer> result = new ArrayList<Integer>();
        // you supply the body of this method
        if (start == stop) {
            result.add(start);
            return result;
        } else if (!pathExists(start, stop)) {
            return result;		// empty ArrayList
        } else {
            ArrayList<Integer> visited = new ArrayList<>();
            Iterator<Integer> dfsIterator = new DFSIterator(start);
            while (dfsIterator.hasNext()) {
                int vertex = dfsIterator.next();
                if (vertex == stop) {
                    break;
                }
                visited.add(vertex);
            }
            int end = stop;
            result.add(end);
            for(int i = visited.size() - 1; i >= 0;i--) {
                if(visited.get(i) == start && isAdjacent(start, end)) {
                    result.add(visited.get(i));
                    break;
                } else if(isAdjacent(visited.get(i), end)) {
                    result.add(visited.get(i));
                    end = visited.get(i);
                }
            }
            Collections.reverse(result);
            return result;
        }
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;

        // TODO: Instance variables here!

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            // TODO: YOUR CODE HERE
        }

        public boolean hasNext() {
            // TODO: YOUR CODE HERE
            return false;
        }

        public Integer next() {
            // TODO: YOUR CODE HERE
            return 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
		
		public int getFrom() {
			return this.from;
		}
		
		public int getTo() {
			return this.to;
		}
		
		public int getWeight() {
			return this.weight;
		}
		
		public void setWeight(int weight) {
			this.weight = weight;
		}

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
}
