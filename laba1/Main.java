class Main {

	public static void main(String[] args) {
		// sum:
		// четные и отриц
		// нечет и отриц
		int sum1 = 0, sum2 = 0;
		for (int i = 0; i < args.length; i++) {
			try {
				int value = Integer.valueOf(args[i]);
				if (value < 0) {
					if (value % 2 == 0) sum1 += value;
					else sum2 += value;
				}
			} catch (Exception e) {}
		}
		System.out.println("sum1: " + sum1);
		System.out.println("sum2: " + sum2);
	}
}