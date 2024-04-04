import java.util.ArrayList;
import java.util.List;

public class BufferPool {
    private List<Buffer> buffers;
    private int capacity;

    public BufferPool(int cap) {
        capacity = cap;
        buffers = new ArrayList<>(capacity);
        initializeBuffers();
    }


    private void initializeBuffers() {
        for (int i = 0; i < capacity; i++) {
            buffers.add(new Buffer());
        }
    }


    public Buffer getBuffer() {
        if (buffers.isEmpty()) {
            throw new IllegalStateException("Buffer pool is empty");
        }
        
        Buffer leastRecentlyUsed = buffers.get(0);
        for (Buffer buffer: buffers) {
            if (buffer.getLastUsedTime() < leastRecentlyUsed.getLastUsedTime()) {
                leastRecentlyUsed = buffer;
            }
        }
        
        leastRecentlyUsed.setLastUsedTime(System.currentTimeMillis());
        
        return leastRecentlyUsed;
    }


    public void releaseBuffer(Buffer buffer) {
        if (!buffers.contains(buffer)) {
            throw new IllegalArgumentException("Buffer not in the pool");
        }
        
        buffer.reset();
    }


    public void addKeyValue(short key, short value) {
        Buffer leastRecentlyUsed = getBuffer();
        leastRecentlyUsed.addKeyValue(key, value);
    }


    public int getCapacity() {
        return capacity;
    }


    public List<KVPair<Integer, Integer>> getAllSortedPairs() {
        // TODO Auto-generated method stub
        return null;
    }


    public String getCacheHits() {
        // TODO Auto-generated method stub
        return null;
    }


    public String getDiskReads() {
        // TODO Auto-generated method stub
        return null;
    }


    public String getDiskWrites() {
        // TODO Auto-generated method stub
        return null;
    }
}
