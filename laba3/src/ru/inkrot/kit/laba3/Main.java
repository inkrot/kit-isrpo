package ru.inkrot.kit.laba3;

import com.sun.jmx.snmp.Timestamp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

interface AccessEventListener {
    void setLogFile(File logFile);
    void outputToConsole(String message);
    String inputFromConsole(Scanner scanner);

    /**
     *
     * @param arr Integers array
     * @param i The index of the element in array.
     * @return If i is -1 then returns the length of the array, in other cases - the element value
     */
    int arrayAccess(int[] arr, int i);
}

class Adder {

    AccessEventListener access;

    public Adder(AccessEventListener access) {
        this.access = access;
    }

    public void calculateSums() {
        int sum1 = 0, sum2 = 0;
        Scanner sc = new Scanner(System.in);
        String dataFilePath = access.inputFromConsole(sc);
        try {
            FileReader fileReader = new FileReader(new File(dataFilePath));
            BufferedReader reader = new BufferedReader(fileReader);
            access.setLogFile(new File(Paths.get("").toAbsolutePath() + reader.readLine()));
            int[] elements = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int length = access.arrayAccess(elements, -1);
            for (int i = 0; i < length; i++) {
                int value = access.arrayAccess(elements, i);
                if (value < 0) {
                    if (value % 2 == 0) sum1 += value;
                    else sum2 += value;
                }
            }
            access.outputToConsole("Сумма чётных и отрицательных: " + sum1);
            access.outputToConsole("Сумма нечётных и отрицательных: " + sum2);
        } catch (FileNotFoundException e) {
            access.outputToConsole("Ошибка: " + e.getLocalizedMessage());
            return;
        } catch (IOException e) {
            access.outputToConsole("Ошибка: невозможно считать строку");
            return;
        }
    }
}

class MyListener implements AccessEventListener {

    private File logFile;
    private String preLogInitMessage = null;

    private void writeLog(String info, String message) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String msg = "[" + timestamp.getDate() + " / " + info + "]: " + message + "\n";
        if (logFile == null) {
            preLogInitMessage = msg;
            return;
        }
        try {
            msg = "[" + timestamp.getDate() + " / " + info + "]: " + message + "\n";
            if (preLogInitMessage != null) {
                Files.write(logFile.toPath(), preLogInitMessage.getBytes(), StandardOpenOption.APPEND);
                preLogInitMessage = null;
            }
            Files.write(logFile.toPath(), msg.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    @Override
    public void outputToConsole(String message) {
        System.out.println(message);
        writeLog("Обращение к потоку вывода на консоль", message);
    }

    @Override
    public String inputFromConsole(Scanner scanner) {
        String line = scanner.nextLine();
        writeLog("Обращение к потоку ввода с консоли", line);
        return line;
    }

    @Override
    public int arrayAccess(int[] arr, int i) {
        if (i == -1) {
            writeLog("Обращение к указанному массиву", "длина массива = " + arr.length);
            return arr.length;
        } else {
            writeLog("Обращение к указанному массиву", "значение элемента [" + i + "] = " +arr[i]);
            return arr[i];
        }
    }

    public static void main(String[] args) {
        // Data file example: C:\Users\Admin\Desktop\Учеба\3 курс\ИСРПО\isrpo-labs\laba3\src\resources\data.txt
        AccessEventListener eventsListener = new MyListener();
        Adder adder = new Adder(eventsListener);
        adder.calculateSums();
    }
}