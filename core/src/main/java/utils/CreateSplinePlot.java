package utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class CreateSplinePlot {

	public static void createPlot(String outputFileName, String title, String Xlabel, String Ylabel, XYSeries... series) {

		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries s : series) {
			dataset.addSeries(s);
		}

		NumberAxis xax = new NumberAxis(Xlabel);
		NumberAxis yax = new NumberAxis(Ylabel);
		XYSplineRenderer spline = new XYSplineRenderer();
		spline.setPrecision(10);
		// spline.setSeriesShapesVisible(0, true);
		// spline.setSeriesShapesVisible(1, true);

		XYPlot xyplot = new XYPlot(dataset, xax, yax, spline);

		// xyplot.setBackgroundPaint(Color.pink);
		xyplot.setDomainGridlinePaint(Color.WHITE);
		xyplot.setRangeGridlinePaint(Color.WHITE);

		JFreeChart chart = new JFreeChart(xyplot);
		chart.setTitle(title);

		try {
			ChartUtilities.saveChartAsJPEG(new File(outputFileName), chart, 500, 300);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart.");
		}

		display(chart);
	}

	private static void display(JFreeChart chart) {
		ApplicationFrame frame = new ApplicationFrame("Graph");
		ChartPanel chartPanel = new ChartPanel(chart);
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}

	public static void addAll(XYSeries series, double[] Xvals, double[] Yvals) {
		assert Xvals.length == Yvals.length;
		for (int i = 0; i < Xvals.length; i++) {
			series.add(Xvals[i], Yvals[i]);
		}
	}

	@SuppressWarnings("unused")
	private static void createCplot() {
		XYSeries series1 = new XYSeries("MCTS");
		double[] Xvals1 = { 0.1, 0.5, 1, 2 };
		double[] Yvals1 = { 30.1, 31.8, 29.8, 29.2 };
		addAll(series1, Xvals1, Yvals1);

		double[] Xvals2 = { 0.1, 0.5, 1, 2 };
		double[] Yvals2 = { 73.4, 71.9, 75.15, 77.86 };
		XYSeries series2 = new XYSeries("Greedy");
		addAll(series2, Xvals2, Yvals2);

		double[] Xvals3 = { 300, 500, 1000, 2000 };
		double[] Yvals3 = { 31.8, 32.3, 32.5, 33.4 };
		XYSeries series3 = new XYSeries("MCTS");
		addAll(series3, Xvals3, Yvals3);

		createPlot(	"plots/MCTStime.jpg", "MCTS performance with number of simulations", "Number of simulations",
					"Average score", series3);

	}


}