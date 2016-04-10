/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouserun.mouse;

import static java.lang.Math.abs;
import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author dcasquel
 */
public class RatonA extends Mouse {

    MDinamica1 mapa; //Mapa que almacena el nº de casillas visitadas 
    int ultimoMovimiento;
    ArrayList<Grid> cerrados;
    MGrid1 casillas; //Casillas por las que vamos pasando
    ArrayList<Integer> deshacer;
    boolean bifurcacion;
    int retroceso;
    ArrayList<Integer> aux;
    PriorityQueue<Grid> abiertos;

    public RatonA() {
        super("A*");
        ultimoMovimiento = 0;
        mapa = new MDinamica1(1);
        casillas = new MGrid1(1);
        cerrados = new ArrayList();
        abiertos = new PriorityQueue(80, new Comparadora());
        deshacer = new ArrayList<Integer>();
        bifurcacion = false;
        aux = new ArrayList<Integer>();
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        if (mapa.at(cheese.getX(), cheese.getY()) != 0) {
            if (abiertos.size() == 0) {
                abiertos.add(currentGrid);
            }
            System.out.printf("%s %s %s \n",abiertos.size(),cerrados.size(),deshacer.size());
            cerrados.add(currentGrid);
            abiertos.clear();
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
            if (abiertos.size() > 1 && bifurcacion == false) {
                bifurcacion = true;
            }
            if(deshacer.size() == 0){
                bifurcacion = false;
            }
            if (abiertos.size() == 0 && cerrados.size() != 0 && deshacer.size() != 0) {
               int aux = deshacer.get(deshacer.size()-1);
                deshacer.remove(deshacer.size() -1);
                return aux;
            }
            if (abiertos.peek() == casillas.at(currentGrid.getX(), currentGrid.getY() + 1)) {
                if (bifurcacion == true) {
                    deshacer.add(2);
                }
                    return 1;
                
            }

            if (abiertos.peek() == casillas.at(currentGrid.getX(), currentGrid.getY() - 1)) {
                if (bifurcacion == true) {
                    deshacer.add(1);
                }
                    return 2;
                
            }
            if (abiertos.peek() == casillas.at(currentGrid.getX() + 1, currentGrid.getY())) {
                if (bifurcacion == true) {
                    deshacer.add(3);
                }
                    return 4;
                
            }
            if (abiertos.peek() == casillas.at(currentGrid.getX() - 1, currentGrid.getY())) {
                if (bifurcacion == true) {
                    deshacer.add(4);
                }
                    return 3;
                
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
        return 5;
    }

    @Override
    public void newCheese() {
        cerrados.clear();
        deshacer.clear();
    }

    @Override
    public void respawned() {
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

class MDinamica1 {

    private int TAM;
    private int matriz[][];

    public MDinamica1(int tamIni) {
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

class MGrid1 {

    private int TAM;
    private Grid matriz[][];

    public MGrid1(int tamIni) {
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

class Comparadora implements Comparator<Grid> {

    @Override
    public int compare(Grid currentGrid, Grid cheese) {
        return abs(currentGrid.getX() - cheese.getX()) + abs(currentGrid.getY() - cheese.getY());

    }

}
