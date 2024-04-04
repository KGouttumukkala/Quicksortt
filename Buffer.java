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
        System.out.println("File size before reading: " + fis.available());
        fis.read(data, 0, blockSize);
        System.out.println("File size after reading: " + fis.available());
    }
    
    public void writeToFile(FileOutputStream fos) throws IOException {
        System.out.println("File size before writing: " + fos.getChannel().size());
        fos.write(data);
        isDirty = false;
        System.out.println("File size after writing: " + fos.getChannel().size());
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
        // Calculate the index in the data array to store the key
        int keyIndex = calculateKeyIndex();

        // Store the key and value in the data array
        data[keyIndex] = (byte) (key & 0xFF);
        data[keyIndex + 1] = (byte) ((key >> 8) & 0xFF);
        data[keyIndex + 2] = (byte) (value & 0xFF);
        data[keyIndex + 3] = (byte) ((value >> 8) & 0xFF);

        isDirty = true;
    }

    private int calculateKeyIndex() {
        int index = calculateIndex() * 4;
        return index;
    }

    private int calculateIndex() {
        int pairsPerBuffer = bufferSize / 4;
        int currentIndex = (int) (System.currentTimeMillis() % pairsPerBuffer);
        return currentIndex;
    }
}
