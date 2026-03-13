import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
//import java.util.ArrayList;
//import java.util.Collections;
import java.util.Arrays;
import java.util.Random;
import org.knowm.xchart.*;
import java.util.*;

public class Main  {

    


    public static void main(String[] args) {
    
        String header = null;
        int linesToProcess = 250000;
        String[] data = new String[linesToProcess];
        int[] volumes = new int[linesToProcess];
        


        try {
            processData(data, volumes, linesToProcess, header);  // buradaki IOException yakalanacak
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        
        

        int[] sizes = {500, 1000, 2000, 4000, 8000, 16000};

        double[][][] results = new double[sizes.length][3][10]; // [size][scenario][alg] 0:random, 1:sorted, 2:reverse

        for (int s = 0; s < sizes.length; s++) {
            int size = sizes[s];
            int[] baseVolumes = Arrays.copyOf(volumes, size);
            String[] baseData = Arrays.copyOf(data, size);

            // Random scenario
            for (int alg = 0; alg < 10; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    shuffleArrays(baseVolumes, baseData);
                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksort(baseVolumes, 0, size - 1);
                    else if (alg == 1) quicksort(baseVolumes, baseData, 0, size - 1);
                    else if (alg == 2) insertionSort(baseVolumes);
                    else if (alg == 3) insertionSort(baseVolumes, baseData);
                    else if (alg == 4) mergeSort(baseVolumes);
                    else if (alg == 5) mergeSort(baseVolumes, baseData);
                    else if (alg == 6) shellSort(baseVolumes);
                    else if (alg == 7) shellSort(baseVolumes, baseData);
                    else if (alg == 8) radixSort(baseVolumes, 6);
                    else if (alg == 9) radixSort(baseVolumes, baseData, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][0][alg] = total / 10.0;
            }

            // Sorted scenario
            quicksort(baseVolumes, baseData, 0, size - 1); // sort to make it sorted
            for (int alg = 0; alg < 10; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksort(baseVolumes, 0, size - 1);
                    else if (alg == 1) quicksort(baseVolumes, baseData, 0, size - 1);
                    else if (alg == 2) insertionSort(baseVolumes);
                    else if (alg == 3) insertionSort(baseVolumes, baseData);
                    else if (alg == 4) mergeSort(baseVolumes);
                    else if (alg == 5) mergeSort(baseVolumes, baseData);
                    else if (alg == 6) shellSort(baseVolumes);
                    else if (alg == 7) shellSort(baseVolumes, baseData);
                    else if (alg == 8) radixSort(baseVolumes, 6);
                    else if (alg == 9) radixSort(baseVolumes, baseData, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][1][alg] = total / 10.0;
            }

            // Reverse scenario
            reverse(baseVolumes, baseData); // reverse to make it reverse sorted
            for (int alg = 0; alg < 10; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksort(baseVolumes, 0, size - 1);
                    else if (alg == 1) quicksort(baseVolumes, baseData, 0, size - 1);
                    else if (alg == 2) insertionSort(baseVolumes);
                    else if (alg == 3) insertionSort(baseVolumes, baseData);
                    else if (alg == 4) mergeSort(baseVolumes);
                    else if (alg == 5) mergeSort(baseVolumes, baseData);
                    else if (alg == 6) shellSort(baseVolumes);
                    else if (alg == 7) shellSort(baseVolumes, baseData);
                    else if (alg == 8) radixSort(baseVolumes, 6);
                    else if (alg == 9) radixSort(baseVolumes, baseData, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][2][alg] = total / 10.0;
            }
        }

        // Print results
        String[] algNames = {"quicksort original", "quicksort overloaded", "insertionSort original", "insertionSort overloaded", 
                             "mergeSort original", "mergeSort overloaded", "shellSort original", "shellSort overloaded", 
                             "radixSort original", "radixSort overloaded"};
        String[] scenarios = {"Random", "Sorted", "Reverse"};

        for (int s = 0; s < sizes.length; s++) {
            System.out.println("Size: " + sizes[s]);
            for (int scen = 0; scen < 3; scen++) {
                System.out.println("  " + scenarios[scen] + ":");
                for (int alg = 0; alg < 10; alg++) {
                    System.out.println("    " + algNames[alg] + ": " + results[s][scen][alg] + " ms");
                }
            }
            System.out.println();
        }
        
        List<Integer> xData = Arrays.asList(1,2,3,4,5);
        List<Integer> yData = Arrays.asList(10,20,15,30,25);

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Line Graph")
                .xAxisTitle("X")
                .yAxisTitle("Y")
                .build();

        chart.addSeries("Example", xData, yData);

        new SwingWrapper<>(chart).displayChart();
    }



    static void reverse(int[] arr, String[] data){
        int left = 0;
        int right = arr.length - 1;

        while(left < right){

            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            String temp2 = data[left];
            data[left] = data[right];
            data[right] = temp2;

            left++;
            right--;
        }
    }

    static void shuffleArrays(int[] numbers, String[] data) {
        if (numbers.length != data.length) {
            throw new IllegalArgumentException("Arrays must be the same length!");
        }

        Random rnd = new Random();

        for (int i = numbers.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1); // 0..i arası random index

            // swap numbers
            int tempNum = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = tempNum;

            // swap data
            String tempStr = data[i];
            data[i] = data[j];
            data[j] = tempStr;
        }
    }


    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void swap(String[] arr, int i, int j) {
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    
    static void processData (String[] data, int[] volumes, int linesToProcess, String header ) throws IOException {
        
        BufferedReader br = new BufferedReader(new FileReader("C:\\JavaAssignments\\java1\\java\\data\\all_stocks_5yr.csv"));
        String line;

        // skip header
        header = br.readLine();
        int counter = 0;

        while ((line = br.readLine()) != null && counter < linesToProcess) {
            data[counter] = line;
            String[] parts = line.split(",");
            int volume = Integer.parseInt(parts[5]);
            volumes[counter] = volume;
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



    static void quicksort (int[] arr, int low, int high){

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

    static void quicksort (int[] arr, String[] data, int low, int high){

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
            int pivot = partition(arr, data, low, high);
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

    static int partition(int[] arr, int low, int high){
        int pivot = arr[high];
        int i = low - 1;
        for(int j = low; j < high; j++){
            if(arr[j] <= pivot){
                i = i + 1;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    static int partition(int[] arr, String[] data, int low, int high){
        int pivot = arr[high];
        int i = low - 1;
        for(int j = low; j < high; j++){
            if(arr[j] <= pivot){
                i = i + 1;
                swap(arr, i, j);
                swap(data, i, j);
            }
        }
        swap(arr, i + 1, high);
        swap(data, i + 1, high);
        return i + 1;
    }

    
    static void insertionSort(int[] arr) {

        for(int j = 0; j < arr.length; j++){
            int key = arr[j];
            int i = j - 1;
            while( i >= 0 && arr[i] > key){
                arr[i + 1] = arr[i];
                i--;
            } 
            arr[i + 1] = key;
        
        }

    }

    static void insertionSort(int[] arr, String[] data) {

        for(int j = 0; j < arr.length; j++){
            int key = arr[j];
            String keyData = data[j];
            int i = j - 1;
            while( i >= 0 && arr[i] > key){
                arr[i + 1] = arr[i];
                data[i + 1] = data[i];
                i--;
            } 
            arr[i + 1] = key;
            data[i + 1] = keyData;
        
        }

    }


    static void mergeSort(int[] arr) {
        int n = arr.length;
        int[] temp = new int[n];
        int currSize = 1;
        while ( currSize < n){
            int leftStart = 0;
            while(leftStart < n-1){
                int mid = Math.min(leftStart + currSize - 1, n-1);
                int rightEnd =  Math.min(leftStart + 2 * currSize - 1, n-1);
                merge(arr, temp, leftStart, mid, rightEnd);
                leftStart = leftStart + 2*currSize;

            }
            currSize = currSize * 2;
        }

    }

    static void mergeSort(int[] arr, String[] data) {
        int n = arr.length;
        int[] temp = new int[n];
        String[] tempData = new String[n];
        int currSize = 1;
        while ( currSize < n){
            int leftStart = 0;
            while(leftStart < n-1){
                int mid = Math.min(leftStart + currSize - 1, n-1);
                int rightEnd =  Math.min(leftStart + 2 * currSize - 1, n-1);
                merge(arr, data, temp, tempData, leftStart, mid, rightEnd);
                leftStart = leftStart + 2*currSize;

            }
            currSize = currSize * 2;
        }

    }

    static void merge(int[] arr, int[] temp, int left, int mid, int right){
        int i = left;
        int j = mid + 1;
        int k = left;
        while(i <= mid && j <= right){
            if(arr[i] <= arr[j]){
                temp[k] = arr[i];
                i++;
            }
            else{
                temp[k] = arr[j];
                j++;
            }
            k++;
        }
        while(i <= mid){
            temp[k] = arr[i];
            i++;
            k++;
            
        }
        while(j <= right){
            temp[k] = arr[j];
            j++;
            k++;
        }
        for(i = left; i <= right; i++){
            arr[i] = temp[i]; 
        }
    
    }

    static void merge(int[] arr, String[] data, int[] temp, String[] tempData, int left, int mid, int right){
        int i = left;
        int j = mid + 1;
        int k = left;
        while(i <= mid && j <= right){
            if(arr[i] <= arr[j]){
                temp[k] = arr[i];
                tempData[k] = data[i];
                i++;
            }
            else{
                temp[k] = arr[j];
                tempData[k] = data[j];
                j++;
            }
            k++;
        }
        while(i <= mid){
            temp[k] = arr[i];
            tempData[k] = data[i];
            i++;
            k++;
            
        }
        while(j <= right){
            temp[k] = arr[j];
            tempData[k] = data[j];
            j++;
            k++;
        }
        for(i = left; i <= right; i++){
            arr[i] = temp[i];
            data[i] = tempData[i];
        }
    
    }

    static void shellSort(int[] arr){
        int n = arr.length;
        int h = 1;
        while(h < n/3){
            h = 3*h + 1;
        }
        while( h >= 1){
            for(int i = h; i <= n-1; i++){
                for(int j = i; j >=h && arr[j] < arr[j - h]; j -= h){
                    swap(arr, j, j - h);
                }
            }
            h = h / 3;
        
        }
    }

    static void shellSort(int[] arr, String[] data){
        int n = arr.length;
        int h = 1;
        while(h < n/3){
            h = 3*h + 1;
        }
        while( h >= 1){
            for(int i = h; i <= n-1; i++){
                for(int j = i; j >=h && arr[j] < arr[j - h]; j -= h){
                    swap(arr, j, j - h);
                    swap(data, j, j - h);
                }
            }
            h = h / 3;
        
        }
    }

    static void radixSort(int[] arr, int numbofdigits){
        for(int pos = 0; pos < numbofdigits; pos++){
            
            int[] sorted = countingsort(arr, pos);

            for(int i = 0; i < arr.length; i++){
                arr[i] = sorted[i];
            }
        }

    }

    static void radixSort(int[] arr, String[] data, int numbofdigits){
        for(int pos = 0; pos < numbofdigits; pos++){
            
            countingsort(arr, data, pos);
        }

    }

    static int[] countingsort(int[] arr, int pos){
        int[] count = new int[10];
        int size = arr.length;
        int[] output = new int[size];
        
        for(int i = 0; i < size; i++){
            int digit = getDigit(arr[i], pos);
            count[digit] = count[digit] + 1;
        }
        for(int i = 1; i < 10; i++){
            count[i] = count[i] + count[i-1];
        }
        for(int i = size - 1; i >= 0; i--){
            int digit = getDigit(arr[i], pos);
            count[digit] = count[digit] - 1;
            output[count[digit]] = arr[i];
        }
    
        return output;
    }

    static void countingsort(int[] arr, String[] data, int pos){
        int[] count = new int[10];
        int size = arr.length;
        int[] output = new int[size];
        String[] outputData = new String[size];
        
        for(int i = 0; i < size; i++){
            int digit = getDigit(arr[i], pos);
            count[digit] = count[digit] + 1;
        }
        for(int i = 1; i < 10; i++){
            count[i] = count[i] + count[i-1];
        }
        for(int i = size - 1; i >= 0; i--){
            int digit = getDigit(arr[i], pos);
            count[digit] = count[digit] - 1;
            output[count[digit]] = arr[i];
            outputData[count[digit]] = data[i];
        }
        
        for(int i = 0; i < size; i++){
            arr[i] = output[i];
            data[i] = outputData[i];
        }
    }

    static int getDigit(int number, int pos) {
        int divisor = 1;
        for(int i = 0; i < pos; i++) divisor *= 10;
        return (number / divisor) % 10;
    }

}


    