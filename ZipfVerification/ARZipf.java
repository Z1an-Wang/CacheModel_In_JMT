
import java.util.Random;

public class ARZipf {

    private double alpha_;
    private int max_number;
    private Random rng;

    public ARZipf(double alpha, int max){
        this.rng = new Random();
        this.alpha_ = alpha;
        this.max_number = max;
    }

    public int nextRand(){
        double alpha = this.alpha_;
        int max = this.max_number;

        while (true) {
            final double u = hIntegral(max + 0.5, alpha)
                    + rng.nextDouble() * ((hIntegral(1.5, alpha) - 1.0) - hIntegral(max + 0.5, alpha));
            double x = hIntegralInverse(u, alpha);
            int k = (int) (x + 0.5);
            if (k < 1) {
                k = 1;
            } else if (k > max) {
                k = max;
            }
            if (k - x <= 2.0 - hIntegralInverse(hIntegral(2.5, alpha) - h(2, alpha), alpha)
                    || u >= hIntegral(k + 0.5, alpha) - h(k, alpha)) {
                return k;
            }
        }
    }


    private static double h(double x, double alpha) {
        return Math.exp(-alpha * Math.log(x));
    }

    private static double hIntegral(double x, double alpha) {
        double logX = Math.log(x);
        return func2((1.0 - alpha) * logX) * logX;
    }

    private static double hIntegralInverse(double x, double alpha) {
        double t = x * (1.0 - alpha);
        // t should in range [-1, +inf).
        if (t < -1.0) {
            t = -1;
        }
        return Math.exp(func1(t) * x);
    }

    // A Taylor series expansion is used, if x is close to 0.
    private static double func1(double x) {
        if (Math.abs(x) > 1e-8) {
            return Math.log1p(x) / x;
        } else {
            return 1.0 - x * ((1.0 / 2.0) - x * ((1.0 / 3.0) - x * (1.0 / 4.0)));
        }
    }

    // A Taylor series expansion is used, if x is close to 0.
    private static double func2(double x) {
        if (Math.abs(x) > 1e-8) {
            return Math.expm1(x) / x;
        } else {
            return 1.0 + x * (1.0 / 2.0) * (1.0 + x * (1.0 / 3.0) * (1.0 + x * (1.0 / 4.0)));
        }
    }
}
