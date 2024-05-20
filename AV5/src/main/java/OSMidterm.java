package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

public class OSMidterm {

    //TODO: Initialize the semaphores you need
    static Lock lock = new ReentrantLock();


    public static void main(String[] args) throws InterruptedException {

        //STARTING CODE, DON'T MAKE CHANGES
        //-----------------------------------------------------------------------------------------
        String final_text="Bravo!!! Ja resi zadacata :)";
        int m=10, n=100;
        Object[][] data = new Object[m][n];
        Random rand = new Random();
        int k=0;
        for (int i=0;i<m;i++) {
            for (int j=0;j<n;j++) {
                int random = rand.nextInt(100);
                if(random%2==0 & k<final_text.length()) {
                    data[i][j] = final_text.charAt(k);
                    k++;
                } else {
                    data[i][j] = rand.nextInt(100);
                }
            }
        }

        DataMatrix matrix = new DataMatrix(m,n, data);
        StatisticsResource statisticsResource = new StatisticsResource();
        //-----------------------------------------------------------------------------------------

        //ONLY TESTING CODE, SO YOU CAN TAKE A LOOK OF THE OUTPUT YOU NEED TO GET AT THE END
        //YOU CAN COMMENT OR DELETE IT AFTER YOU WRITE THE CODE USING THREADS
        //-----------------------------------------------------------------------------------------
//        Concatenation concatenation = new Concatenation(matrix, statisticsResource);
//
//        concatenation.concatenate();
//
//        statisticsResource.printString();
        //-----------------------------------------------------------------------------------------

        //TODO: Run the threads from the Concatenation class
        ArrayList<Concatenation> concatenations = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            Concatenation concatenation = new Concatenation(matrix,statisticsResource);
            concatenations.add(concatenation) ;
            concatenation.execute(i,n);

        }

        //TODO: Wait 10seconds for all threads to finish
        concatenations.forEach(i-> {
            try {
                i.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //TODO: Print the string you get, call function printString()
        statisticsResource.printString();

        //TODO: Check if some thread is still alive, if so kill it and print "Possible deadlock"
        concatenations.forEach(c->{
            if(c.isAlive()){
                System.out.println("Possible deadlock");
            }
        });

    }

    // TODO: Make the Concatenation Class  a Thread Class
    static class Concatenation extends Thread{

        private DataMatrix matrix;
        private StatisticsResource statisticsResource;
        private int myRow,columns;

        public Concatenation(DataMatrix matrix, StatisticsResource statisticsResource) {
            this.matrix = matrix;
            this.statisticsResource = statisticsResource;
        }
        //concatenation function implemented on the whole matrix, so you can take a look of the task's logic
//        public void concatenate() {
//            for (int i=0;i<this.matrix.getM();i++) {
//                for (int j=0;j<this.matrix.getN();j++) {
//                    if (this.matrix.isString(i,j)) {
//                        this.statisticsResource.concatenateString(this.matrix.getEl(i,j).toString());
//                    }
//                }
//            }
//        }

        @Override
        public void run() {
            concatenate_by_row();
        }

        public void concatenate_by_row(){
            //TODO: Implement this function
            // add  arguments in the function if needed
            for (int i = 0; i < columns; i++) {
                if (this.matrix.isString(myRow,i)) {
                    lock.lock();
                    this.statisticsResource.concatenateString(this.matrix.getEl(myRow,i).toString());
                    lock.unlock();
                }
            }

        }
        public void execute(int myRow,int columns){
            //TODO: call the concatenate_by_row() function
            this.myRow = myRow;
            this.columns = columns;
            this.start();
        }


    }

    //-------------------------------------------------------------------------
    //YOU ARE NOT CHANGING THE CODE BELOW
    static class DataMatrix {

        private int m,n;
        private Object[][] data;

        public DataMatrix(int m, int n, Object[][] data) {
            this.m = m;
            this.n = n;
            this.data = data;
        }

        public int getM() {
            return m;
        }

        public int getN() {
            return n;
        }

        public Object[][] getData() {
            return data;
        }

        public Object getEl(int i, int j) {
            return data[i][j];
        }

        public Object[] getRow(int pos) {
            return this.data[pos];
        }

        public Object[] getColumn(int pos) {
            Object[] result = new Object[m];
            for (int i=0;i<m;i++) {
                result[i]=data[i][pos];
            }
            return result;
        }

        public boolean isString(int i, int j) {
            return this.data[i][j] instanceof Character;
        }


    }

    static class StatisticsResource {

        private String concatenatedString;

        public StatisticsResource() {
            this.concatenatedString = "";
        }

        //function for String concatenation
        public void concatenateString(String new_character) {
            concatenatedString+=new_character;
        }

        //function for printing the concatenated string
        public void printString() {
            System.out.println("Here is the phrase from the matrix: " + concatenatedString);
        }
    }
}
