/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork.functions;

/**
 *
 * @author user
 */
public interface ActivationFunction {
    public double value(double arg);
    public double derivative(double arg);
}
