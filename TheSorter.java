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
            for (int i = low; i <= high; i++) {
                if (getShortValue(i) != getShortValue(low)) {
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
        int j = high + 1;
        while (true) {
            do {
                i++;
            }
            while (getShortValue(i) < pivot);

            do {
                j--;
            }
            while (getShortValue(j) > pivot);

            if (i >= j) {
                return j;
            }
            bufferPool.swap(i, j);
        }
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
