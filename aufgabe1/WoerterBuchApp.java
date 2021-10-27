import java.util.Scanner;

public class WoerterBuchApp {

    public static void main(String[] args) {

        Dictionary<String, String> wb = new SortedArrayDictionary<String, String>();

        wb.insert("lesen", "read");
        wb.insert("schreiben", "write");
        wb.insert("fliegen", "fly");
        wb.insert("zielen", "aim");
        wb.insert("fahren", "drive");
        System.out.println(wb.toString());

        System.out.println(wb.search("fliegen"));
        System.out.println(wb.search("lesen"));

        wb.remove("fahren");
        wb.remove("lesen");
        System.out.println("\n" + wb.toString());
        System.out.println(" size=" + wb.size());


        for (Dictionary.Entry<String, String> e : wb) {
            System.out.println(e.getKey() + ": " + e.getValue() + " search: " + wb.search(e.getKey()));
        }


    }
}
