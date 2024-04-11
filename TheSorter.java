import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TheSorter {
    private BufferPool pool;
    private Statistics statistics;

    public TheSorter(String dataFileName, int numBuffers, Statistics stats) throws FileNotFoundException {
        pool = new BufferPool(numBuffers, new File(dataFileName));
        statistics = stats;
    }

    public void quickSort() throws IOException {
        int[] data = readDataFromFile(); // Read data into an array from the file
        quickSortRecursive(data, 0, data.length - 1); // Sort the data array
        writeDataToFile(data); // Write the sorted data back to the file
    }

    private int[] readDataFromFile() throws IOException {
        // Logic to read data into an array from the file using the buffer pool
        // Example:
        // byte[] bytes = pool.read(...);
        // Convert bytes to an integer array or your data format
        return new int[] { 5, 3, 8, 2, 1, 7, 4 }; // Placeholder, replace with actual data
    }

    private void writeDataToFile(int[] data) throws IOException {
        // Logic to write data from the array back to the file using the buffer pool
        // Example:
        // byte[] bytes = convertIntArrayToBytes(data);
        // pool.write(bytes, ...);
    }

    private void quickSortRecursive(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            //check for duplicates
            quickSortRecursive(array, low, pivotIndex - 1);
            quickSortRecursive(array, pivotIndex + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
