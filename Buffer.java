import java.io.*;

public class Buffer {
    private byte[] data;
    private boolean isDirty;
    private int bufferSize;
    
    public Buffer(int size) {
        bufferSize = size;
        data = new byte[bufferSize];
        isDirty = false;
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
}
