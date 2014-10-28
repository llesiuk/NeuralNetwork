/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork.functions;

/**
 *
 * @author user
 */
public class Sigmoid implements ActivationFunction {

    private final double beta;

    public Sigmoid() {
        this.beta = 1.0;
    }

    public Sigmoid(double beta) {
        this.beta = beta;
    }

    @Override
    public double value(double arg) {
        return 1.0 / (1.0 + Math.exp((-1.0) * this.beta * arg));
    }

    @Override
    public double derivative(double arg) {
        return value(arg) * (1.0 - value(arg));
    }

}
