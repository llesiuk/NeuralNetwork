/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.functions.*;

/**
 *
 * @author user
 */
public class Network {

    private List<Layer> layers;
    private List<Integer> quantities;
    private List<List<Double>> networkInput, expectedValues, testInput, testExpected;
    private int type, epochMax;
    private double learningFactor, momentum;

    public Network() {
        this.quantities = new ArrayList<Integer>();
        this.quantities.add(1);
        this.quantities.add(2);
        this.quantities.add(1);
        this.layers = new ArrayList<Layer>();
        for (int i = 0; i < this.quantities.size(); i++) {
            this.layers.add(new Layer(this.quantities.get(i)));
        }
    }

    public Network(List<Integer> quantities) {
        this.quantities = quantities;
        this.layers = new ArrayList<Layer>();
        for (int i = 0; i < this.quantities.size(); i++) {
            this.layers.add(new Layer(this.quantities.get(i)));
        }
    }

    public Network(List<Integer> layersSizes, double weightsMinRange, double weightsMaxRange, double biasValue, ActivationFunction af, double learningFactor, double momentum, int epochMax) {
        this.learningFactor = learningFactor;
        this.momentum = momentum;
        this.epochMax = epochMax;
        this.layers = new ArrayList<Layer>();
        this.quantities = new ArrayList<Integer>(layersSizes);
        for (int i = 0; i < this.quantities.size(); i++) {
            this.layers.add(new Layer(this.quantities.get(i), biasValue, af, learningFactor, momentum));
        }
        for (int i = 1; i < this.quantities.size(); i++) {
            this.layers.get(i).randomWeights(weightsMinRange, weightsMaxRange, this.quantities.get(i - 1));

        }
    }

    public Network(String filename, double biasValue, ActivationFunction af, double learningFactor, double momentum, int epochMax) {
        BufferedReader br;
        String line;
        int numberOfLayers;
        this.learningFactor = learningFactor;
        this.momentum = momentum;
        this.epochMax = epochMax;
        this.quantities = new ArrayList<Integer>();
        this.layers = new ArrayList<Layer>();
        try {
            br = new BufferedReader(new FileReader(filename));
            line = br.readLine();
            numberOfLayers = Integer.parseInt(line);
            for (int i = 0; i < numberOfLayers + 1; i++) {
                line = br.readLine();
                this.quantities.add(Integer.parseInt(line));
                this.layers.add(new Layer(this.quantities.get(i), biasValue, af, learningFactor, momentum));
            }
            for (int i = 1; i < numberOfLayers + 1; i++) {
                for (int j = 0; j < this.quantities.get(i); j++) {
                    line = br.readLine();
                    this.layers.get(i).getNuerons().get(j).setWeights(toList(line));
                }
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
    }
    
    public void inputFromFile4(String trainFilename, String testFilename) {
        BufferedReader br;
        String line, currentFolder, newFolder;
        List<Double> oneLine;
        List<List<Double>> wholeInput = null, wholeExpected = null, wholeTestInput = null;
        List<List<Integer>> help = null;
        int numberOfNetworks = 0, inputSize = 0, currentExpected = 0, outputSize = 5;
        this.type = 2;
        try {
            br = new BufferedReader(new FileReader(trainFilename));
            wholeInput = new ArrayList<List<Double>>();
            wholeExpected = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                oneLine = toList(line);
                wholeExpected.add(new ArrayList<Double>());
//                switch (oneLine.get(oneLine.size() - 1).intValue()) {
//                    case 1:
//                        wholeExpected.get(wholeExpected.size() - 1).add(1.0);
//                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
//                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
//                        break;
//
//                    case 2:
//                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
//                        wholeExpected.get(wholeExpected.size() - 1).add(1.0);
//                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
//                        break;
//
//                    case 3:
//                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
//                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
//                        wholeExpected.get(wholeExpected.size() - 1).add(1.0);
//                        break;
//                }
                currentExpected = oneLine.get(oneLine.size() - 1).intValue();
                for (int i = 0; i < outputSize; i++) {
                    wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                }
                wholeExpected.get(wholeExpected.size() - 1).set(currentExpected - 1, 1.0);
                oneLine.remove(oneLine.size() - 1);
                wholeInput.add(oneLine);
                line = br.readLine();
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
        
        try {
            br = new BufferedReader(new FileReader(testFilename));
            wholeTestInput = new ArrayList<List<Double>>();
            this.testExpected = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                oneLine = toList(line);
                this.testExpected.add(new ArrayList<Double>());
//                switch (oneLine.get(oneLine.size() - 1).intValue()) {
//                    case 1:
//                        this.testExpected.get(this.testExpected.size() - 1).add(1.0);
//                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
//                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
//                        break;
//
//                    case 2:
//                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
//                        this.testExpected.get(this.testExpected.size() - 1).add(1.0);
//                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
//                        break;
//
//                    case 3:
//                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
//                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
//                        this.testExpected.get(this.testExpected.size() - 1).add(1.0);
//                        break;
//                }
                currentExpected = oneLine.get(oneLine.size() - 1).intValue();
                for (int i = 0; i < outputSize; i++) {
                    testExpected.get(testExpected.size() - 1).add(0.0);
                }
                testExpected.get(testExpected.size() - 1).set(currentExpected - 1, 1.0);
                oneLine.remove(oneLine.size() - 1);
                wholeTestInput.add(oneLine);
                line = br.readLine();
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
        
            this.networkInput = wholeInput;
            this.expectedValues = wholeExpected;
            this.testInput = wholeTestInput;
    }
    
    

    public void inputFromFile3(String trainFilename, String testFilename) {
        BufferedReader br;
        String line;
        this.type = 3;
        try {
            br = new BufferedReader(new FileReader(trainFilename));
            this.networkInput = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                this.networkInput.add(toList(line));
                line = br.readLine();
            }
            br.close();
            this.expectedValues = new ArrayList<List<Double>>(networkInput);
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
        try {
            br = new BufferedReader(new FileReader(testFilename));
            this.testInput = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                this.testInput.add(toList(line));
                line = br.readLine();
            }
            br.close();
            this.testExpected = new ArrayList<List<Double>>(testInput);
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
    }

    public void inputFromFile2(String trainFilename, String testFilename) {
        BufferedReader br;
        String line, currentFolder, newFolder;
        List<Double> oneLine;
        List<List<Double>> wholeInput = null, wholeExpected = null, wholeTestInput = null;
        List<List<Integer>> help = null;
        int numberOfNetworks = 0, inputSize = 0;
        this.type = 2;
        try {
            br = new BufferedReader(new FileReader(trainFilename));
            wholeInput = new ArrayList<List<Double>>();
            wholeExpected = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                oneLine = toList(line);
                wholeExpected.add(new ArrayList<Double>());
                switch (oneLine.get(oneLine.size() - 1).intValue()) {
                    case 1:
                        wholeExpected.get(wholeExpected.size() - 1).add(1.0);
                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                        break;

                    case 2:
                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                        wholeExpected.get(wholeExpected.size() - 1).add(1.0);
                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                        break;

                    case 3:
                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                        wholeExpected.get(wholeExpected.size() - 1).add(0.0);
                        wholeExpected.get(wholeExpected.size() - 1).add(1.0);
                        break;
                }
                oneLine.remove(oneLine.size() - 1);
                wholeInput.add(oneLine);
                line = br.readLine();
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
        
        try {
            br = new BufferedReader(new FileReader(testFilename));
            wholeTestInput = new ArrayList<List<Double>>();
            this.testExpected = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                oneLine = toList(line);
                this.testExpected.add(new ArrayList<Double>());
                switch (oneLine.get(oneLine.size() - 1).intValue()) {
                    case 1:
                        this.testExpected.get(this.testExpected.size() - 1).add(1.0);
                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
                        break;

                    case 2:
                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
                        this.testExpected.get(this.testExpected.size() - 1).add(1.0);
                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
                        break;

                    case 3:
                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
                        this.testExpected.get(this.testExpected.size() - 1).add(0.0);
                        this.testExpected.get(this.testExpected.size() - 1).add(1.0);
                        break;
                }
                oneLine.remove(oneLine.size() - 1);
                wholeTestInput.add(oneLine);
                line = br.readLine();
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
        
        numberOfNetworks = binomialCoefficient(wholeInput.get(0).size(), this.quantities.get(0));
        inputSize = this.quantities.get(0);
        if(inputSize == 2) {
            help = new ArrayList<List<Integer>>();
            for (int i = 0; i < 6 / 2; i++) {
                for (int j = 0; j < 6 / 2 - i; j++) {
                    //System.out.println("" + (i) + " " + (j + inputSize + i - 1));
                    help.add(new ArrayList<Integer>());
                    help.get(help.size()-1).add(i);
                    help.get(help.size()-1).add(j+inputSize+i-1);
                }
            }
            //System.out.println(help.size());    
        }
        //System.out.println(numberOfNetworks);        
        currentFolder = "IN" + this.quantities.get(0) + " N" + this.quantities.get(1) + " n" + this.learningFactor + " m" + this.momentum + " e" + this.epochMax;
        new File(currentFolder).mkdir();
        for(int i = 0; i < numberOfNetworks; i++) {
            switch(inputSize) {
                case 1:
                    this.networkInput = new ArrayList<List<Double>>();
                    this.expectedValues = new ArrayList<List<Double>>();
                    this.testInput = new ArrayList<List<Double>>();
                    for(int j = 0; j < wholeInput.size(); j++) {
                        this.networkInput.add(new ArrayList<Double>());
                        this.networkInput.get(j).add(wholeInput.get(j).get(i));
                        this.expectedValues.add(new ArrayList<Double>(wholeExpected.get(j)));
                        this.testInput.add(new ArrayList<Double>());
                        this.testInput.get(j).add(wholeTestInput.get(j).get(i));
                    }                    
                    newFolder = currentFolder + "/" + (i+1);
                    new File(newFolder).mkdir();
                    saveNetwork(newFolder + "/before.txt");
                    propagation(newFolder + "/errors.txt");
                    saveNetwork(newFolder + "/after.txt");
                    check(newFolder + "/out.txt");
                    break;
                    
                case 2:
                    this.networkInput = new ArrayList<List<Double>>();
                    this.expectedValues = new ArrayList<List<Double>>();
                    this.testInput = new ArrayList<List<Double>>();
                    for(int j = 0; j < wholeInput.size(); j++) {
                        this.networkInput.add(new ArrayList<Double>());
                        //System.out.println(j + " " + help.get(i).get(0) + " " + help.get(i).get(1));
                        this.networkInput.get(j).add(wholeInput.get(j).get(help.get(i).get(0)));
                        this.networkInput.get(j).add(wholeInput.get(j).get(help.get(i).get(1)));
                        this.expectedValues.add(new ArrayList<Double>(wholeExpected.get(j)));
                        this.testInput.add(new ArrayList<Double>());
                        this.testInput.get(j).add(wholeTestInput.get(j).get(help.get(i).get(0)));
                        this.testInput.get(j).add(wholeTestInput.get(j).get(help.get(i).get(1)));
                    }                    
                    newFolder = currentFolder + "/" + (i+1);
                    new File(newFolder).mkdir();
                    saveNetwork(newFolder + "/before.txt");
                    propagation(newFolder + "/errors.txt");
                    saveNetwork(newFolder + "/after.txt");
                    check(newFolder + "/out.txt");
                    break;
                    
                case 3:
                    this.networkInput = new ArrayList<List<Double>>();
                    this.expectedValues = new ArrayList<List<Double>>();
                    this.testInput = new ArrayList<List<Double>>();
                    for(int j = 0; j < wholeInput.size(); j++) {
                        this.networkInput.add(new ArrayList<Double>(wholeInput.get(j)));
                        this.networkInput.get(j).remove(inputSize-i);
                        this.expectedValues.add(new ArrayList<Double>(wholeExpected.get(j)));
                        this.testInput.add(new ArrayList<Double>(wholeTestInput.get(j)));
                        this.testInput.get(j).remove(inputSize-i);
                    }
                    newFolder = currentFolder + "/" + (i+1);
                    new File(newFolder).mkdir();
                    saveNetwork(newFolder + "/before.txt");
                    propagation(newFolder + "/errors.txt");
                    saveNetwork(newFolder + "/after.txt");
                    check(newFolder + "/out.txt");
                    break;
                    
                case 4:
                    this.networkInput = wholeInput;
                    this.expectedValues = wholeExpected;
                    this.testInput = wholeTestInput;
                    //this.testExpected = wholeTestExcepted;
                    newFolder = currentFolder + "/" + (i+1);
                    new File(newFolder).mkdir();
                    saveNetwork(newFolder + "/before.txt");
                    propagation(newFolder + "/errors.txt");
                    saveNetwork(newFolder + "/after.txt");
                    check(newFolder + "/out.txt");
                    break;
            }
        }
        
    }

    public void inputFromFile1(String trainFilename, String testFilename) {
        BufferedReader br;
        String line;
        List<Double> oneLine;
        this.type = 1;
        try {
            br = new BufferedReader(new FileReader(trainFilename));
            this.networkInput = new ArrayList<List<Double>>();
            this.expectedValues = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                oneLine = toList(line);
                this.expectedValues.add(new ArrayList<Double>());
                this.networkInput.add(new ArrayList<Double>());
                this.networkInput.get(this.networkInput.size() - 1).add(oneLine.get(0));
                this.expectedValues.get(this.expectedValues.size() - 1).add(oneLine.get(1));
                line = br.readLine();
            }
            br.close();
            this.layers.get(this.quantities.size() - 1).setActivationFunction(new Linear());
        } catch (IOException iOException) {
            //System.out.println("Problem z odczytem pliku.");
        }
        try {
            br = new BufferedReader(new FileReader(testFilename));
            this.testInput = new ArrayList<List<Double>>();
            this.testExpected = new ArrayList<List<Double>>();
            line = br.readLine();
            while (line != null) {
                oneLine = toList(line);
                this.testExpected.add(new ArrayList<Double>());
                this.testInput.add(new ArrayList<Double>());
                this.testInput.get(this.testInput.size() - 1).add(oneLine.get(0));
                this.testExpected.get(this.testExpected.size() - 1).add(oneLine.get(1));
                line = br.readLine();
            }
            br.close();
        } catch (IOException iOException) {
            System.out.println("Problem z odczytem pliku.");
        }
    }

    public void setInputFirst(List<Double> input) {
        this.layers.get(0).setInputFirst(input);
    }

    public void computeOutput() {
        this.layers.get(0).passOutput();
        for (int i = 1; i < this.quantities.size(); i++) {
            this.layers.get(i).setInput(this.layers.get(i - 1).getCompleteOutput());
            this.layers.get(i).computeOutput();
        }
    }

    public void improveWeights() {
        for (int i = this.quantities.size() - 1; i > 0; i--) {
            this.layers.get(i).improveWeights();
        }
    }

    public void forwards(int inputIndex) {
        setInputFirst(this.networkInput.get(inputIndex));
        computeOutput();
        //System.out.println(getNetworkOutput());
        this.layers.get(this.quantities.size() - 1).setExpectedValue(this.expectedValues.get(inputIndex));
    }

    public void test(int inputIndex) {
        setInputFirst(this.testInput.get(inputIndex));
        computeOutput();
        this.layers.get(this.quantities.size() - 1).setExpectedValue(this.testExpected.get(inputIndex));
    }

    public void backwards(int inputIndex) {
        this.layers.get(this.quantities.size() - 1).lastLayerError();
        for (int i = this.quantities.size() - 2; i > 0; i--) {
            for (int j = 0; j < this.quantities.get(i); j++) {
                this.layers.get(i).getNuerons().get(j).usualError(this.layers.get(i + 1).weightError(j + 1));
            }
        }
        improveWeights();
    }

    public void propagation(String errorFilename) {
        int epoch = 0, counter1 = 0, counter2 = 0;
        double rms1, rms2, error;
        try {
            FileWriter fstream = new FileWriter(errorFilename);
            BufferedWriter out = new BufferedWriter(fstream);
            do {
                epoch++;
                for (int i = 0; i < this.networkInput.size(); i++) {
                    forwards(i);
                    backwards(i);
                    //computeOutput();
                }
                //if (this.testInput != null && !this.testInput.isEmpty()) {
                    counter1 = counter2 = 0;
                    rms1 = rms2 = 0.0;
                    for(int i = 0; i < this.networkInput.size(); i++) {                        
                        forwards(i);                        
                        error = this.layers.get(this.quantities.size() - 1).outputError();
                        rms1 += error;
                        if(this.type == 2) {
                            if(checkTrainOutput(i)){
                                counter1++;
                            }
                        }
                    }
                    out.write(epoch + " " + Math.sqrt(rms1 / (double) (this.networkInput.size() * this.quantities.get(this.quantities.size() - 1))));
                    if(this.type == 2) {
                        out.write(" " + (double)counter1 / this.networkInput.size());
                    }
                    for (int i = 0; i < this.testInput.size(); i++) {
                        test(i);
                        error = this.layers.get(this.quantities.size() - 1).outputError();
                        rms2 += error;
                        if(this.type == 2) {
                            if(checkTestOutput(i)){
                                counter2++;
                            }
                        }
                    }
                    out.write(" " + Math.sqrt(rms2 / (double) (this.testInput.size() * this.quantities.get(this.quantities.size() - 1))));
                    if(this.type == 2) {
                        out.write(" " + (double)counter2 / this.testInput.size());
                    }
                //}
                out.newLine();
                //System.out.println(errorMax);
                mixNetworkInput();
            } while (epoch < this.epochMax);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean checkTrainOutput(int index) {
        List<Double> out = this.layers.get(this.quantities.size() - 1).getCompleteOutput();
        for(int i = 0; i < out.size(); i++) {
            if(Math.abs(out.get(i) - this.expectedValues.get(index).get(i)) > 0.1) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkTestOutput(int index) {
        List<Double> out = this.layers.get(this.quantities.size() - 1).getCompleteOutput();
        for(int i = 0; i < out.size(); i++) {
            if(Math.abs(out.get(i) - this.testExpected.get(index).get(i)) > 0.1) {
                return false;
            }
        }
        return true;
    }

    public void mixNetworkInput() {
        int changes = this.networkInput.size();
        int index1, index2;
        List<Double> temp;
        Random rand = new Random();
        for (int i = 0; i < changes; i++) {
            index1 = rand.nextInt(changes);
            index2 = rand.nextInt(changes);
            temp = this.networkInput.get(index1);
            this.networkInput.set(index1, this.networkInput.get(index2));
            this.networkInput.set(index2, temp);
            temp = this.expectedValues.get(index1);
            this.expectedValues.set(index1, this.expectedValues.get(index2));
            this.expectedValues.set(index2, temp);
        }
    }

    private List<Double> toList(String weights) {
        ArrayList<Double> result = new ArrayList<Double>();
        String split[] = weights.split(" ");
        for (int i = 0; i < split.length; i++) {
            result.add(Double.parseDouble(split[i]));
        }
        return result;
    }
    
    private int factorial(int x) {
        int result = 1;
        for(int i = 1; i <= x; i++) {
            result *= i;
        }
        return result;
    }
    
    public int binomialCoefficient(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n-k));
    }

    public void saveNetwork(String filename) {
        try {
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("" + (this.quantities.size() - 1));
            out.newLine();
            for (int i = 0; i < this.quantities.size(); i++) {
                out.write(this.quantities.get(i).toString());
                out.newLine();
            }
            for (int i = 1; i < this.quantities.size(); i++) {
                out.write(this.layers.get(i).toString());
            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveHiddenOutput(String filename) {
       try {
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            for (int j = 0; j < this.testInput.size(); j++)
            {
                test(j);
                for (int i = 0; i < this.layers.get(1).getNuerons().size(); i++) {
                    out.write("" + this.layers.get(1).getNuerons().get(i).getOutput() + " ");
                }
                out.newLine();
            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void check() {

        for (int i = 0; i < this.networkInput.size(); i++) {
            System.out.println(this.networkInput.get(i));
            System.out.println(this.expectedValues.get(i));
            setInputFirst(this.networkInput.get(i));
            computeOutput();
            System.out.println(getNetworkOutput());
            System.out.println();
        }
    }

    public void check(String filename) {
        try {
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            //if (this.testInput != null && !this.testInput.isEmpty()) {
                for (int i = 0; i < this.testInput.size(); i++) {
                    //System.out.println(this.networkInput.get(i));
                    //System.out.println(this.expectedValues.get(i));
                    setInputFirst(this.testInput.get(i));
                    computeOutput();
                    //System.out.println(getNetworkOutput());
                    //System.out.println();
                    for(int j = 0; j < this.testInput.get(i).size(); j++) {
                        out.write(this.testInput.get(i).get(j).toString() + " ");
                    }
                    for(int j = 0; j < getNetworkOutput().size(); j++) {
                       out.write(getNetworkOutput().get(j).toString() + " "); 
                    }                    
                    out.newLine();
                }    
//            } else {
//                for (int i = 0; i < this.networkInput.size(); i++) {
//                    //System.out.println(this.networkInput.get(i));
//                    //System.out.println(this.expectedValues.get(i));
//                    setInputFirst(this.networkInput.get(i));
//                    computeOutput();
//                    //System.out.println(getNetworkOutput());
//                    //System.out.println();
//                    for(int j = 0; j < this.networkInput.size(); j++) {
//                        out.write(this.networkInput.get(i).get(j).toString() + " ");
//                    }
//                    for(int j = 0; j < getNetworkOutput().size(); j++) {
//                       out.write(getNetworkOutput().get(0).toString() + " "); 
//                    }                    
//                    out.newLine();
//                }
//            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Double> getNetworkOutput() {
        return this.layers.get(quantities.size() - 1).getCompleteOutput();
    }

    public List<List<Double>> getNetworkInput() {
        return networkInput;
    }
}
