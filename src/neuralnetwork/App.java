/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import neuralnetwork.functions.*;

/**
 *
 * @author user
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    //java neuralnetwork.App -V 1,2,3 -N 4 3 4 range -0.5 0.5 lub -F "network.txt" -f sigm,lin,htan -n 0.1 -a 0.6 -e 10000
    private void run(String[] args) {
        Integer type = null, numberOfLayers = null, numberOfEpochs = null;
        Double rangeMin = null, rangeMax = null, eta = null, alfa = null, bias = null;
        String networkFilename = null, trainFilename = null, testFilename = null, errorsFilename = null, saveFilename = null, testingFilename = null;
        List<Integer> quantities = null;
        ActivationFunction af = null;
        Network neuralNet;

        if (args.length == 0) {
            printHelp();
            System.exit(-1);
        }

        for (int i = 0; i < args.length;) {
            if (args[i].contentEquals("-V")) {
                try {
                    type = Integer.parseInt(args[i + 1]);
                    if (type > 4 || type < 1) {
                        System.out.println("Nieobslugiwany numer zadania. Zakres to 1, 2, 3. Aplikacja zostala zamknieta");
                        System.exit(-2);
                    }
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Napotkano wartosc znakowa \"" + args[i + 1] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                    System.exit(-3);
                }
                trainFilename = args[i + 2];
                if (!new File(trainFilename).isFile()) {
                    System.out.println("Proponowany plik treningowy nie istnieje :(");
                    System.exit(-7);
                }
                testFilename = args[i + 3];
                if (!new File(testFilename).isFile()) {
                    System.out.println("Proponowany plik testowy nie istnieje :(");
                    System.exit(-7);
                }
                i += 4;
            } else if (args[i].contentEquals("-N")) {
                int j = 0;
                try {
                    numberOfLayers = Integer.parseInt(args[i + 1 + j]);
                    quantities = new ArrayList<Integer>();
                    for (j = 1; j <= numberOfLayers + 1; j++) {
                        quantities.add(Integer.parseInt(args[i + 1 + j]));
                    }
                    i += numberOfLayers + 3;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Napotkano wartosc znakowa \"" + args[i + 1 + j] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                    System.exit(-3);
                }
                if (args[i].contentEquals("-r")) {
                    try {
                        i++;
                        rangeMin = Double.parseDouble(args[i]);
                        i++;
                        rangeMax = Double.parseDouble(args[i]);
                        i++;
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Napotkano wartosc znakowa \"" + args[i] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                        System.exit(-3);
                    }
                } else {
                    System.out.println("Brak parametru zakresu losowych wag. Aplikacja zostala zamknieta.");
                    System.exit(-5);
                }
            } else if (args[i].contentEquals("-F")) {
                networkFilename = args[i + 1];
                if (!new File(networkFilename).isFile()) {
                    System.out.println("Proponowany plik z siecia nie istnieje :(");
                    System.exit(-7);
                }
                i += 2;
            } else if (args[i].contentEquals("-b")) {
                try {
                    bias = Double.parseDouble(args[i + 1]);
                    if (bias > 1.0 || bias < 0.0) {
                        System.out.println("Zakres dla biasu to 0.0 - 1.0, 0.0 oznacza wylaczenie biasu.");
                        System.exit(-8);
                    }
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Napotkano wartosc znakowa \"" + args[i] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                    System.exit(-3);
                }
                i += 2;
            } else if (args[i].contentEquals("-f")) {
                if (args[i + 1].contains("sigm")) {
                    af = new Sigmoid();
                } else if (args[i + 1].contains("lin")) {
                    af = new Linear();
                } else if (args[i + 1].contains("tan")) {
                    af = new HyperbolicTangent();
                } else {
                    System.out.println("Nieznana funkcja aktywacji. Aplikacja zostala zamknieta.");
                    System.exit(-4);
                }
                i += 2;
            } else if (args[i].contentEquals("-n")) {
                i++;
                try {
                    eta = Double.parseDouble(args[i]);
                    i++;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Napotkano wartosc znakowa \"" + args[i] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                    System.exit(-3);
                }
            } else if (args[i].contentEquals("-a")) {
                i++;
                try {
                    alfa = Double.parseDouble(args[i]);
                    i++;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Napotkano wartosc znakowa \"" + args[i] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                    System.exit(-3);
                }
            } else if (args[i].contentEquals("-e")) {
                i++;
                try {
                    numberOfEpochs = Integer.parseInt(args[i]);
                    i++;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Napotkano wartosc znakowa \"" + args[i] + "\", gdzie oczekiwana byla wartosc liczbowa. Aplikacja zostala zamknieta.");
                    System.exit(-3);
                }
            } else if (args[i].contentEquals("-s")) {
                saveFilename = args[i + 1];
                i += 2;
            } else if (args[i].contentEquals("-o")) {
                errorsFilename = args[i + 1];
                i += 2;
            } else if (args[i].contentEquals("-t")) {
                testingFilename = args[i + 1];
                i += 2;
            } else if (i < args.length) {
                System.out.println("Wprowadzono nieobslugiwany parametr \"" + args[i] + "\". Aplikacja zostaje zamknieta.");
                System.exit(-2);
            }
        }

        if (type == null) {
            System.out.println("Nalezy przy pomocy parametru -V sprecyzowac numer zadania dla ktorego chcemy przeprowadzic badanie.");
            System.exit(-10);
        }

        if (af == null) {
            System.out.println("Nalezy podac przy pomoca parametru -f funkcje aktywacji.");
            System.exit(-10);
        }

        if (bias == null) {
            System.out.println("Nalezy podac przy pomoca parametru -b wartosc biasu.");
            System.exit(-10);
        }

        if (eta == null) {
            System.out.println("Nalezy podac przy pomoca parametru -n wartosc wspolczynnika nauki.");
            System.exit(-10);
        }

        if (alfa == null) {
            System.out.println("Nalezy podac przy pomocy parametru -a wartosc momentum(alfa).");
            System.exit(-10);
        }

        if (numberOfEpochs == null) {
            System.out.println("Nalezy podac przy pomocy parametru -e ilosc epok do wykonania.");
            System.exit(-10);
        }

        if (errorsFilename == null) {
            System.out.println("Nalezy podac przy pomocy parametru -o plik do ktorego zostana zapisane bledy dla poszczegolnych epok.");
            System.exit(-10);
        }

        if (quantities == null && networkFilename == null) {
            System.out.println("Nalezy podac charakterytyke sieci neuronowej przy pomocy parametrow -N i -r lub parametru -F");
            System.exit(-10);
        }

        if (quantities == null) {
            System.out.println();
            System.out.println("Wywolanie aplikacja dla nastepujacych parametrow:");
            System.out.println("Wersja dla zadania: " + type);
            System.out.println("Siec wczytana z pliku: " + networkFilename);
            System.out.println("Wspolczynnik nauki: " + eta + ", bias: " + bias + ", momentum(alfa): " + alfa + ", liczba epok: " + numberOfEpochs);
            System.out.print("Trwa proces nauki sieci...");
            neuralNet = new Network(networkFilename, bias, af, eta, alfa, numberOfEpochs);
        } else {
            System.out.println();
            System.out.println("Wywolanie aplikacja dla nastepujacych parametrow:");
            System.out.println("Wersja dla zadania: " + type);
            System.out.print("Siec o " + numberOfLayers + " warstwach, " + quantities.get(0) + " wejsciach, ");
            for (int i = 0; i < quantities.size() - 2; i++) {
                System.out.print("" + quantities.get(i + 1) + " neronach na warstwie " + (i + 1) + ", ");
            }
            System.out.println(quantities.get(quantities.size() - 1) + " wyjsciach.");
            System.out.println("Wspolczynnik nauki: " + eta + ", bias: " + bias + ", momentum(alfa): " + alfa + ", liczba epok: " + numberOfEpochs);
            System.out.print("Trwa proces nauki sieci...");
            neuralNet = new Network(quantities, rangeMin, rangeMax, bias, af, eta, alfa, numberOfEpochs);
        }
        switch (type) {
            case 1:
                neuralNet.inputFromFile1(trainFilename, testFilename);
                neuralNet.propagation(errorsFilename);
                if (saveFilename != null) {
                    neuralNet.saveNetwork(saveFilename);
                }
                if (testingFilename != null) {
                    neuralNet.check(testingFilename);
                }
                break;

            case 2:
                neuralNet.inputFromFile2(trainFilename, testFilename);
                break;

            case 3:
                neuralNet.inputFromFile3(trainFilename, testFilename);
                neuralNet.propagation(errorsFilename);
                if (saveFilename != null) {
                    neuralNet.saveNetwork(saveFilename);
                }
                if (testingFilename != null) {
                    neuralNet.check(testingFilename);
                }
                neuralNet.saveHiddenOutput("HiddenOutput.txt");
                break;

            case 4:
                neuralNet.inputFromFile4(trainFilename, testFilename);
                neuralNet.propagation(errorsFilename);
                if (saveFilename != null) {
                    neuralNet.saveNetwork(saveFilename);
                }
                if (testingFilename != null) {
                    neuralNet.check(testingFilename);
                }
                break;
        }
        System.out.println();
        System.out.println("Siec neuronowa zakonczyla swoja prace, teraz mozesz sprawdzic utworzone pliki.");
        System.out.println();
    }

    private void printHelp() {
        System.out.println();
        System.out.println("Aplikacja wykorzystujaca siec neuronowa do rozwiazania zadan laboratoryjnych.");
        System.out.println();
        System.out.println("Parametry niezbedne do dzialania aplikacji:");
        System.out.println("-V nr_zadania(1,2,3) plik_treningowy plik_testowy");
        System.out.println("-N ilosc_warstw ilosc_wejsc ilosci_neuronow_na_warstwach");
        System.out.println("-r zakres_dolny zakres_gorny (dla losowych wag)");
        System.out.println("-F plik_z_siecia (ilosci neuronow i wagi)");
        System.out.println("-f funkcja_aktywacji(sigm, lin, htan)");
        System.out.println("-b bias (0.0 do 1.0, 0.0 => wylaczony)");
        System.out.println("-n wartosc_eta(wspolczynnik nauki)");
        System.out.println("-a alfa(momentum)");
        System.out.println("-e liczba_epok");
        System.out.println("-s siec_po_nauce.txt (opcjonalny)");
        System.out.println("-o bledy.txt(plik na bledy w poszczegolnych epokach)");
        System.out.println("-t testowany.txt(jakosc sieci badana po nauce na danych testowych, opcjonalny)");
        System.out.println();
        System.out.println("Przykladowe uzycia:");
        System.out.println("-V 3 in.txt in.txt -F network.txt -f sigm -n 0.3 -a 0.9 -e 1500");
        System.out.println("-V 2 train.txt test.txt -N 2 3 8 3 -r -0.5 0.5 -f sigm -n 0.3 -a 0.9 -e 15000");
        System.out.println("-V 1 train.txt test.txt -N 2 3 10 3 -r -0.5 0.5 -f htan -n 0.1 -a 0.6 -e 10000");
    }
}
