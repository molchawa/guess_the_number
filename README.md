# guess_the_number - Zgadnij liczbę! - gra na Androida
Mobile app repository.
Gra na platformę Android dla max. 4 użytkowników, która polega na zadaniu przedziału, z którego zostanie wylosowana liczba (lub liczby). Nastepnie zadaniem graczy jest z zgadnięcie, jaka liczba zostałw wylosowana. Gra zapewnia dwa tryby: dla każdego losowana jest taka sama liczba lub dla każdego losowana jest inna liczba.
## Jak zagrać?

W celu uruchomienia gry należy wygenerować .apk, następnie zainstalować i postępować zgodnie z instrukcjami. 

### Wymagania

Gra jest przeznaczona dla urządzeń z wersją Androida min. 5.0 Lollipop (API Level 21)

### Demo

![Image](https://github.com/molchawa/guess_the_number/blob/master/screens/screen1.png)
![Image](https://github.com/molchawa/guess_the_number/blob/master/screens/screen2.png)
![Image](https://github.com/molchawa/guess_the_number/blob/master/screens/screen3.png)
![Image](https://github.com/molchawa/guess_the_number/blob/master/screens/screen4.png)
![Image](https://github.com/molchawa/guess_the_number/blob/master/screens/screen5.png)

## Struktura projektu

Projekt tworzony w środowisku Android Studio. Składa się z 8 klas (pisanych w języku Java), przy czym 6 z nich stanowi osobne Activity. Wszystkie klasy dziedziczą po klasie AppCompatActivity, która to klasa pozwala na wsparcie wtecz (dzięki niej aplikacja będzie działała oraz wyglądała w miarę podobnie nawet na starszych wersjach systemu Android). Ponad to plik AndroidManifest.xml, który zawiera nazwy wszystkich Activity, ustala warunki uruchomieniowe, a także potrzebne permissions (jak dla zastosowanych w projekcie wibracji).

`<uses-permission android:name="android.permission.VIBRATE"/>`

Na projekt składają się także pliki .xml zawierające layout poszczególnych Activity oraz customowego dialog boxa. Ponad to w osobnych plikach źródłowych są zawarte wszystkie teskty (poza tworzonymi dynamicznie), a także kolory czy wymiary.
 
### Opis klas

#### MainActivity

Klasa, która stanowi pierwszy ekran po otworzeniu aplikacji. Zawiera menu, z którego można: rozpocząć *Nową grę*,wejść w ekran *Ustawień*, przejść do *Zasad gry* lub po prostu opuścić grę.

#### AboutActivity

Klasa, która zawiera jedynie referencję do wyglądu. Wyświetla *zasady gry*.

#### OptionsActivity

Klasa, która zawiera widok wyboru opcji. Opcje wybiera się z wykorzystaniem SeekBara (ustalenie zakresu wyświetlania alertu mniejszy/większy) oraz RadioButtons (do wyboru trybu gry). Wszystkie zmiany zapisywane są w SharedPreferences w postaci parametrów (dzięki czemu łatwo je uzyskać z każdego Activity).

#### AGameActivity

Klasa, która stanowi pierwszy ekran gry. Najpierw ustala się ilość graczy (1-4 RadioButtons), a następnie od graczy pobierane są nicki.
Gracze zapisywani są w strukturze danych (ArrayList) w postaci obiektów klasy Player (klasy utworzonej właśnie by symulować użytkownika). W trakcie wprowadzania sprawdzane jest, czy nick się nie powtarza (nie dopuszcza się więcej niż jednego gracza o danym nicku).

#### BGameActivity

Właściwa część gry. Najpierw użytkownik podaje przedział losowania. Nie może być on mniejszy niż 100 (z założeń gry). Następnie losowana jest kolejność użytkwoników (by kolejność wprowadzania nicku nie miała znaczenia). Natsępnie losowana jest liczba lub liczby (w zależności od trybu gry). Użytkownik prowadzi grę podając liczby i sprawdzając, czy są liczbami wylosowanymi. Jeśli podana przez gracza liczba leży w otoczeniu wylosowanej liczby (otoczenie określone przez zakres z wybierany w *Ustawieniach*: -z/+z % maksimum przedziału) użytkownik jest informowany, czy podaa przez niego liczba jest mniejsza/większa od wylosowanej. Gdy gracz zgadnie liczbę, kończy swoją rozgrywkę, a informuje go o tym stosowny napis (Toast) oraz wibracje.

#### ResultActivity

Ekran wyświetlający wyniki (gracz oraz liczba prób do momentu trafienia w wylosowaną liczbę) w postaci tabeli budowanej dunamicznie (w zależnośco od ilości graczy).

#### Player

Klasa symulująca gracza. Zawiera takie pola prywatne jak: nick, liczba prób,  liczba (wylosowana liczba), tymczasowa liczba (podawana przez użytkownika), zmienna warunkowa określająca czy liczba została odgadnieta. Ponad to klasa zawiera funkcje dostępowe (*settery* oraz *gettery*).

#### MessagesManager

Klasa, która zbiera w sobie funkcje odpowiedzialne za kontakt z użytkownikiem (wyświetlanie *toasta* oraz *dialog boxa*).

### Ciekawsze metody oraz fragmenty kodu

#### Metoda sortująca graczy

Gracze ustawiani są w wylosowanej kolejności. KOrzysta z klasy *Random*

```
    public ArrayList<Player> sortingPlayers(ArrayList<Player> list, int amountOfPlayers) {
        ArrayList<Player> templistOfPlayers = new ArrayList<Player>();
        ArrayList<Integer> numbersForOrder = new ArrayList<Integer>();
        //generating new order
        Random generator = new Random();
        for (int i = 0; i < amountOfPlayers; i++) {
            numbersForOrder.add(i);
        }
        //sorting list of players
        for (int i = 0; i < amountOfPlayers; i++) {
            int order = generator.nextInt(numbersForOrder.size());
            templistOfPlayers.add(i, list.get(numbersForOrder.get(order)));
            numbersForOrder.remove(order);
        }
        return templistOfPlayers;
    }
```
#### Metoda losująca liczbę (lub liczby)

Losowanie liczbyw  w zależności od trybu.

```
    public void numberRandomization(ArrayList<Player> list, int noOfPlayers, int m, int min, int max) {
        switch (m) {
            case 1:
                //there is one number for all players
                for (int i = 0; i < noOfPlayers; i++) {
                    Random generator = new Random();
                    list.get(i).setNumber(generator.nextInt((max - min) + 1) + min);

                }
                break;
            case 2:
                //each player has his own, generated number
                Random generator = new Random();
                int tempNumber = generator.nextInt((max - min) + 1) + min;
                for (int i = 0; i < noOfPlayers; i++) {
                    list.get(i).setNumber(tempNumber);

                }
                break;
        }
    }
```
#### Przesyłanie listy graczy pomiędzy Activity

Odbywa się na zasadzie tworzenia obiektu klasy Intent i przesyłania go do innego Activity. By móc korzytsać z tej możliwości należało klasę Player
ustalić jako *Serializable*. 

```
        Intent i = new Intent(BGameActivity.this, ResultActivity.class);
        i.putExtra("listOfPlayers", listOfPlayers);
        i.putExtra("numberOfPlayers", noOfPlayers);
        startActivity(i);
```
#### Metoda nadpisująca działanie przycisku *back*

Tak, aby w trakcie gry możliwe było jedynie wyjście do *Menu* (z utratą obecnego postępu gry). Użytkownik jest pytany, czy aby na pewno chce opuścić obecną
grę w postaci stosownego *dialog boxa* (obiekt z klasy *MessagesManager*).

```
public void onBackPressed() {
        //super.onBackPressed();

        //to ask whether user wants to exit the game or not
        MessagesManager.getSimplyAlertDialog(getString(R.string.questionAboutEndingText),
                getString(R.string.exitMenu), getString(R.string.yesText),
                getString(R.string.cancellingText), this,
                new DialogInterface.OnClickListener() {

                    public static final int BUTTON_POSITIVE = -1;
                    public static final int BUTTON_NEGATIVE = -2;

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case BUTTON_POSITIVE:
                                Intent intent = new Intent(BGameActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();
    }
```
#### Wibracje

Implementacja wibracji według zadanego wzoru. Z wykorzystaniem klasy *Vibrator*.

```
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pat = {250, 100, 250, 100, 250, 100};
            vib.vibrate(pat, -1);
```

## Autor

 **Magdalena Olchawa** - *Studentka Informatyki, PWr, I rok* 
