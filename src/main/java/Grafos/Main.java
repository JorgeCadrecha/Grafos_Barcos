package Grafo;

import java.util.*;

class Arista {
    int destino;
    int distancia;

    public Arista(int destino, int distancia) {
        this.destino = destino;
        this.distancia = distancia;
    }
}

class Grafo {
    private int vertices;
    private LinkedList<Arista>[] adyacetes;

    public Grafo(int vertices) {
        this.vertices = vertices;
        adyacetes = new LinkedList[vertices];

        for (int i = 0; i < vertices; i++) {
            adyacetes[i] = new LinkedList<>();
        }
    }

    public void agregarArista(int origen, int destino, int distancia) {
        adyacetes[origen].add(new Arista(destino, distancia));
        adyacetes[destino].add(new Arista(origen, distancia));
    }

    public void recorrerGrafo(int inicio) {
        boolean[] visitado = new boolean[vertices];
        recorrerGrafoEnProfundidad(inicio, visitado);
    }

    private void recorrerGrafoEnProfundidad(int vertice, boolean[] visitado) {
        visitado[vertice] = true;
        System.out.print(vertice + " ");

        for (Arista arista : adyacetes[vertice]) {
            if (!visitado[arista.destino]) {
                recorrerGrafoEnProfundidad(arista.destino, visitado);
            }
        }
    }

    public void caminoMasCorto(int origen, int destino) {
        int[] distancias = new int[vertices];
        Arrays.fill(distancias, Integer.MAX_VALUE);
        distancias[origen] = 0;

        /** Le pregunté a ChatGPT que me ayudara a sacar el método y me recomendó usar PriorityQueue, así como también
         * me recomendó usar Comparator.comparingInt. Y los métodos de offer y poll, los cuales no conocía.
         */
        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingInt(arista -> arista.distancia));
        pq.offer(new Arista(origen, 0));

        while (!pq.isEmpty()) {
            Arista actual = pq.poll();
            for (Arista arista : adyacetes[actual.destino]) {
                int nuevaDistancia = actual.distancia + arista.distancia;
                if (nuevaDistancia < distancias[arista.destino]) {
                    distancias[arista.destino] = nuevaDistancia;
                    pq.offer(new Arista(arista.destino, nuevaDistancia));
                }
            }
        }

        System.out.println("La distancia más corta desde " + origen + " hasta " + destino + " es: " + distancias[destino]);
    }

    public void eliminarVerticeConMasAristas() {
        int maxAristas = -1;
        int verticeAEliminar = -1;

        for (int i = 0; i < vertices; i++) {
            int cantidadAristas = adyacetes[i].size();
            if (cantidadAristas > maxAristas) {
                maxAristas = cantidadAristas;
                verticeAEliminar = i;
            }
        }

        if (verticeAEliminar != -1) {
            adyacetes[verticeAEliminar] = new LinkedList<>();
            for (int i = 0; i < vertices; i++) {
                int finalVerticeAEliminar = verticeAEliminar;
                adyacetes[i].removeIf(arista -> arista.destino == finalVerticeAEliminar);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(5);

        grafo.agregarArista(0, 1, 10);
        grafo.agregarArista(1, 2, 20);
        grafo.agregarArista(2, 3, 30);
        grafo.agregarArista(3, 4, 40);
        grafo.agregarArista(4, 0, 50);
        grafo.agregarArista(1, 4, 15);

        grafo.recorrerGrafo(0);
        System.out.println();

        grafo.caminoMasCorto(0, 3);
        grafo.eliminarVerticeConMasAristas();
        grafo.recorrerGrafo(0);
        System.out.println();
        grafo.caminoMasCorto(0, 3);

    }
}