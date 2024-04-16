import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

public class BufferPool {
    private Buffer[] buffers;
    private int totalNumBuffers;
    private RandomAccessFile file;
    private Queue<Integer> bufferQueue;
    private int[] indexes;

    public BufferPool(int numBuffers, File f) throws IOException {
        totalNumBuffers = numBuffers;
        buffers = new Buffer[totalNumBuffers];
        file = new RandomAccessFile(f, "rw");
        indexes = new int[numBuffers * 1024];
        bufferQueue = new LinkedList<>();
        populateBuffers();
    }


    private void populateBuffers() throws IOException {
        for (int i = 0; i < totalNumBuffers; i++) {
            byte[] bytesFromDisk = getInitialBytesFromFile(i);
            buffers[i] = new Buffer(bytesFromDisk); // Initialize each buffer
                                                    // object
            bufferQueue.offer(i);
            int start = i * 1024;
            int end = start + 1023;
            for (int j = start; j <= end; j++) {
                indexes[j] = j;
            }
        }
    }


    private byte[] getInitialBytesFromFile(int i) throws IOException {
        byte[] bytes = new byte[4096]; // Assuming each buffer can hold 4096
                                       // bytes
        long bytePosition = (long)i * bytes.length;
        file.seek(bytePosition);
        file.read(bytes);
        return bytes;
    }


    private int getBufferNumberFromPosition(int position) {
        return position / 1024;
    }


    private int getBufferByteOffsetFromPosition(int position) {
        return (position % 1024) * 4; // Assuming each record is 4 bytes
    }


    private int getFileByteNumberFromIndex(int index) {
        return index * 4; // Assuming each record is 4 bytes
    }


    public void close() throws IOException {
        file.close(); // Close the file
    }


    public byte[] getByte(int index) throws IOException {
        int position = checkIfIndexIsInBuffers(index);
        if (position == -1) {
            byte[] b = getBytesFromFile(index);
            addIndexAndBytesToFront(index, b);
            return b;
        }
        else {
            int bufferNum = getBufferNumberFromPosition(position);
            int bufferPosition = getBufferByteOffsetFromPosition(position);
            byte[] b = buffers[bufferNum].getBytes(bufferPosition);
            moveIndexAndBytesToFront(position, bufferNum, index,
                bufferPosition);
            return b;
        }
    }


    private void addIndexAndBytesToFront(int index, byte[] b)
        throws IOException {
        int bufferNum = bufferQueue.poll();
        bufferQueue.offer(bufferNum);
        byte[] bytesToCommit = buffers[bufferNum].addBytesToFront(b);
        int indexToCommit = indexes[bufferNum * 1024 + 1023];
        int startingPos = bufferNum * 1024 + 1023;
        int endingPos = bufferNum * 1024;
        for (int i = startingPos; i > endingPos; i--) {
            indexes[i] = indexes[i - 1];
        }
        indexes[endingPos] = index;
        int byteNumberToCommit = getFileByteNumberFromIndex(indexToCommit);
        file.seek(byteNumberToCommit);
        file.write(bytesToCommit);
    }


    private void moveIndexAndBytesToFront(
        int position,
        int bufferNum,
        int index,
        int bufferPosition) {
        int startingPos = position;
        int endingPos = bufferNum * 1024;
        for (int i = startingPos; i > endingPos; i--) {
            indexes[i] = indexes[i - 1];
        }
        indexes[endingPos] = index;
        buffers[bufferNum].moveBytesToFront(bufferPosition);
        bufferQueue.remove(bufferNum);
        bufferQueue.offer(bufferNum);
    }


    private byte[] getBytesFromFile(int index) throws IOException {
        byte[] bytes = new byte[4]; // Assuming each buffer can hold 4096 bytes
        long bytePosition = getFileByteNumberFromIndex(index);
        file.seek(bytePosition);
        file.read(bytes);
        return bytes;
    }


    public void swap(int i, int j) throws IOException {
        int positionI = checkIfIndexIsInBuffers(i);
        int positionJ = checkIfIndexIsInBuffers(j);
        if (positionI == -1) {
            byte[] b = getBytesFromFile(i);
            addIndexAndBytesToFront(i, b);
        }
        if (positionJ == -1) {
            byte[] b = getBytesFromFile(j);
            addIndexAndBytesToFront(j, b);
        }
        if (positionI == -1 || positionJ == -1) {
            positionI = checkIfIndexIsInBuffers(i);
            positionJ = checkIfIndexIsInBuffers(j);
        }

        int bufferNumI = getBufferNumberFromPosition(positionI);
        int bufferOffsetI = getBufferByteOffsetFromPosition(positionI);
        byte[] byteI = buffers[bufferNumI].getBytes(bufferOffsetI);
        int bufferNumJ = getBufferNumberFromPosition(positionJ);
        int bufferOffsetJ = getBufferByteOffsetFromPosition(positionJ);
        byte[] byteJ = buffers[bufferNumJ].getBytes(bufferOffsetJ);

        buffers[bufferNumI].setBytes(byteJ, bufferOffsetI);
        buffers[bufferNumJ].setBytes(byteI, bufferOffsetJ);
        buffers[bufferNumI].setDirty();
        buffers[bufferNumJ].setDirty();
    }


    private int checkIfIndexIsInBuffers(int index) {
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] == index) {
                return i;
            }
        }
        return -1;
    }


    public void printBuffers() {
        for (int i = 0; i < totalNumBuffers; i++) {
            System.out.println("Buffer " + i + ":");
            if (buffers[i] != null) {
                System.out.println(new String(buffers[i].getBytes())); // Assuming
                                                                       // bytes
                                                                       // represent
                                                                       // text
                                                                       // data
            }
            else {
                System.out.println("Empty");
            }
            System.out.println("----------");
        }
        System.out.println("Indexes:");
        for (int index : indexes) {
            System.out.print(index + " ");
        }
        System.out.println("\n----------");
    }


    public void flush() throws IOException {

        for (int i = 0; i < totalNumBuffers; i++) {
            if (buffers[i].isDirty()) {
                for (int j = i * 1024; j < (i + 1) * 1024; j++) {
                    int index = indexes[j];
                    int position = getBufferByteOffsetFromPosition(j);
                    byte[] b = buffers[i].getBytes(position);
                    int filePosition = getFileByteNumberFromIndex(index);
                    file.seek(filePosition);
                    file.write(b);
                }
            }
            buffers[i].setClean();
        }
    }
}
