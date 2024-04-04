/**
 * {Project Description Here}
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The class containing the main method.
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {

    /**
     * @param args
     *      Command line parameters.  See the project spec!!!
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Quicksort <data-file-name> <numb-buffers> <stat-file-name>");
            return;
        }
        String dataFileName = args[0];
        int numbBuffers;
        try {
            numbBuffers = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number of buffers.");
            return;
        }
        String statFileName = args[2];
        
        BufferPool bufferPool = new BufferPool(numbBuffers);
        readDataIntoBuffers(dataFileName, bufferPool);
        long startTime = System.currentTimeMillis();
        quickSort(bufferPool);
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        writeSortedDataToFile(dataFileName, bufferPool);
        generateRuntimeStatistics(statFileName, bufferPool, runtime);
        
    }

    private static void generateRuntimeStatistics(
        String statFileName,
        BufferPool bufferPool, long runtime) {
        // TODO Auto-generated method stub
        
    }

    private static void writeSortedDataToFile(String dataFileName, BufferPool bufferPool) {
        List<KVPair<Integer, Integer>> sortedPairs = bufferPool.getAllSortedPairs();

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dataFileName)))) {
            for (KVPair<Integer, Integer> pair : sortedPairs) {
                dos.writeShort(pair.getKey());
                dos.writeShort(pair.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error writing sorted data to file: " + e.getMessage());
        }
    }

    private static void quickSort(BufferPool bufferPool) {
        Buffer buffer = null;
        try {
            buffer = bufferPool.getBuffer();
        } catch (IllegalStateException e) {
            System.err.println("Error: Buffer pool is empty.");
            return;
        }
        
        List<KVPair<Integer, Integer>> pairs = buffer.getPairs();
        quickSort(pairs, 0, pairs.size() - 1);
        buffer.setPairs(pairs);
        bufferPool.releaseBuffer(buffer);
    }
    
    private static void quickSort(List<KVPair<Integer, Integer>> pairs, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(pairs, low, high);
            quickSort(pairs, low, partitionIndex - 1);
            quickSort(pairs, partitionIndex + 1, high);
        }
    }

    private static int partition(List<KVPair<Integer, Integer>> pairs, int low, int high) {
        KVPair<Integer, Integer> pivot = pairs.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (pairs.get(j).getKey() < pivot.getKey()) {
                i++;
                swap(pairs, i, j);
            }
        }
        swap(pairs, i + 1, high);
        return i + 1;
    }

    private static void swap(List<KVPair<Integer, Integer>> pairs, int i, int j) {
        KVPair<Integer, Integer> temp = pairs.get(i);
        pairs.set(i, pairs.get(j));
        pairs.set(j, temp);
    }


    private static void readDataIntoBuffers(String dataFileName, BufferPool bufferPool) {
        FileGenerator fileGenerator = new FileGenerator(dataFileName, bufferPool.getCapacity());
        fileGenerator.generateFile(FileType.BINARY);
        File file = new File(dataFileName);
        if (!file.exists()) {
            System.err.println("Error: Generated file not found.");
            return;
        }

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            while (dis.available() > 0) {
                short key = dis.readShort();
                short value = dis.readShort();
                bufferPool.addKeyValue(key, value);
            }
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }

    
    private static void writeStatisticsToFile(String fileName, int cacheHits, int diskReads, int diskWrites, long runtime) {
    }
}
