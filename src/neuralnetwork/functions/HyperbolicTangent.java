/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork.functions;

/**
 *
 * @author user
 */
public class HyperbolicTangent implements ActivationFunction {

    private final double beta;

    public HyperbolicTangent() {
        this.beta = 1.0;
    }

    public HyperbolicTangent(double beta) {
        this.beta = beta;
    }

    @Override
    public double value(double arg) {
        double EXP = Math.exp((-1.0) * this.beta * arg);
        return (1.0 - EXP) / (1.0 + EXP);
    }

    @Override
    public double derivative(double arg) {
        return 1.0 - (value(arg) * value(arg));
    }

}
