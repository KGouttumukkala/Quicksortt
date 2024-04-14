import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class TheSorter {
    private BufferPool bufferPool;
    private String filename;
    private int numBuffers;

    public TheSorter(String filename, int numBuffers) throws FileNotFoundException {
        this.filename = filename;
        this.numBuffers = numBuffers;
        this.bufferPool = new BufferPool(numBuffers, new File(filename)); // Initialize your BufferPool
    }

    public void sortFile() throws IOException {
        // Load the file into the buffer pool
        loadFileIntoBufferPool();

        // Perform Quicksort on the file through the buffer pool
        quicksort(0, getFileSize() / 4 - 1); // Assuming each record is 4 bytes

        // Write the sorted data back to the file
        writeSortedDataToFile();
    }

    private void loadFileIntoBufferPool() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filename, "rw")) {
            long fileSize = file.length();
            long bytesPerBuffer = 4096; // Size of each buffer in bytes

            for (int i = 0; i < fileSize / bytesPerBuffer; i++) {
                byte[] data = new byte[(int) bytesPerBuffer];
                file.seek(i * bytesPerBuffer);
                file.read(data);
                bufferPool.insert(i, data); // Insert data into buffer pool
            }
        }
    }

    private void quicksort(int low, int high) throws IOException {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quicksort(low, pivotIndex - 1);
            quicksort(pivotIndex + 1, high);
        }
    }

    private int partition(int low, int high) throws IOException {
        // Choose pivot value from a record in the file
        short pivotKey = getKeyFromRecord(low); // Adjust based on your file structure
        int i = low - 1;
        int j = high + 1;

        while (true) {
            do {
                i++;
            } while (getKeyFromRecord(i) < pivotKey);

            do {
                j--;
            } while (getKeyFromRecord(j) > pivotKey);

            if (i >= j) {
                return j;
            }

            // Swap records in the file through the buffer pool
            swapRecords(i, j);
        }
    }

    private short getKeyFromRecord(int recordIndex) throws IOException {
        byte[] recordData = bufferPool.readRecord(recordIndex);
        // Extract the key value from the record data
        return extractKey(recordData); // Implement this method based on your file structure
    }

    private void swapRecords(int recordIndex1, int recordIndex2) throws IOException {
        byte[] recordData1 = bufferPool.readRecord(recordIndex1);
        byte[] recordData2 = bufferPool.readRecord(recordIndex2);
        bufferPool.writeRecord(recordIndex1, recordData2);
        bufferPool.writeRecord(recordIndex2, recordData1);
    }

    private void writeSortedDataToFile() throws IOException {
        // Write the sorted data from the buffer pool back to the file
        bufferPool.flushToFile(filename);
    }

    private int getFileSize() {
        File file = new File(filename);
        return (int)file.length();
    }

    private short extractKey(byte[] recordData) {
        // Extract the key value from the record data
        // This method will depend on your file structure
        return ByteBuffer.wrap(recordData).getShort(); // Example assuming key is a short
    }
}
