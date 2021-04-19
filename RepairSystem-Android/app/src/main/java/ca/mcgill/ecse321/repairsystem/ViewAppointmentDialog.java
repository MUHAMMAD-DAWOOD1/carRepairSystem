package ca.mcgill.ecse321.repairsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class ViewAppointmentDialog extends AppCompatDialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String detailView = homePage.displayAppInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Appointment Information")
                .setMessage(Html.fromHtml("<br> &nbsp;&nbsp; <b>Start Time: </b>" + detailView.substring(0,detailView.indexOf(")")) +
                        "<br><br>  &nbsp;&nbsp; <b>Service Type: </b>" + detailView.substring(detailView.indexOf(")")+1,detailView.indexOf("(")) +
                        "<br><br>  &nbsp;&nbsp; <b>Mechanic: </b>"  + detailView.substring(detailView.indexOf(">")+1,detailView.length()) +
                        "<br><br>  &nbsp;&nbsp; <b>Appointment Status: </b>" + detailView.substring(detailView.indexOf(")")+1,detailView.indexOf("(")) ))
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
