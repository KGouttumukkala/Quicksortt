import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Buffer {
    private byte[] data;
    private boolean isDirty;
    private int bufferSize;
    private AtomicLong lastUsedTime;
    
    public Buffer() {
        bufferSize = 4096;
        data = new byte[bufferSize];
        isDirty = false;
        lastUsedTime = new AtomicLong(System.currentTimeMillis());
    }
    
    public void readFromFile(FileInputStream fis, int blockSize) throws IOException {
        fis.read(data, 0, blockSize);
    }
    
    public void writeToFile(FileOutputStream fos) throws IOException {
        fos.write(data);
        isDirty = false;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] newData) {
        data = newData;
        isDirty = true;
    }
    
    public boolean isDirty() {
        return isDirty;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public long getLastUsedTime() {
        return lastUsedTime.get();
    }

    public void setLastUsedTime(long time) {
        lastUsedTime.set(time);
    }
    
    public void reset() {
        
    }

    public List<Record> getRecords() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setRecords(List<Record> records) {
        // TODO Auto-generated method stub
        
    }
}
