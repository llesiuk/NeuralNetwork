/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import neuralnetwork.functions.*;

/**
 *
 * @author user
 */
public class Neuron {

    private final double biasValue, learningFactor, momentum;
    private double expectedValue, output, error, sumIW;
    private ActivationFunction activationFunction;
    private List<Double> input, weights, weightsOld;

    public Neuron() {
        this.biasValue = 0.0;
        this.activationFunction = new Sigmoid();
        this.learningFactor = 0.2;
        this.momentum = 0.6;
    }

    public Neuron(double biasValue, ActivationFunction af, double learningFactor, double momentum) {
        this.biasValue = biasValue;
        this.activationFunction = af;
        this.learningFactor = learningFactor;
        this.momentum = momentum;
    }

    public void setInput(List<Double> input) {
        this.input = new ArrayList<Double>(input);
        this.input.add(0, this.biasValue);
    }

    public void setInputFirst(Double input) {
        this.input = new ArrayList<Double>();
        this.input.add(input);
    }

    public void setWeights(List<Double> weights) {
        this.weights = new ArrayList<Double>(weights);
        this.weightsOld = new ArrayList<Double>(weights);
    }

    public void randomWeights(double rangeMin, double rangeMax, int quantity) {
        Random r = new Random();
        this.weights = new ArrayList<Double>();
        for (int i = 0; i < quantity; i++) {
            this.weights.add(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
        }
        this.weights.add(0, 0.0);
        this.weightsOld = new ArrayList<Double>(this.weights);
    }

    public void lastLayerError() {
        this.error = this.activationFunction.derivative(this.sumIW) * (this.expectedValue - this.output);
    }

    public void usualError(double weightsErrorsSum) {
        this.error = this.activationFunction.derivative(this.sumIW) * weightsErrorsSum;
    }

    public double weightError(int index) {
        return this.weights.get(index) * this.error;
    }

    public void passOutput() {
        this.output = this.input.get(0);
    }

    public void computeOutput() {
        this.sumIW = 0.0;
        for (int i = 0; i < this.input.size(); i++) {
            this.sumIW += this.input.get(i) * this.weights.get(i);
        }
        this.output = this.activationFunction.value(this.sumIW);
    }

    public void improveWeights() {
        double sum = 0.0;
        for (int i = 0; i < this.weights.size(); i++, sum = 0.0) {
            sum += this.learningFactor * this.error * this.input.get(i) + (this.momentum * (this.weights.get(i) - this.weightsOld.get(i)));
            this.weightsOld.set(i, this.weights.get(i));
            this.weights.set(i, sum + this.weightsOld.get(i));
        }
    }

    public double outputError() {
        return Math.pow(this.output - this.expectedValue, 2) / 2.0;
    }

    @Override
    public String toString() {
        String returned = "";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("##0.000000", otherSymbols);
        for (Double current : this.weights) {
            //returned += (double) Math.round(current * 100000)/1000000 + " ";
            returned += df.format(current) + " ";
        }
        //returned = returned.substring(0, returned.length()-1);
        return returned;
    }

    public double getOutput() {
        return output;
    }

    public void setExpectedValue(double expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public double getError() {
        return error;
    }

    public double getSumIW() {
        return sumIW;
    }

}
