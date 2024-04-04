import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BufferPool {
    private List<Buffer> buffers;
    private int capacity;
    private int cacheHits;
    private int diskReads;
    private int diskWrites;

    public BufferPool(int cap) {
        capacity = cap;
        buffers = new ArrayList<>(capacity);
        cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;
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
        if (leastRecentlyUsed.isDirty()) {
            diskWrites++;
        } else {
            cacheHits++;
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
        List<KVPair<Integer, Integer>> allPairs = new ArrayList<>();
        for (Buffer buffer : buffers) {
            List<KVPair<Integer, Integer>> bufferPairs = buffer.getPairs();
            if (bufferPairs != null) {
                allPairs.addAll(bufferPairs);
            }
        }
        Collections.sort(allPairs, (pair1, pair2) -> pair1.getKey().compareTo(pair2.getKey()));
        return allPairs;
    }


    public int getCacheHits() {
        return cacheHits;
    }


    public int getDiskReads() {
        return diskReads;
    }


    public int getDiskWrites() {
        return diskWrites;
    }
}
