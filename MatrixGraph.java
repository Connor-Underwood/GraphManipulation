import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;

public class MatrixGraph {

    int[][] matrix;

    public MatrixGraph(int vertices) {
        this.matrix = new int[vertices][vertices];
    }

    public static MatrixGraph read(String filepath) throws IOException {
        InputStream istr = new FileInputStream(filepath);
        BufferedReader br = new BufferedReader(new InputStreamReader(istr));

        int vertices = Integer.parseInt(br.readLine());

        MatrixGraph mg = new MatrixGraph(vertices);

        String line;
        int lineIndex = 0;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(" ");
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() > 0) {
                    mg.matrix[lineIndex][Integer.parseInt(split[i])] += 1;
                }
            }
            lineIndex++;
        }



        br.close();
        istr.close();
        return mg;
    }
}
