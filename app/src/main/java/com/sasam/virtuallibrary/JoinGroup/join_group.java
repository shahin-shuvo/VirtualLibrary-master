package com.sasam.virtuallibrary.JoinGroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class join_group extends AppCompatActivity {
     public EditText joinCode;
     public String grpID;
     public Button joinButton;
    public static Activity object;
    boolean exist = false;
     String status;

    private DatabaseReference  groupDatabase,mDatabase;
     String code;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        object = this;
        setContentView(R.layout.activity_join_group);


        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        joinCode = (EditText) this.findViewById(R.id.inputCode);



        joinButton = (Button) this.findViewById(R.id.joinGroup);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // checkExistence();
                checkExistence(new MyCallback() {
                    @Override
                    public void onCallback(boolean exist) {
                        System.out.println("htryjetuyftyouhbuujbnjnk5jet6jmyu");
                        System.out.println(exist);
                        JoinFactory joinFactory = new JoinFactory();
                        if (!exist) {
                            Join join1 = joinFactory.getJoinType("normal", object);
                            join1.grpJoin();
                        }
                        else{
                            Join join2 = joinFactory.getJoinType("rejoin", object);
                            join2.grpJoin();
                        }



                    }
                });


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
      //  getMenuInflater().inflate(R.menu.join_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


     void showAlertSuccess(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Group join successfull !")
                .setConfirmText("Yes,Done!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        // loadGroupPage();

                    }
                })
                .show();
    }

    public void checkExistence(final MyCallback myCallback)
    {
        exist = false;
        code = joinCode.getText().toString();
        if (!code.equals("") && !code.equals("")) {

            groupDatabase = MainActivity.Connection("Groups");
            groupDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        if(childSnapshot.child("code").getValue(String.class).equals(code))
                        {
                             grpID = childSnapshot.child("groupID").getValue(String.class);

                             mDatabase = MainActivity.Connection("Users").child(MainActivity.getUserID()).child("userGroupList");
                             mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                        if(childSnapshot.getValue(String.class).equals(grpID))
                                        {
                                            System.out.println("#########################################");
                                            System.out.println(childSnapshot.getValue(String.class));
                                            System.out.println(grpID);
                                            exist= true;
                                            status = "true";
                                            break;
                                        }

                                    }
                                    myCallback.onCallback(exist);


                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });

                        }

                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

        }
    }

    public interface MyCallback {
        void onCallback(boolean exist);
    }

}
