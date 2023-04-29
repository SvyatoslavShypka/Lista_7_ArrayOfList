import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class HashMap<TKey, TValue> {

    public Node[] nodeArray;
    // capacity
    private int initialSize;
    // load_factor is used to perform calculations and guess if automatic increase
    // is required
    // current_size should be increment when current_no_elements x load_factor becomes large
    private double loadFactor;
    int elementsQuantity;

    int current_size;

//    private HashMap<TKey, TValue> hashMap;
    private int hash;
    private Function<TKey, Integer> hashFunction;

    class Node {
        private OneWayLinkedList<TKey, TValue> linkedList;

        Node(TKey key, TValue value) {
            linkedList = new OneWayLinkedList<>();
            linkedList.add(key, value);
        }
    }

    public HashMap(int initialSize, double loadFactor, Function<TKey, Integer> hashFunction) {
        // TODO: Zainicjuj nową instancję klasy HashMap według podanych parametrów.
        //    InitialSize - początkowy rozmiar HashMap
        //    LoadFactor - stosunek elementów do rozmiaru HashMap po przekroczeniu którego należy podwoić rozmiar HashMap.
        //    HashFunction - funkcja, według której liczony jest hash klucza.
        //       Przykład użycia:   int hash = hashFunction.apply(key);

        nodeArray = (Node[]) Array.newInstance(Node.class, initialSize);
        current_size = initialSize;
        elementsQuantity = 0;

        this.initialSize = initialSize;
        this.loadFactor = loadFactor;
        this.hashFunction = hashFunction;
    }

    public void add(TKey key, TValue value) throws DuplicateKeyException {
        // TODO: Dodaj nową parę klucz-wartość. Rzuć wyjątek DuplicateKeyException, jeżeli dany klucz już istnieje w HashMap.
        int position = calculatePosition(key);
        if (containsKey(key)) {
            throw new DuplicateKeyException();
        } else {
            if (nodeArray[position] == null) {
                nodeArray[position] = new Node(key, value);
            } else {
                nodeArray[position].linkedList.add(key, value);
            }
            elementsQuantity++;
        }
        if (elementsQuantity >= (current_size * loadFactor)) {
            increaseHashmap();
        }
    }

    public void clear() {
        // TODO: Wyczyść zawartość HashMap.
        nodeArray = (Node[]) Array.newInstance(Node.class, initialSize);
        current_size = initialSize;
        elementsQuantity = 0;
    }

    public boolean containsKey(TKey key) {
        // TODO: Sprawdź, czy HashMap zawiera już dany klucz.
        int position = calculatePosition(key);
        if (nodeArray.length <= position || nodeArray[position] == null) return false;
        return nodeArray[position].linkedList.containsKey(key);
    }

    public boolean containsValue(TValue value) {
        // TODO: Sprawdź, czy HashMap zawiera już daną wartość.
        for (int i = 0; i < current_size; i++) {
            if (nodeArray[i] != null) {
                if(nodeArray[i].linkedList.containsValue(value)) return true;
            }
        }
        return false;
    }

    public int elements() {
        // TODO: Zwróć liczbę par klucz-wartość przechowywaną w HashMap.
        return elementsQuantity;
    }

    public TValue get(TKey key) throws NoSuchElementException {
        // TODO: Pobierz wartość powiązaną z danym kluczem. Rzuć wyjątek NoSuchElementException, jeżeli dany klucz nie istnieje.
        if (!containsKey(key)) throw new NoSuchElementException();
        int position = calculatePosition(key);
        int index = nodeArray[position].linkedList.indexOfKey(key);
        return nodeArray[position].linkedList.getValue(index);
    }

    public void put(TKey key, TValue value) {
        // TODO: Przypisz daną wartość do danego klucza.
        //   Jeżeli dany klucz już istnieje, nadpisz przypisaną do niego wartość.
        //   Jeżeli dany klucz nie istnieje, dodaj nową parę klucz-wartość.
        // calculate position to insert the new element
        int position = calculatePosition(key);
        if (nodeArray[position] == null) {
            nodeArray[position] = new Node(key, value);
            elementsQuantity++;
        } else {
            if (nodeArray[position].linkedList.containsKey(key)) {
                int index = nodeArray[position].linkedList.indexOfKey(key);
                nodeArray[position].linkedList.setValue(index, value);
            } else {
                nodeArray[position].linkedList.add(key, value);
                elementsQuantity++;
            }
        }
    }

    public TValue remove(TKey key) {
        // TODO: Usuń parę klucz-wartość, której klucz jest równy podanej wartości.
        if (!containsKey(key)) {
            return null;
        }
        TValue result = get(key);
        int position = calculatePosition(key);

        //put value null in deleted place
        nodeArray[position].linkedList.remove(key);
        //decrease quantity of elements
        elementsQuantity--;
        return result;
    }

    public int size() {
        // TODO: Zwróć obecny rozmiar HashMap.
        return current_size;
    }

    // HashMap should be doubled when current_no_elements becomes large (up to loadFactor % of capacity)
    public void increaseHashmap() {
        int newLength = current_size * 2;
        nodeArray = Arrays.copyOf(nodeArray, newLength);
        current_size = newLength;
    }

/*
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + initialSize;
        result = prime * result + current_no_elements;
        result = prime * result + current_size;
        result = prime * result + Float.floatToIntBits((float) loadFactor);
        result = prime * result + Arrays.hashCode(nodeArray);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HashMap other = (HashMap) obj;
        if (initialSize != other.initialSize)
            return false;
        if (current_no_elements != other.current_no_elements)
            return false;
        if (current_size != other.current_size)
            return false;
        if (Float.floatToIntBits((float) loadFactor) != Float.floatToIntBits((float) other.loadFactor))
            return false;
        if (!Arrays.equals(nodeArray, other.nodeArray))
            return false;
        return true;
    }
*/

    private int calculatePosition(TKey key) {
        return hashFunction.apply(key);
    }
}
