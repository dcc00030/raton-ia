/*
package mouserun.mouse;

import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class M16A10 extends Mouse {

    MDinamica mapa; //Mapa que almacena el nº de casillas visitadas 
    int ultimoMovimiento;
    ArrayList<Grid> cerrados, abiertos;
    MGrid casillas; //Casillas por las que vamos pasando
    ArrayList<Integer> deshacer;
    boolean retrocediendo;
    int retroceso;
    ArrayList<Integer> aux;

    public M16A10() {
        super("Spd Gonzalez");
        ultimoMovimiento = 0;
        mapa = new MDinamica(1);
        casillas = new MGrid(1);
        cerrados = new ArrayList();
        abiertos = new ArrayList();
        deshacer = new ArrayList<Integer>();
        retrocediendo = false;
        aux = new ArrayList<Integer>();
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese) {
//        System.out.printf("%s\n", retrocediendo);

        if (mapa.at(cheese.getX(), cheese.getY()) != 0) {
            if (abiertos.size() == 0) {
//                System.out.printf("Lista de abierto alla vamos!\n ");
                abiertos.add(currentGrid);
            }
            if (currentGrid.getX() == cheese.getX() && currentGrid.getY() == cheese.getY()) {
//                System.out.printf("Encontre el queso!, nos limpiamos \n ");
                abiertos.clear();
                retrocediendo = false;
                cerrados.clear();
                //deshacer.clear();
            } else if (retrocediendo && currentGrid == abiertos.get(0)) {
//                System.out.printf("Dejamos de retroceder porque estamos en la casilla objetivo\n ");
                retrocediendo = false;
                //deshacer.clear();
                deshacer.addAll(aux);

                aux.clear();
                retroceso = deshacer.size() - 1;
            } else {
                if (retrocediendo) {
//                    System.out.printf("Retrocedemos \n ");
//                    System.out.printf("%s %s \n", deshacer.size(), retroceso);
                    if (retroceso == deshacer.size()) {
                        retroceso--;
                    }
                    int mov = deshacer.get(retroceso--);
                    if (mov == 1) {
                        aux.add(2);
                    }
                    if (mov == 2) {
                        aux.add(1);
                    }
                    if (mov == 3) {
                        aux.add(4);
                    }
                    if (mov == 4) {
                        aux.add(3);
                    }
                    return mov;
                } else {
//                    System.out.printf("Expandimos casilla \n ");
                    if (currentGrid.canGoUp() && mapa.at(currentGrid.getX(), currentGrid.getY() + 1) != 0) {
                        if (false == cerrados.contains(casillas.at(currentGrid.getX(), currentGrid.getY() + 1))) {
                            abiertos.add(casillas.at(currentGrid.getX(), currentGrid.getY() + 1));
                        }
                    }
                    if (currentGrid.canGoDown() && mapa.at(currentGrid.getX(), currentGrid.getY() - 1) != 0) {
                        if (false == cerrados.contains(casillas.at(currentGrid.getX(), currentGrid.getY() - 1))) {
                            abiertos.add(casillas.at(currentGrid.getX(), currentGrid.getY() - 1));
                        }
                    }
                    if (currentGrid.canGoLeft() && mapa.at(currentGrid.getX() - 1, currentGrid.getY()) != 0) {
                        if (false == cerrados.contains(casillas.at(currentGrid.getX() - 1, currentGrid.getY()))) {
                            abiertos.add(casillas.at(currentGrid.getX() - 1, currentGrid.getY()));
                        }
                    }
                    if (currentGrid.canGoRight() && mapa.at(currentGrid.getX() + 1, currentGrid.getY()) != 0) {
                        if (false == cerrados.contains(casillas.at(currentGrid.getX() + 1, currentGrid.getY()))) {
                            abiertos.add(casillas.at(currentGrid.getX() + 1, currentGrid.getY()));
                        }
                    }
//                    System.out.printf("la casilla actual se manda a cerrados \n ");
                    cerrados.add(currentGrid);
//                    System.out.printf("%s \n ", abiertos.size());
                    if (!abiertos.isEmpty()) {
                        abiertos.remove(0);
                    }

                    if (currentGrid.canGoUp() && casillas.at(currentGrid.getX(), currentGrid.getY() + 1) == abiertos.get(0)) {
//                        System.out.printf("movimiento abajo\n ");
                        deshacer.add(2);
                        retroceso++;
                        return 1;
                    } else if (currentGrid.canGoDown() && casillas.at(currentGrid.getX(), currentGrid.getY() - 1) == abiertos.get(0)) {
                        deshacer.add(1);
                        retroceso++;
                        return 2;
                    } else if (currentGrid.canGoLeft() && casillas.at(currentGrid.getX() - 1, currentGrid.getY()) == abiertos.get(0)) {
                        deshacer.add(4);
                        retroceso++;
                        return 3;
                    } else if (currentGrid.canGoRight() && casillas.at(currentGrid.getX() + 1, currentGrid.getY()) == abiertos.get(0)) {
                        deshacer.add(3);
                        retroceso++;
                        return 4;
                    } else {
//                        System.out.printf("La casilla no esta contigua, retrocedemos \n ");
                        retrocediendo = true;
                    }
                }
            }

        } else {

            casillas.add(currentGrid);

            if (mapa.at(currentGrid.getX(), currentGrid.getY()) == 0) {
                incExploredGrids();
            }
            mapa.inc(currentGrid.getX(), currentGrid.getY());

            int[] vPosibles = new int[10];
            for (int i = 0; i < 10; i++) {
                vPosibles[i] = 0;
            }
            int movPosibles = analizarMovimiento(currentGrid, vPosibles);
            int m1, m2, m3;

            switch (movPosibles) {
                case 1:
                    ultimoMovimiento = vPosibles[0];

                    if (vPosibles[0] == 1) {
                        deshacer.add(2);
                    }
                    if (vPosibles[0] == 2) {
                        deshacer.add(1);
                    }
                    if (vPosibles[0] == 3) {
                        deshacer.add(4);
                    }
                    if (vPosibles[0] == 4) {
                        deshacer.add(3);
                    }
                    retroceso++;
                    return vPosibles[0];

                case 2:

                    m1 = obtenerMovimiento(vPosibles[0], currentGrid);
                    m2 = obtenerMovimiento(vPosibles[1], currentGrid);
                    if (m1 < m2) {
                        ultimoMovimiento = vPosibles[0];
                        if (vPosibles[0] == 1) {
                            deshacer.add(2);
                        }
                        if (vPosibles[0] == 2) {
                            deshacer.add(1);
                        }
                        if (vPosibles[0] == 3) {
                            deshacer.add(4);
                        }
                        if (vPosibles[0] == 4) {
                            deshacer.add(3);
                        }
                        retroceso++;
                        return vPosibles[0];
                    } else if (m1 > m2) {
                        if (vPosibles[1] == 1) {
                            deshacer.add(2);
                        }
                        if (vPosibles[1] == 2) {
                            deshacer.add(1);
                        }
                        if (vPosibles[1] == 3) {
                            deshacer.add(4);
                        }
                        if (vPosibles[1] == 4) {
                            deshacer.add(3);
                        }
                        ultimoMovimiento = vPosibles[1];
                        retroceso++;
                        return vPosibles[1];
                    } else if (ultimoMovimiento == vPosibles[0]) {

                        ultimoMovimiento = vPosibles[0];
                        if (vPosibles[0] == 1) {
                            deshacer.add(2);
                        }
                        if (vPosibles[0] == 2) {
                            deshacer.add(1);
                        }
                        if (vPosibles[0] == 3) {
                            deshacer.add(4);
                        }
                        if (vPosibles[0] == 4) {
                            deshacer.add(3);
                        }
                        retroceso++;
                        return vPosibles[0];
                    } else {
                        ultimoMovimiento = vPosibles[1];
                        if (vPosibles[1] == 1) {
                            deshacer.add(2);
                        }
                        if (vPosibles[1] == 2) {
                            deshacer.add(1);
                        }
                        if (vPosibles[1] == 3) {
                            deshacer.add(4);
                        }
                        if (vPosibles[1] == 4) {
                            deshacer.add(3);
                        }
                        retroceso++;
                        return vPosibles[1];
                    }

                default:

                    m1 = obtenerMovimiento(vPosibles[0], currentGrid);
                    m2 = obtenerMovimiento(vPosibles[1], currentGrid);
                    m3 = obtenerMovimiento(vPosibles[2], currentGrid);
                    int menor = m1;
                    int movimiento = vPosibles[0];
                    if (m2 < menor) {
                        menor = m2;
                        movimiento = vPosibles[1];
                        if (m3 < menor) {
                            menor = m3;
                            movimiento = vPosibles[2];
                        } else if (m3 == menor) {
                            if (ultimoMovimiento != vPosibles[2]) {
                                menor = m3;
                                movimiento = vPosibles[2];
                            }
                        }
                    } else if (m2 == menor) {
                        if (ultimoMovimiento != vPosibles[1]) {
                            menor = m2;
                            movimiento = vPosibles[1];
                            if (m3 < menor) {
                                menor = m3;
                                movimiento = vPosibles[2];
                            } else if (m3 == menor) {
                                if (ultimoMovimiento != vPosibles[2]) {
                                    menor = m3;
                                    movimiento = vPosibles[2];
                                }
                            }
                        }
                    }
                    if (movimiento == 1) {
                        deshacer.add(2);
                    }
                    if (movimiento == 2) {
                        deshacer.add(1);
                    }
                    if (movimiento == 3) {
                        deshacer.add(4);
                    }
                    if (movimiento == 4) {
                        deshacer.add(3);
                    }
                    retroceso++;
                    return movimiento;

            }

        }
        return -1;
    }

    @Override
    public void newCheese() {
        abiertos.clear();
        retrocediendo = false;
        cerrados.clear();
        //deshacer.clear();
    }

    @Override
    public void respawned() {
        abiertos.clear();
        retrocediendo = false;
        cerrados.clear();
    }

    private int analizarMovimiento(Grid currentGrid, int[] movPosibles) {

        int nPosibles = 0;
        if (currentGrid.canGoUp()) {
            movPosibles[nPosibles] = 1;
            nPosibles++;
        }
        if (currentGrid.canGoDown()) {
            movPosibles[nPosibles] = 2;
            nPosibles++;
        }
        if (currentGrid.canGoLeft()) {
            movPosibles[nPosibles] = 3;
            nPosibles++;
        }
        if (currentGrid.canGoRight()) {
            movPosibles[nPosibles] = 4;
            nPosibles++;
        }
        if (nPosibles == 4) {
            for (int i = 0; i < 4; i++) {
                if (movPosibles[i] == ultimoMovimiento) {
                    for (int j = i; j < 3; j++) {
                        movPosibles[j] = movPosibles[j + 1];
                    }
                }
            }
        }
        return nPosibles;
    }

    private int obtenerMovimiento(int a, Grid currentGrid) {
        switch (a) {
            case 1:
                return mapa.at(currentGrid.getX(), currentGrid.getY() + 1);

            case 2:
                return mapa.at(currentGrid.getX(), currentGrid.getY() - 1);

            case 3:
                return mapa.at(currentGrid.getX() - 1, currentGrid.getY());

            case 4:
                return mapa.at(currentGrid.getX() + 1, currentGrid.getY());

        }
        return 0;
    }

}

class MDinamica {

    private int TAM;
    private int matriz[][];

    public MDinamica(int tamIni) {
        if (tamIni < 0) {
            tamIni *= (-1);
        }
        TAM = tamIni;
        matriz = new int[TAM][TAM];
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                matriz[i][j] = 0;
            }
        }
    }

    private void aumentarMatriz() {
        int nuevaMatriz[][] = new int[TAM * 2][TAM * 2];
        for (int i = 0; i < TAM * 2; i++) {
            for (int j = 0; i < TAM * 2; i++) {
                nuevaMatriz[i][j] = 0;
            }
        }
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                nuevaMatriz[i][j] = matriz[i][j];
            }
        }

        TAM *= 2;
        matriz = nuevaMatriz;
    }

    public int at(int x, int y) {
        while (x >= TAM || y >= TAM) {
            aumentarMatriz();
        }
        return matriz[x][y];
    }

    public void inc(int x, int y) {
        while (x >= TAM || y >= TAM) {
            aumentarMatriz();
        }
        matriz[x][y]++;
    }
}

class MGrid {

    private int TAM;
    private Grid matriz[][];

    public MGrid(int tamIni) {
        if (tamIni < 0) {
            tamIni *= (-1);
        }
        TAM = tamIni;
        matriz = new Grid[TAM][TAM];
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                matriz[i][j] = new Grid(-1, -1);
            }
        }
    }

    private void aumentarMatriz() {
        Grid nuevaMatriz[][] = new Grid[TAM * 2][TAM * 2];
        for (int i = 0; i < TAM * 2; i++) {
            for (int j = 0; i < TAM * 2; i++) {
                nuevaMatriz[i][j] = new Grid(-1, -1);
            }
        }
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                nuevaMatriz[i][j] = matriz[i][j];
            }
        }

        TAM *= 2;
        matriz = nuevaMatriz;
    }

    public Grid at(int x, int y) {
        while (x >= TAM || y >= TAM) {
            aumentarMatriz();
        }
        return matriz[x][y];
    }

    public void add(Grid grid) {
        while (grid.getX() >= TAM || grid.getY() >= TAM) {
            aumentarMatriz();
        }
        matriz[grid.getX()][grid.getY()] = grid;

    }

}
*/