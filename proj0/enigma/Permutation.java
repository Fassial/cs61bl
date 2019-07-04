package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
public class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    public Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        // FIXME - Assign any additional instance variables.
		_cycles = cycles;
    }
	
	/** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }
	
    /** Return Split CYCLES. */
    static String[] splitCycles(String cycles) {
        if (cycles.contains(" ")) {
            cycles = cycles.replaceAll(" ", "");
            String[] split2 = cycles.split("\\)\\(");
            split2[0] = split2[0].substring(1);
            int last = split2.length - 1;
            split2[last] = split2[last].substring(0, split2[last].length() - 1);
            return split2;
        } else {
            cycles = cycles.replaceAll("\\(", "");
            cycles = cycles.replaceAll("\\)", "");
            String[] split1 = {cycles};
            return split1;
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    public int size() {
        // return 0; // FIXME - How do we ask the alphabet for its size?
		return _alphabet.size();
    }

    /** Return the index result of applying this permutation to the character
     *  at index P in ALPHABET. */
    public int permute(int p) {
    	// NOTE: it might be beneficial to have one permute() method always call the other
        // return 0;  // FIXME - How do we use our instance variables to get the index that P permutes to?
		int cIn = wrap(p);
        char pIn = _alphabet.toChar(cIn);
        char pOut = permute(pIn);
        int cOut = _alphabet.toInt(pOut);
        return cOut;
    }

    /** Return the index result of applying the inverse of this permutation
     *  to the character at index C in ALPHABET. */
    public int invert(int c) {
    	// NOTE: it might be beneficial to have one invert() method always call the other
        // return 0;  // FIXME - How do we use our instance variables to get the index that C inverts to?
		int cIn = wrap(c);
        char pIn = _alphabet.toChar(cIn);
        char pOut = invert(pIn);
        int cOut = _alphabet.toInt(pOut);
        return cOut;
    }

    /** Return the character result of applying this permutation to the index
     * of character P in ALPHABET. */
    public char permute(char p) {
    	// NOTE: it might be beneficial to have one permute() method always call the other
        // return 0;  // FIXME - How do we use our instance variables to get the character that P permutes to?
		char result;
        result = p;
        if (_cycles.equals("")) {
            return result;
        } else {
            String [] newcycles = splitCycles(_cycles);
            for (int i = 0; i < newcycles.length; i += 1) {
                if (newcycles[i].contains(String.valueOf(p))) {
                    String thecycle = newcycles[i];
                    int index = thecycle.indexOf(String.valueOf(p));
                    if (index != thecycle.length() - 1) {
                        result = thecycle.charAt(index + 1);
                    } else {
                        result = thecycle.charAt(0);
                    }
                }
            }
            return result;
        }
    }

    /** Return the character result of applying the inverse of this permutation
	 * to the index of character P in ALPHABET. */
    public char invert(char c) {
    	// NOTE: it might be beneficial to have one invert() method always call the other
        // return 0;  // FIXME - How do we use our instance variables to get the character that C inverts to?
		char result;
        result = c;
        if (_cycles.equals("")) {
            return result;
        } else {
            String [] newcycles = splitCycles(_cycles);
            for (int i = 0; i < newcycles.length; i += 1) {
                if (newcycles[i].contains(String.valueOf(c))) {
                    String thecycle = newcycles[i];
                    int index = thecycle.indexOf(String.valueOf(c));
                    if (index != 0) {
                        result = thecycle.charAt(index - 1);
                    } else {
                        result = thecycle.charAt(thecycle.length() - 1);
                    }
                }
            }
            return result;
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    public Alphabet alphabet() {
        return _alphabet;
    }
	
	/** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        String [] newcycles = splitCycles(_cycles);
        for (int i = 0; i < newcycles.length; i += 1) {
            if (newcycles[i].length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    // FIXME - How do we store which letter permutes/inverts to which?

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
	/** A string in the form "(cccc) (cc) ...". */
    private String _cycles;

    // Some starter code for unit tests. Feel free to change these up!
    // To run this through command line, from the proj0 directory, run the following:
    // javac enigma/Permutation.java enigma/Alphabet.java enigma/CharacterRange.java enigma/EnigmaException.java
    // java enigma/Permutation
    public static void main(String[] args) {
        Permutation perm = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", new CharacterRange('A', 'Z'));
        System.out.println(perm.size() == 26);
        System.out.println(perm.permute('A') == 'B');
        System.out.println(perm.invert('B') == 'A');
        System.out.println(perm.permute(0) == 1);
        System.out.println(perm.invert(1) == 0);
    }
}
