package com.example.petme;

import static com.example.petme.CalendarFragment.clickedDate;
import static com.example.petme.CalendarUtil.*;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<Date> dayList;
    ArrayList<TodoListItem> todoList;
    TodoListAdapter todoListAdapter;

    CalendarViewHolder _holder;

    public CalendarAdapter(ArrayList<Date> dayList, ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter){
        this.dayList = dayList;
        this.todoList = todoList;
        this.todoListAdapter = todoListAdapter;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.setDate(dayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        Calendar dateCalendar;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateCalendar = Calendar.getInstance();
            this.parentView = itemView.findViewById(R.id.parent_view);
        }

        public void setDate(Date day, int position) {

            dateCalendar.setTime(day);

            setBackground();
            setText(position);

            itemView.setOnClickListener(view -> {
                if (_holder != null) _holder.setBackground();

                Drawable drawable = (Drawable) ContextCompat.getDrawable(itemView.getContext(), R.drawable.clicked_cell);
                itemView.setBackground(drawable);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                clickedDate = format.format(day);

                //CalendarDialog 실행
                alertCalendarDialog();
            });

        }

        private void setBackground() {

            //넘어온 년 월
            int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
            int displayMonth = dateCalendar.get(Calendar.MONTH);
            int displayYear = dateCalendar.get(Calendar.YEAR);
            //현재 년 월 일
            int currentYear = currentDateInfo.get(Calendar.YEAR);
            int currentMonth = currentDateInfo.get(Calendar.MONTH);
            int currentDay = currentDateInfo.get(Calendar.DAY_OF_MONTH);

            //비교해서 년, 월 같으면 진한색 아니면 연한색으로 변경
            if(displayYear==currentYear && displayMonth==currentMonth){
                parentView.setBackgroundColor(Color.parseColor("#D5D5D5"));

                //날짜까지 맞으면 색상 표시
                if(displayDay==currentDay) {
                    itemView.setBackgroundColor((Color.parseColor("#CEFBC9")));

                }
            } else{
                parentView.setBackgroundColor(Color.parseColor("#F6F6F6"));
            }

        }

        private void setText(int position) {
            TextView dayText = itemView.findViewById(R.id.tv_day);

            dayText.setText(String.valueOf(dateCalendar.get(Calendar.DAY_OF_MONTH)));
            //텍스트 색상 지정
            if((position + 1) % 7 == 0){    //토요일
                dayText.setTextColor(Color.BLUE);
            } else if(position % 7 == 0) {  //일요일
                dayText.setTextColor(Color.RED);
            }

        }

        public void alertCalendarDialog(){
            //recyclerView, LinearLayout 순으로 탈출
            ViewGroup calendarDialogViewGroup = (ViewGroup) parentView.getParent().getParent();
            LayoutInflater inflater = LayoutInflater.from(calendarDialogViewGroup.getContext());

            CalendarDialog calendarDialog = new CalendarDialog(inflater, todoList, todoListAdapter);
            calendarDialog.run();
        }
    }
}
