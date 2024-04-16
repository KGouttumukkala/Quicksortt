import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Random;

public class TheSorter {
    private BufferPool bufferPool;
    private File file;
    private int numBuffers;
    private int numRecords;
    private Statistics stats;

    public TheSorter(String filename, int numBuffers, Statistics stats)
        throws IOException {
        File file = new File(filename);
        numRecords = (int)file.length() / 4;
        this.bufferPool = new BufferPool(numBuffers, file, stats);
        this.stats = stats;
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
            Random random = new Random();
            int randomIndex = low + random.nextInt(high - low + 1);
            short pivotValue = getShortValue(randomIndex);
            long startTime = System.currentTimeMillis();

            for (int i = low + 1; i <= high; i++) {
                if (getShortValue(i) != pivotValue) {
                    allRepeats = false;
                    break;
                }
            }
            long endTime = System.currentTimeMillis();
            stats.timeCheckingDuplicates += endTime - startTime;
            if (!allRepeats) {
                int pivotIndex = partition(low, high, randomIndex);
                quickSortRecursive(low, pivotIndex - 1);
                quickSortRecursive(pivotIndex + 1, high);
            }
        }
    }

    private int partition(int low, int high, int pivotIndex) throws IOException {
        bufferPool.swap(pivotIndex, high);
        short pivot = getShortValue(high);
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (getShortValue(j) < pivot) {
                i++;
                bufferPool.swap(i, j);
            }
        }
        bufferPool.swap(i + 1, high);
        return (i + 1);
    }

    private short getShortValue(int index) throws IOException {
        byte[] bytes = bufferPool.getByte(index);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getShort();
    }
}
