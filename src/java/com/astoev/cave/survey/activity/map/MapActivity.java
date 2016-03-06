package com.astoev.cave.survey.activity.map;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.astoev.cave.survey.R;
import com.astoev.cave.survey.activity.MainMenuActivity;
import com.astoev.cave.survey.activity.draw.AbstractDrawingActivity;
import com.astoev.cave.survey.activity.draw.MapDrawingActivity;

/**
 * Created by IntelliJ IDEA.
 * User: astoev
 * Date: 1/23/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapActivity extends MainMenuActivity implements View.OnTouchListener {

    private MapView map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);
        map = (MapView) findViewById(R.id.mapSurface);
        map.setOnTouchListener(this);

        // no top bar
        getSupportActionBar().hide();

        // zoom controls
        final ZoomControls zoom = (ZoomControls) findViewById(R.id.mapZoom);
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aView) {
                map.zoomIn();
                zoom.setIsZoomOutEnabled(map.canZoomOut());
                zoom.setIsZoomInEnabled(map.canZoomIn());
            }
        });
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aView) {
                map.zoomOut();
                zoom.setIsZoomOutEnabled(map.canZoomOut());
                zoom.setIsZoomInEnabled(map.canZoomIn());
            }
        });

        // plan/section toggle
        final ToggleButton viewToggle = (ToggleButton) findViewById(R.id.mapHorizonatalToggle);
        viewToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.setHorizontalPlan(!isChecked);
                // explicitly reset the zoom when switching plan/scale
                zoom.setIsZoomOutEnabled(map.canZoomOut());
                zoom.setIsZoomInEnabled(map.canZoomIn());
            }
        });

        // drawings toggle
        final ToggleButton drawingsToggle = (ToggleButton) findViewById(R.id.mapDrawingToggle);
        drawingsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.setDrawingsVisible(isChecked);
            }
        });
    }

    @Override
    public boolean onTouch(View aView, MotionEvent aMotionEvent) {

        if (aMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            map.resetMove(aMotionEvent.getX(), aMotionEvent.getY());
        } else if (aMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            map.move(aMotionEvent.getX(), aMotionEvent.getY());
        }

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        map = (MapView) findViewById(R.id.mapSurface);
        map.setOnTouchListener(this);
    }

    public void annotateMap(View aView) {
        Intent intent = new Intent(this, MapDrawingActivity.class);
        intent.putExtra(AbstractDrawingActivity.PARAM_MAP_FLAG, true);
        intent.putExtra(AbstractDrawingActivity.PARAM_MAP_HORIZONTAL, map.isHorizontalPlan());
        intent.putExtra(AbstractDrawingActivity.PARAM_MAP_MOVEX, map.getMoveX());
        intent.putExtra(AbstractDrawingActivity.PARAM_MAP_MOVEY, map.getMoveY());
        intent.putExtra(AbstractDrawingActivity.PARAM_MAP_SCALE, map.getScale());
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        map.preserveView();
        super.onPause();
    }
}