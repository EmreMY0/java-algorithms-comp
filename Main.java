import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Main  {

    


    public static void main(String[] args) {
    
        List<String> data = new ArrayList<>();
        List<Integer> volumes = new ArrayList<>();
        int linesToProcess = 5000;


        try {
            processData(data, volumes, linesToProcess);  // buradaki IOException yakalanacak
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //quicksort(volumes, data, 0, 1000);
        insertionSort(volumes, data);


        for(int i = 0; i < 100; i++){
            System.out.println(volumes.get(i));
        }

        
}
    
    static void processData (List<String> data, List<Integer> volumes, int linesToProcess ) throws IOException {
        
        BufferedReader br = new BufferedReader(new FileReader("all_stocks_5yr.csv"));
        String line;

        // Başlık varsa atlayabilirsin
        String header = br.readLine();
        int counter = 0;

        while ((line = br.readLine()) != null && counter < linesToProcess) {
            data.add(line);
            String[] parts = line.split(",");
            int volume = Integer.parseInt(parts[5]);
            volumes.add(volume);
            counter++;
            
        }
        br.close();

        //test
        
         /* 
        for(int i = 0; i < 100; i++){
            System.out.println(data.get(i));
        }
        
        for(int i = 0; i < 100; i++){
            System.out.println(volumes.get(i));
        }
        */
    
    }



    static void quicksort (List<Integer> arr, List<String> data, int low, int high){

        int stackSize = high - low + 1;
        int[] stack = new int[stackSize];
        int top = -1;
        top  = top + 1;
        stack[top] = low;
        top = top + 1;
        stack[top] = high;
        while(top >= 0){
            high = stack[top];
            top = top - 1;
            low = stack[top];
            top = top - 1;
            int pivot = partition(arr, low, high);
            if(pivot - 1 > low){
                top = top + 1;
                stack[top] = low;
                top = top + 1;
                stack[top] = pivot - 1;
            }
            if(pivot + 1 < high ){
                top = top + 1;
                stack[top] = pivot + 1;
                top = top + 1;
                stack[top] = high;
            }
        }
    }

    static int partition(List<Integer> arr, int low, int high){
        int pivot = arr.get(high);
        int i = low - 1;
        for(int j = low; j < high; j++){
            if(arr.get(j) <= pivot){
                i = i + 1;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        return i + 1;
    }

    static void insertionSort(List<Integer> arr, List<String> data) {

        for(int j = 0; j < arr.size(); j++){
            int key = arr.get(j);
            int i = j - 1;
            while( i >= 0 && arr.get(i) > key){
                arr.set(i + 1, arr.get(i));
                i--;
            } 
            arr.set(i + 1, key);
        
        }

    }

}
