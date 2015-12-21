package ua.kpi.dzidzoiev.is.prime;

import java.math.BigInteger;

/**
 * Created by dzidzoiev on 12/22/15.
 */
public class MillerRabin implements PrimeAlgorithm {
    @Override
    public boolean isPrime(Number number) {
        long _number = number.longValue();
        if (_number < 0)
            return false;
        if (_number <= 2)
            return true;

        return BigInteger.valueOf(_number).isProbablePrime(50);
    }
}
