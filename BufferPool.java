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
            byte[] bytesFromDisk = getBytesFromFile(i * 1024, 1024);
            buffers[i].changeBytes(bytesFromDisk);
            int start = i * 1024;
            int end = start + 1023;
            for (int j = start; j <= end; j++) {
                indexes[j] = j;
            }
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
        int frontNumber = bufferQueue.poll();
        buffers[frontNumber].addBytesToFront(b);
        bufferQueue.offer(frontNumber);
        return b;
    }
    
    public void flush() throws IOException {
    }

    public void swap(int index1, int index2) {
        Buffer temp = buffers[index1];
        buffers[index1] = buffers[index2];
        buffers[index2] = temp;
    }
    
    private int containsIndex(int i) {
        for (int index = 0; index < indexes.length; index++) {
            if (indexes[index] == i) {
                return index;
            }
        }
        return -1;
    }
    
    private byte[] getBytesFromFile(int offset, int length) throws IOException {
        byte[] bytes = new byte[length];
        file.seek(offset);
        file.read(bytes);
        return bytes;
    }
}
