import java.util.Arrays;

public class Buffer{
    
    private int index;
    private boolean isDirty;
    private byte[] bytes;
    
    public Buffer(byte[] array) {
        isDirty = false;
        bytes = array;
    }
    
    public Buffer copy() {
        Buffer copy = new Buffer(this.bytes);
        if (isDirty()) {
            copy.setDirty();
        }
        return copy;
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
    
    public int getIndex() {
        return index;
    }
    
    public void changeIndex(int i) {
        index = i;
    }
    
    public byte[] getBytes() {
        return bytes;
    }
    
    public void changeBytes(byte[] toChange) {
        bytes = toChange;
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
        byte[] removedBytes = new byte[newBytes.length];
        System.arraycopy(bytes, 0, removedBytes, 0, newBytes.length); // Copy the bytes being replaced
        System.arraycopy(bytes, newBytes.length, bytes, 0, bytes.length - newBytes.length); // Shift existing bytes
        System.arraycopy(newBytes, 0, bytes, bytes.length - newBytes.length, newBytes.length); // Add new bytes to the front
        return removedBytes; // Return the removed bytes
    }

}