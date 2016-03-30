package edu.naukma.reshetnov.plsi;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by reshet on 11/6/15.
 */
public class HzApp {
  public static void main(String[] args) {
    HazelcastInstance hz = Hazelcast.newHazelcastInstance();
    IList<Object> mylist = hz.getList("mylist");

    Stream
        .iterate(100, e -> e +1 )
        .limit(3000)
        .forEach(mylist::add);

    System.out.println(mylist.getPartitionKey());
    System.out.println("Hazelcast:" + hz.getCluster().getMembers());
    System.out.println("Size of mylist:" + mylist.size());

//    double sum = Stream.iterate(100, e -> e + 1)
//        .filter(HzApp::isPrime)
//        .mapToDouble(Math::sqrt)
//        .limit(2000)
//        .sum();
//    System.out.println(sum);
  }

  public static boolean isPrime(int number) {
    return IntStream.range(1, number).noneMatch(e -> number % e == 0);
  }
}
