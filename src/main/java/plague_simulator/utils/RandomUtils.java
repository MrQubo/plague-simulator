package plague_simulator.utils;

import java.util.Random;
import java.util.function.Supplier;

import plague_simulator.Global;

public class RandomUtils {
  static public Random getRandomInstance() {
    return Global.getInstance().getRandom();
  }

  static public boolean trueWithProbability(double probability) {
    return trueWithProbability(probability, getRandomInstance());
  }
  static public boolean trueWithProbability(double probability, final Random random) {
    return (random.nextDouble() < probability);
  }


  static public <T> T chooseWithProbability(double probability, T whenTrue, T whenFalse) {
    return chooseWithProbability(probability, whenTrue, whenFalse, getRandomInstance());
  }
  static public <T> T chooseWithProbability(double probability, T whenTrue, T whenFalse, final Random random) {
    return chooseWithProbability(probability, () -> whenTrue, () -> whenFalse, random);
  }

  static public <T> T chooseWithProbability(double probability, Supplier<? extends T> whenTrue, Supplier<? extends T> whenFalse) {
    return chooseWithProbability(probability, whenTrue, whenFalse, getRandomInstance());
  }
  static public <T> T chooseWithProbability(
      double probability,
      Supplier<? extends T> whenTrue,
      Supplier<? extends T> whenFalse,
      final Random random
  ) {
    if (trueWithProbability(probability, random)) {
      return whenTrue.get();
    } else {
      return whenFalse.get();
    }
  }


  static public int nextInt() {
    return nextInt(getRandomInstance());
  }
  static public int nextInt(final Random random) {
    return random.nextInt();
  }

  static public int nextInt(int to) {
    return nextInt(to, getRandomInstance());
  }
  static public int nextInt(int to, final Random random) {
    return random.nextInt(to);
  }

  static public int nextInt(int from, int to) {
    return nextInt(from, to, getRandomInstance());
  }
  static public int nextInt(int from, int to, final Random random) {
    return random.ints(1, from, to).findAny().getAsInt();
  }

  static public int nextIntBinomialTruncated(int to) {
    return nextIntBinomialTruncated(to, getRandomInstance());
  }
  // Generates int from the range [0, to) in such way that smaller numbers have higher occurrence probability.
  static public int nextIntBinomialTruncated(int to, final Random random) {
    if (to <= 0) { throw new IllegalArgumentException("`to` must be positive"); }

    int res = nextIntBinomialIncluding(-to+1, to, random);
    if (res <= 0) {
      return -res;
    } else {
      return res - 1;
    }
  }

  static public int nextIntBinomialTruncated(int from, int to) {
    return nextIntBinomialTruncated(from, to, getRandomInstance());
  }
  // Generates int from the range [from, to).
  static public int nextIntBinomialTruncated(int from, int to, final Random random) {
    if (from >= to) { throw new IllegalArgumentException("`to` must be greater than `from`"); }
    if (to - from <= 0) { throw new IllegalArgumentException("`to - from` must fit in int size"); }

    return nextIntBinomialTruncated(to - from, random) + from;
  }

  static public int nextIntBinomialIncluding(int to) {
    return nextIntBinomialIncluding(to, getRandomInstance());
  }
  static public int nextIntBinomialIncluding(int to, final Random random) {
    return nextIntBinomialIncluding(0, to, random);
  }

  static public int nextIntBinomialIncluding(int from, int to) {
    return nextIntBinomialIncluding(from, to, getRandomInstance());
  }
  static public int nextIntBinomialIncluding(int from, int to, final Random random) {
    if (from > to) { throw new IllegalArgumentException("`to` must be greater than or equal to `from`"); }

    int x = from;

    for (int i = from ; i < to ; i += 1) {
      if (random.nextBoolean()) {
        x += 1;
      }
    }

    return x;
  }


  static public long nextLong() {
    return nextLong(getRandomInstance());
  }
  static public long nextLong(final Random random) {
    return random.nextLong();
  }

  static public long nextLong(long to) {
    return nextLong(to, getRandomInstance());
  }
  static public long nextLong(long to, final Random random) {
    return nextLong(0, to, random);
  }

  static public long nextLong(long from, long to) {
    return nextLong(from, to, getRandomInstance());
  }
  static public long nextLong(long from, long to, final Random random) {
    return random.longs(1, from, to).findAny().getAsLong();
  }


  static public double nextDoubleGaussian(double mean, double sd) {
    return nextDoubleGaussian(mean, sd, getRandomInstance());
  }
  static public double nextDoubleGaussian(double mean, double sd, final Random random) {
    return random.nextGaussian() * sd + mean;
  }
}
