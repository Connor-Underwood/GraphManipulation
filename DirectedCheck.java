public class DirectedCheck {

    public static int[] computeInDegrees(ListGraph a) {
        return a.in_deg_count();
    }

    public static int[] dagCheck(ListGraph a) {
        return a.checkDag();
    }

}
