import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferPool {
    private Buffer[] buffers;
    private int totalNumBuffers;
    private RandomAccessFile file;
    private int currentNumBuffers;

    public BufferPool(int numBuffers, File f) throws FileNotFoundException {
        totalNumBuffers = numBuffers;
        buffers = new Buffer[totalNumBuffers];
        currentNumBuffers = 0;
        file = new RandomAccessFile(f, "rw");
    }
    
    public void write(int i, byte[] w) throws IOException {
        file.seek(i);
        file.write(w);
    }
    
    public byte[] read(int i) throws IOException {
        byte[] b = new byte[4];
        file.seek(i);
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
                currentNumBuffers++;
            }
        }
        
        currentNumBuffers = 0;
        return numWrites;
    }
    
    public void insert() {
        
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

}
