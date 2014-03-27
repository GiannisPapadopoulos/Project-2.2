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
	
	// interval=3
	// Total time running 200.005 cars spawned 254
	// averageDistanceTravelled 360.6508814055344
	// averageVelocity 18.453836664331373
	// averageTimeTravelled 74.32996262681895
	// averageTimeStopped 0.5972571030259133
	//
	// interval=4
	// Total time running 200.006 cars spawned 272
	// averageDistanceTravelled 375.72912870722706
	// averageVelocity 14.940827606050231
	// averageTimeTravelled 88.07157482774996
	// averageTimeStopped 0.6734591902588769
	//
	// interval=5
	// Total time running 200.017 cars spawned 260
	// averageDistanceTravelled 410.63977986115674
	// averageVelocity 12.835193062745608
	// averageTimeTravelled 112.1540373288668
	// averageTimeStopped 0.7223366510409576
	//
	// interval=6
	// Total time running 200.004 cars spawned 275
	// averageDistanceTravelled 341.93096926740105
	// averageVelocity 15.821126174926759
	// averageTimeTravelled 87.71228366043731
	// averageTimeStopped 0.658108904502774
	//
	// interval=7
	// Total time running 200.012 cars spawned 262
	// averageDistanceTravelled 439.4377118143542
	// averageVelocity 15.156164984045358
	// averageTimeTravelled 106.29285110276321
	// averageTimeStopped 0.6792814536341305
	//
	// interval=8
	// Total time running 200.005 cars spawned 259
	// averageDistanceTravelled 415.31815723041154
	// averageVelocity 14.323175320754181
	// averageTimeTravelled 104.6232908867501
	// averageTimeStopped 0.6971660374521135

	private static void createCplot() {
		XYSeries series1 = new XYSeries("Velocity");
		double[] Xvals1 = { 2, 3, 4, 5, 6, 7, 8 };
		//Average speed;
		double[] Yvals1 = { 18.886, 18.453, 14.940, 12.835, 15.821, 15.156, 14.323 };
		addAll(series1, Xvals1, Yvals1);

		double[] Xvals2 = Xvals1;
		// % waiting time
		double[] Yvals2 = { 0.588, 0.597, 0.673, 0.722, 0.658, 0.679, 0.679 };
		XYSeries series2 = new XYSeries("% of time spent on traffic lights");
		addAll(series2, Xvals2, Yvals2);


		createPlot(	"plots/TrafficLightTimers2.jpg", "Time spent waiting on traffic lights", "interval",
					"% Waiting time",
					series2);

	}

	public static void main(String[] args) {
		createCplot();
	}


}