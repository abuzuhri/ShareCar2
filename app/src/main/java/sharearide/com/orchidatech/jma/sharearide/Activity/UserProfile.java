package sharearide.com.orchidatech.jma.sharearide.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.facebook.login.LoginManager;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.Logic.Validation;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestFinished;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnRequestListener;

public class UserProfile extends ActionBarActivity {
    private EditText email,phone;
    private TextView username,tv_completeProfile;
Typeface font;
    private EditText password,re_password,current_password,ed_invisible;
    private ImageButton edit_email,edit_phone;
    private Button save;
    private Button btn_changePassword;
    private LinearLayout ll_changePassword;
    private ProgressBar update_progress,update_save_progress;
    Validation validation;
    private  Toolbar tool_bar;
    private static final int RESULT_LOAD_IMAGE = 1;
    boolean ifAllValid = true;

    private CircleImageView circleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        tool_bar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(tool_bar);
        final long id = getSharedPreferences("pref",MODE_PRIVATE).getLong("id",-1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        // ed_invisible=(EditText)findViewById(R.id.ed_invisible);
        //    ed_invisible.setVisibility(View.GONE);
        //   ed_invisible.requestFocus();
        ll_changePassword=(LinearLayout)findViewById(R.id.ll_changePassword);
        password=(EditText)findViewById(R.id.password);
        current_password=(EditText)findViewById(R.id.current_password );
        re_password=(EditText)findViewById(R.id.re_password);
        username=(TextView)findViewById(R.id.username);
        tv_completeProfile=(TextView)findViewById(R.id.tv_completeProfile);
        // edit_email=(ImageButton)findViewById(R.id.edit_email);
        // edit_phone=(ImageButton)findViewById(R.id.edit_phone);
        save=(Button)findViewById(R.id.save);
        save.setVisibility(View.GONE);
        font= Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");
        email.setTypeface(font);
        phone.setTypeface(font);
        current_password.setTypeface(font);
        re_password.setTypeface(font);
        password.setTypeface(font);
        if(UserDAO.getUserById(id).getEmail()==null || UserDAO.getUserById(id).getPhone()==null)
            tv_completeProfile.setVisibility(View.VISIBLE);
        update_save_progress= (ProgressBar) findViewById(R.id.update_save_progress);
        btn_changePassword= (Button) findViewById(R.id.btn_changePassword);
        if(getSharedPreferences("pref", Context.MODE_PRIVATE).getInt("network", -1) != -1)
            btn_changePassword.setVisibility(View.GONE);
        circleView= (CircleImageView) findViewById(R.id.circleView);

        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        String imageUrl = UserDAO.getUserById(id).getImage();
        update_progress= (ProgressBar) findViewById(R.id.update_progress);

        update_progress.setVisibility(View.VISIBLE);

        Picasso.with(UserProfile.this).load(Uri.parse(imageUrl)).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).
                into(circleView, new Callback() {
                    @Override
                    public void onSuccess() {
                        update_progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        update_progress.setVisibility(View.GONE);
                        //  holder.profile.setImageResource(R.drawable.ic_contact_picture);
                        circleView.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact_picture));
                    }
                });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                save.setVisibility(View.VISIBLE);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.VISIBLE);

            }
        });



        username.setText(UserDAO.getUserById(id).getUsername());
        phone.setText(UserDAO.getUserById(id).getPhone());
        email.setText(UserDAO.getUserById(id).getEmail());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifAllValid = true;
                if (!validation.isValidEmailAddress(email.getText().toString())) {
                    email.setError("Enter correct email ");
                    ifAllValid = false;
                }
                else if (TextUtils.isEmpty(phone.getText().toString())) {
                    phone.setError("Enter correct Phone ");
                    ifAllValid = false;

                } else if (password.getText().toString().length() > 0) {
                    if (!re_password.getText().toString().equals(password.getText().toString())) {
                        re_password.setError(" Password doesn't match ");
                        ifAllValid = false;
                    } else if (!current_password.getText().toString().equals(UserDAO.getUserById(id).getPassword())) {
                        ifAllValid = false;
                        current_password.setError("The Current Password is incorrect ,please try again  ");

                    } else if (!validation.passwordcontainsNumber(password.getText().toString())) {
                        ifAllValid = false;
                        password.setError("Password must be at least 8 character containing numbers");

                    }

                }
                InternetConnectionChecker.isConnectedToInternet(UserProfile.this, new OnInternetConnectionListener() {
                    @Override
                    public void internetConnectionStatus(boolean status) {
                        if (status) {


                            if (ifAllValid) {
                                save.setVisibility(View.GONE);
                                update_save_progress.setVisibility(View.VISIBLE);
                                final String _password= password.getText().toString().length()> 0?password.getText().toString() : UserDAO.getUserById(id).getPassword();
                                MainUserFunctions.updateProfile(UserProfile.this, id, email.getText().toString(), phone.getText().toString(), _password, new OnRequestFinished() {

                                    @Override
                                    public void onFinished(String s) {
                                        UserDAO.updateProfile(id, email.getText().toString(), phone.getText().toString(), _password);
                                        update_save_progress.setVisibility(View.GONE);
                                        save.setVisibility(View.VISIBLE);

                                        startActivity(new Intent(UserProfile.this, ShareRide.class));

                                    }

                                    @Override
                                    public void onFailed() {

                                        update_save_progress.setVisibility(View.GONE);
                                        save.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                        } else

                        {
                            update_save_progress.setVisibility(View.GONE);
                            save.setVisibility(View.VISIBLE);
                            LayoutInflater li = LayoutInflater.from(UserProfile.this);
                            View v = li.inflate(R.layout.warning, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserProfile.this);

                            // set more_info.xml to alertdialog builder
                            alertDialogBuilder.setView(v);
                            TextView tittle = (TextView) v.findViewById(R.id.tittle);
                            ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            close_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            // show it
                            alertDialog.show();
                        }

                    }
                });

            }


        });
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_changePassword.setVisibility(View.GONE);
                ll_changePassword.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            if (TextUtils.isEmpty(email.getText().toString())|| TextUtils.isEmpty(phone.getText().toString())) {
                if (getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).getInt("network", -1) == 2) {
                    LoginManager.getInstance().logOut();
                    getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("network").commit();

                } else if (getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).getInt("network", -1) == 1) {
                    if (Login.mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(Login.mGoogleApiClient);
                        Login.mGoogleApiClient.disconnect();
                        Login.mGoogleApiClient.connect();
                    }
                    getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("network").commit();
                }
                getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit().remove("id").commit();

                Intent intent = new Intent(UserProfile.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
               } else

                    finish();
                //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                //closing transition animations
                overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


    @Override
    protected void onPause() {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null) {

            Uri selectedImage=data.getData();
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            final Bitmap  bitmap = ShrinkBitmap(picturePath, 360, 360);

            if(bitmap == null){
                Toast.makeText(getApplicationContext(), "please choose valid image", Toast.LENGTH_LONG).show();

                return;
            }
            circleView.setImageAlpha(100);
            circleView.setEnabled(false);

            update_progress.setVisibility(View.VISIBLE);
//                profileImageView.setImageBitmap(bitmap);

        /*
         * Convert the image to a string
         * */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, 0);
            Log.i("image", image_str);
            MainUserFunctions.uploadImage(getApplicationContext(), getSharedPreferences("pref", MODE_PRIVATE).getLong("id", -1), image_str,new OnRequestFinished() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onFinished(String imageUrl) {
                    update_progress.setVisibility(View.GONE);
                    circleView.setImageAlpha(255);
                    circleView.setEnabled(true);
                    UserDAO.updateUserPhoto(getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1), imageUrl);
                    // Picasso.with(getApplicationContext()).load(Uri.parse(imageUrl)).into(profileImageView);
                    circleView.setImageBitmap(bitmap);



                }

                @Override
                public void onFailed() {
                    update_progress.setVisibility(View.GONE);
                    circleView.setImageAlpha(255);
                    circleView.setEnabled(true);
                }
            });

        }


    }
    Bitmap ShrinkBitmap(String file, int width, int height){

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

        if (heightRatio > 1 || widthRatio > 1)
        {
            if (heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
}
