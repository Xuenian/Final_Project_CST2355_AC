package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    Member4MainActivity parent = null;

    public boolean iAmTablet;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle infoToPass = getArguments(); //returns the arguments set before

        final String passedMessage = infoToPass.getString("Message");
        final long idPassed = infoToPass.getLong("ID");

        View screen = inflater.inflate(R.layout.activity_message_details, container, false);
        TextView msg = screen.findViewById(R.id.textViewStationDetails01M4);
        TextView idDis = screen.findViewById(R.id.textViewStationDetails02M4);

        msg.setText("Message is: " + passedMessage);
        idDis.setText("ID = " + idPassed);

        Button btn = (Button)screen.findViewById(R.id.deleteMessageBtnM4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iAmTablet) {
                    parent.deleteMessage(idPassed, passedMessage); //call function from parent
                    getActivity().getFragmentManager().popBackStack();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.m4_dialog_message);
                    builder.setTitle(R.string.m4_dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("ID", idPassed);
                            resultIntent.putExtra("Message", passedMessage);
                            getActivity().setResult(Activity.RESULT_OK, resultIntent);
                            getActivity().finish();// go to previous activity
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent();
                            getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);
                            getActivity().finish();// go to previous activity
                        }
                    });
                    builder.show();
                }
            }
        });

        Snackbar sb = Snackbar.make(screen, "Delete the Station and return to the list", Snackbar.LENGTH_LONG);
        sb.show();

        return screen;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if(iAmTablet)
            parent = (Member4MainActivity)  context; //find out which activity has the fragment
    }

    public void setIsTablet(boolean b){
        iAmTablet = b;
    }
}
