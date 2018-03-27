	import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
		
public class Grapher extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	XYSeries[][] states;  
	
	
	public Grapher(Environment env) {
		
		this.states = new XYSeries[env.rows][env.columns];    
		
		for (int row =0;row< env.rows;row++ ) {
    			for (int col = 0; col<env.columns;col++) {
    			states[row][col] = new XYSeries(col + "," + row);
    			}
		}
	}
	
	public void AddIterationDataToDataset(float[][] data, int iteration, Environment env) {    
        for (int row =0;row< env.rows;row++ ) {
        		for (int col = 0; col<env.columns;col++) {
        			this.states[row][col].add(iteration, data[row][col]);
        		}
        }     
    }
	
	public JPanel CreatePanel(Environment env) {
		
		JPanel panel = new JPanel();
		
		XYSeriesCollection dataset = new XYSeriesCollection();
        
        for (int row =0;row< env.rows;row++ ) {
    			for (int col = 0; col<env.columns;col++) {
    				if (env.rewards[row][col] == AgentApp.AWALLSQ) {
    					
    				} else {
    					dataset.addSeries(states[row][col]);
    				}
    			
    			}
        }	
		
		JFreeChart chart = ChartFactory.createXYLineChart(
                "Utilities per state against iterations",
                "Iterations",
                "Utility",
                dataset, 
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new Dimension(1600, 900));
       
        panel.add(chartPanel);
        
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        
        XYPlot plot = (XYPlot)chart.getPlot();
        XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer)plot.getRenderer();
        
        
        int series = 0;
        for (int row =0;row< env.rows;row++ ) {
    			for (int col = 0; col<env.rows;col++) {
    				if (env.rewards[row][col] == AgentApp.AWALLSQ) {
    					
    				} else {
    					rend.setSeriesShapesVisible(series, false);
    					series++;
    				}
    			
    			}
        }	

        

        return panel;
	}
	    
}

