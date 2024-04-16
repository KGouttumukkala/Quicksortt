import java.util.Arrays;

public class Buffer {

    private boolean isDirty;
    private byte[] bytes;

    public Buffer(byte[] array) {
        isDirty = false;
        bytes = array;
    }


    public boolean isDirty() {
        return isDirty;
    }


    public void setDirty() {
        isDirty = true;
    }


    public void setClean() {
        isDirty = false;
    }


    public byte[] getBytes() {
        return bytes;
    }


    public int getSizeOfBuffer() {
        return bytes.length;
    }


    public byte[] getBytes(int offset) {
        return Arrays.copyOfRange(bytes, offset, offset + 4);

    }


    public void moveBytesToFront(int index) {
        byte[] temp = new byte[4];
        System.arraycopy(bytes, index, temp, 0, 4);
        System.arraycopy(bytes, 0, bytes, 4, index);
        System.arraycopy(temp, 0, bytes, 0, 4);
    }


    public byte[] addBytesToFront(byte[] newBytes) {
        byte[] removedBytes = new byte[4];
        System.arraycopy(bytes, bytes.length - 4, removedBytes, 0, 4);
        System.arraycopy(bytes, 0, bytes, 4, bytes.length - 4);
        System.arraycopy(newBytes, 0, bytes, 0, 4);
        return removedBytes;
    }


    public void setBytes(byte[] newBytes, int offset) {
        System.arraycopy(newBytes, 0, bytes, offset, newBytes.length);
    }

}
