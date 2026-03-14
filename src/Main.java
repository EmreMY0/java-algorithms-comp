import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import org.knowm.xchart.*;

//import java.util.ArrayList;
//import java.util.Collections;
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


        
        

        /*
        int[] sizes = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        double[][][] results = new double[sizes.length][3][10]; // [size][scenario][alg] 0:random, 1:sorted, 2:reverse

        for (int s = 0; s < sizes.length; s++) {
            int size = sizes[s];

            // Keep original data for each size so each algorithm sees the same input state.
            int[] origVolumes = Arrays.copyOf(volumes, size);
            String[] origData = Arrays.copyOf(data, size);

            // Prepare pre-sorted and pre-reversed versions for the Sorted/Reverse scenarios.
            int[] sortedVolumes = Arrays.copyOf(origVolumes, size);
            String[] sortedData = Arrays.copyOf(origData, size);
            quicksort(sortedVolumes, sortedData, 0, size - 1);

            int[] reversedVolumes = Arrays.copyOf(sortedVolumes, size);
            String[] reversedData = Arrays.copyOf(sortedData, size);
            reverse(reversedVolumes, reversedData);

            // Random scenario
            for (int alg = 0; alg < 10; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    int[] runVolumes = Arrays.copyOf(origVolumes, size);
                    String[] runData = Arrays.copyOf(origData, size);
                    shuffleArrays(runVolumes, runData);

                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksort(runVolumes, 0, size - 1);
                    else if (alg == 1) quicksort(runVolumes, runData, 0, size - 1);
                    else if (alg == 2) insertionSort(runVolumes);
                    else if (alg == 3) insertionSort(runVolumes, runData);
                    else if (alg == 4) mergeSort(runVolumes);
                    else if (alg == 5) mergeSort(runVolumes, runData);
                    else if (alg == 6) shellSort(runVolumes);
                    else if (alg == 7) shellSort(runVolumes, runData);
                    else if (alg == 8) radixSort(runVolumes, 6);
                    else if (alg == 9) radixSort(runVolumes, runData, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][0][alg] = total / 10.0;
            }

            // Sorted scenario
            for (int alg = 0; alg < 10; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    int[] runVolumes = Arrays.copyOf(sortedVolumes, size);
                    String[] runData = Arrays.copyOf(sortedData, size);

                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksort(runVolumes, 0, size - 1);
                    else if (alg == 1) quicksort(runVolumes, runData, 0, size - 1);
                    else if (alg == 2) insertionSort(runVolumes);
                    else if (alg == 3) insertionSort(runVolumes, runData);
                    else if (alg == 4) mergeSort(runVolumes);
                    else if (alg == 5) mergeSort(runVolumes, runData);
                    else if (alg == 6) shellSort(runVolumes);
                    else if (alg == 7) shellSort(runVolumes, runData);
                    else if (alg == 8) radixSort(runVolumes, 6);
                    else if (alg == 9) radixSort(runVolumes, runData, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][1][alg] = total / 10.0;
            }

            // Reverse scenario
            for (int alg = 0; alg < 10; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    int[] runVolumes = Arrays.copyOf(reversedVolumes, size);
                    String[] runData = Arrays.copyOf(reversedData, size);

                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksort(runVolumes, 0, size - 1);
                    else if (alg == 1) quicksort(runVolumes, runData, 0, size - 1);
                    else if (alg == 2) insertionSort(runVolumes);
                    else if (alg == 3) insertionSort(runVolumes, runData);
                    else if (alg == 4) mergeSort(runVolumes);
                    else if (alg == 5) mergeSort(runVolumes, runData);
                    else if (alg == 6) shellSort(runVolumes);
                    else if (alg == 7) shellSort(runVolumes, runData);
                    else if (alg == 8) radixSort(runVolumes, 6);
                    else if (alg == 9) radixSort(runVolumes, runData, 6);
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
            for (int scen = 0; scen < scenarios.length; scen++) {
                System.out.println("  " + scenarios[scen] + ":");
                for (int alg = 0; alg < 10; alg++) {
                    System.out.println("    " + algNames[alg] + ": " + results[s][scen][alg] + " ms");
                }
            }
            System.out.println();
        }

        // Build 6 line charts: one for each scenario (Random / Sorted / Reverse) and one per method set (original vs overloaded)
        List<Integer> xData = Arrays.stream(sizes).boxed().collect(Collectors.toList());
        String[] baseAlgNames = {"Quicksort", "Insertion Sort", "Merge Sort", "Shell Sort", "Radix Sort"};

        for (int scen = 0; scen < scenarios.length; scen++) {
            // Original method variants
            XYChart chartOriginal = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title(scenarios[scen] + " - Original methods")
                    .xAxisTitle("Input size")
                    .yAxisTitle("Average time (ms)")
                    .build();

            for (int i = 0; i < baseAlgNames.length; i++) {
                int algIndex = 2 * i; // original method indices: 0,2,4,6,8
                List<Double> yData = new ArrayList<>();
                for (int s = 0; s < sizes.length; s++) {
                    yData.add(results[s][scen][algIndex]);
                }
                chartOriginal.addSeries(baseAlgNames[i], xData, yData);
            }

            new SwingWrapper<>(chartOriginal).displayChart();

            // Overloaded method variants
            XYChart chartOverloaded = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title(scenarios[scen] + " - Overloaded methods")
                    .xAxisTitle("Input size")
                    .yAxisTitle("Average time (ms)")
                    .build();

            for (int i = 0; i < baseAlgNames.length; i++) {
                int algIndex = 2 * i + 1; // overloaded method indices: 1,3,5,7,9
                List<Double> yData = new ArrayList<>();
                for (int s = 0; s < sizes.length; s++) {
                    yData.add(results[s][scen][algIndex]);
                }
                chartOverloaded.addSeries(baseAlgNames[i], xData, yData);
            }

            new SwingWrapper<>(chartOverloaded).displayChart();
        }
    */
    
    
        // Modified code for indirect sorting methods only
        int[] sizes = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        double[][][] results = new double[sizes.length][3][5]; // [size][scenario][alg] 0:random, 1:sorted, 2:reverse

        for (int s = 0; s < sizes.length; s++) {
            int size = sizes[s];

            // Keep original data for each size
            int[] origVolumes = Arrays.copyOf(volumes, size);

            // Prepare sorted index
            int[] sortedIndex = createIndex(size);
            quicksortIndirect(sortedIndex, origVolumes, 0, size - 1);

            // Prepare reversed index
            int[] reversedIndex = Arrays.copyOf(sortedIndex, size);
            reverseIndex(reversedIndex);

            // Random scenario
            for (int alg = 0; alg < 5; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    int[] runIndex = createIndex(size);
                    shuffleIndex(runIndex);

                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksortIndirect(runIndex, origVolumes, 0, size - 1);
                    else if (alg == 1) insertionSortIndirect(runIndex, origVolumes);
                    else if (alg == 2) mergeSortIndirect(runIndex, origVolumes);
                    else if (alg == 3) shellSortIndirect(runIndex, origVolumes);
                    else if (alg == 4) radixSortIndirect(runIndex, origVolumes, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][0][alg] = total / 10.0;
            }

            // Sorted scenario
            for (int alg = 0; alg < 5; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    int[] runIndex = Arrays.copyOf(sortedIndex, size);

                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksortIndirect(runIndex, origVolumes, 0, size - 1);
                    else if (alg == 1) insertionSortIndirect(runIndex, origVolumes);
                    else if (alg == 2) mergeSortIndirect(runIndex, origVolumes);
                    else if (alg == 3) shellSortIndirect(runIndex, origVolumes);
                    else if (alg == 4) radixSortIndirect(runIndex, origVolumes, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][1][alg] = total / 10.0;
            }

            // Reverse scenario
            for (int alg = 0; alg < 5; alg++) {
                long total = 0;
                for (int run = 0; run < 10; run++) {
                    int[] runIndex = Arrays.copyOf(reversedIndex, size);

                    long start = System.currentTimeMillis();
                    if (alg == 0) quicksortIndirect(runIndex, origVolumes, 0, size - 1);
                    else if (alg == 1) insertionSortIndirect(runIndex, origVolumes);
                    else if (alg == 2) mergeSortIndirect(runIndex, origVolumes);
                    else if (alg == 3) shellSortIndirect(runIndex, origVolumes);
                    else if (alg == 4) radixSortIndirect(runIndex, origVolumes, 6);
                    long end = System.currentTimeMillis();
                    total += (end - start);
                }
                results[s][2][alg] = total / 10.0;
            }
        }

        // Print results
        String[] algNames = {"quicksort indirect", "insertionSort indirect", "mergeSort indirect", "shellSort indirect", "radixSort indirect"};
        String[] scenarios = {"Random", "Sorted", "Reverse"};

        for (int s = 0; s < sizes.length; s++) {
            System.out.println("Size: " + sizes[s]);
            for (int scen = 0; scen < scenarios.length; scen++) {
                System.out.println("  " + scenarios[scen] + ":");
                for (int alg = 0; alg < 5; alg++) {
                    System.out.println("    " + algNames[alg] + ": " + results[s][scen][alg] + " ms");
                }
            }
            System.out.println();
        }

        // Build 3 line charts: one for each scenario
        List<Integer> xData = Arrays.stream(sizes).boxed().collect(Collectors.toList());
        String[] baseAlgNames = {"Quicksort", "Insertion Sort", "Merge Sort", "Shell Sort", "Radix Sort"};

        for (int scen = 0; scen < scenarios.length; scen++) {
            XYChart chart = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title(scenarios[scen] + " - Indirect methods")
                    .xAxisTitle("Input size")
                    .yAxisTitle("Average time (ms)")
                    .build();

            for (int i = 0; i < baseAlgNames.length; i++) {
                List<Double> yData = new ArrayList<>();
                for (int s = 0; s < sizes.length; s++) {
                    yData.add(results[s][scen][i]);
                }
                chart.addSeries(baseAlgNames[i], xData, yData);
            }

            new SwingWrapper<>(chart).displayChart();
        }

    

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

        for(int j = 1; j < arr.length; j++){
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

        for(int j = 1; j < arr.length; j++){
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

    static int[] createIndex(int size){
        int[] index = new int[size];
        for(int i = 0; i < size; i++){
            index[i] = i;
        }
        return index;
    }

    static void quicksortIndirect(int[] index, int[] volumes, int low, int high){

        int stackSize = high - low + 1;
        int[] stack = new int[stackSize];
        int top = -1;

        stack[++top] = low;
        stack[++top] = high;

        while(top >= 0){

            high = stack[top--];
            low = stack[top--];

            int p = partitionIndirect(index, volumes, low, high);

            if(p - 1 > low){
                stack[++top] = low;
                stack[++top] = p - 1;
            }

            if(p + 1 < high){
                stack[++top] = p + 1;
                stack[++top] = high;
            }
        }
    }

    static int partitionIndirect(int[] index, int[] volumes, int low, int high){

        int pivot = volumes[index[high]];
        int i = low - 1;

        for(int j = low; j < high; j++){

            if(volumes[index[j]] <= pivot){

                i++;
                swap(index, i, j);
            }
        }

        swap(index, i+1, high);
        return i+1;
    }

    static void insertionSortIndirect(int[] index, int[] volumes){

        for(int j = 1; j < index.length; j++){

            int keyIndex = index[j];
            int keyValue = volumes[keyIndex];

            int i = j - 1;

            while(i >= 0 && volumes[index[i]] > keyValue){

                index[i+1] = index[i];
                i--;
            }

            index[i+1] = keyIndex;
        }
    }

    static void mergeSortIndirect(int[] index, int[] volumes){

        int n = index.length;
        int[] temp = new int[n];

        int currSize = 1;

        while(currSize < n){

            int leftStart = 0;

            while(leftStart < n-1){

                int mid = Math.min(leftStart + currSize - 1, n-1);
                int rightEnd = Math.min(leftStart + 2*currSize -1, n-1);

                mergeIndirect(index, volumes, temp, leftStart, mid, rightEnd);

                leftStart += 2*currSize;
            }

            currSize *= 2;
        }
    }

    static void mergeIndirect(int[] index, int[] volumes, int[] temp, int left, int mid, int right){

        int i = left;
        int j = mid+1;
        int k = left;

        while(i <= mid && j <= right){

            if(volumes[index[i]] <= volumes[index[j]]){
                temp[k++] = index[i++];
            }
            else{
                temp[k++] = index[j++];
            }
        }

        while(i <= mid) temp[k++] = index[i++];
        while(j <= right) temp[k++] = index[j++];

        for(i = left; i <= right; i++)
            index[i] = temp[i];
    }


    static void shellSortIndirect(int[] index, int[] volumes){

        int n = index.length;
        int h = 1;

        while(h < n/3)
            h = 3*h + 1;

        while(h >= 1){

            for(int i = h; i < n; i++){

                for(int j = i; j >= h && volumes[index[j]] < volumes[index[j-h]]; j -= h){

                    swap(index, j, j-h);
                }
            }

            h /= 3;
        }
    }    


    static void radixSortIndirect(int[] index, int[] volumes, int digits){

        for(int pos = 0; pos < digits; pos++){

            countingSortIndirect(index, volumes, pos);
        }
    }

    static void countingSortIndirect(int[] index, int[] volumes, int pos){

        int n = index.length;

        int[] output = new int[n];
        int[] count = new int[10];

        for(int i=0;i<n;i++){

            int digit = getDigit(volumes[index[i]], pos);
            count[digit]++;
        }

        for(int i=1;i<10;i++)
            count[i]+=count[i-1];

        for(int i=n-1;i>=0;i--){

            int digit = getDigit(volumes[index[i]], pos);
            output[--count[digit]] = index[i];
        }

        for(int i=0;i<n;i++)
            index[i] = output[i];
    }

    static void shuffleIndex(int[] index) {
        Random rnd = new Random();
        for (int i = index.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            swap(index, i, j);
        }
    }

    static void reverseIndex(int[] index) {
        int left = 0;
        int right = index.length - 1;
        while (left < right) {
            swap(index, left, right);
            left++;
            right--;
        }
    }

}
    