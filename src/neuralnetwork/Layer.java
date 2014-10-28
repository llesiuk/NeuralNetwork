/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.util.ArrayList;
import java.util.List;
import neuralnetwork.functions.*;

/**
 *
 * @author user
 */
public class Layer {

    private final List<Neuron> nuerons;
    private final int quantity;
    private List<Double> completeOutput;

    public Layer() {
        this.quantity = 1;
        this.nuerons = new ArrayList<Neuron>();
        this.completeOutput = new ArrayList<Double>();
    }

    public Layer(int quantity) {
        this.quantity = quantity;
        this.nuerons = new ArrayList<Neuron>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.add(new Neuron());
        }
        this.completeOutput = new ArrayList<Double>();
    }

    public Layer(int quantity, double biasValue, ActivationFunction af, double learningFactor, double momentum) {
        this.quantity = quantity;
        this.nuerons = new ArrayList<Neuron>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.add(new Neuron(biasValue, af, learningFactor, momentum));
        }
        this.completeOutput = new ArrayList<Double>();
    }

    public void setInput(List<Double> input) {
        for (Neuron current : this.nuerons) {
            current.setInput(input);
        }
    }

    public void setInputFirst(List<Double> input) {
        for (int i = 0; i < input.size(); i++) {
            this.nuerons.get(i).setInputFirst(input.get(i));
        }
    }

    public void lastLayerError() {
        for (Neuron current : this.nuerons) {
            current.lastLayerError();
        }
    }

    public double weightError(int inputIndex) {
        double result = 0.0;
        for (Neuron current : this.nuerons) {
            result += current.weightError(inputIndex);
        }
        return result;

    }

    public void improveWeights() {
        for (Neuron current : this.nuerons) {
            current.improveWeights();
        }
    }

    public void passOutput() {
        this.completeOutput = new ArrayList<Double>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.get(i).passOutput();
            this.completeOutput.add(i, this.nuerons.get(i).getOutput());
        }
    }

    public void computeOutput() {
        this.completeOutput = new ArrayList<Double>();
        for (int i = 0; i < this.quantity; i++) {
            this.nuerons.get(i).computeOutput();
            this.completeOutput.add(i, this.nuerons.get(i).getOutput());
        }
    }

    public void randomWeights(double rangeMin, double rangeMax, int quantity) {
        for (Neuron current : this.nuerons) {
            current.randomWeights(rangeMin, rangeMax, quantity);
        }
    }

    public void setExpectedValue(List<Double> values) {
        for (int i = 0; i < values.size(); i++) {
            this.nuerons.get(i).setExpectedValue(values.get(i));
        }
    }

    public void setActivationFunction(ActivationFunction af) {
        for (Neuron current : this.nuerons) {
            current.setActivationFunction(af);
        }
    }

    public double outputError() {
        double result = 0.0;
        for (Neuron current : this.nuerons) {
            result += current.outputError();
        }
        return result;
    }

    @Override
    public String toString() {
        String returned = "";
        for (Neuron current : this.nuerons) {
            returned += current.toString() + System.getProperty("line.separator");
        }
        return returned;
    }

    public List<Double> getCompleteOutput() {
        return completeOutput;
    }

    public List<Neuron> getNuerons() {
        return nuerons;
    }

}
