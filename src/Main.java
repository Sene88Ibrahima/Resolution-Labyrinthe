import algorithms.BFSSolver;
import algorithms.DFSSolver;
import algorithms.MazeSolver;
import model.Cell;
import model.Maze;
import utils.MazeLoader;
import ui.MazeGUI;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n=== Résolution de Labyrinthe ===");
        System.out.println("1. Mode Console");
        System.out.println("2. Mode Graphique");
        System.out.print("Choix : ");

        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        if (modeChoice == 2) {
            // Mode graphique
            javax.swing.SwingUtilities.invokeLater(() -> {
                MazeGUI gui = new MazeGUI();
                gui.setVisible(true);
            });
        } else {
            // Mode console
            runConsoleMode();
        }
    }

    private static void runConsoleMode() {
        while (true) {
            System.out.println("\n=== Mode Console ===");
            System.out.println("1. Charger un labyrinthe depuis un fichier");
            System.out.println("2. Générer un labyrinthe aléatoire");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            if (choice == 3) {
                break;
            }

            try {
                Maze maze = null;
                if (choice == 1) {
                    System.out.print("Nom du fichier : ");
                    String filename = scanner.nextLine();
                    maze = MazeLoader.loadFromFile(filename);
                } else if (choice == 2) {
                    System.out.print("Nombre de lignes (min 3) : ");
                    int rows = scanner.nextInt();
                    System.out.print("Nombre de colonnes (min 3) : ");
                    int cols = scanner.nextInt();
                    System.out.print("Densité des murs (entre 0 et 1) : ");
                    double density = scanner.nextDouble();
                    maze = MazeLoader.generateRandom(rows, cols, density);
                }

                if (maze != null) {
                    System.out.println("\nLabyrinthe initial :");
                    System.out.println(maze);

                    MazeSolver[] solvers = {new DFSSolver(), new BFSSolver()};
                    for (MazeSolver solver : solvers) {
                        System.out.println("\nRésolution avec " + solver.getName());
                        maze.resetVisited();
                        
                        List<Cell> solution = solver.solve(maze);
                        
                        if (!solution.isEmpty()) {
                            // Marquer le chemin solution
                            for (Cell cell : solution) {
                                if (cell != maze.getStart() && cell != maze.getEnd()) {
                                    cell.setType(Cell.CellType.SOLUTION);
                                }
                            }
                            
                            System.out.println("Solution trouvée !");
                            System.out.println("Nombre d'étapes : " + solver.getStepCount());
                            System.out.println("Temps d'exécution : " + solver.getExecutionTime() + "ms");
                            System.out.println(maze);
                            
                            // Restaurer le chemin
                            for (Cell cell : solution) {
                                if (cell != maze.getStart() && cell != maze.getEnd()) {
                                    cell.setType(Cell.CellType.PATH);
                                }
                            }
                        } else {
                            System.out.println("Aucune solution trouvée !");
                        }
                    }

                    System.out.print("\nVoulez-vous sauvegarder ce labyrinthe ? (o/n) : ");
                    if (scanner.next().toLowerCase().startsWith("o")) {
                        System.out.print("Nom du fichier : ");
                        scanner.nextLine(); // Consommer le retour à la ligne
                        String filename = scanner.nextLine();
                        MazeLoader.saveToFile(maze, filename);
                        System.out.println("Labyrinthe sauvegardé !");
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la manipulation du fichier : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
        
        System.out.println("Au revoir !");
        scanner.close();
    }
} 