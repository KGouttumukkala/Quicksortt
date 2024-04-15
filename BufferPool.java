import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

public class BufferPool {
    private Buffer[] buffers;
    private int totalNumBuffers;
    private RandomAccessFile file;
    private int currentNumBuffers;
    private Queue<Integer> bufferQueue;
    private int[] indexes;

    public BufferPool(int numBuffers, File f) throws FileNotFoundException {
        totalNumBuffers = numBuffers;
        buffers = new Buffer[totalNumBuffers];
        currentNumBuffers = 0;
        file = new RandomAccessFile(f, "rw");
        indexes = new int[numBuffers * 1024];
        bufferQueue = new LinkedList<>();
        for (int i = 0; i < numBuffers; i++) {
            bufferQueue.offer(i);
        }
    }
    
    public void write(int i, byte[] w) throws IOException {
        file.seek(i);
        file.write(w);
    }
    
    public byte[] getByte(int i) throws IOException {
        int index = containsIndex(i);
        if (index == -1) {
            return getByteFromFile(i);
        }
        else {
            int bufferNum = index / 1024;
            int offset = index % 1024;
            byte[] bytes = buffers[bufferNum].getBytes(offset);
            updateBuffersAndIndexes(bufferNum, offset, i, index);
            return bytes;
        }
    }
    
    private void updateBuffersAndIndexes(int bufferNum, int offset, int index, int oldPosition) {
        int newPosition = bufferNum * 1024;
        for (int i = newPosition + 1; i < (bufferNum + 1) * 1024; i++){
            indexes[i] = indexes[i - 1];
        }
        indexes[newPosition] = index;
        buffers[bufferNum].moveBytesToFront(offset);
    }

    private byte[] getByteFromFile(int i) throws IOException {
        byte[] b = new byte[4];
        file.seek(i * 4);
        file.read(b);
        return b;
    }
    
    public int flush() throws IOException {
        Buffer current = null;
        int numWrites = 0;
        
        for (int i = 0; i < currentNumBuffers; i++) {
            current = buffers[i];
            if (current.isDirty()) {
                byte[] b = current.getBytes();
                write(current.getIndex(), b);
                buffers[i] = null;
                numWrites++;
            }
        }
        return numWrites;
    }
    
    public Buffer getBuffer(int in) {
        Buffer current = null;
        
        for (int i = 0; i < currentNumBuffers; i++) {
            current = buffers[i];
            int start = current.getIndex();
            int end = start + current.getSizeOfBuffer();
            if (end > in && start <= in) {
                return current;
            }
        }
        return null;
    }

    public void swap(int index1, int index2) {
        Buffer temp = buffers[index1];
        buffers[index1] = buffers[index2];
        buffers[index2] = temp;
    }

    public void writeRecord(int recordIndex2, byte[] recordData1) {
        // TODO Auto-generated method stub
        
    }

    public void flushToFile(String filename) {
        // TODO Auto-generated method stub
        
    }

    public byte[] readRecord(int recordIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void evictLRU() {
        
    }
    
    private int containsIndex(int i) {
        for (int index = 0; index < indexes.length; index++) {
            if (indexes[index] == i) {
                return index;
            }
        }
        return -1;
    }
}
