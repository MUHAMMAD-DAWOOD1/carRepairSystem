package ca.mcgill.ecse321.repairsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class homePage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    /*variables for time picker date picker */
    private EditText etDate;
    private TextView tvTimer2;
    private int t2Hour, t2Minute;
    private Calendar calendar;
    private int year, month, day;


    /*variables for book appointment and add timeslot*/
    private String startT = "";
    private String endT = "";
    private String startYmd = "";
    private String startTime = "";
    private String endTime = "";
    private String service = "";
    private String timeSlotId = "";
    private String appId = "";
    private String carId = "";
    private String mechId = "";
    private String customerId;

    private String error = null;
    private FloatingActionButton addCarBtn;
    private Button bookAppBtn;
    private PopupMenu menu;
    private PopupMenu menuCar;

    private ArrayList<String> mechIds = new ArrayList<>();
    private ArrayList<String> mechNames = new ArrayList<>();
    private ArrayList<String> carIds = new ArrayList<>();
    private ArrayList<String> carTypes = new ArrayList<>();
    private static String selectedAppointmentInfos = "";
    private static ArrayList<String> appointmentInfosList = new ArrayList<String>();
    private static String viewAppointmentInfo = "";
    private static String selectedMech="";
    private static ArrayList<String> selectedMechList = new ArrayList<String>();

    private ArrayList<String> appointments = new ArrayList<String>();

    private final Context context = this;
    private int duration = Toast.LENGTH_SHORT;

    /*variables for homepage veiw change*/
    private Boolean bookAppointmentIsVisible = false;
    private Boolean makePaymentIsVisible = false;
    private Boolean editProfileIsVisible = false;
    private Boolean homePageIsVisible = true;
    private View bookAppointmentView;
    private View makePaymentView;
    private View editProfileView;
    private View homePageView;

    /*variables for customer profile*/
    private TextView tv_name;
    private TextView tv_password;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_address;
    private TextView tv_credit;
    private TextView tv_debit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*variables for nav bar/ view change*/
        bookAppointmentView = findViewById(R.id.bookAppointmentView);
        makePaymentView = findViewById(R.id.makePaymentView);
        editProfileView = findViewById(R.id.editProfileView);
        homePageView = findViewById(R.id.homePageView);

        bookAppointmentView.setVisibility(View.GONE);
        editProfileView.setVisibility(View.GONE);
        makePaymentView.setVisibility(View.GONE);
        homePageView.setVisibility(View.VISIBLE);
        customerId = getIntent().getStringExtra("customerId");



        /*variables for book appointment */
        addCarBtn = (FloatingActionButton) findViewById(R.id.addCarBtn);
        bookAppBtn = (Button) findViewById(R.id.bookAppBtn);

        //date picker & time picker
        tvTimer2 = findViewById(R.id.tv_timer2);
        etDate = findViewById(R.id.et_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        ////////////////// BOOK APPOINTMENT//////////////////

        /*add car methods*/
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display popup window
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.prompts_add_car, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);

                EditText carInput = (EditText) promptView.findViewById(R.id.carTypeInput);
                EditText WinterTiresInput = (EditText) promptView.findViewById(R.id.WinterTiresInput);
                EditText NumberofKilometersInput = (EditText) promptView.findViewById(R.id.NumberofKilometersInput);
                EditText Notes = (EditText) promptView.findViewById(R.id.Notes);


                // setup a dialog window
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Add Car", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                RequestParams requestParams = new RequestParams();
                                String request = "";
                                request = request.concat(customerId);
                                request = request.concat("?carType=" + carInput.getText().toString());
                                request = request.concat("&winterTires=" + WinterTiresInput.getText().toString());
                                request = request.concat("&numOfKilometers=" + NumberofKilometersInput.getText().toString());
                                request = request.concat("&note=" + Notes.getText().toString());

                                HttpUtils.post("/car/" + request, requestParams, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Toast toast = Toast.makeText(context, "Car Added!", duration);
                                        toast.show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        try {
                                            error += errorResponse.get("message").toString();
                                        } catch (JSONException e) {
                                            error += e.getMessage();
                                        }
                                        refreshErrorMessage();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
            }
        });

        /*date set methods*/
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        homePage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        //format startYmd
                        if (month < 10 & day < 10) {
                            startYmd = year + "-0" + month + "-0" + day;
                        } else if (month >= 10 & day < 10) {
                            startYmd = year + "-" + month + "-0" + day;
                        } else if (month < 10 & day >= 10) {
                            startYmd = year + "-0" + month + "-" + day;
                        } else {
                            startYmd = year + "-" + month + "-" + day;
                        }
                        etDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();//display on the screen
            }
        });

        /*time set methods*/
        tvTimer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        homePage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                t2Hour = hourOfDay;
                                t2Minute = minute;
                                String time = t2Hour + ":" + t2Minute;
                                //format starT
                                if (t2Hour < 10 & t2Minute < 10) {
                                    startT = "-0" + t2Hour + ":0" + t2Minute;
                                } else if (t2Hour < 10 & t2Minute >= 10) {
                                    startT = "-0" + t2Hour + ":" + t2Minute;
                                } else if (t2Hour >= 10 & t2Minute < 10) {
                                    startT = "-" + t2Hour + ":0" + t2Minute;
                                } else {
                                    startT = "-" + t2Hour + ":" + t2Minute;
                                }
                                //format endT
                                if (t2Hour < 9 & t2Minute < 10) {
                                    endT = "-0" + (hourOfDay + 1) + ":0" + t2Minute;
                                } else if (t2Hour < 9 & t2Hour >= 10) {
                                    endT = "-0" + (hourOfDay + 1) + ":" + t2Minute;
                                } else if (t2Hour >= 9 & t2Minute < 10) {
                                    endT = "-" + (hourOfDay + 1) + ":0" + t2Minute;
                                } else {
                                    endT = "-" + (hourOfDay + 1) + ":" + t2Minute;
                                }
                                endTime = startYmd + endT;
                                startTime = startYmd + startT;

                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    tvTimer2.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                error = "";
                                String request = "";
                                request = request.concat(startTime);
                                request = request.concat("?endTime=" + endTime);

                                HttpUtils.post("/timeslot/" + request, new RequestParams(), new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            timeSlotId = response.getString("id");
                                            Toast toast = Toast.makeText(context, "Time Slot Added!", duration);
                                            toast.show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        try {
                                            error += errorResponse.get("message").toString();
                                        } catch (JSONException e) {
                                            error += e.getMessage();
                                        }
                                        refreshErrorMessage();
                                    }
                                });

                            }
                        }, 24, 0, true
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t2Hour, t2Minute); //update time
                timePickerDialog.show(); //display time on the screen
            }
        });


        //book appointment method
//book appointment method
        bookAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = "";

                RequestParams requestParams = new RequestParams();
                String request = "";
                request = request.concat(customerId);
                request = request.concat("?timeSlotId=" + timeSlotId);
                request = request.concat("&carId=" + carId);
                request = request.concat("&services=" + service);
                request = request.concat("&note=car_fix");

                //book appointment
                HttpUtils.post("/appointment/" + request, new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            appId = response.getString("id");

                            RequestParams requestParams = new RequestParams();
                            String request2 = "";
                            request2 = request2.concat(mechId);
                            request2 = request2.concat("?appointmentId=" + appId);

                            //add mechanic to the appointment
                            HttpUtils.put("/appointment/addMechanic/" + request2, new RequestParams(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    refreshErrorMessage();
                                    Toast toast = Toast.makeText(context, "Appointment Booked!", duration);
                                    toast.show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    try {
                                        error += errorResponse.get("message").toString();
                                    } catch (JSONException e) {
                                        error += e.getMessage();
                                    }
                                    refreshErrorMessage();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {
                            error += errorResponse.get("message").toString();
                        } catch (JSONException e) {
                            error += e.getMessage();
                        }
                    }
                });
            }

        });

        updateView(); //update main page view

        //////////// BOTTOM NAV BAT MENU/////////
        setInfo();
        viewCrendentials(findViewById(R.id.cNameText));
        updateProfile(findViewById(R.id.cNameText));
        toBook(findViewById(R.id.bookAppointmentView));
        toEditProfile(findViewById(R.id.editProfileView));
        toPayment(findViewById(R.id.makePaymentView));
        toHome(findViewById(R.id.homePageView));

    }


    ////////////////// BOOK APPOINTMENT - SELECT MECH, SERVICE, CAR//////////////////
    /*display popup menu for mechanic list*/
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.mech_popup_menu);
        menu = popup;

        HttpUtils.get("/mechanics/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        /*set popup menu item for mechanic list*/
                        mechIds.add(response.getJSONObject(i).getString("id"));
                        String name = response.getJSONObject(i).getString("name");
                        mechNames.add(name);
                        if (i == 0) {
                            menu.getMenu().findItem(R.id.item1).setTitle(name);
                        } else if (i == 1) {
                            menu.getMenu().findItem(R.id.item2).setTitle(name);
                        } else if (i == 2) {
                            menu.getMenu().findItem(R.id.item3).setTitle(name);
                        } else if (i == 3) {
                            menu.getMenu().findItem(R.id.item4).setTitle(name);
                        }
                    } catch (JSONException e) {
                        error += e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
        popup.show();
    }


    /*display popup menu for car list*/
    public void carPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.car_popup_menu);
        menuCar = popup;

        String request = "";
        request = request.concat(customerId);
        HttpUtils.get("/cars/" + request, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        /*set popup menu item for car list*/

                        String id = response.getJSONObject(i).getString("id");
                        String carT = response.getJSONObject(i).getString("carType");
                        carIds.add(id);
                        carTypes.add(carT);
                        if (i == 0) {
                            menuCar.getMenu().findItem(R.id.car1).setTitle(carT);
                        } else if (i == 1) {
                            menuCar.getMenu().findItem(R.id.car2).setTitle(carT);
                        } else if (i == 2) {
                            menuCar.getMenu().findItem(R.id.car3).setTitle(carT);
                        } else if (i == 3) {
                            menuCar.getMenu().findItem(R.id.car4).setTitle(carT);
                        }
                    } catch (JSONException e) {
                        error += e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
        popup.show();
    }

    /*display popup menu for service list*/
    public void servicePopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.service_popup_menu);
        popup.show();
    }

    /*select menu item for popup menu*/
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            Toast.makeText(this, mechNames.get(0) + " selected", duration).show();
            mechId = mechIds.get(0).toString();
        } else if (item.getItemId() == R.id.item2) {
            Toast.makeText(this, mechNames.get(1) + " selected", duration).show();
            mechId = mechIds.get(1).toString();
        } else if (item.getItemId() == R.id.item3) {
            Toast.makeText(this, mechNames.get(2) + " selected", duration).show();
            mechId = mechIds.get(2).toString();
        } else if (item.getItemId() == R.id.item4) {
            Toast.makeText(this, mechNames.get(3) + " selected", duration).show();
            mechId = mechIds.get(3).toString();
        } else if (item.getItemId() == R.id.car1) {
            Toast.makeText(this, carTypes.get(0) + " selected", duration).show();
            carId = carIds.get(0).toString();
        } else if (item.getItemId() == R.id.car2) {
            Toast.makeText(this, carTypes.get(1) + " selected", duration).show();
            carId = carIds.get(1).toString();
        } else if (item.getItemId() == R.id.car3) {
            Toast.makeText(this, carTypes.get(2) + " selected", duration).show();
            carId = carIds.get(2).toString();
        } else if (item.getItemId() == R.id.car4) {
            Toast.makeText(this, carTypes.get(3) + " selected", duration).show();
            carId = carIds.get(3).toString();
        } else if (item.getItemId() == R.id.CarRepair) {
            Toast.makeText(this, "CarRepair selected", duration).show();
            service = "CarRepair";
        } else if (item.getItemId() == R.id.OilChange) {
            Toast.makeText(this, "OilChange selected", duration).show();
            service = "OilChange";
        } else if (item.getItemId() == R.id.RegularCheckup) {
            Toast.makeText(this, "RegularCheckup selected", duration).show();
            service = "RegularCheckup";
        } else if (item.getItemId() == R.id.CarWash) {
            Toast.makeText(this, "CarWash selected", duration).show();
            service = "CarWash";
        } else if (item.getItemId() == R.id.TireChange) {
            Toast.makeText(this, "TireChange selected", duration).show();
            service = "TireChange";
        } else if (item.getItemId() == R.id.RoadsideAssistance) {
            Toast.makeText(this, "RoadsideAssistance selected", duration).show();
            service = "RoadsideAssistance";
        } else if (item.getItemId() == R.id.Towing) {
            Toast.makeText(this, "Towing selected", duration).show();
            service = "Towing";
        } else if (item.getItemId() == R.id.CarInspection) {
            Toast.makeText(this, "CarInspection selected", duration).show();
            service = "CarInspection";
        } else if (item.getItemId() == R.id.Other) {
            Toast.makeText(this, "Other selected", duration).show();
            service = "Other";
        }
        return false;
    }


    ///////////CUSTOMER PROFILE////////////////////
    public void setInfo() {
        EditText payment_name = (EditText) findViewById(R.id.payment_name);
        EditText payment_email = (EditText) findViewById(R.id.payment_email);
        EditText payment_address = (EditText) findViewById(R.id.payment_address);
        EditText payment_credit = (EditText) findViewById(R.id.payment_credit);
        EditText payment_debit = (EditText) findViewById(R.id.payment_debit);
        EditText payment_amount = (EditText) findViewById(R.id.payment_amount);


        RequestParams requestParams = new RequestParams();

        HttpUtils.get("customer/" + customerId.toString(), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //display defualt(current) customer credentials
                    payment_name.setText("Name: "+ response.getString("name"));
                    payment_email.setText("Email: "+response.getString("email"));
                    payment_address.setText("Address: "+response.getString("address"));
                    payment_credit.setText("Credit: card ends with XXX "+response.getString("creditHash").substring(response.getString("creditHash").length()-4));
                    payment_debit.setText("Debit: card ends with XXX "+response.getString("debitHash").substring(response.getString("debitHash").length()-4));
                    payment_amount.setText("Amount: "+"50$");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get(" email and password ").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }

            }
        });
    }


    public void viewCrendentials(View v) {
        //current customer credentials
        tv_name = (TextView) findViewById(R.id.cNameText);
        tv_password = (TextView) findViewById(R.id.cPasswordText);
        tv_phone = (TextView) findViewById(R.id.cPhoneText);
        tv_email = (TextView) findViewById(R.id.cEmailText);
        tv_address = (TextView) findViewById(R.id.cAddressText);
        tv_credit = (TextView) findViewById(R.id.cCreditText);
        tv_debit = (TextView) findViewById(R.id.cDebitText);

        RequestParams requestParams = new RequestParams();

        HttpUtils.get("customer/" + customerId.toString(), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //display defualt(current) customer credentials
                    tv_name.setText(response.getString("name"));
                    tv_password.setText(response.getString("password"));
                    tv_phone.setText(response.getString("phone"));
                    tv_email.setText(response.getString("email"));
                    tv_address.setText(response.getString("address"));
                    tv_credit.setText(response.getString("creditHash"));
                    tv_debit.setText(response.getString("debitHash"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get(" email and password ").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }

            }
        });

    }

    public void updateProfile(View v) {
        error = "";
        //default setup displaying customer crednetials
        Button toUpdate = findViewById(R.id.profileUpdateButton);
        tv_name = (TextView) findViewById(R.id.cNameText);
        tv_password = (TextView) findViewById(R.id.cPasswordText);
        tv_phone = (TextView) findViewById(R.id.cPhoneText);
        tv_email = (TextView) findViewById(R.id.cEmailText);
        tv_address = (TextView) findViewById(R.id.cAddressText);
        tv_credit = (TextView) findViewById(R.id.cCreditText);
        tv_debit = (TextView) findViewById(R.id.cDebitText);

        toUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "";
                String name = tv_name.getText().toString();
                for (int i = 0; i < name.length(); i++) {
                    if (Character.compare(name.charAt(i), ' ') == 0) {
                        if (i == name.length() - 1) {
                            name = name.substring(0, name.length() - 1) + "%20";
                        } else {
                            name = name.substring(0, i) + "%20" + name.substring(i + 1, name.length());
                        }

                    }
                }
                String address = tv_address.getText().toString();
                for (int i = 0; i < address.length(); i++) {
                    if (Character.compare(address.charAt(i), ' ') == 0) {
                        if (i == address.length() - 1) {
                            address = address.substring(0, address.length() - 1) + "%20";
                        } else {
                            address = address.substring(0, i) + "%20" + address.substring(i + 1, address.length());
                        }
                    }
                }
                request = request.concat(tv_email.getText().toString());
                request = request.concat("?newName=" + name);
                request = request.concat("&newPassword=" + tv_password.getText().toString());
                request = request.concat("&newPhone=" + tv_phone.getText().toString());
                request = request.concat("&newCredit=" + tv_credit.getText().toString());
                request = request.concat("&newDebit=" + tv_debit.getText().toString());
                request = request.concat("&newAddress=" + address);

                //update customer credentials
                HttpUtils.put("customer/" + request, new RequestParams(), new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {

                            //display customer credentials
                            tv_name.setText(response.getString("name"));
                            tv_password.setText(response.getString("password"));
                            tv_phone.setText(response.getString("phone"));
                            tv_email.setText(response.getString("email"));
                            tv_address.setText(response.getString("address"));
                            tv_credit.setText(response.getString("creditHash"));
                            tv_debit.setText(response.getString("debitHash"));
                            finish();
                            startActivity(getIntent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {
                            error += errorResponse.get("").toString();
                        } catch (JSONException e) {
                            error += e.getMessage();
                        }
                    }
                });
            }
        });

    }


////////VIEW APPOINTMENTS///////////////
    ArrayList<String> appIds = new ArrayList<>();
    public void updateView() {
        /*get all appointment list*/
        HttpUtils.get("appointments/" + customerId.toString(), new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                appIds = new ArrayList<>();
                appointments = new ArrayList<String>();
                appointmentInfosList = new ArrayList<>();
                selectedMechList =  new ArrayList<>();
                appointments.add("Service          Start Time");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String toAdd = "";
                        viewAppointmentInfo="";
                        String Service = response.getJSONObject(i).getJSONArray("services").getJSONObject(0).getString("serviceType");
                        String Status = response.getJSONObject(i).getString("status");

                        toAdd = toAdd.concat(Service + "        ");
                        JSONObject timeSlot = response.getJSONObject(i).getJSONObject("timeSlot");
                        toAdd = toAdd.concat(timeSlot.getString("startTime").substring(0,10));
                        toAdd = toAdd.concat(" " + timeSlot.getString("startTime").substring(11,timeSlot.getString("startTime").length()-3));
                        viewAppointmentInfo=timeSlot.getString("startTime").substring(0,10) + " " + timeSlot.getString("startTime").substring(11,timeSlot.getString("startTime").length()-3);
                        viewAppointmentInfo+=")"+Service +"(";
                        viewAppointmentInfo+=Status+">";
                        appointmentInfosList.add(viewAppointmentInfo);
                        appIds.add(response.getJSONObject(i).getString("id"));
                        //time type mech status
                        appointments.add(toAdd);


                    } catch (JSONException e) {
                        error += e.getMessage();
                    }
                }

                final ListView listView = (ListView) findViewById(R.id.appointmentListHome);
                final StableArrayAdapter adapter = new StableArrayAdapter(homePage.this, android.R.layout.simple_list_item_1, appointments);
                listView.setAdapter(adapter);
                String delims = "[    ]";

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedAppointmentInfos = appointmentInfosList.get(i-1);
                        HttpUtils.get("appointment/" + appIds.get(i-1), new RequestParams(), new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                            {
                                try {
                                    selectedMech="";
                                    JSONArray mechanics= response.getJSONArray("mechanics");
                                    String mechanicId = mechanics.getJSONObject(0).getString("id");
                                    HttpUtils.get("mechanic/" + mechanicId, new RequestParams(), new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                                        {
                                            try {
                                                selectedMech = response.getString("name");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Toast.makeText(homePage.this, "The " + ordinal(i) + " appointment item is selected", duration).show();
                        openDialog();
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
            }
        });
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }

    public void openDialog() {
        ViewAppointmentDialog viewAppointmentDialog = new ViewAppointmentDialog();
        viewAppointmentDialog.show(getSupportFragmentManager(), "view appointment popup dialog");
    }

    public static String displayAppInfo(){
        return selectedAppointmentInfos+selectedMech;
    }


    /*retreive appointment*/
    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    ///////////MAKE PAYMENT///////
    public void makePayment(View v) {
        Toast toast = Toast.makeText(context, "Payment received!", duration);
        toast.show();
    }

    ////////////BOTTOM NAV MENU BAR///////////
    /*navigate to book app view*/
    public void toBook(View v) {
        Button toBookHome = findViewById(R.id.bookAppointmentHome);
        Button toBookBook = findViewById(R.id.bookAppointmentBook);
        Button toBookPay = findViewById(R.id.bookAppointmentPay);
        Button toBookMake = findViewById(R.id.bookAppointmentEdit);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bookAppointmentIsVisible) {
                    bookAppointmentView.setVisibility(View.VISIBLE);
                    editProfileView.setVisibility(View.GONE);
                    makePaymentView.setVisibility(View.GONE);
                    homePageView.setVisibility(View.GONE);
                    bookAppointmentIsVisible = true;
                    makePaymentIsVisible = false;
                    editProfileIsVisible = false;
                    homePageIsVisible = false;
                }
            }
        };

        toBookHome.setOnClickListener(listener);
        toBookBook.setOnClickListener(listener);
        toBookMake.setOnClickListener(listener);
        toBookPay.setOnClickListener(listener);

    }

    /*navigate to edit profile view*/
    public void toEditProfile(View v) {
        Button toEditHome = findViewById(R.id.editProfileHome);
        Button toEditBook = findViewById(R.id.editProfileBook);
        Button toEditMake = findViewById(R.id.editProfileEdit);
        Button toEditPay = findViewById(R.id.editProfilePay);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editProfileIsVisible) {
                    bookAppointmentView.setVisibility(View.GONE);
                    editProfileView.setVisibility(View.VISIBLE);
                    makePaymentView.setVisibility(View.GONE);
                    homePageView.setVisibility(View.GONE);
                    bookAppointmentIsVisible = false;
                    makePaymentIsVisible = false;
                    editProfileIsVisible = true;
                    homePageIsVisible = false;
                }
            }
        };

        toEditHome.setOnClickListener(listener);
        toEditBook.setOnClickListener(listener);
        toEditMake.setOnClickListener(listener);
        toEditPay.setOnClickListener(listener);
    }

    /*navigate to logout view*/
    public void bye(View v) {
        Button toLogOutHome = findViewById(R.id.logoutHome);
        Button toLogOutPay = findViewById(R.id.logoutPay);
        Button toLogOutBook = findViewById(R.id.logoutBook);
        Button toLogOutEdit = findViewById(R.id.logoutEdit);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homePage.this, MainActivity.class));
            }
        };

        toLogOutHome.setOnClickListener(listener);
        toLogOutPay.setOnClickListener(listener);
        toLogOutBook.setOnClickListener(listener);
        toLogOutEdit.setOnClickListener(listener);
    }

    /*navigate to payment view*/
    public void toPayment(View v) {
        Button toPayHome = findViewById(R.id.paymentHome);
        Button toPayPay = findViewById(R.id.paymentPay);
        Button toPayBook = findViewById(R.id.paymentBook);
        Button toPayMake = findViewById(R.id.paymentEdit);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!makePaymentIsVisible) {
                    bookAppointmentView.setVisibility(View.GONE);
                    editProfileView.setVisibility(View.GONE);
                    makePaymentView.setVisibility(View.VISIBLE);
                    homePageView.setVisibility(View.GONE);
                    bookAppointmentIsVisible = false;
                    makePaymentIsVisible = true;
                    editProfileIsVisible = false;
                    homePageIsVisible = false;
                }
setInfo();
            }
        };

        toPayHome.setOnClickListener(listener);
        toPayPay.setOnClickListener(listener);
        toPayBook.setOnClickListener(listener);
        toPayMake.setOnClickListener(listener);
    }

    /*navigate to main page view*/
    public void toHome(View v) {
        Button toHomeHome = findViewById(R.id.homeHome);
        Button toHomeBook = findViewById(R.id.homeBook);
        Button toHomePay = findViewById(R.id.homePay);
        Button toHomeEdit = findViewById(R.id.homeEdit);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homePageIsVisible) {
                    updateView();
                    bookAppointmentView.setVisibility(View.GONE);
                    editProfileView.setVisibility(View.GONE);
                    makePaymentView.setVisibility(View.GONE);
                    homePageView.setVisibility(View.VISIBLE);
                    bookAppointmentIsVisible = false;
                    makePaymentIsVisible = false;
                    editProfileIsVisible = false;
                    homePageIsVisible = true;
                }
            }
        };

        toHomeHome.setOnClickListener(listener);
        toHomeBook.setOnClickListener(listener);
        toHomePay.setOnClickListener(listener);
        toHomeEdit.setOnClickListener(listener);
    }

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }


}
