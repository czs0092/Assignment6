package com.example.assignment6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    EditText Month, Day, Year, Price, Item, DayFrom, MonthFrom, YearFrom, DayTo, MonthTo, YearTo, PriceFrom, PriceTo;;
    Button Add, Sb, Fl, FlClear;
    TextView balance;
    DBManagement db;
    TableLayout history;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Item = findViewById(R.id.Item);
        Add = findViewById(R.id.Add);
        Sb = findViewById(R.id.Sub);
        Price = findViewById(R.id.Price);
        balance = findViewById(R.id.balance);
        //Date = findViewById(R.id.Date);
        Day = findViewById(R.id.Day);
        Month = findViewById(R.id.Month);
        Year = findViewById(R.id.Year);
        history = (TableLayout) findViewById(R.id.tableHistory);
        db = new DBManagement(this);
        AddTransaction();
        takeHistory();
        Fl = findViewById(R.id.Filter);
        FlClear = findViewById(R.id.Clear);
        DayFrom =  findViewById(R.id.DayFrom);
        MonthFrom = findViewById(R.id.MonthFrom);
        YearFrom = findViewById(R.id.DayFrom);
        DayTo = findViewById(R.id.DayTo);
        MonthTo = findViewById(R.id.MonthTo);
        YearTo = findViewById(R.id.YearTo);
        PriceFrom = findViewById(R.id.PriceFrom);
        PriceTo = findViewById(R.id.PriceTo);
        Filter();
        ClearFilter();
    }

    public void AddTransaction(){
        Add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(Price.getText().toString());
                        Model model = new Model();
                        model.mDay =  Day.getText().toString();
                        model.mMonth =  Month.getText().toString();
                        model.mYear =  Year.getText().toString();

                        model.mItem = Item.getText().toString();
                        model.mPrice = price;
                        boolean result = db.createTransaction(model);

                        if (result)
                            Toast.makeText(MainActivity.this, "Successfully Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Failed to Create", Toast.LENGTH_LONG).show();
                        takeHistory();
                        ClearText();
                    }
                }
        );

        Sb.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(Price.getText().toString()) * -1;
                        Model model = new Model();
                        model.mDay =  Day.getText().toString();
                        model.mMonth =  Month.getText().toString();
                        model.mYear =  Year.getText().toString();
                        model.mItem = Item.getText().toString();
                        model.mPrice = price;
                        boolean result = db.createTransaction(model);

                        if (result)
                            Toast.makeText(MainActivity.this, "Successfully Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Failed to Create", Toast.LENGTH_LONG).show();
                        takeHistory();
                        ClearText();
                    }
                }
        );
    }

    public void takeHistory(){
        Cursor result = db.pullData();
        getHistory(result, false);
    }

    public void getHistory(Cursor result, boolean filtered){
        if (result == null){
            return;
        }
        StringBuffer buffer = new StringBuffer();
        ClearTable();
        Double balance = 0.00;

        while(result.moveToNext()){
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams columnLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            columnLayout.weight = 1;

            TextView year = new TextView(this);
            year.setLayoutParams(columnLayout);
            year.setText(result.getString(2));
            tr.addView(year);

            TextView day = new TextView(this);
            day.setLayoutParams(columnLayout);
            day.setText(result.getString(3));
            tr.addView(day);

            TextView month = new TextView(this);
            month.setLayoutParams(columnLayout);
            month.setText(result.getString(4));
            tr.addView(month);

            TextView priceView = new TextView(this);
            priceView.setLayoutParams(columnLayout);
            priceView.setText(result.getString(5));
            tr.addView(priceView);

            TextView item = new TextView(this);
            item.setLayoutParams(columnLayout);
            item.setText(result.getString(1));
            tr.addView(item);

            history.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            double price = Double.parseDouble(result.getString(3));
            balance += price;
        }

        MainActivity.this.balance.setText("Current Balance: $" + Double.toString(balance));
    }

    public void ClearText(){
        MainActivity.this.Year.setText("");
        MainActivity.this.Month.setText("");
        MainActivity.this.Day.setText("");
        MainActivity.this.Price.setText("");
        MainActivity.this.Item.setText("");
    }

    public void ClearTable(){
        int count = history.getChildCount();
        for (int i = 1; i < count; i++) {
            history.removeViewAt(1);
        }
    }

    public void Filter(){
        Fl.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String priceFromString = PriceFrom.getText().toString();
                        String priceToString = PriceTo.getText().toString();
                        String dayFrom = DayFrom.getText().toString();
                        String monthFrom = MonthFrom.getText().toString();
                        String yearFrom = YearFrom.getText().toString();
                        String dayTo = DayTo.getText().toString();
                        String monthTo = MonthTo.getText().toString();
                        String yearTo = YearTo.getText().toString();



                        Cursor result = db.getFilteredData(priceFromString, priceToString, yearFrom, yearTo, dayFrom, dayTo, monthFrom, monthTo);
                        getHistory(result, true);
                    }
                }
        );
    }

    public void ClearFilter(){
        FlClear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClearText();
                        takeHistory();
                    }
                }
        );
    }

    public String CreateDate(String year, String day, String month){
        if (year.isEmpty() || month.isEmpty() || day.isEmpty()) {
            return "";
        }
        else {
            return year + month + day;
        }
    }

}
