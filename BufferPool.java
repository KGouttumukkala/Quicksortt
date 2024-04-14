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
    
    public void insert(int i, byte[] b) throws IOException {
        Buffer remove = null;
        if (currentNumBuffers == totalNumBuffers) {
            remove = buffers[buffers.length - 1].copy();
            Buffer buffer = new Buffer(i, b);
            System.arraycopy(buffers, 0, buffers, 1, buffers.length - 1);
            buffers[0] = buffer;
            write(remove.getIndex(), remove.getBytes());
        }
        else {
            System.arraycopy(buffers, 0, buffers, 1, buffers.length - 1);
            buffers[0] = new Buffer(i, b);
            currentNumBuffers++;
        }
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

    public void swapBuffers(int index1, int index2) {
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

    public byte[] readRecord(int recordIndex2) {
        // TODO Auto-generated method stub
        return null;
    }

}
