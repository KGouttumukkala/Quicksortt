import java.util.Arrays;

public class Buffer{
    
    private int index;
    private boolean isDirty;
    private byte[] bytes;
    
    public Buffer(int i, byte[] array) {
        index = i;
        isDirty = false;
        bytes = array;
    }
    
    public Buffer copy() {
        Buffer copy = new Buffer(this.index, this.bytes);
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
    
    public void addBytesToFront(byte[] newBytes) {
        System.arraycopy(bytes, 0, bytes, newBytes.length, bytes.length - newBytes.length);
        System.arraycopy(newBytes, 0, bytes, 0, newBytes.length);
    }
}