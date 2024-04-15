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
        for (int i = 0; i < numBuffers; i++) {
            bufferQueue.offer(i);
        }
        populateBuffers();
    }
    
    private void populateBuffers() throws IOException {
        for (int i = 0; i < totalNumBuffers; i++) {
            byte[] bytesFromDisk = getBytesFromFile();
            buffers[i].changeBytes(bytesFromDisk);
            int start = i * 1024;
            int end = start + 1023;
            for (int j = start; j <= end; j++) {
                indexes[j] = j;
            }
        }
    }
    
    private int getBufferNumberFromIndex(int index) {
        return index / 1024;
    }
    
    private int getBufferByteOffsetFromIndex(int index) {
        
    }
    
    private int getByteNumberFromIndexAndBufferNumber(int index, int bufferNumber) {
        
    }
    
    private int getFileByteNumberFromIndex(int index) {
        return index * 4;
    }
}
