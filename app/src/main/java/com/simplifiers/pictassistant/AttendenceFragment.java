package com.simplifiers.pictassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class AttendenceFragment extends Fragment {
    BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendence, container, false);

        BarChart barChart = (BarChart) view.findViewById(R.id.barchart);

        List<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, 10f));
        entries.add(new BarEntry(1f, 100f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));

        BarDataSet set = new BarDataSet(entries, "Attendence");
        BarData data = new BarData(set);

        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);

        barChart.animateXY(1000, 1000);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setTouchEnabled(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

        return view;
    }

}
