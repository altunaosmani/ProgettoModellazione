# The Lost Elf

Gioco di ruolo testuale-grafico in Java/JavaFX. Eren, giovane elfo del villaggio di Tanusha, segue le tracce della madre Elara attraverso tre livelli (Foresta dei Sussurri, Castello di Ardan, Lago degli Specchi), risolvendo enigmi, parlando con i personaggi e facendo scelte che influenzano attributi e finale.

Progetto per Metodologie di Programmazione – AA 2025/26. Package base: it.unicam.cs.mpgc.rpg130579.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle

### Istruzioni

```bash
git clone <url-del-repository>
cd <nome-cartella>
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

Su Windows usare `gradlew.bat` al posto di `./gradlew`.

Il salvataggio viene scritto in `savegame.json` nella cartella da cui si avvia il gioco.

---

## 🎮 Funzionalità principali
- 3 livelli collegati, ciascuno con una missione
- Esplorazione di luoghi, raccolta oggetti, inventario
- Enigmi (scelta, oggetto richiesto, sequenza di frammenti) e meccanismi sbloccabili
- Dialoghi ramificati con scelte condizionate dagli attributi
- Attributi (Sapienza, Persuasione, Percezione), bonus da oggetti, finali multipli
- Salvataggio/caricamento in JSON

La descrizione completa dell'architettura è nella **Wiki** del repository.

---

## 🤖 Uso di strumenti di AI

- Strumento/i utilizzato/i: Claude, Gemini
- Claude utilizzato per:
  - chiarire concetti SOLID e pattern
  - revisione del codice e ricerca di bug
  - bozza di README/Wiki
  - realizzazione di classi test

- Gemini utilizzato per:
   - suggerimenti sulla struttura dei package
   - stesura di javadoc
   
Tutto il codice è stato scritto/rivisto, compreso e adattato da me.

Dettaglio completo nella Wiki, pagina **Uso dell'AI**.

