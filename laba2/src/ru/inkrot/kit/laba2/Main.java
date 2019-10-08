package ru.inkrot.kit.laba2;

class Main {

    public static void main(String[] args) throws MyException {
        // sum:
        // четные и отриц
        // нечет и отриц
        int sum1 = 0, sum2 = 0;
        int length = Integer.valueOf(args[0]);
        if (args.length-1 < length) throw new MyException("В массиве число элементов меньше указанного");
        else if (args.length-1 > length) throw new MyException("В массиве число элементов больше указанного");
        for (int i = 1; i < args.length; i++) {
            String s = args[i];
            try {
                int value = Integer.valueOf(args[i]);
                if (value < 0) {
                    if (value % 2 == 0) sum1 += value;
                    else sum2 += value;
                }
            } catch (NumberFormatException e) {
                throw new MyException("В строке отсутствует символ");
            }
        }
        System.out.println("sum1: " + sum1);
        System.out.println("sum2: " + sum2);
    }
}