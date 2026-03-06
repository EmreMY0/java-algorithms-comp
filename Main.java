import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Main  {

    public static void main(String[] args) {
        
        try {
            processData();  // buradaki IOException yakalanacak
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
}
    
    static void processData () throws IOException {
        List<String> rows = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("all_stocks_5yr.csv"));
        String line;

        // Başlık varsa atlayabilirsin
        String header = br.readLine();

        while ((line = br.readLine()) != null) {
            rows.add(line);
        }
        br.close();

        //test
        
        /* 
        for(int i = 0; i < 100; i++){
            System.out.println(rows.get(i));
        }
        */ 
    
    }



    void quicksort (int[] arr, int low, int high){

    }
}
