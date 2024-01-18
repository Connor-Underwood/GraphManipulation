import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static String dataPath = "Tests";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Test> tests = Arrays.asList(
            new Test(1, "treeCheck",          17, 2000),
            new Test(1, "countTriangles",     15, 2000),
            new Test(1, "vertexClusterCoeff", 17, 2000),
            new Test(1, "graphClusterCoeff",  17, 2000),
            new Test(2, "computeInDegrees",   17, 2000),
            new Test(2, "dagCheck",           17, 2000)
        );

        // Run only a single test
        if (args.length > 0) {
            int testNum = Integer.parseInt(args[0]);
            runTest(tests.get(testNum));

        // Run all tests and print total score
        } else {
            int[] partScores = new int[2];
            int[] partTotals = new int[2];

            for (Test test : tests) {
                runTest(test);
                partScores[test.part - 1] += test.score();
                partTotals[test.part - 1] += test.value;
            }

            // Print scores
            System.out.println();
            for (int part = 0; part < 2; part++) {
                System.out.println("Total score for part " + (part + 1) + ": " + partScores[part] + " / " + partTotals[part]);
            }
        }

        System.exit(0);
    }

    private static void runTest(Test test) throws ExecutionException, InterruptedException {
        System.out.println("Running part " + test.part + ": " + test.methodName + "...");

        try {
            test.run();
        } catch (Throwable e) {
            System.out.println("\tTest case failed! Refer to the stacktrace below:");
            e.printStackTrace(System.err);
        }

        System.out.println("\tScore: " + test.score() + " / " + test.value);
    }

    private static class Test {
        int part;
        String methodName;
        int value;
        int timeout;

        int totalGraphs = 0;
        int passedGraphs = 0;

        Test(int part, String methodName, int value, int timeout) {
            this.part = part;
            this.methodName = methodName;
            this.value = value;
            this.timeout = timeout;
        }

        void run() throws IOException {
            Path expectedPath = Paths.get(Utils.ROOT + dataPath, "expected", methodName + ".txt");
            List<String> lines = Files.readAllLines(expectedPath);

            totalGraphs = lines.size();
            passedGraphs = 0;

            for (String line : lines) {
                List<String> tokens = Arrays.asList(line.split(" "));
                String graphName = tokens.get(0);

                String inputFile = "part_" + part + "/" + graphName + ".txt";
                String inputPath = Utils.ROOT + dataPath + "/input/" + inputFile;

                try {
                    switch (methodName) {
                        case "treeCheck" -> {
                            ListGraph G = ListGraph.read(inputPath);
                            Utils.checkEquals(Boolean.parseBoolean(tokens.get(1)), UndirectedCheck.treeCheck(G),
                                    inputFile + " treeCheck is incorrect");
                        }
                        case "countTriangles" -> {
                            MatrixGraph G = MatrixGraph.read(inputPath);
                            Utils.checkEquals(Integer.parseInt(tokens.get(1)), UndirectedCheck.countTriangles(G),
                                    inputFile + " triangle count is incorrect");
                        }
                        case "vertexClusterCoeff" -> {
                            ListGraph G = ListGraph.read(inputPath);
                            double[] resultSol = new double[tokens.size() - 1];
                            for (int i = 1; i < tokens.size(); i++) {
                                resultSol[i - 1] = Double.parseDouble(tokens.get(i));
                            }

                            int n = readGraphSize(inputPath);
                            double[] result = new double[n];
                            for (int v = 0; v < n; v++) {
                                result[v] = UndirectedCheck.vertexClusterCoeff(G, v);
                            }
                            Utils.checkArrayEquals(resultSol, result,
                                    inputFile + " vertex coefficient(s) incorrect");
                        }
                        case "graphClusterCoeff" -> {
                            ListGraph G = ListGraph.read(inputPath);
                            Utils.checkAlmostEquals(Double.parseDouble(tokens.get(1)), UndirectedCheck.graphClusterCoeff(G),
                                    inputFile + " graph cluster coefficient incorrect");
                        }
                        case "computeInDegrees" -> {
                            ListGraph G = ListGraph.read(inputPath);
                            int[] resultSol = new int[tokens.size() - 1];
                            for (int i = 1; i < tokens.size(); i++) {
                                resultSol[i - 1] = Integer.parseInt(tokens.get(i));
                            }

                            int[] result = DirectedCheck.computeInDegrees(G);

                            Utils.checkArrayEquals(resultSol, result,
                                    inputFile + " in-degrees incorrect");
                        }
                        case "dagCheck" -> {
                            ListGraph G = ListGraph.read(inputPath);
                            Utils.checkArrayEquals(new int[]{Integer.parseInt(tokens.get(1)), Integer.parseInt(tokens.get(2))}, DirectedCheck.dagCheck(G),
                                    inputFile + " DAG check incorrect");
                        }
                    }

                    passedGraphs++;
                } catch (AssertionError e) {
                    System.out.println("\t" + e.getMessage());
                    //e.printStackTrace(System.out);
                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                }
            }
        }

        private static int readGraphSize(String filepath) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            int result = Integer.parseInt(reader.readLine());
            reader.close();
            return result;
        }

        public int score() {
            return (totalGraphs > 0) ? (int) Math.floor((double)passedGraphs / totalGraphs * value) : 0;
        }
    }
}
