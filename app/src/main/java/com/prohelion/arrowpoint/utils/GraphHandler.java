package com.prohelion.arrowpoint.utils;

import android.graphics.PointF;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.*;
import com.example.arrowpoint.R;
import com.prohelion.canbus.model.CarData;

/**
 * Created by Admin on 3/05/2015.
 */
public class GraphHandler{

    //Spinner Index
    private static final int VEHICLE_SPEED = 0;
    private static final int BUS_POWER = 1;
    private static final int ARRAY_POWER = 2;
    private static final int MOTOR_TEMP = 3;
    private static final int MAX_CELL_TEMP = 4;
    private static final int CONTROLLER_TEMP = 5;
    private static final int MIN_CELL_VOLTAGE = 6;
    private static final int MAX_CELL_VOLTAGE = 7;
    private static final int BATTERY_VOLTAGE = 8;
    private static final int BATTERY_AMPS = 9;

    // Private Variables
    private static int MAX_X_Values = 500;
    private SimpleXYSeries series = null;
    private int currentItem = 0;

    private PointF minXY;
    private PointF maxXY;


    public void setupGraph(XYPlot graph, String label,int text_color, int line_color){

        // Add series to graph
        series = new SimpleXYSeries(label);
        series.useImplicitXVals();

        LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter(line_color, null, null, null);
        lineAndPointFormatter.getLinePaint().setStrokeWidth(5);
        graph.addSeries(series, lineAndPointFormatter);

         int color = R.color.text_color;
        // Layout And Styling
        /*

        @todo: This has all be commented out as it should be driven from the fragment_graph_new files not via code
               also it is broken with the latest version of the code

        graph.getLayoutManager().remove(graph.getLegend());
        graph.getLayoutManager().remove(graph.getDomainLabelWidget());
        graph.getGraph().getBackgroundPaint().setColor(Color.TRANSPARENT);
        graph.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
        graph.getGraph().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        graph.getGraphWidget().getDomainLabelPaint().setColor(Color.TRANSPARENT);
        graph.getGraphWidget().getDomainOriginLabelPaint().setColor(text_color);
        graph.getGraphWidget().getDomainOriginLinePaint().setColor(text_color);
        graph.getGraphWidget().getDomainGridLinePaint().setColor(text_color);
        graph.getGraph().getRangeOriginLinePaint().setColor(text_color);
        //graph.getGraph().getRangegetRangeOriginLabelPaint().setColor(text_color);
        graph.setRangeValueFormat(new DecimalFormat("#.#"));
        graph.getGraph().getRangeOriginLabelPaint().setColor(line_color);
        graph.getGraphWidget().getRangeLabelPaint().setTextSize(15);
        graph.getRangeLabelWidget().getLabelPaint().setColor(text_color);
        graph.getTitleWidget().getLabelPaint().setColor(text_color);
*/

        //graph.setRangeBoundaries(0, BoundaryMode.AUTO, 120, BoundaryMode.AUTO);
        setScale(graph, 0, 120, 1);
        graph.setDomainBoundaries(0, MAX_X_Values, BoundaryMode.FIXED);
        graph.setDomainStepValue(5);
        graph.setRangeLabel("Initial Title");
        //graph.getRangeLabelWidget().pack();
        resetGraph(graph, "Vehicle Speed", "Velocity (Km/h)");

    }


    private void setScale(XYPlot graph, int minY, int maxY, double stepVal) {

        graph.setRangeBoundaries( minY, BoundaryMode.AUTO, maxY , BoundaryMode.AUTO);
        graph.setRangeStep(StepMode.INCREMENT_BY_VAL,stepVal);
    }


    public void addData(XYPlot graph, CarData carData,int itemIndex) {



        double lastValue = 0;

        // get rid the oldest sample in history:
        if (series.size() > MAX_X_Values) {
            series.removeFirst();
        }

        // Select which data to add and change the axis scale if data source is changed
        if (itemIndex == VEHICLE_SPEED) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Vehicle Speed", "Velocity (Km/h)");
                setScale(graph, 0, 120, 2);
                series.addLast(null, carData.getLastSpeed() - 1);
            }
            lastValue = carData.getLastSpeed();

        } else if (itemIndex == BUS_POWER) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Bus Power", "Power (KW)");
                setScale(graph, -5, 5, 0.5);
                series.addLast(null, carData.getLastBusPower() - 0.1);
            }
            lastValue = carData.getLastBusPower();

        } else if (itemIndex == ARRAY_POWER) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Array Power", "Power (W)");
                setScale(graph, 0, 1200, 10);
            }
            lastValue = (int)carData.getLastArrayTotalPower();

        } else if (itemIndex == MOTOR_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Motor Temp", "Temperature (C)");
                setScale(graph, 0, 100, 0.1);
            }
            lastValue = carData.getLastMotorTemp();

        } else if (itemIndex == MAX_CELL_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Maximum Cell Temp", "Temperature (C)");
                setScale(graph, 0, 100, 0.1);
            }
            lastValue = (carData.getLastMaxCellTemp() * 0.10);

        } else if (itemIndex == CONTROLLER_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Controller Temp", "Temperature (C)");
                setScale(graph, 0, 100, 0.1);
            }
            lastValue = carData.getLastControllerTemp();

        } else if (itemIndex == MIN_CELL_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Minimum Cell Voltage", "Voltage (V)");
                setScale(graph, 0, 5, 0.05);
            }
            lastValue = (carData.getLastMinimumCellV() * 0.001);

        } else if (itemIndex == MAX_CELL_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Maximum Cell Voltage", "Voltage (V)");
                setScale(graph, 0, 5, 0.05);
            }
            lastValue = (carData.getLastMaximumCellV() * 0.001);
        }else if (itemIndex == BATTERY_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Battery Voltage", "Voltage (V)");
                setScale(graph, 0, 200, 1);
            }
            lastValue = (int)carData.getLastBatteryVolts();
        }else if (itemIndex == BATTERY_AMPS) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Battery Current", "Current (A)");
                setScale(graph, -60, 70, 2);
            }
            lastValue = carData.getLastBatteryAmps();
        }

        // add the latest data sample to corresponding series
        currentItem = itemIndex;
        series.addLast(null, lastValue);

        //Redraw Graph
        graph.redraw();

    }


    public void clearGraph(){
        int series_size = series.size();
        for(int i = 0; i < series_size; i ++){
            series.removeFirst();
        }
    }

    private void resetGraph(XYPlot graph, String label, String units) {
        //Clear all values from series
        int series_size = series.size();
        for(int i = 0; i < series_size; i ++){
            series.removeFirst();
        }

        graph.setTitle(label);
        graph.setRangeLabel(units);
    }

}
