public class p01 {

    public static void main(String[] args) {

        //stworzenie tablicy
        int n = 23;
        int[][] room = new int[n][n];
        int[][] room2 = new int[n][n];
        int[][] room3 = new int [n][n];

        //ustawienie myszy (2)
        int mouseX = (int)(Math.random()*room.length);
        int mouseY = (int)(Math.random()*room.length);
        room[mouseX][mouseY] = 2;

        System.out.println("Pozycja startowa: " + mouseX + ", " + mouseY);

        //wypelnienie przynajmniej 10% powierzchni serami (1)
        //n=23;  n*n=529;  10%n = 53
        int cheese = n + (int)(Math.random()*((n*n) - n));
        System.out.println("Liczba serów: " + cheese);
        int counter = 0;

        while(counter<cheese) {
            int x = (int)(Math.random()*room.length);
            int y = (int)(Math.random()*room.length);
            if(room[x][y] == 0) {
                room[x][y] = 1;
                counter++;
            }
        }

        //skopiowanie zawartosci 'pokoju' do dwoch kolejnych

        for(int i = 0; i < room.length; i++) {
            for(int j = 0; j < room[i].length; j++) {
                room2[i][j] = room[i][j];
                room3[i][j] = room[i][j];
            }
        }



        System.out.println();
        printRoom(room);

        //strategie
        strategia01(mouseX, mouseY, room, cheese);

        strategia02(mouseX, mouseY, room2, cheese);

        strategia03(mouseX, mouseY, room3, cheese);

    }

    //drukuje zawartosc pokoju
    public static void printRoom(int[][] room){
        for(int i = 0; i < room.length; i++) {
            for(int j = 0; j < room[i].length; j++) {
                System.out.print(room[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    //strategia 1 - idzie na slepo
    public static void strategia01(int x, int y, int[][] room, int cheese) {
        System.out.println("*****STRATEGIA 1*****");

        int counter = 0;

        //mysz idzie lewego górnego rogu
        boolean cornerY = false;
        boolean cornerX = false;
        int roomLength = room.length - 1;
        while (!cornerX) {
            if (y == 0) {
                cornerX = true;
                continue;
            } else {
                room[x][y] = 3;
                y--;
            }
            if (room[x][y] == 1) {
                cheese--;
            }
            room[x][y] = 2;
            counter++;
            printRoom(room);
        }

        while (!cornerY) {
            if (x == 0) {
                cornerY = true;
                continue;
            } else {
                room[x][y] = 3;
                x--;
            }
            if (room[x][y] == 1) {
                cheese--;
            }
            room[x][y] = 2;
            counter++;
            printRoom(room);
        }

        //mysz porusza sie po pokoju "od sciany do sciany"
        int k = 0;
        for (int i = 0; i < room.length; i++) {
            if (i % 2 == 0) {
                if (cheese > 0) {

                    for (int j = 0; j < room[i].length; j++) {
                        if (room[i][j] == 1) {
                            cheese--;
                        }
                        room[i][j] = 2;
                        if (j > 0) {
                            room[i][j - 1] = 3;
                        }
                        printRoom(room);
                        counter++;
                        k = j;
                        if (cheese <= 0) {
                            break;
                        }
                    }
                }
            } else {
                if (cheese > 0) {
                    for (int j = room[i].length - 1; j >= 0; j--) {
                        if (room[i][j] == 1) {
                            cheese--;
                        }
                        room[i][j] = 2;
                        if (j < room[i].length - 1) {
                            room[i][j + 1] = 3;
                        }
                        printRoom(room);
                        counter++;
                        k = j;
                        if (cheese <= 0) {
                            break;
                        }
                    }
                }
            }
            room[i][k] = 3;
        }

        //podsumowanie
        System.out.println("Zakonczono w " + counter + " ruchach.");
        System.out.println();
    }

    //szuka najblizszego sera, zwraca indeks
    public static int[] findClosestCheese(int[][] room, int mouseX, int mouseY) {
        int[] value = {0, 0};
        double distance = Double.MAX_VALUE;
        for(int i = 0; i < room.length; i++) {
            for(int j = 0; j < room[i].length; j++) {
                if(room[i][j] != 1) {
                    continue;
                }
                double deltaX = Math.abs(mouseX - i);
                double deltaY = Math.abs(mouseY - j);
                double cheeseDistance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
                if(cheeseDistance < distance) {
                    distance = cheeseDistance;
                    value[0] = i;
                    value[1] = j;
                }
            }
        }
        return value;
    }

    //strategia 2 - czuje najblizszy ser
    public static void strategia02(int x, int y, int[][] room, int cheese){
        System.out.println("*****STRATEGIA 2*****");
        int counter = 0;

        while(cheese>0) {
            int[] closestCheese = findClosestCheese(room, x, y);
            while(x!= closestCheese[0] || y != closestCheese[1]) {
                byte osX = 0;
                byte osY = 0;

                //ruch gora/dol
                if(closestCheese[0] < x) {
                    osX = -1;
                } else if (closestCheese[0] > x) {
                    osX = 1;
                }

                //ruch lewo/prawo
                if (closestCheese[1] < y) {
                    osY = -1;
                } else if (closestCheese[1] > y) {
                    osY = 1;
                }

                room[x][y] = 3;
                x = x+osX;
                y = y+osY;
                room[x][y] = 2;
                printRoom(room);
                counter++;
            }
            cheese--;

        }
        System.out.println("Zakonczono w " + counter + " ruchach.");
        System.out.println();
    }

    //zwraca indeksy wszystkich pozycji sera
    public static int[][] findAllCheese(int[][] room, int cheese) {

        int[][] value = new int[cheese][2];
        int counter = 0;

        for(int i = 0; i < room.length; i++) {
            if(i % 2 == 0) {
                for(int j = 0; j < room[i].length; j++) {
                    if(room[i][j] == 1) {
                        value[counter][0] = i;
                        value[counter][1] = j;
                        counter++;

                    }

                }
            } else {
                for(int j = room[i].length - 1; j >= 0 ; j--) {
                    if(room[i][j] == 1) {
                        value[counter][0] = i;
                        value[counter][1] = j;
                        counter++;
                    }
                }
            }

        }

        return value;
    }

    //strategia 3 - wie gdzie jest ser
    public static void strategia03(int x, int y, int[][] room, int cheese) {
        System.out.println("*****STRATEGIA 3*****");

        int[][] allCheese = findAllCheese(room, cheese);
        int counter = 0;

        for(int i = 0; i < allCheese.length; i++) {
            if (allCheese[i][0] == -1 && allCheese[i][1] == -1) {
                continue;
            }
            while(x!= allCheese[i][0] || y != allCheese[i][1]) {
                byte osX = 0;
                byte osY = 0;

                //ruch gora/dol
                if(allCheese[i][0] < x) {
                    osX = -1;
                } else if (allCheese[i][0] > x) {
                    osX = 1;
                }

                //ruch lewo/prawo
                if (allCheese[i][1] < y) {
                    osY = -1;
                } else if (allCheese[i][1] > y) {
                    osY = 1;
                }

                room[x][y] = 3;
                x = x+osX;
                y = y+osY;

                //jezeli mysz po drodze zebrala ser, to "usuwam" go z tablicy
                if(room[x][y] == 1) {
                    for(int j = i + 1; j < allCheese.length; j++) {
                        if(x == allCheese[j][0] && y == allCheese[j][1]) {
                            allCheese[j][0] = -1;
                            allCheese[j][1] = -1;
                        }
                    }
                }

                room[x][y] = 2;
                printRoom(room);
                counter++;
            }
        }

        System.out.println("Zakonczono w " + counter + " ruchach.");
    }



}
