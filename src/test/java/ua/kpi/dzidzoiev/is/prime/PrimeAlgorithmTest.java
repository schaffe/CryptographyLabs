package ua.kpi.dzidzoiev.is.prime;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimeAlgorithmTest {

    public static final Map<Integer, Boolean> primeTestMap = new HashMap(){{
        put(-1,     false);
        put(0,      false);
        put(1,      true);
        put(2,      true);
        put(3,      true);
        put(4,      false);
        put(5,      true);
        put(10,     false);
        put(11,     true);
        put(100,     false);
        put(112,    false);
        put(400,    false);
        put(501,    false);
        put(503,    true);
//        put(32452843,true);
//        put(67867979,true);
    }};

    @Test
    public void testSieveOfErastothenes() throws Exception {
        PrimeAlgorithm alg = new SieveOfEratosthenes();
//        Assert.assertEquals(false, alg.isPrime(100));

        for (Map.Entry<Integer, Boolean> entry : primeTestMap.entrySet()) {
            Assert.assertEquals(entry.getKey().toString(), entry.getValue(), alg.isPrime(entry.getKey()));
        }
    }
}