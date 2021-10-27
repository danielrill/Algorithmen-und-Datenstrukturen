import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K, V> implements Dictionary<K, V> {

    protected int size;
    private LinkedList<Entry<K,V>> [] tab;
    private int DEF_CAPACITY = 41;

    @SuppressWarnings("unchecked") // Raw use
    public HashDictionary() {
        this.tab = new LinkedList[DEF_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked") // Raw use
    public HashDictionary(int initSize) {
        if ( initSize < 1)
            throw new ArrayIndexOutOfBoundsException();

        this.tab = new LinkedList[initSize];
        this.size = 0;

    }

    private int hash(K key) {
        int adr = 0;
        adr = key.hashCode();
        if (adr < 0) {
            adr = -(adr);   // in case of Overflow
        }
        return (adr % (tab.length));
    }

    @Override
    public V insert(K key, V value) {
        int adr = hash(key);

        if (search(key) != null) {
            for (var element : tab[adr]) {
                if (element.getKey().equals(key)) {
                    V oldValue = element.getValue();
                    element.setValue(value);
                    return oldValue;
                }
            }
        }

        if (tab[adr] == null) {
            tab[adr] = new LinkedList<Entry<K, V>>();
        }
        tab[adr].add(new Entry<K, V>(key, value));
        size++;
        return null;
    }

    @Override
    public V search(K key) {
        int adr = hash(key);
        if (tab[adr] != null) {
            for (var element : tab[adr]) {
                if (element.getKey().equals(key)) {
                    return element.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int adr = hash(key);

        for (var element : tab[adr]) {
            if (element.getKey().equals(key)) {
                V oldValue = element.getValue();
                tab[adr].remove(element);
                size--;
                return oldValue;
            }
        }
        return null;
    }


    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashDictionaryIterator();
    }

    private class HashDictionaryIterator implements Iterator<Entry<K,V>>{

        private int idx;
        Iterator<Entry<K,V>> it;

        public HashDictionaryIterator(){
            this.idx = -1;
        }

        @Override
        public boolean hasNext() {
            if (it != null && it.hasNext()) {
                return true;
            } else {
                while (++this.idx < tab.length) {
                    if (tab[idx]!= null) {
                        it = tab[idx].iterator();
                        return it.hasNext();
                    }
                }
                return false;
            }
        }

        @Override
        public Entry<K, V> next() {
            return it.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
