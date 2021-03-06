import java.util.ArrayList;

/* An AmoebaFamily is a tree, where nodes are Amoebas, each of which can have
   any number of children. */
public class AmoebaFamily {

    /* ROOT is the root amoeba of this AmoebaFamily */
    private Amoeba root;

    /* Creates an AmoebaFamily, where the first Amoeba's name is NAME. */
    public AmoebaFamily(String name) {
        root = new Amoeba(name, null);
    }

    /* Adds a new Amoeba with CHILDNAME to this AmoebaFamily as the youngest
       child of the Amoeba named PARENTNAME. This AmoebaFamily must contain an
       Amoeba named PARENTNAME. */
    public void addChild(String parentName, String childName) {
        root.addChildHelper(parentName, childName);
    }
	
	private void printHelper(Amoeba p, Amoeba root) {
		int deep;
		if(p == root) {
			deep = 0;
		} else {
			deep = 1;
			Amoeba q = p;
			while(q.parent != root) {
				deep += 1;
				q = q.parent;
			}
		}
		for(int i = 0;i < deep;i++) {
			System.out.print("    ");
		}
		System.out.println(p.name);
		return;
	}
	
	private void printDispHelper(Amoeba p, Amoeba root) {
		if(p != null) {
			printHelper(p, root);
			if(p.children.size() != 0) {
				for(int i = 0;i < p.children.size();i++) {
					printDispHelper(p.children.get(i), root);
				}
			}
		}
	}

    /* Prints the name of all Amoebas in this AmoebaFamily in preorder, with
       the ROOT Amoeba printed first. Each Amoeba should be indented four spaces
       more than its parent. */
    public void print() {
        // TODO
		printDispHelper(root, root);	
    }

    /* Returns the length of the longest name in this AmoebaFamily. */
    public int longestNameLength() {
        return root.longestNameLengthHelper();
    }

    /* Returns the longest name in this AmoebaFamily. */
    public String longestName() {
        // TODO
        // return null;
		return root.longestNameHelper();
    }

    /* An Amoeba is a node of an AmoebaFamily. */
    public static class Amoeba {

        private String name;
        private Amoeba parent;
        private ArrayList<Amoeba> children;

        public Amoeba(String name, Amoeba parent) {
            this.name = name;
            this.parent = parent;
            this.children = new ArrayList<Amoeba>();
        }

        public String toString() {
            return name;
        }

        public Amoeba getParent() {
            return parent;
        }

        public ArrayList<Amoeba> getChildren() {
            return children;
        }

        /* Adds child with name CHILDNAME to an Amoeba with name PARENTNAME. */
        public void addChildHelper(String parentName, String childName) {
            if (name.equals(parentName)) {
                Amoeba child = new Amoeba(childName, this);
                children.add(child);
            } else {
                for (Amoeba a : children) {
                    a.addChildHelper(parentName, childName);
                }
            }
        }

        /* Returns the length of the longest name between this Amoeba and its
           children. */
        public int longestNameLengthHelper() {
            int maxLengthSeen = name.length();
            for (Amoeba a : children) {
                maxLengthSeen = Math.max(maxLengthSeen,
                                         a.longestNameLengthHelper());
            }
            return maxLengthSeen;
        }
		
		public String longestNameHelper() {
			String maxStringSeen = name;
			for (Amoeba a : children) {
				String childMaxStringSeen = a.longestNameHelper();
				if(maxStringSeen.length() < childMaxStringSeen.length()) {
					maxStringSeen = childMaxStringSeen;
				}
            }
			return maxStringSeen;
		}
    }
}
