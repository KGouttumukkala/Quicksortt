public class Buffer{
    
    private int index;
    private boolean isDirty;
    private byte[] bytes;
    
    public Buffer(int i, byte[] array) {
        index = i;
        isDirty = false;
        bytes = array;
    }
    
    public void setSingleByte(byte val, int i) {
        bytes[i] = val;
        isDirty = true;
    }
    
    public Buffer copy() {
        Buffer copy = new Buffer(this.index, this.bytes);
        if (isDirty()) {
            copy.setDirty(true);
        }
        return copy;
    }
    
    public boolean isDirty() {
        return isDirty;
    }
    
    public void setDirty(boolean toSet) {
        isDirty = toSet;
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
}