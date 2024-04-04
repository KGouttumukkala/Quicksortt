/**
 * {Project Description Here}
 */



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
        long startTime = System.currentTimeMillis();
        String dataFileName = args[0];
        int numbBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];
        
        BufferPool bufferPool = new BufferPool(numbBuffers);
        readDataIntoBuffers(dataFileName, bufferPool);
        quickSort(bufferPool);
        writeSortedDataToFile(dataFileName, bufferPool);
        generateRuntimeStatistics(statFileName, bufferPool);
        
    }

    private static void generateRuntimeStatistics(
        String statFileName,
        BufferPool bufferPool) {
        // TODO Auto-generated method stub
        
    }

    private static void writeSortedDataToFile(
        String dataFileName,
        BufferPool bufferPool) {
        // TODO Auto-generated method stub
        
    }

    private static void quickSort(BufferPool bufferPool) {
        // TODO Auto-generated method stub
        
    }

    private static void readDataIntoBuffers(
        String dataFileName,
        BufferPool bufferPool) {
        // TODO Auto-generated method stub
        
    }
    
    private static long calculateRuntime(long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    private static void writeStatisticsToFile(String fileName, int cacheHits, int diskReads, int diskWrites, long runtime) {
    }
}
