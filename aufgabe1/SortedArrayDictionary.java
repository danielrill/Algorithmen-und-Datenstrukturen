import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K,V> {


    protected static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K, V>[] data;

    @SuppressWarnings("unchecked") // Raw use
    public SortedArrayDictionary(){
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0)
            return data[i].value;
        else
            return null;
    }

    private int searchKey(K key) {      // binary search
        int li = 0;
        int re = size -1;

        while ( re >= li ){
            int m = (li + re) / 2;
            if (key.compareTo(data[m].key) < 0)
                re = m - 1;
            else if (key.compareTo(data[m].key) > 0)
                li = m + 1;
            else return m;  // key found
        }
        return -1;          // not found
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);
        // If already there, override
        if (i != -1) {          // i= -1  searchKey not found
            V r = data[i].value;
            data[i].value = value;
            return r;
        }
        // New Entry;
        if (data.length == size) {
            data = Arrays.copyOf(data, 2*size);
        }
        int j = size -1;
        while (j >= 0 && key.compareTo(data[j].key) < 0) {
            data[j+1] = data[j];
            j--;
        }
        data[j+1] = new Entry<K,V>(key,value);
        size++;
        return null;
    }


    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1)
            return null;
        // Datensatz loeschen und Lücke schließen
        V r = data[i].value;
        //if (size - 1 - i >= 0) System.arraycopy(data, i + 1, data, i, size - 1 - i);
        /* Performante alternative zu:  (Auch für Referenztypen ?) */
        for (int j = i; j < size-1; j++)
            data[j] = data[j+1];

        data[--size] = null;
        return r;
    }

    @Override
    public int size() {
        return this.size;
    }

    protected void ensureCapacity(int newSize) {
        // TODO ? size ausreichend ? wenn nein kopiere.
    }
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new SortedArrayIterator();
    }
    private class SortedArrayIterator implements Iterator<Entry<K,V>> {

        private int current;

        public SortedArrayIterator() {
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return this.current < SortedArrayDictionary.this.size;
        }
        public Entry<K,V> next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return data[current++];
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

/*
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {

            int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                current++;
                return data[current];
            }
        };
    }
*/


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Dictionary.Entry<K,V> e : this) {                       // thanks to iterator !
            sb.append(e.getKey() + " : " + e.getValue() + " \n");
        }

        /*
        for (int i = 0; i < this.size; ++i) {


            sb.append(this.data[i].key.toString());
            //sb.append(String.format("%5s: %5s ", " ", " "));
            sb.append(": ");
            sb.append(this.data[i].value.toString());
            sb.append('\n');
            // Eine möglichkeit schön tabellarisch darzustellen

        }
        */

        return sb.toString();
    }
}
