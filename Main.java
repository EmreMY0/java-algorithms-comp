import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Main  {

    


    public static void main(String[] args) {
    
        String header = null;
        int linesToProcess = 50;
        List<String> data = new ArrayList<>();
        List<Integer> volumes = new ArrayList<>();
        


        try {
            processData(data, volumes, linesToProcess, header);  // buradaki IOException yakalanacak
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //quicksort(volumes, data, 0, 1000);
        //insertionSort(volumes, data);
        //mergeSort(volumes, data);
        //shellSort(volumes, data);
        radixSort(volumes, data, 6);

        for(int i = 0; i < 50; i++){
            System.out.println(volumes.get(i));
        }

        
}
    
    static void processData (List<String> data, List<Integer> volumes, int linesToProcess, String header ) throws IOException {
        
        BufferedReader br = new BufferedReader(new FileReader("all_stocks_5yr.csv"));
        String line;

        // skip header
        header = br.readLine();
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


    static void mergeSort(List<Integer> arr, List<String> data) {
        int n = arr.size();
        int[] temp = new int[n];
        int currSize = 1;
        while ( currSize < n){
            int leftStart = 0;
            while(leftStart < n-1){
                int mid = Math.min(leftStart + currSize - 1, n-1);
                int rightEnd =  Math.min(leftStart + 2 * currSize - 1, n-1);
                merge(arr, data, temp, leftStart, mid, rightEnd);
                leftStart = leftStart + 2*currSize;

            }
            currSize = currSize * 2;
        }

    }

    static void merge(List<Integer> arr, List<String> data, int[] temp, int left, int mid, int right){
        int i = left;
        int j = mid + 1;
        int k = left;
        while(i <= mid && j <= right){
            if(arr.get(i) <= arr.get(j)){
                temp[k] = arr.get(i);
                i++;
            }
            else{
                temp[k] = arr.get(j);
                j++;
            }
            k++;
        }
        while(i <= mid){
            temp[k] = arr.get(i);
            i++;
            k++;
            
        }
        while(j <= right){
            temp[k] = arr.get(j);
            j++;
            k++;
        }
        for(i = left; i <= right; i++){
            arr.set(i, temp[i]); 
        }
    
    }

    static void shellSort(List<Integer> arr, List<String> data){
        int n = arr.size();
        int h = 1;
        while(h < n/3){
            h = 3*h + 1;
        }
        while( h >= 1){
            for(int i = h; i <= n-1; i++){
                for(int j = i; j >=h && arr.get(j) < arr.get(j - h); j -= h){
                    Collections.swap(arr, j, j - h);
                }
            }
            h = h / 3;
        
        }
    }

    static void radixSort(List<Integer> arr, List<String> data, int numbofdigits){
        for(int pos = 0; pos < numbofdigits; pos++){
            
            List<Integer> sorted = countingsort(arr, data, pos);

            Collections.copy(arr, sorted);
        }

    }

    static List<Integer> countingsort(List<Integer> arr, List<String> data, int pos){
        int[] count = new int[10];
        int size = arr.size();
        List<Integer> output = new ArrayList<>(Collections.nCopies(size, 0));
        
        for(int i = 0; i < size; i++){
            int digit = getDigit(arr.get(i), pos);
            count[digit] = count[digit] + 1;
        }
        for(int i = 1; i < 10; i++){
            count[i] = count[i] + count[i-1];
        }
        for(int i = size - 1; i >= 0; i--){
            int digit = getDigit(arr.get(i), pos);
            count[digit] = count[digit] - 1;
            output.set(count[digit], arr.get(i));
        }
    
        return output;
    }

    static int getDigit(int number, int pos) {
        int divisor = 1;
        for(int i = 0; i < pos; i++) divisor *= 10;
        return (number / divisor) % 10;
    }

}


    