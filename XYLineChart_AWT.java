package ProbablisticCounting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.LongToIntFunction;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYLineChart_AWT extends ApplicationFrame 
{
   public XYLineChart_AWT( String applicationTitle, String chartTitle, HashMap result )
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         "Actual Cardinality" ,
         "Estimated Cardinality" ,
         createDataset(chartTitle, result) ,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 640 , 480 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      renderer.setSeriesPaint( 0 , Color.RED );
     // renderer.setSeriesPaint( 1 , Color.GREEN );
     // renderer.setSeriesPaint( 2 , Color.YELLOW );
      renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
    //  renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
     // renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
      
   }
   
   private XYDataset createDataset(String chartTitle, HashMap result)
   {
      final XYSeries series = new XYSeries(chartTitle);  
      
     /*Iterator<Integer> itr2 = result.keySet().iterator();
      while (itr2.hasNext()) {
          Integer key = itr2.next();
          //i += key + map.get(key);
          series.add(key,Integer(result.get(key)));
      } 
      Map<Integer, Integer> map = new HashMap<Integer, Integer>();
      for (Map.Entry<Integer,Integer> entry : result.entrySet())
      {
    	  series.add(entry.getKey(),entry.getValue());
      }
        */       
      Iterator it = result.entrySet().iterator();
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
         
          series.add((Integer)pair.getKey(),(Integer)pair.getValue());
         // System.out.println(pair.getKey() + " = " + pair.getValue());
          it.remove(); // avoids a ConcurrentModificationException
      }

      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( series );          
      
      return dataset;
   }

   public static void main( String[ ] args ) 
   {
      
   }
}