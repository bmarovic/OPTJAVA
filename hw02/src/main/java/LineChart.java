import Jama.Matrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LineChart extends JFrame {

    public LineChart(ArrayList<Matrix> data,String location) {

        JPanel chartPanel = createChartPanel(data);
        add(chartPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(getPreferredSize());
        setVisible(true);
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        printAll(g);
        g.dispose();
        try {
            ImageIO.write(image, "png", new File(location));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private JPanel createChartPanel(ArrayList<Matrix> data) {
        String chartTitle = "Trajectory";
        String xAxisLabel = "X1";
        String yAxisLabel = "X2";

        XYDataset dataset = createDataset(data);

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset);

        return new ChartPanel(chart);
    }

    private XYDataset createDataset(ArrayList<Matrix> data) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("X1-X2");
        for (int i = 0; i < data.size(); i++) {
            series1.add(data.get(i).get(0,0), data.get(i).get(1,0));
        }
        dataset.addSeries(series1);

        return dataset;
    }
}
