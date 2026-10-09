package it.unicam.cs.mpgc.rpg130579;

public class OggettoTest {

    public static void main(String[] args) {
        Oggetto medallion = new Oggetto(
                "medaglione",
                "Medaglione d'argento",
                "Un medaglione con un occhio e tre rami."
        );

        int promosso = 0;
        int fallito = 0;

        if (medallion.getId().equals("medaglione")) {
            promosso++;
        } else {
            fallito++;
            System.out.println("ERRORE: ID non corretto.");
        }

        if (medallion.getNome().equals("Medaglione d'argento")) {
            promosso++;
        } else {
            fallito++;
            System.out.println("ERRORE: nome non corretto.");
        }

        if (medallion.getDescrizione().equals(
                "Un medaglione con un occhio e tre rami."
        )) {
            promosso++;
        } else {
            fallito++;
            System.out.println("ERRORE: descrizione non corretta.");
        }

        try {
            new Oggetto("", "Chiave", "Una vecchia chiave.");
            fallito++;
            System.out.println(
                    "ERRORE: avrebbe dovuto rifiutare un ID vuoto."
            );
        } catch (IllegalArgumentException e) {
            promosso++;
        }

        System.out.println("Test superati: " + promosso);
        System.out.println("Test falliti: " + fallito);
    }
}