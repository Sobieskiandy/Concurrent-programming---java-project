/*
Programowanie Współbieżne - Projekt
Autor: Piotr Lada
Grupa: WCY23IY1S1
Nr zadania projektowego: 5
Tytuł zadania projektowego: Zakład produkcyjny
Zadanie nr: PW-5
Język implementacji: Java
Środowisko implementacyjne: Eclipse, Intelij IDEA, Netbeans
Termin wykonania: ostatnie zajęcia
Podstawowe wymagania:
a. liczba procesów sekwencyjnych powinna być dobrana z wyczuciem tak, aby zachować czytelność interfejsu i
jednocześnie umożliwić zobrazowanie reprezentatywnych przykładów, Done
b. kod źródłowy programu musi być tak skonstruowany, aby można było „swobodnie” modyfikować liczbę
procesów sekwencyjnych (za wyjątkiem zadań o ściśle określonej liczbie procesów),
c. graficzne zobrazowanie działania procesów współbieżnych, Robię
d. odczyt domyślnych danych wejściowych ze sformatowanego, tekstowego pliku danych (xml, properties, inne),
e. możliwość modyfikacji danych wejściowych poprzez GUI.
Sprawozdanie (w formie elektronicznej) powinno zawierać następujące elementy:
1) stronę tytułową,
2) niniejszą treść zadania,
3) syntetyczny opis problemu – w tym wszystkie przyjęte założenia,
4) wykaz własnych procesów sekwencyjnych,
5) wykaz współdzielonych zasobów,
6) wykaz wyróżnionych punktów synchronizacji,
7) wykaz obiektów synchronizacji,
8) listing programu.
Problem do rozwiązania:
Zakład produkcyjny.
Założenia:
Zakład produkcyjny produkuje na kilku liniach wyroby tego samego typu korzystając z dwóch rodzajów surowców.
Surowce różnego rodzaju przechowywane są w oddzielnych magazynach, które
zaopatrywane są przez jeden samochód w sposób wyłączny.
Surowce danego rodzaju dowożone są po spadku zapasu w magazynie poniżej ustalonego poziomu.
Produkcja wyrobów na linii zajmuje losowy czas. Wyroby gotowe trafiają z linii do magazynu wyjściowego,
z którego co losowy czas są zabierane w formie paczek (ustalona liczba
sztuk).
 */
package org.example.pw_projekt_gui;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
/*
DONE
 - podstawa(obiekty, menu do testów)
 - dodałem zmienne typu Atomic
 - poprawne przenoszenie długości tablicy oraz elementów listyliniiprodukcyjnych
 - częściowe dodanie współbieżności między obiektami(Alg. Peterson,_,_, Alg. Dekker)
  - idk czemu w Linii Produkcyjnej by dobrze zdefiniować samochód w sferze3 muszę dodać +1
  współbieżność między obiektami
TODO
 - GUI
 - Wczytywanie z plików
 - klasa Losuje, linijka 80 i MW1, przy losowaniu dodaj metodę która zatrzyma poprzednie.
 */
public class HelloApplication extends Application {
    //metoda dostarczająca do MNZ kiedy zostanie wywołana. Jest ona sekcją krytyczną symbolizująca dostęp do zasobu jakim jest samochód dostawczy który przewozi pewną zmienną ilość określonych zasobów w określonej ilości do określonego MNZ.
    static Semaphore semaMNZ = new Semaphore(1);
    static Semaphore semaLP = new Semaphore(1);static Semaphore semaLP1 = new Semaphore(1);
    static Semaphore semaMW = new Semaphore(1);
    public static AtomicInteger iloscMagazynowNaZasoby=new AtomicInteger(0);
    public static AtomicInteger iloscMagazynowWyjsciowych=new AtomicInteger(0);
    public static MagazynWyjsciowy MW1;
    public static Parent root;
    public static Parent settingsRoot;
    public static Stage settingsStage;
    public static AtomicInteger iloscZasobow=new AtomicInteger(0);
    public static AtomicInteger iloscLiniiProdukcyjnych = new AtomicInteger(0);
    public static AtomicInteger iloscSamochodow = new AtomicInteger(0);
    public static AtomicReference<AtomicReferenceArray<Integer>> listaSamochodowA = new AtomicReference<>(new AtomicReferenceArray<>(0));
    public static AtomicReference<AtomicReferenceArray<Integer>> listaSamochodowB = new AtomicReference<>(new AtomicReferenceArray<>(0));
    public static AtomicReference<AtomicReferenceArray<Integer>> listaSamochodowC = new AtomicReference<>(new AtomicReferenceArray<>(0));
    public static AtomicReference<AtomicReferenceArray<Integer>> listaSamochodowD = new AtomicReference<>(new AtomicReferenceArray<>(0));
    public static AtomicReferenceArray<String> listaZasobow= new AtomicReferenceArray<String>(iloscZasobow.get());//String[] listaZasobow=null;
    public static AtomicReference<AtomicReferenceArray<Samochod>> listaSamochodow = new AtomicReference<>(new AtomicReferenceArray<>(iloscSamochodow.get()));
    public static AtomicReference<AtomicReferenceArray<MagazynNaZasoby>> listaMagazynowNaZasoby = new AtomicReference<>(new AtomicReferenceArray<>(iloscMagazynowNaZasoby.get()));//AtomicReferenceArray<MagazynNaZasoby> listaMagazynowNaZasoby=new AtomicReferenceArray<MagazynNaZasoby>(iloscMagazynowNaZasoby.get());//MagazynNaZasoby[] listaMagazynowNaZasoby=null;
    public static AtomicReference<AtomicReferenceArray<MagazynWyjsciowy>> listaMagazynowWyjsciowych = new AtomicReference<>(new AtomicReferenceArray<>(iloscMagazynowWyjsciowych.get()));
    public static AtomicReference<AtomicReferenceArray<LiniaProdukcyjna>> listaLiniiProdukcyjnych = new AtomicReference<>(new AtomicReferenceArray<>(iloscLiniiProdukcyjnych.get()));//Thread[] listaLiniiProdukcyjnych=null;
    int s=0;
    boolean st=false;
    public ExecutorService executor;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        root = fxmlLoader.load();
        //scena
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Zakład produkcyjny");
        stage.setScene(scene);
        stage.show();
        Pane root = new Pane();
        new Losuje().domyslne0(HelloApplication.iloscZasobow, HelloApplication.iloscLiniiProdukcyjnych, HelloApplication.iloscMagazynowNaZasoby, HelloApplication.iloscMagazynowWyjsciowych,HelloApplication.listaZasobow, HelloApplication.listaMagazynowNaZasoby, HelloApplication.listaLiniiProdukcyjnych, HelloApplication.listaMagazynowWyjsciowych, root);
        Button StartBtn = new Button("Start");root.getChildren().add(StartBtn);
        Button StopBtn = new Button("Stop");root.getChildren().add(StopBtn);
        Button SettingsBtn = new Button("Ustawienia");root.getChildren().add(SettingsBtn);
        Button ExitBtn = new Button("Wyjdź");root.getChildren().add(ExitBtn);
        StartBtn.setLayoutX(0);StartBtn.setLayoutY(0);
        StopBtn.setLayoutX(50);StopBtn.setLayoutY(0);
        SettingsBtn.setLayoutX(100);SettingsBtn.setLayoutY(0);
        ExitBtn.setLayoutX(175);ExitBtn.setLayoutY(0);
        scene = new Scene(root, 1080, 520);
        stage.setScene(scene);
        stage.show();
        StartBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //MagazynyNaZasoby
            //if(s==0){
                if (executor != null && !executor.isShutdown()) {
                    return; // już działa
                }
                executor = Executors.newFixedThreadPool(iloscLiniiProdukcyjnych.get());
                AtomicReferenceArray<LiniaProdukcyjna> linie = HelloApplication.listaLiniiProdukcyjnych.get();
                for (int i = 0; i < HelloApplication.iloscLiniiProdukcyjnych.get(); i++) {
                    LiniaProdukcyjna lp = linie.get(i);
                    executor.submit(lp);
                }
                executor = Executors.newFixedThreadPool(iloscMagazynowNaZasoby.get());
                AtomicReferenceArray<MagazynNaZasoby> linie1 = HelloApplication.listaMagazynowNaZasoby.get();
                for (int i = 0; i < HelloApplication.iloscMagazynowNaZasoby.get(); i++) {
                    MagazynNaZasoby mnz = linie1.get(i);
                    executor.submit(mnz);
                }
                executor = Executors.newFixedThreadPool(iloscMagazynowWyjsciowych.get());
                AtomicReferenceArray<MagazynWyjsciowy> linie2 = HelloApplication.listaMagazynowWyjsciowych.get();
                for (int i = 0; i < HelloApplication.iloscMagazynowWyjsciowych.get(); i++) {
                    MagazynWyjsciowy mw = linie2.get(i);
                    executor.submit(mw);
                }
                executor = Executors.newFixedThreadPool(iloscSamochodow.get());
                AtomicReferenceArray<Samochod> linie3 = HelloApplication.listaSamochodow.get();
                for (int i = 0; i < HelloApplication.iloscSamochodow.get(); i++) {
                    Samochod s1 = linie3.get(i);
                    executor.submit(s1);
                }
                s++;//tylko jeden raz
            //}
            /*
            else{
                for (int i = 0; i < HelloApplication.iloscLiniiProdukcyjnych.get(); i++) {
                    LiniaProdukcyjna lp = HelloApplication.listaLiniiProdukcyjnych.get().get(i);
                    if (lp != null) {
                        //lp.start();
                    }
                }
                for (int i = 0; i < HelloApplication.iloscMagazynowNaZasoby.get(); i++) {
                    MagazynNaZasoby lmnz = HelloApplication.listaMagazynowNaZasoby.get().get(i);
                    if (lmnz != null) { 
                        //lmnz.start();
                    }
                }
                //HelloApplication.MW1.start();
                for (int i = 0; i < HelloApplication.iloscSamochodow.get(); i++) {
                    Samochod sa = HelloApplication.listaSamochodow.get().get(i);
                    if (sa != null) {
                        //sa.start();
                    }
                }
                s++;//każdy kolejny raz, by runnable
            }*/
        });
        StopBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            for (int i = 0; i < HelloApplication.iloscSamochodow.get(); i++) {
                Samochod sa = HelloApplication.listaSamochodow.get().get(i);
                if (sa != null) {
                    sa.stop();
                }
            }
            for (int i = 0; i < HelloApplication.iloscLiniiProdukcyjnych.get(); i++) {
                LiniaProdukcyjna lp = HelloApplication.listaLiniiProdukcyjnych.get().get(i);
                if (lp != null) {
                    lp.stop();
                }
            }
            for (int i = 0; i < HelloApplication.iloscMagazynowNaZasoby.get(); i++) {
                MagazynNaZasoby mnz = HelloApplication.listaMagazynowNaZasoby.get().get(i);
                if (mnz != null) {
                    mnz.stop();
                }
            }
            for (int i = 0; i < HelloApplication.iloscMagazynowWyjsciowych.get(); i++) {
                MagazynWyjsciowy mw = HelloApplication.listaMagazynowWyjsciowych.get().get(i);
                if (mw != null) {
                    mw.stop();
                }
            }
            if (executor != null) {
                executor.shutdownNow();
                try {
                    if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                        System.out.println("Wątki nie zakończyły się na czas");
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
            //HelloApplication.MW1.interrupt();
            /*
            try{
                //HelloApplication.MW1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        });
        SettingsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FXMLLoader fxmlSettingsLoader = new FXMLLoader(HelloApplication.class.getResource("settings-view.fxml"));
            try{
                settingsRoot = fxmlSettingsLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene1 = new Scene(settingsRoot, 600, 440);
            settingsStage = new Stage();
            settingsStage.setTitle("Zakład produkcyjny");
            settingsStage.setScene(scene1);
            settingsStage.show();
        });
        ExitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (executor == null) {
                executor = Executors.newFixedThreadPool(1);
            }
            executor.shutdownNow();
            try{
                executor.awaitTermination(5,TimeUnit.SECONDS);
            }catch(InterruptedException e){
                Thread.getAllStackTraces();
            }
            Platform.exit();
        });
    }
    public static void main(String[] args)
    {
        launch();
    }
}