public class BufferPool {
    private Buffer[] buffers;
    private int totalNumBuffers;

    public BufferPool(int numBuffers) {
        totalNumBuffers = numBuffers;
        buffers = new Buffer[totalNumBuffers];
    }

}
