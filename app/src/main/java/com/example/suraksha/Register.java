package com.example.suraksha;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText mynum, gar1, gar2, gar3;
    private static final int REQUEST_CODE = 1;
    DbHandler db;
    int temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db=new DbHandler(Register.this);

        mynum = (EditText)findViewById(R.id.editText);
        gar1 = (EditText)findViewById(R.id.editText4);
        gar2 = (EditText)findViewById(R.id.editText5);
        gar3 = (EditText)findViewById(R.id.editText6);

        if(db.check()==1)
        {
            Cursor rs = db.getData();
            rs.moveToFirst();
            mynum.setText(rs.getString(0));
            gar1.setText(rs.getString(1));
            gar2.setText(rs.getString(2));
            gar3.setText(rs.getString(3));
        }
    }

    public void e1(View v){
        temp=1;
        openlist();
    }

    public void e2(View v){
        temp=2;
        openlist();
    }
    public void e3(View v){
        temp=3;
        openlist();
    }

    public void editnum(String num){
        switch(temp){
            case 1: gar1.setText(num);
            break;
            case 2: gar2.setText(num);
            break;
            case 3: gar3.setText(num);
            break;
        }
    }

    public void openlist(){
        Uri uri = Uri.parse("content://contacts");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                editnum(number);

            }
        }
    };


    public void goback(View v){
        Intent i = new Intent(Register.this,MainActivity.class);
        startActivity(i);
    }

    public void save(View v){

        String my_num = mynum.getText().toString();
        String gar_1 = gar1.getText().toString();
        String gar_2 = gar2.getText().toString();
        String gar_3 = gar3.getText().toString();

        if(my_num.isEmpty()){
            Toast.makeText(this, "Please Enter your Mobile Number!!!", Toast.LENGTH_SHORT).show();
        }
        else {

            if(my_num.trim().length()<10){
                Toast.makeText(this, "Invalid Mobile Number!!!", Toast.LENGTH_SHORT).show();
                mynum.setTextColor(Color.parseColor("#ff0000"));
            }

            else {

                mynum.setTextColor(Color.parseColor("#000000"));
                if (gar_1.isEmpty()) {
                    Toast.makeText(this, "Please Enter atleast one Number for Guardian!!!", Toast.LENGTH_SHORT).show();
                } else {

                    if(gar_1.trim().length()<10){
                        Toast.makeText(this, "Invalid Mobile Number!!!", Toast.LENGTH_SHORT).show();
                        gar1.setTextColor(Color.parseColor("#ff0000"));
                    }

                    else {

                        gar1.setTextColor(Color.parseColor("#000000"));

                        if(!gar_2.isEmpty())
                        {
                            if(gar_2.trim().length()<10){
                                Toast.makeText(this, "Invalid Mobile Number!!!", Toast.LENGTH_SHORT).show();
                                gar2.setTextColor(Color.parseColor("#ff0000"));
                            }
                            else{
                                gar2.setTextColor(Color.parseColor("#000000"));
                                if(!gar_3.isEmpty())
                                {
                                    if(gar_3.trim().length()<10){
                                        Toast.makeText(this, "Invalid Mobile Number!!!", Toast.LENGTH_SHORT).show();
                                        gar3.setTextColor(Color.parseColor("#ff0000"));
                                    }
                                    else{
                                        gar3.setTextColor(Color.parseColor("#000000"));

                                        db.addContact(new Contact(my_num, gar_1, gar_2, gar_3));

                                        Toast.makeText(this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(Register.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                                else{
                                    db.addContact(new Contact(my_num, gar_1, gar_2, gar_3));

                                    Toast.makeText(this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Register.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }
                        }

                       else{
                            if(!gar_3.isEmpty())
                            {
                                if(gar_3.trim().length()<10){
                                    Toast.makeText(this, "Invalid Mobile Number!!!", Toast.LENGTH_SHORT).show();
                                    gar3.setTextColor(Color.parseColor("#ff0000"));
                                }
                                else{
                                    gar3.setTextColor(Color.parseColor("#000000"));

                                    db.addContact(new Contact(my_num, gar_1, gar_2, gar_3));

                                    Toast.makeText(this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Register.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }
                            else{

                                db.addContact(new Contact(my_num, gar_1, gar_2, gar_3));

                                Toast.makeText(this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Register.this, MainActivity.class);
                                startActivity(i);
                            }
                        }


                    }

                }
            }
        }
    }

}
