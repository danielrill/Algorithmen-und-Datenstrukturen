import java.sql.SQLOutput;
import java.util.Arrays;

public class UnionFind {

    int p[];

    public UnionFind( int n ) {
        p = new int[n];
        Arrays.fill(p, -1);
    }

    /*
    Vereinigt die beiden Menge s1 und s2.
     */
    public void union(int s1, int s2) {

        s1 = find(s1);      // Repräsentanten
        s2 = find(s2);

        if (p[s1] >= 0 ||p[s2] >= 0)
            return;
        if(s1 == s2)
            return;

        // Höhe von s1 < höhe von s2
        if(-p[s1] < -p[s2]) {
            p[s2] = p[s2] - p[s1] + 1;  // S2 wächst um Größe von S1
            p[s1] = s2;
        } else {
            if( p[s1] == p[s2] )  // Höhe von s1 erhöht sich um 1
                p[s1] --;

            p[s2] = s1;
        }

    }

    /*
    Liefert den Repräsentanten der Menge zurück, zu der e gehört.
     */
    public int find(int e) {
        while(p[e] >= 0)
            e = p[e];
        return e;
    }

    /*
    Liefert die Anzahl der Mengen in der Partitionierung zurück.
     */
    public int size(){
        int size = 0;

        for (int e : p)
            if(e <= -1) size++;
        return size;
    }

    public void printUnion() {
        for (int i = 0; i < p.length; i++)
            System.out.print("\t" + i);
        System.out.println();
        for(int e : p)
            System.out.print("\t" + e);

        System.out.println();
    }

    // TEST
    public static void main(String[] args) {
        UnionFind test = new UnionFind(8);
        System.out.println("Neuer Wald mit " + test.size() + " Bäumen");
        test.printUnion();

        test.union(1,0);
        System.out.println("Partition , neue Size : " + test.size());

        test.union(5,7);
        System.out.println("Partition , neue Size : " + test.size());

        test.union(5,1);
        System.out.println("Partition , neue Size : " + test.size());

        // Bereits gemerged
        test.union(0,7);
        System.out.println("Partition , neue Size : " + test.size() + " size darf sich nicht ändern");


        System.out.println("5 is Repräsentant in : " + test.find(5));

        test.printUnion();


    }
}
