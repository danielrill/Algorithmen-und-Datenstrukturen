

import java.util.Comparator;
import java.util.Iterator;


/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys, 
 * or by a Comparator provided at set creation time, depending on which constructor is used. 
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 * 
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K, V> implements Dictionary<K, V> {

    private Comparator<? super K> cmp;
    private V returnValue;

    @SuppressWarnings("unchecked") // Raw use
    public BinaryTreeDictionary() {
        this.cmp = (x,y) -> ((Comparable<? super K>) x).compareTo(y);
        size = 0;
    }

    @SuppressWarnings("unchecked") // Raw use
    public BinaryTreeDictionary(Comparator<? super K> c) {
        if (c == null)
            cmp = (x,y) -> ((Comparable<? super K>) x).compareTo(y);
    }

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null)
            root.parent = null;

        return returnValue;
    }

    private Node<K,V> insertR(K key, V value, Node<K,V> p) {
        if (p == null) {
            p = new Node(key,value);    // Raw use
            ++size;
            returnValue = null;

        } else if (cmp.compare(key, p.key) < 0) {
            p.left = insertR(key, value, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (cmp.compare(key, p.key) > 0) {
            p.right = insertR(key, value, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }
        } else { // KEY bereits vorhanden
            returnValue = p.value;
            p.value = value;
        }
        p = balance(p);
        return p;
    }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K,V> p) {
        if (p == null) {
            return null;
        } else if  (cmp.compare(key, p.key) < 0) {
            return  searchR(key, p.left);
        } else if (cmp.compare(key, p.key) > 0) {
            return searchR(key, p.right);
        } else
            return p.value;
    }

    @Override
    public V remove(K key) {
        root = removeR(key, root);
        if ( root != null) {
            root.parent = null;
        }
        if (returnValue != null) {
            size --;
        }

        return returnValue;
    }

    private Node<K,V> removeR(K key, Node<K,V> p) {
        if ( p == null) {
            returnValue = null;

        } else if (cmp.compare(key, p.key) < 0) {
            p.left = removeR(key, p.left);
            if (p.left != null)
                p.left.parent = p;

        } else if (cmp.compare(key, p.key) > 0) {
            p.right = removeR(key, p.right);
            if (p.right != null)
                p.right.parent = p;

        } else if (p.left == null || p.right == null) {
            // ein oder kein Kind:
            returnValue = p.value;
            p = (p.left != null) ? p.left : p.right;
            if (p != null)
                p.parent = p;

        } else {
            // zwei Kinder
            MinEntry<K,V> min = new MinEntry<K,V>();
            p.right = getMinR(p.right, min);
            if (p.right != null)
                p.right.parent = p;

            returnValue = p.value;
            p.key = min.key;
            p.value = min.value;
        }

        p = balance(p);
        return p;
    }

    private Node<K,V> getMinR(Node<K,V> p, MinEntry<K,V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        } else {
            p.left = getMinR(p.left, min);
        }
        p = balance(p);
        return p;
    }

    private static class MinEntry<K,V> {
        private K key;
        private V value;
    }

    @Override
    public int size() {
        return this.size;
    }


    // AVL - Tree  Methods
    private int getHeight(Node<K,V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }

    private int getBalance(Node<K,V> p) {
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }

    private Node<K,V> balance(Node<K,V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        if (getBalance(p) == -2) {          // Fall A1
            if (getBalance(p.left) <= 0)
                p = rotateRight(p);
            else                            // Fall A2
                p = rotateLeftRight(p);
        }
        else if (getBalance(p) == +2) {     // Fall B1
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p);
            else                            // Fall B2
                p = rotateRightLeft(p);
        }
        return p;
    }

    private Node<K,V> rotateRight(Node<K,V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        p.left = q.right;

        if (p.left != null)
            p.left.parent = p;

        q.right = p;
        q.right.parent = q;

        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;

        return q;
    }

    private Node<K,V> rotateLeft(Node<K,V> p) {
        assert p.right != null;
        Node<K, V> q = p.right;
        p.right = q.left;

        if (p.right != null) {
            p.right.parent = p;
        }

        q.left = p;
        q.left.parent = q;
        p.height = Math.max(getHeight(p.left) + 1, getHeight(p.right));
        q.height = Math.max(getHeight(q.left) + 1, getHeight(q.right));

        return q;
    }

    private Node<K,V> rotateLeftRight(Node<K,V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        return rotateRight(p);
    }
    private Node<K,V> rotateRightLeft(Node<K,V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Dictionary.Entry<K,V> e: this)
            sb.append(e.getKey() + " : " + e.getValue() + "\n");

        return sb.toString();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BinaryTreeIterator();
    }
    private class BinaryTreeIterator implements Iterator<Entry<K,V>> {

        private Node<K,V> current;

        public BinaryTreeIterator() {
            this.current = null;
        }

        public boolean hasNext() {
            if (root == null) {
                return false;
            }
            if (root != null && current == null) {
                current = leftMostDescendant(root);
                return true;
            }

            if (current != null) {
                if (current.right != null)
                    current = leftMostDescendant(current.right);
                else {
                    current = parentOfLeftMostAncestor(current);
                    return current != null;
                }
                return true;
            }

            return false;
        }

        public Entry<K,V> next() {
            return new Entry<K,V>(current.key, current.value);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Node<K,V> leftMostDescendant(Node<K,V> p) {
        assert p != null;
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K,V> parentOfLeftMostAncestor(Node<K,V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent; // kann auch null sein
    }

    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }


    
    private Node<K, V> root = null;
    private int size = 0;
    
    // ...

	/**
	 * Pretty prints the tree
	 */
	public void prettyPrint() {
        printR(0, root);
    }


    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }
}
