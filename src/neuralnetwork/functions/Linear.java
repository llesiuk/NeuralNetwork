/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork.functions;

/**
 *
 * @author user
 */
public class Linear implements ActivationFunction {

    private final double a, b;

    public Linear() {
        this.a = 1.0;
        this.b = 0.0;
    }

    public Linear(double paramA, double paramB) {
        this.a = paramA;
        this.b = paramB;
    }

    @Override
    public double value(double arg) {
        return this.a * arg + this.b;
    }

    @Override
    public double derivative(double arg) {
        return this.a;
    }

}
