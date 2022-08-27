import java.util.ArrayList;
import java.util.Random;

public class ITSZipf {

    private double alpha;
    private int max_number;
    private Random rng;
    private ArrayList<Double> CDF_List;

    public ITSZipf(double alpha, int max){
        this.rng = new Random();
        this.alpha = alpha;
        this.max_number = max;
        this.CDF_List = new ArrayList<Double>(max+1);
        for(int i=0; i<=max; i++){
            CDF_List.add(i, (Double) cdf(i));
        }
    }

    public int nextRand() {
        return Binary_search(0, max_number, rng.nextDouble());
    }

    private int Binary_search(int lower, int upper, double probability) {
        if (lower + 1 == upper) {
            return upper;
        } else {
            int mid = (int) (lower + upper) / 2;
            if (probability > CDF_List.get(lower).doubleValue() && probability < CDF_List.get(mid).doubleValue()) {
                return Binary_search(lower, mid, probability);
            } else {
                return Binary_search(mid, upper, probability);
            }
        }
    }

    public double pmf(int x) {
        if (x <= 0 || x > max_number) {
            return 0.0;
        }
        return 1 / (Math.pow(x, alpha) * Harmonic(alpha, max_number));
    }

    public double cdf(int x) {
        if (x <= 0) {
            return 0.0;
        } else if (x >= max_number) {
            return 1.0;
        }
        return Harmonic(alpha, x) / Harmonic(alpha, max_number);
    }

    public double mean() {
        return Harmonic(alpha - 1, max_number) / Harmonic(alpha, max_number);
    }

    public double variance() {
        double s = Harmonic(alpha, max_number);
        double s1 = Harmonic(alpha - 1, max_number);
        double s2 = Harmonic(alpha - 2, max_number);
        return s2 / s - Math.pow(s1, 2) / Math.pow(s, 2);
    }

    private static double Harmonic(double alpha, int num) {
        double total = 0.0;
        for (int i = 1; i <= num; i++) {
            total += 1 / Math.pow(i, alpha);
        }
        return total;
    }
}
