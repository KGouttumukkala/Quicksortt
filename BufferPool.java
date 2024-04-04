import java.util.ArrayList;
import java.util.List;

public class BufferPool {
    private List<Buffer> buffers;
    private int capacity;
    private int bufferSize;

    public BufferPool(int cap, int buffSize) {
        capacity = cap;
        buffers = new ArrayList<>(capacity);
        bufferSize = buffSize;
        initializeBuffers();
    }


    private void initializeBuffers() {
        for (int i = 0; i < capacity; i++) {f
            buffers.add(new Buffer(bufferSize));
        }
    }


    public Buffer getBuffer() {
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
        buffer.reset();
    }
}
