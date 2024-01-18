public class UndirectedCheck {




    public static boolean treeCheck(ListGraph a) {
        if (a.vertices <= 1 && a.adjacencyList.length == 0) {
            return false;
        }
        boolean[] marked = new boolean[a.vertices];

        for (int i = 0; i < a.vertices; i++) {
            if (!marked[i]) {
                if (a.isCyclic(i, marked, -1)) {
                    return false;
                }
            }
        }

        for (int i = 0; i < marked.length; i++) {
            if (!marked[i]) {
                return false;
            }
        }

        return true;

    }
    public static int getTrace(int[][] matrix) {
        int trace = 0;
        for (int i = 0; i < matrix.length; i++) {
            trace += matrix[i][i];
        }
        return trace;
    }
    public static void multiply(int[][] A, int[][] B,
                         int[][] C)
    {
        for (int i = 0; i < A.length; i++)
        {
            for (int j = 0; j < A.length; j++)
            {
                C[i][j] = 0;
                for (int k = 0; k < A.length;
                     k++)
                {
                    C[i][j] += A[i][k]*
                            B[k][j];
                }
            }
        }
    }
    public static int countTriangles(MatrixGraph a) {
        int[][] temp1 = new int[a.matrix.length][a.matrix.length];
        int[][] temp2 = new int[a.matrix.length][a.matrix.length];

        multiply(a.matrix, a.matrix, temp1);

        multiply(a.matrix, temp1, temp2);

        return getTrace(temp2) / 6;

    }

    public static double vertexClusterCoeff(ListGraph a, int u) {
        return a.local_coeff(u);
    }

    public static double graphClusterCoeff(ListGraph a) {
        double numerator = 0;
        for (int i = 0; i < a.vertices; i++) {
            numerator += vertexClusterCoeff(a, i);
        }
        return numerator / a.vertices;
    }
}
