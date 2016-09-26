package com.harrywhewell.scrolldatepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;


/**
 * Date picker which displays the months of the year in a horizontal list.
 * If no end date is set the date picker will continue to infinity.
 */
public class MonthScrollDatePicker extends LinearLayout implements TitleValueCallback, OnChildDateSelectedListener {

    private TextView mYearTextView;
    private TextView mFullDateTextView;
    private RecyclerView mMonthRecyclerView;

    private MonthScrollDatePickerAdapter adapter;

    private int baseTextColor;

    private boolean showTitle;
    private boolean showFullDate;

    private Style style;

    private OnDateSelectedListener listener;

    public MonthScrollDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.date_picker_scroll_month, this, true);
        getViewElements();
        setAttributeValues(context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScrollDatePicker, 0, 0));
        MonthScrollDatePickerViewHolder.onDateSelected(this);
        initView();
    }

    /**
     * Get all the view elements from date_picker_scroll_month layout.
     */
    private void getViewElements(){
        mYearTextView = (TextView) findViewById(R.id.date_picker_scroll_month_year);
        mFullDateTextView = (TextView) findViewById(R.id.date_picker_scroll_month_full_date);
        mMonthRecyclerView = (RecyclerView) findViewById(R.id.date_picker_scroll_month_recycler_view);
    }

    /**
     * If attribute values have been set, get them.
     * If not get default values.
     * @param a styled attributes
     */
    private void setAttributeValues(TypedArray a){
        int baseColor;
        int selectedTextColor;
        int selectedColor;
        try{
            baseTextColor = a.getColor(R.styleable.ScrollDatePicker_baseTextColor,
                    getResources().getColor(R.color.default_base_text));
            baseColor = a.getColor(R.styleable.ScrollDatePicker_baseColor,
                    getResources().getColor(R.color.default_base));
            selectedTextColor = a.getColor(R.styleable.ScrollDatePicker_selectedTextColor,
                    getResources().getColor(R.color.default_selected_text));
            selectedColor = a.getColor(R.styleable.ScrollDatePicker_selectedColor,
                    getResources().getColor(R.color.default_selected));

            showTitle = a.getBoolean(R.styleable.ScrollDatePicker_showTitle, true);
            showFullDate = a.getBoolean(R.styleable.ScrollDatePicker_showFullDate, true);

        } finally {
            a.recycle();
        }

        Drawable background = Util.setDrawableBackgroundColor(
                getResources().getDrawable(R.drawable.bg_circle_drawable), baseColor);
        Drawable selectedBackground = Util.setDrawableBackgroundColor(
                getResources().getDrawable(R.drawable.bg_circle_drawable), selectedColor);
        style = new Style(selectedColor, baseColor, selectedTextColor, baseTextColor, background, selectedBackground);
    }

    /**
     * Set values onto view elements.
     */
    private void initView(){
        setShowTitle(this.showTitle);
        setShowFullDate(this.showFullDate);
        setTextColor();
        initRecyclerView();
    }

    /**
     * set up Recycler view and its adapter
     */
    private void initRecyclerView(){
        adapter = new MonthScrollDatePickerAdapter(style, this);
        mMonthRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mMonthRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Set values onto view elements.
     * @param show boolean, if true show title
     */
    private void setShowTitle(boolean show){
        if(show){
            mYearTextView.setVisibility(VISIBLE);
        }else{
            mYearTextView.setVisibility(GONE);
        }
    }

    /**
     * Set values onto view elements.
     * @param show boolean, if true show full date
     */
    private void setShowFullDate(boolean show){
        if(show){
            mFullDateTextView.setVisibility(VISIBLE);
        }else{
            mFullDateTextView.setVisibility(GONE);
        }
    }

    /**
     * Sets the text color of full date and title.
     */
    private void setTextColor(){
        mYearTextView.setTextColor(baseTextColor);
        mFullDateTextView.setTextColor(baseTextColor);
    }

    /**
     * Gets the current selected date as a Date.
     */
    public void getSelectedDate(OnDateSelectedListener listener){
       this.listener = listener;
    }

    /**
     * Sets the start month on the MonthScrollDatePicker.
     * Presumes year as local.
     * @param month start month
     */
    public void setStartMonth(int month){
        adapter.setStartMonth(month);
        adapter.notifyDataSetChanged();
    }

    /**
     * Sets the start date on the MonthScrollDatePicker
     * @param month start month
     * @param year start year
     */
    public void setStartDate(int month, int year){
        adapter.setStartDate(month, year);
        adapter.notifyDataSetChanged();
    }

    /**
     * Sets the end date of the MonthScrollDatePicker.
     * @param month end month
     * @param year end year
     */
    public void setEndDate(int month, int year){
        adapter.setEndDate(month, year);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTitleValueReturned(LocalDate date) {
        mYearTextView.setText(date.toString("yyyy"));
    }

    @Override
    public void onDateSelectedChild(@Nullable LocalDate date) {
        if(date != null) {
            mFullDateTextView.setText(String.format("%s %s", date.toString("MMMM"), date.toString("yyyy")));
            listener.onDateSelected(date.toDate());
        }
    }
}