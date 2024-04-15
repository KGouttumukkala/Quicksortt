import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

public class BufferPool {
    private Buffer[] buffers;
    private int totalNumBuffers;
    private RandomAccessFile file;
    private Queue<Integer> bufferQueue;
    private int[] indexes;

    public BufferPool(int numBuffers, File f) throws IOException {
        totalNumBuffers = numBuffers;
        buffers = new Buffer[totalNumBuffers];
        file = new RandomAccessFile(f, "rw");
        indexes = new int[numBuffers * 1024];
        bufferQueue = new LinkedList<>();
        populateBuffers();
        printBuffers();
    }
    
    private void populateBuffers() throws IOException {
        for (int i = 0; i < totalNumBuffers; i++) {
            byte[] bytesFromDisk = getInitialBytesFromFile(i);
            buffers[i] = new Buffer(bytesFromDisk); // Initialize each buffer object
            bufferQueue.offer(i);
            int start = i * 1024;
            int end = start + 1023;
            for (int j = start; j <= end; j++) {
                indexes[j] = j;
            }
        }
    }
    
    private byte[] getInitialBytesFromFile(int i) throws IOException {
        byte[] bytes = new byte[4096]; // Assuming each buffer can hold 4096 bytes
        long bytePosition = (long) i * bytes.length;
        file.seek(bytePosition);
        file.read(bytes);
        return bytes;
    }
    
    private int getBufferNumberFromIndex(int index) {
        return index / 1024;
    }
    
    private int getBufferByteOffsetFromIndex(int index) {
        return (index % 1024) * 4; // Assuming each record is 4 bytes
    }
    
    private int getFileByteNumberFromIndex(int index) {
        return index * 4; // Assuming each record is 4 bytes
    }

    public void close() throws IOException {
        file.close(); // Close the file
    }

    public byte[] getByte(int index) {
        // TODO Auto-generated method stub
        return null;
    }
    

    public void swap(int i, int j) {
        // TODO Auto-generated method stub
    }
    
    private int checkIfIndexIsInBuffers(int index) {
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] == index) {
                return i;
            }
        }
        return -1;
    }

    
    public void printBuffers() {
        for (int i = 0; i < totalNumBuffers; i++) {
            System.out.println("Buffer " + i + ":");
            if (buffers[i] != null) {
                System.out.println(new String(buffers[i].getBytes())); // Assuming bytes represent text data
            } else {
                System.out.println("Empty");
            }
            System.out.println("----------");
        }
        System.out.println("Indexes:");
        for (int index : indexes) {
            System.out.print(index + " ");
        }
        System.out.println("\n----------");
    }
}
