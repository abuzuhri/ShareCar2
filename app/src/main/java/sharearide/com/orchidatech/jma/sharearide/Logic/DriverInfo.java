package sharearide.com.orchidatech.jma.sharearide.Logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


/**
 * Created by Shadow on 9/8/2015.
 */
public class DriverInfo {

    /*
    public static void showInputDialog(Context context) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set input_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editText_input_dialog);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    */

    public static void CallIntent(Context context, String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        context.startActivity(callIntent);
    }

    public static void MessageIntent(Context context, String to, String body) {

        //SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage("phoneNo", null, "sms message", null, null);

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", to);
        smsIntent.putExtra("body", body);

        try {
            context.startActivity(smsIntent);
            //context.finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void EmailIntent(Context context, String to, String cc, String subject, String body) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        //emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        //emailIntent.putExtra(Intent.EXTRA_CC, new String[] {cc});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Choose an Email client: "));
            //activity.finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void QuickMessageIntent(Context context) {
        // go to quick message activity..
        //Intent intent = new Intent(context, QuickMessage.class);
        //intent.setData();
       // context.startActivity(intent);
    }
}
