package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.nodes.actions.ActionNode;
import hr.fer.zemris.optjava.dz12.nodes.actions.Move;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class WorldFrame extends JFrame {

    public World world;
    private JPanel[][] cells;
    private List<ActionNode> route;
    private int index = 0;

    public WorldFrame(World world, List<ActionNode> route){
        this.world = world.duplicate();
        this.route = route;
        cells = new JPanel[world.getWidth()][world.getHeight()];
        JPanel boardPanel = new JPanel();
        add(boardPanel, BorderLayout.CENTER);
        boardPanel.setLayout(new GridLayout(world.getWidth(), world.getHeight(), 1, 1));
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(20, 20));
                cells[y][x] = cell;
                if (!world.getAtCoordinate(y, x)) {
                    cell.setBackground(Color.GRAY);
                } else cell.setBackground(Color.BLACK);
                boardPanel.add(cell);
            }
        }

        cells[world.getXcoord()][world.getYcoord()].setBackground(Color.BLUE);
        JButton buttonStart = new JButton("Start");
        buttonPanel.add(buttonStart);

        buttonStart.addActionListener(e -> {
            Thread thread = new Thread(() -> {
                buttonStart.setEnabled(false);
                while (action()) {
                    action();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            thread.start();
        });
    }

    private boolean action() {
        if (index >= route.size()) return false;
        if (route.get(index) instanceof Move) {
            cells[world.getYcoord()][world.getXcoord()].setBackground(Color.GRAY);
            route.get(index).executeOrder(world);
            cells[world.getYcoord()][world.getXcoord()].setBackground(Color.BLUE);
        } else {
            route.get(index).executeOrder(world);
        }
        index++;
        return true;
    }

}
