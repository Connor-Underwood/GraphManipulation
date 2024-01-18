import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ListGraph {

    LinkedList[] adjacencyList;

    int vertices;

    boolean[] marked;

    public ListGraph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new LinkedList[vertices];
        for (int i = 0; i < vertices; i++) {
            adjacencyList[i] = new LinkedList();
        }
    }
    public static class LinkedList {
        Vertex head;
        int size;

        public LinkedList() {
            this.head = null;
            this.size = 0;
        }

        public void addNode(Vertex newVertex) {
            if (this.head == null) {
                this.head = newVertex;
                this.size++;
                return;
            }
            Vertex curr = this.head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newVertex;
            this.size++;
        }

        public int getSize() {
            return this.size;
        }

        public boolean contains(int value) {
            Vertex curr = this.head;
            while (curr != null) {
                if (curr.value == value) {
                    return true;
                }
                curr = curr.next;
            }
            return false;
        }

        public void remove(int value) {
            Vertex curr = this.head;
            if (this.head.value == value) {
                this.head = head.next;
                this.size--;
                return;
            }
            while (curr.next != null) {
                if (curr.next.value == value) {
                    curr.next = curr.next.next;
                    size--;
                    return;
                }
                curr = curr.next;
            }

        }
    }

    public static class Vertex {
        int value;
        Vertex next;

        public Vertex(int value) {
            this.value = value;
            this.next = null;
        }
    }

    public LinkedList adjacentNodes(int value) {
        return this.adjacencyList[value];
    }
    public static ListGraph read(String filepath) throws IOException {
        InputStream istr = new FileInputStream(filepath);
        BufferedReader br = new BufferedReader(new InputStreamReader(istr));

        int numVertices = Integer.parseInt(br.readLine());

        ListGraph lg = new ListGraph(numVertices);

        String line = "";

        int lineIndex = 0;

        while ((line = br.readLine()) != null) {
            String[] split = line.split(" ");
            if (split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    if (split[i].length() > 0) {
                        Vertex v = new Vertex(Integer.parseInt(split[i]));
                        lg.adjacencyList[lineIndex].addNode(v);
                    }
                }
            }
            lineIndex++;
        }

        br.close();
        istr.close();


        return lg;
    }


    public boolean isCyclic(int v, boolean[] marked, int parent) {
        marked[v] = true;

        LinkedList list = adjacentNodes(v);

        Vertex vert = list.head;
        int i;
        while (vert != null) {
            i = vert.value;
            if (!marked[vert.value]) {
                if (isCyclic(vert.value, marked, v)) {
                    return true;
                }
            } else if (i != parent) {
                return true;
            }
            vert = vert.next;
        }

        return false;
    }

    public double local_coeff(int u) {
        LinkedList list = adjacencyList[u];
        int deg_u = list.getSize();
        if (deg_u < 2) {
            return 0;
        }
        double denom = deg_u * (deg_u - 1);

        Vertex v = list.head;
        double pairs = 0;
        while (v != null) {
            LinkedList friends_of_v = adjacencyList[v.value];
            Vertex v_friend = friends_of_v.head;
            while (v_friend != null) {
                if (list.contains(v_friend.value)) {
                    pairs++;
                }
                v_friend = v_friend.next;
            }
            v = v.next;
        }

        return (pairs) / denom;
    }

    public int[] in_deg_count() {
        int[] in_count = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            LinkedList list = adjacencyList[i];
            Vertex w = list.head;
            while (w != null) {
                in_count[w.value] += 1;
                w = w.next;
            }
        }
        return in_count;
    }


    public int[] checkDag() {
        int[] in_deg_count = in_deg_count();

        int sinkSize = 0;
        int sourceSize = 0;
        for (int i = 0; i < vertices; i++) {
            if (in_deg_count[i] == 0) {
                sourceSize++;
            }
            if (adjacencyList[i].head == null) {
                sinkSize++;
            }
        }
        LinkedList S = new LinkedList();
        for (int i = 0; i < in_deg_count.length; i++) {
            if (in_deg_count[i] == 0) {
                S.addNode(new Vertex(i));
            }
        }

        while (S.size != 0) {
            Vertex v = S.head;
            S.remove(v.value);

            LinkedList v_friends = adjacencyList[v.value];
            Vertex friend = v_friends.head;

            while (friend != null) {
                in_deg_count[friend.value] -= 1;
                if (in_deg_count[friend.value] == 0) {
                    S.addNode(new Vertex(friend.value));
                }
                friend = friend.next;
            }
        }

        for (int i = 0; i < in_deg_count.length; i++) {
            if (in_deg_count[i] != 0) {
                return new int[] {-1, -1};
            }
        }

        return new int[] {sourceSize, sinkSize};
    }




}
