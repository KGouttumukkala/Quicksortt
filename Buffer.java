import java.io.*;
import java.util.ArrayList;
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
        data = new byte[bufferSize];
        isDirty = false;
    }


    @SuppressWarnings("unchecked")
    public List<KVPair<Integer, Integer>> getPairs() {
        List<KVPair<Integer, Integer>> pairsList = new ArrayList<>();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            while (bis.available() > 0) {
                pairsList.add((KVPair<Integer, Integer>) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pairsList;
    }

    public void setPairs(List<KVPair<Integer, Integer>> pairs) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            for (KVPair<Integer, Integer> pair : pairs) {
                oos.writeObject(pair);
            }
            oos.flush();
            data = bos.toByteArray();
            isDirty = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addKeyValue(short key, short value) {
        // TODO Auto-generated method stub
        
    }
}
