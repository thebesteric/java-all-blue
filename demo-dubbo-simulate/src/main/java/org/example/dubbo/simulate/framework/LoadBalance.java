package org.example.dubbo.simulate.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LoadBalance {
    public static URL random(Set<URL> urls) {
        List<URL> list = new ArrayList<>(urls);
        Random random = new Random();
        int seed = random.nextInt(list.size());
        return list.get(seed);
    }
}
