package org.example.pw_projekt_gui;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.concurrent.atomic.AtomicReferenceArray;
public class LiniaProdukcyjna implements Runnable{
    int id;
    String zasob;
    boolean pracuje=false;
    boolean produkt=false;
    private volatile boolean running = true;
    private Pane root;
    int x=350;
    int y;
    LiniaProdukcyjna(int id, String zasob, boolean produkt, Pane root) {
        this.id = id;
        this.zasob = zasob;
        this.produkt=produkt;
        this.root = root;
        //System.out.println("Linia Produkcyjna id:"+id+" używa zasobu "+zasob);
        y=id*100+(id-1)*50;
        /*if(id==1){//chcę by pierwsza linia produkcyjna (LP) od razu tworzyła samochód,jeżdżący od LP do magazynu wyjściowego (MW)
            //System.out.println("IloscSamochodowPRZED:"+Main.iloscSamochodow.get());
            Losuje.powiekszTablice(HelloApplication.listaSamochodow, HelloApplication.iloscSamochodow.get()+1);
            for(int i=HelloApplication.iloscSamochodow.get();i<HelloApplication.iloscSamochodow.get()+1;i++)
            {
                //System.out.println("i:"+i);
                HelloApplication.listaSamochodow.get().set(i,new Samochod((i+1),false,3,0,null,root,x,y,false));
                //Powiększe tablicę listaSamochodowC
                Losuje.powiekszTablice(HelloApplication.listaSamochodowC,1);
                HelloApplication.listaSamochodowC.get().set(0,(1));
            }
            HelloApplication.iloscSamochodow.set(HelloApplication.iloscSamochodow.get()+1);
            //System.out.println("IloscSamochodowPO:"+Main.iloscSamochodow.get());
        }*/
        Image image = new Image("file:/C:/Users/Piotr/OneDrive/Pulpit/WAT/4 Semestr/PW/PW_Projekt_GUI_Ten/src/main/resources/org/example/pw_projekt_gui/lp.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
        imageView.setLayoutX(x);imageView.setLayoutY(y);
    }
    @Override
    public void run(){
        /*while (running) {
            // praca w tle
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // bardzo ważne
                break;
            }
        }*/
        while(running){//!Thread.currentThread().isInterrupted()){
            if(!pracuje&&!produkt){
                // UWAGA ! TUTAJ JEST BŁĄD KTÓRY POWODUJE BRAK KOŃCA WĄTKÓW
                System.out.println("LP"+id+": potrzebuję zasobu "+zasob+".");
                for (int i = 0; i < HelloApplication.listaSamochodowB.get().length(); i++){
                    int idSamochodu = HelloApplication.listaSamochodowB.get().get(i);
                    Samochod samochod = HelloApplication.listaSamochodow.get().get(idSamochodu-1);
                    LiniaProdukcyjna LP = HelloApplication.listaLiniiProdukcyjnych.get().get(id);
                    System.out.println("LP"+id+" sięga po S"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar);//+", LP: "+LP.id+", MNZ: "+MNZ.id);
                    if(!samochod.pracuje&&samochod.obszar==2&&HelloApplication.semaLP.tryAcquire()){
                        System.out.println("LP"+id+" wywołuję S>"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar+", LP: "+LP.id);
                        for (int j = 0; j < HelloApplication.listaMagazynowNaZasoby.get().length(); j++) {
                            MagazynNaZasoby MNZ = HelloApplication.listaMagazynowNaZasoby.get().get(j);
                            System.out.print("MNZZasob:"+MNZ.rzecz+", LPZasob:"+LP.zasob);
                            //MNZ.set(j, Main.listaMagazynowNaZasoby.get().get(id));
                            if(MNZ.rzecz.equals(LP.zasob)){
                                if(!samochod.pracuje&&samochod.obszar==2&&HelloApplication.semaLP1.tryAcquire()){
                                    System.out.println("LP"+id+" wywołuję S%"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar+", LP: "+LP.id+", MNZ: "+MNZ.id);
                                    samochod.sekcja2(zasob,samochod,MNZ,LP);
                                    System.out.println("LP"+id+" otrzymał od S$"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar+" i teraz pracuje:"+LP.pracuje);
                                    break;
                                }
                            }
                            else{System.out.println("NIE");HelloApplication.semaLP.release();}
                        }
                    }
                }
            }
            else if(pracuje&&!produkt){
                System.out.println("LP"+id+": zaczynam pracę!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                System.out.println("LP"+id+": kończę pracę!");
                pracuje=false;produkt=true;
            }
            else if(!pracuje&&produkt)
            {
                // UWAGA ! TUTAJ JEST BŁĄD KTÓRY POWODUJE BRAK KOŃCA WĄTKÓW
                System.out.println("LP"+id+": posiadam produkt!");
                for (int i = 0; i < HelloApplication.listaSamochodowC.get().length(); i++){
                    int idSamochodu = HelloApplication.listaSamochodowC.get().get(i);
                    Samochod samochod = HelloApplication.listaSamochodow.get().get(idSamochodu-1);
                    LiniaProdukcyjna LP = HelloApplication.listaLiniiProdukcyjnych.get().get(id);
                    System.out.println("LP"+id+" sięga po S"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar);//+", LP: "+LP.id+", MNZ: "+MNZ.id);
                    System.out.println("LP"+id+" wywołuję S#"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar+", LP: "+LP.id);
                    if(!samochod.pracuje&&samochod.obszar==3&&HelloApplication.semaMW.tryAcquire()){
                        MagazynWyjsciowy MW = HelloApplication.MW1;
                        System.out.println("LP"+id+" wywołuję S@"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar+", LP: "+LP.id+", MW: "+MW.id);
                        samochod.sekcja3(samochod,LP,MW);
                        System.out.println("LP"+id+" otrzymał od S!"+samochod.id+" pracuje:"+samochod.pracuje+" obszar:"+samochod.obszar+" i teraz pracuje:"+LP.pracuje);
                        break;
                    }
                }
            }
            System.out.println("LP"+id+" działa w pętli!");
            try {
                Thread.sleep(1000);  // śpij 1 sekundę
            } catch (InterruptedException e) {
                break;  // przerwij pętlę jeśli ktoś wywoła .interrupt()
            }
        }
        System.out.println("Koniec LP"+id+".");
    }
    public void stop() {
        running = false;
    }
}
