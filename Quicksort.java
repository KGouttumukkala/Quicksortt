
/**
 * {Project Description Here}
 */

import java.util.List;
import java.io.*;

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
     *            Command line parameters. See the project spec!!!
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println(
                "Usage: java Quicksort <data-file-name> <numb-buffers> <stat-file-name>");
            return;
        }
        String dataFileName = args[0];
        int numBuffers;
        try {
            numBuffers = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.err.println("Error: Invalid number of buffers.");
            return;
        }
        String statFileName = args[2];
        Statistics statistics = new Statistics();
        TheSorter sorter = new TheSorter(dataFileName, numBuffers, statistics);
        long startTime = System.currentTimeMillis();
        sorter.quickSort();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        statistics.setTime(totalTime);
        statistics.makeFile(dataFileName, statFileName);
    }
}
