import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {

    private long time;
    private int cacheHits;
    private int diskReads;
    private int diskWrites;
    public int timeCheckingArray;
    public int timeCheckingDuplicates;

    public Statistics() {
        time = 0;
        cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;
        timeCheckingArray = 0;
        timeCheckingDuplicates = 0;
    }


    public void setTime(long totalTime) {
        time = totalTime;

    }


    public void incrementCacheHits() {
        cacheHits++;
    }


    public void incrementDiskReads() {
        diskReads++;
    }


    public void incrementDiskWrites() {
        diskWrites++;
    }


    public void makeFile(String sortFileName, String statFileName)
        throws IOException {
        File file = new File(statFileName);
        FileWriter writer = new FileWriter(file.getAbsolutePath()); // This line
                                                                    // has
                                                                    // changed
        BufferedWriter writer2 = new BufferedWriter(writer);
        writer2.write("Filename: " + sortFileName + "\n");
        writer2.write("Cache Hits: " + cacheHits + "\n");
        writer2.write("Disk Reads: " + diskReads + "\n");
        writer2.write("Disk Writes: " + diskWrites + "\n");
        writer2.write("Runtime: " + time + " ms");
        System.out.println("Time checking array:" + timeCheckingArray + " milliseconds");
        System.out.println("Time checking duplicates:" + timeCheckingDuplicates + " milliseconds");
        writer2.close();
    }

}
