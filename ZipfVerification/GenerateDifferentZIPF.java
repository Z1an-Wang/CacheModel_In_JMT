import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class GenerateDifferentZIPF {

    public static void main(String[] args){
        ARZipf zipAR = new ARZipf(3, 100);
        ITSZipf zipITS = new ITSZipf(3, 100);

        TreeMap<Integer, Integer> count_AR = new TreeMap<>();
        TreeMap<Integer, Integer> count_ITS = new TreeMap<>();

        for(int j=0; j<5000; j++){

            int tempAR = zipAR.nextRand();
            int tempITS = zipITS.nextRand();

            Integer target_AR = count_AR.get(Integer.valueOf(tempAR));
            if (target_AR == null) {
                count_AR.put(tempAR, 1);
            } else {
                count_AR.put(tempAR, count_AR.get(tempAR) + 1);
            }

            Integer target_ITS = count_ITS.get(Integer.valueOf(tempITS));
            if (target_ITS == null) {
                count_ITS.put(tempITS, 1);
            } else {
                count_ITS.put(tempITS, count_ITS.get(tempITS) + 1);
            }
        }


        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./ITS.csv"))) {
            Iterator it = count_ITS.keySet().iterator();
            while (it.hasNext()) {
                Integer key = (Integer) it.next();
                bufferedWriter.write(key + "," + count_ITS.get(key) + "\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./AR.csv"))) {
            Iterator it = count_AR.keySet().iterator();
            while (it.hasNext()) {
                Integer key = (Integer) it.next();
                bufferedWriter.write(key + "," + count_AR.get(key) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
