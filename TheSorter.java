import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class TheSorter {
    private BufferPool bufferPool;
    private File file;
    private int numBuffers;
    private int numRecords;

    public TheSorter(String filename, int numBuffers) throws IOException {
        File file = new File(filename);
        numRecords = (int)file.length() / 4;
        this.bufferPool = new BufferPool(numBuffers, file);
    }


    public void quickSort() throws IOException {
        int low = 0;
        int high = numRecords - 1;
        quickSortRecursive(low, high);
        bufferPool.flush();
    }


    private void quickSortRecursive(int low, int high) throws IOException {
        if (low < high) {
            boolean allRepeats = true;
            short pivotValue = getShortValue(low); // Get the pivot value

            // Check if all elements in the partition are equal to the pivot
            for (int i = low + 1; i <= high; i++) {
                if (getShortValue(i) != pivotValue) {
                    allRepeats = false;
                    break;
                }
            }

            if (!allRepeats) {
                int pivotIndex = partition(low, high);
                quickSortRecursive(low, pivotIndex - 1);
                quickSortRecursive(pivotIndex + 1, high);
            }
        }
    }



    private int partition(int low, int high) throws IOException {
        // Choose a pivot (e.g., median of three, random, etc.)
        short pivot = medianOfThree(low, high);

        // Partitioning logic using the buffer pool
        int i = low - 1;
        
        for (int j = low; j <= high - 1; j++) {

            // If current element is smaller than the pivot
            if (getShortValue(j) < pivot) {

                // Increment index of smaller element
                i++;
                bufferPool.swap(i, j);
            }
        }
        bufferPool.swap(i + 1, high);
        return (i + 1);
    }

    
    private short medianOfThree(int low, int high) throws IOException {
        int mid = low + (high - low) / 2;
        short lowValue = getShortValue(low);    
        short midValue = getShortValue(mid);
        short highValue = getShortValue(high);
        if (lowValue > midValue) {
            if (midValue > highValue) {
                return midValue;
            }
            else if (lowValue > highValue) {
                return highValue;
            }
            else {
                return lowValue;
            }
        }
        else {
            if (lowValue > highValue) {
                return lowValue;
            }
            else if (midValue > highValue) {
                return highValue;
            }
            else {
                return midValue;
            }
        }
    }


    private short getShortValue(int index) throws IOException {
        byte[] bytes = bufferPool.getByte(index);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getShort();
    }
}
