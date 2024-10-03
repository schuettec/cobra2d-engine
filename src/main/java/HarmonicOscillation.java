import java.util.function.Function;

public class HarmonicOscillation
    implements Function<Double, Double> {

  private double amplitude = 1d;
  private double frequency = 1d;
  private double phase = 0.25d;

  private boolean flip = false;

  private Type type = Type.COSINUS;

  public HarmonicOscillation() {
    super();
  }

  public HarmonicOscillation(double frequency, double amplitude,
      double phase, HarmonicOscillation.Type type,
      boolean flip) {
    super();
    this.frequency = frequency;
    this.amplitude = amplitude;
    this.phase = phase;
    this.type = type;
    this.flip = flip;
  }

  public enum Type {
    SINUS,
    COSINUS;

    public Function<Double, Double> oscillator() {
      if (SINUS.equals(this)) {
        return Math::sin;
      } else {
        return Math::cos;
      }
    }
  }

  @Override
  public Double apply(Double t) {
    return (amplitude / 2.) * ((flip ? -1 : 1)
        * type.oscillator()
            .apply(((2. * Math.PI) / frequency) * (t + phase))
        + 1);
  }

}
