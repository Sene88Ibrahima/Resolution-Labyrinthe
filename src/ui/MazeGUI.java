package ui;

import model.Cell;
import model.Maze;
import algorithms.MazeSolver;
import algorithms.DFSSolver;
import algorithms.BFSSolver;
import utils.MazeLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MazeGUI extends JFrame {
    private Maze maze;
    private MazePanel mazePanelDFS;
    private MazePanel mazePanelBFS;
    private JTextArea statsAreaDFS;
    private JTextArea statsAreaBFS;
    private JPanel comparisonPanel;
    private JSlider speedSlider;
    private JButton pauseButton;
    private JButton stepButton;
    private JButton helpButton;
    private volatile boolean isPaused = false;
    private volatile boolean stepMode = false;
    private volatile int animationSpeed = 50;
    private Runtime runtime = Runtime.getRuntime();
    private long initialMemory;
    private static final int CELL_SIZE = 30;
    private static final Color WALL_COLOR = new Color(44, 62, 80);
    private static final Color PATH_COLOR = new Color(236, 240, 241);
    private static final Color START_COLOR = new Color(46, 204, 113);
    private static final Color END_COLOR = new Color(231, 76, 60);
    private static final Color SOLUTION_COLOR = new Color(241, 196, 15);
    private static final Color SOLUTION_BORDER = new Color(243, 156, 18);
    private static final Color VISITED_COLOR = new Color(52, 152, 219, 100);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color PANEL_BACKGROUND = new Color(189, 195, 199);
    private volatile boolean nextStep = false;

    public MazeGUI() {
        setTitle("Comparaison des Algorithmes de Résolution de Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        initializeComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Panel principal avec une marge
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel des boutons avec style moderne
        JPanel buttonPanel = createStyledButtonPanel();
        
        // Panel de contrôles
        JPanel controlPanel = createControlPanel();
        
        // Panel central avec les deux labyrinthes
        JPanel mazesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mazesPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel gauche (DFS)
        JPanel leftPanel = createMazePanel("DFS (Depth-First Search)", true);
        
        // Panel droit (BFS)
        JPanel rightPanel = createMazePanel("BFS (Breadth-First Search)", false);
        
        mazesPanel.add(leftPanel);
        mazesPanel.add(rightPanel);

        // Panel de comparaison
        comparisonPanel = createComparisonPanel();

        // Assemblage final
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(mazesPanel, BorderLayout.CENTER);
        mainPanel.add(comparisonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createStyledButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton loadButton = createStyledButton("Charger un labyrinthe", e -> loadMazeFromFile());
        JButton generateButton = createStyledButton("Générer un labyrinthe", e -> generateRandomMaze());
        JButton solveButton = createStyledButton("Résoudre (DFS et BFS)", e -> solveMaze());
        helpButton = createStyledButton("Guide d'utilisation", e -> showHelp());

        buttonPanel.add(loadButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(helpButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(listener);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(BACKGROUND_COLOR);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Contrôles"));

        // Contrôle de vitesse
        JPanel speedPanel = new JPanel(new BorderLayout());
        speedPanel.setBackground(BACKGROUND_COLOR);
        speedPanel.setBorder(BorderFactory.createTitledBorder("Vitesse d'animation"));
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 200, 50);
        speedSlider.setInverted(true);
        speedSlider.addChangeListener(e -> animationSpeed = speedSlider.getValue());
        speedPanel.add(speedSlider);

        // Boutons de contrôle
        JPanel buttonControlPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonControlPanel.setBackground(BACKGROUND_COLOR);
        
        pauseButton = createStyledButton("Pause", e -> togglePause());
        pauseButton.setEnabled(false);
        
        stepButton = createStyledButton("Mode Pas à Pas", e -> {
            if (stepMode) {
                doNextStep();
            } else {
                toggleStepMode();
            }
        });
        stepButton.setEnabled(false);
        
        buttonControlPanel.add(pauseButton);
        buttonControlPanel.add(stepButton);

        controlPanel.add(speedPanel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(buttonControlPanel);

        return controlPanel;
    }

    private void togglePause() {
        isPaused = !isPaused;
        SwingUtilities.invokeLater(() -> {
            pauseButton.setText(isPaused ? "Reprendre" : "Pause");
            pauseButton.setEnabled(true);
        });
    }

    private void toggleStepMode() {
        stepMode = !stepMode;
        nextStep = false;
        SwingUtilities.invokeLater(() -> {
            stepButton.setText(stepMode ? "Étape suivante" : "Mode Pas à Pas");
            if (stepMode) {
                pauseButton.setEnabled(false);
                speedSlider.setEnabled(false);
                stepButton.setEnabled(true);
            } else {
                pauseButton.setEnabled(true);
                speedSlider.setEnabled(true);
            }
        });
    }

    private void doNextStep() {
        if (stepMode) {
            nextStep = true;
        }
    }

    private void showHelp() {
        String helpText = "Guide d'utilisation du solveur de labyrinthe\n\n" +
            "1. Chargement du labyrinthe\n" +
            "   - Cliquez sur 'Charger un labyrinthe' pour ouvrir un fichier\n" +
            "   - Ou utilisez 'Générer un labyrinthe' pour créer un labyrinthe aléatoire\n\n" +
            "2. Contrôles de résolution\n" +
            "   - Vitesse d'animation : Ajustez la vitesse de résolution\n" +
            "   - Pause/Reprendre : Interrompre/reprendre la résolution\n" +
            "   - Mode Pas à Pas : Avancer étape par étape\n\n" +
            "3. Comparaison des algorithmes\n" +
            "   - DFS (Depth-First Search) : Recherche en profondeur\n" +
            "   - BFS (Breadth-First Search) : Recherche en largeur\n\n" +
            "4. Statistiques\n" +
            "   - Nombre d'étapes\n" +
            "   - Temps d'exécution\n" +
            "   - Longueur du chemin\n" +
            "   - Utilisation mémoire\n\n";

        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(236, 240, 241));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JDialog dialog = new JDialog(this, "Guide d'utilisation", true);
        dialog.add(scrollPane);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createMazePanel(String title, boolean isDFS) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titre stylisé
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Panel du labyrinthe
        MazePanel mazePanel = new MazePanel();
        mazePanel.setPreferredSize(new Dimension(400, 400));
        if (isDFS) {
            mazePanelDFS = mazePanel;
            statsAreaDFS = createStatsArea();
            panel.add(statsAreaDFS, BorderLayout.SOUTH);
        } else {
            mazePanelBFS = mazePanel;
            statsAreaBFS = createStatsArea();
            panel.add(statsAreaBFS, BorderLayout.SOUTH);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(mazePanel, BorderLayout.CENTER);

        return panel;
    }

    private JTextArea createStatsArea() {
        JTextArea statsArea = new JTextArea(5, 30);
        statsArea.setEditable(false);
        statsArea.setBackground(new Color(236, 240, 241));
        statsArea.setFont(new Font("Monospace", Font.PLAIN, 12));
        statsArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return statsArea;
    }

    private JPanel createComparisonPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 0, 0),
            BorderFactory.createTitledBorder("Comparaison des Algorithmes")
        ));

        JTextArea comparisonText = new JTextArea(4, 50);
        comparisonText.setEditable(false);
        comparisonText.setBackground(new Color(236, 240, 241));
        comparisonText.setFont(new Font("Monospace", Font.PLAIN, 12));
        panel.add(new JScrollPane(comparisonText), BorderLayout.CENTER);

        return panel;
    }

    private void updateStats(JTextArea statsArea, MazeSolver solver, List<Cell> solution, long startTime) {
        long endTime = System.currentTimeMillis();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory() - initialMemory;
        
        StringBuilder stats = new StringBuilder();
        stats.append("Statistiques de résolution:\n");
        stats.append(String.format("Nombre d'étapes: %d\n", solver.getStepCount()));
        stats.append(String.format("Temps d'exécution: %d ms\n", solver.getExecutionTime()));
        stats.append(String.format("Longueur du chemin: %d\n", solution.size()));
        stats.append(String.format("Mémoire utilisée: %.2f MB\n", memoryUsed / (1024.0 * 1024.0)));
        statsArea.setText(stats.toString());
    }

    private void updateComparison(DFSSolver dfsSolver, BFSSolver bfsSolver, 
                                List<Cell> dfsSolution, List<Cell> bfsSolution) {
        JTextArea comparisonText = (JTextArea) ((JScrollPane) comparisonPanel.getComponent(0)).getViewport().getView();
        StringBuilder comparison = new StringBuilder();
        
        // Comparaison des performances
        comparison.append("Analyse comparative des algorithmes:\n");
        comparison.append(String.format("DFS vs BFS:\n"));
        comparison.append(String.format("- Étapes explorées: %d vs %d (Différence: %d)\n",
            dfsSolver.getStepCount(), bfsSolver.getStepCount(),
            Math.abs(dfsSolver.getStepCount() - bfsSolver.getStepCount())));
        comparison.append(String.format("- Temps d'exécution: %d ms vs %d ms (Différence: %d ms)\n",
            dfsSolver.getExecutionTime(), bfsSolver.getExecutionTime(),
            Math.abs(dfsSolver.getExecutionTime() - bfsSolver.getExecutionTime())));
        
        // Comparaison des chemins
        comparison.append("\nAnalyse des chemins trouvés:\n");
        comparison.append(String.format("- Longueur du chemin DFS: %d cellules\n", dfsSolution.size()));
        comparison.append(String.format("- Longueur du chemin BFS: %d cellules\n", bfsSolution.size()));
        
        // Déterminer quel algorithme a trouvé le chemin le plus court
        if (dfsSolution.size() < bfsSolution.size()) {
            comparison.append("→ DFS a trouvé le chemin le plus court!\n");
        } else if (bfsSolution.size() < dfsSolution.size()) {
            comparison.append("→ BFS a trouvé le chemin le plus court!\n");
        } else {
            comparison.append("→ Les deux algorithmes ont trouvé des chemins de même longueur\n");
        }
        
        // Calculer la différence de longueur
        int lengthDiff = Math.abs(dfsSolution.size() - bfsSolution.size());
        if (lengthDiff > 0) {
            comparison.append(String.format("- Différence de longueur: %d cellules\n", lengthDiff));
        }
        
        // Calculer le pourcentage d'exploration
        int totalCells = maze.getRows() * maze.getCols();
        double dfsExploration = (dfsSolver.getStepCount() * 100.0) / totalCells;
        double bfsExploration = (bfsSolver.getStepCount() * 100.0) / totalCells;
        comparison.append(String.format("\nPourcentage d'exploration:\n"));
        comparison.append(String.format("- DFS: %.1f%% des cellules explorées\n", dfsExploration));
        comparison.append(String.format("- BFS: %.1f%% des cellules explorées\n", bfsExploration));
        
        comparisonText.setText(comparison.toString());
    }

    private void loadMazeFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                maze = MazeLoader.loadFromFile(fileChooser.getSelectedFile().getPath());
                mazePanelDFS.setMaze(new Maze(maze));
                mazePanelBFS.setMaze(new Maze(maze));
                statsAreaDFS.setText("Labyrinthe chargé avec succès !\n");
                statsAreaBFS.setText("Labyrinthe chargé avec succès !\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
            }
        }
    }

    private void generateRandomMaze() {
        try {
            String rowsStr = JOptionPane.showInputDialog(this, "Nombre de lignes (min 3):", "10");
            String colsStr = JOptionPane.showInputDialog(this, "Nombre de colonnes (min 3):", "10");
            String densityStr = JOptionPane.showInputDialog(this, "Densité des murs (entre 0 et 1):", "0.3");

            int rows = Integer.parseInt(rowsStr);
            int cols = Integer.parseInt(colsStr);
            double density = Double.parseDouble(densityStr);

            maze = MazeLoader.generateRandom(rows, cols, density);
            mazePanelDFS.setMaze(new Maze(maze));
            mazePanelBFS.setMaze(new Maze(maze));
            statsAreaDFS.setText("Labyrinthe généré avec succès !\n");
            statsAreaBFS.setText("Labyrinthe généré avec succès !\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération : " + e.getMessage());
        }
    }

    private void solveMaze() {
        if (maze == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord charger ou générer un labyrinthe.");
            return;
        }

        statsAreaDFS.setText("");
        statsAreaBFS.setText("");
        isPaused = false;
        stepMode = false;
        SwingUtilities.invokeLater(() -> {
            pauseButton.setEnabled(true);
            stepButton.setEnabled(true);
            speedSlider.setEnabled(true);
            pauseButton.setText("Pause");
            stepButton.setText("Mode Pas à Pas");
        });

        // Initialiser la mémoire
        initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Désactiver le bouton pendant la résolution
        JButton solveButton = (JButton) ((JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(0)).getComponent(2);
        solveButton.setEnabled(false);

        // Créer un thread séparé pour la résolution et l'animation
        new Thread(() -> {
            // Résoudre avec DFS
            DFSSolver dfsSolver = new DFSSolver();
            Maze dfsMaze = mazePanelDFS.getMaze();
            dfsMaze.resetVisited();
            List<Cell> dfsSolution = dfsSolver.solve(dfsMaze);

            // Résoudre avec BFS
            BFSSolver bfsSolver = new BFSSolver();
            Maze bfsMaze = mazePanelBFS.getMaze();
            bfsMaze.resetVisited();
            List<Cell> bfsSolution = bfsSolver.solve(bfsMaze);

            // Afficher les résultats
            SwingUtilities.invokeLater(() -> {
                updateStats(statsAreaDFS, dfsSolver, dfsSolution, System.currentTimeMillis());
                updateStats(statsAreaBFS, bfsSolver, bfsSolution, System.currentTimeMillis());
            });

            // Animer les solutions simultanément
            int maxSteps = Math.max(dfsSolution.size(), bfsSolution.size());
            for (int i = 0; i < maxSteps; i++) {
                if (stepMode) {
                    SwingUtilities.invokeLater(() -> stepButton.setEnabled(true));
                    while (!nextStep && stepMode) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    nextStep = false;
                }
                
                while (isPaused) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                final int step = i;
                SwingUtilities.invokeLater(() -> {
                    if (step < dfsSolution.size()) {
                        Cell dfsCell = dfsSolution.get(step);
                        if (dfsCell != dfsMaze.getStart() && dfsCell != dfsMaze.getEnd()) {
                            dfsCell.setType(Cell.CellType.SOLUTION);
                        }
                        mazePanelDFS.repaint();
                    }
                    if (step < bfsSolution.size()) {
                        Cell bfsCell = bfsSolution.get(step);
                        if (bfsCell != bfsMaze.getStart() && bfsCell != bfsMaze.getEnd()) {
                            bfsCell.setType(Cell.CellType.SOLUTION);
                        }
                        mazePanelBFS.repaint();
                    }
                });

                if (!stepMode) {
                    try {
                        Thread.sleep(animationSpeed);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            // Réactiver le bouton après la résolution
            SwingUtilities.invokeLater(() -> {
                solveButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stepButton.setEnabled(false);
                speedSlider.setEnabled(true);
            });

            // Mettre à jour la comparaison
            updateComparison(dfsSolver, bfsSolver, dfsSolution, bfsSolution);
        }).start();
    }

    private class MazePanel extends JPanel {
        private Maze maze;
        private List<Cell> visitedCells;
        private List<Cell> solutionPath;

        public void setMaze(Maze maze) {
            this.maze = maze;
            this.visitedCells = null;
            this.solutionPath = null;
            repaint();
        }

        public Maze getMaze() {
            return maze;
        }

        public void setVisitedCells(List<Cell> visited) {
            this.visitedCells = visited;
            repaint();
        }

        public void setSolutionPath(List<Cell> solution) {
            this.solutionPath = solution;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (maze == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int cellWidth = width / maze.getCols();
            int cellHeight = height / maze.getRows();

            // Dessiner les cellules visitées
            if (visitedCells != null) {
                g2d.setColor(VISITED_COLOR);
                for (Cell cell : visitedCells) {
                    int x = cell.getCol() * cellWidth;
                    int y = cell.getRow() * cellHeight;
                    g2d.fillRect(x, y, cellWidth, cellHeight);
                }
            }

            // Dessiner le labyrinthe
            for (int i = 0; i < maze.getRows(); i++) {
                for (int j = 0; j < maze.getCols(); j++) {
                    Cell cell = maze.getCell(i, j);
                    int x = j * cellWidth;
                    int y = i * cellHeight;

                    switch (cell.getType()) {
                        case WALL:
                            g2d.setColor(WALL_COLOR);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case PATH:
                            g2d.setColor(PATH_COLOR);
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case START:
                            drawSpecialCell(g2d, x, y, cellWidth, cellHeight, START_COLOR, "E");
                            break;
                        case END:
                            drawSpecialCell(g2d, x, y, cellWidth, cellHeight, END_COLOR, "S");
                            break;
                        case SOLUTION:
                            drawSolutionCell(g2d, x, y, cellWidth, cellHeight);
                            break;
                    }
                }
            }

            // Dessiner la grille
            g2d.setColor(new Color(189, 195, 199));
            for (int i = 0; i <= maze.getRows(); i++) {
                g2d.drawLine(0, i * cellHeight, width, i * cellHeight);
            }
            for (int j = 0; j <= maze.getCols(); j++) {
                g2d.drawLine(j * cellWidth, 0, j * cellWidth, height);
            }
        }

        private void drawSpecialCell(Graphics2D g2d, int x, int y, int width, int height, 
                                   Color color, String text) {
            g2d.setColor(color);
            g2d.fillRect(x, y, width, height);
            
            // Cercle blanc
            g2d.setColor(Color.WHITE);
            int padding = width / 6;
            g2d.fillOval(x + padding, y + padding, width - 2*padding, height - 2*padding);
            
            // Texte
            g2d.setColor(color);
            g2d.setFont(new Font("Arial", Font.BOLD, Math.min(width, height) / 2));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x + (width - fm.stringWidth(text)) / 2;
            int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);
        }

        private void drawSolutionCell(Graphics2D g2d, int x, int y, int width, int height) {
            g2d.setColor(SOLUTION_COLOR);
            g2d.fillRect(x, y, width, height);
            
            // Effet de brillance
            g2d.setColor(new Color(255, 255, 255, 50));
            g2d.fillRect(x, y, width/2, height/2);
            
            // Bordure
            g2d.setColor(SOLUTION_BORDER);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x + 1, y + 1, width - 2, height - 2);
        }
    }
} 