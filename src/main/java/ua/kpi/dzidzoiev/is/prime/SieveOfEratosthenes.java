package ua.kpi.dzidzoiev.is.prime;

/**
 * Created by dzidzoiev on 12/21/15.
 */
public class SieveOfEratosthenes implements PrimeAlgorithm {
    @Override
    public boolean isPrime(Number number) {
        int intValue = number.intValue();
        if(intValue <= 0)
            return false;
        int limit = (int) Math.ceil(Math.sqrt(number.doubleValue()));
        boolean[] primeArray = new boolean[intValue + 1];
        for (int i = 2; i <= limit; i++) {
            if(!primeArray[i]) {
                for (int mark = i; mark <= intValue; mark += i) {
                    if (mark != i)
                        primeArray[mark] = true;
                }
            }
        }
        return !primeArray[intValue];
    }
}
