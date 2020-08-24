package Control;

import java.util.Iterator;
import java.util.Set;

public class Pruebas {
    public static void main(String[] args) {
        MatrizLexico mt = new MatrizLexico();
        Set<Integer> keys = mt.columnas.keySet();
        
        //Obtaining iterator over set entries
        Iterator<Integer> itr = keys.iterator();
        System.out.println("HASHTABLE");
        //Displaying Key and value pairs
        while (itr.hasNext()) { 
           // Getting Key
           int str = itr.next();

           /* public V get(Object key): Returns the value to which 
            * the specified key is mapped, or null if this map 
            * contains no mapping for the key.
            */
           System.out.println(+str+" - "+mt.columnas.get(str));
        } 
        
    }
}
