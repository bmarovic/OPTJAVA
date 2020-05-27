public interface IDecoder<T> {
    double[] decode(T solution);
    void decode(T solution, double[] vector);
}
