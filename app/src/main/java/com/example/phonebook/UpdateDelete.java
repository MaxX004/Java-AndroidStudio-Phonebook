package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateDelete extends DBActivity {
    protected EditText editName, editLocation, editNumber1, editNumber2, editNumber3;
    protected Button btnUpdate, btnDelete;
    protected String ID;

    private  void BackToMain(){
        finishActivity(200);
        Intent i = new Intent(UpdateDelete.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        editName=findViewById(R.id.editName);
        editLocation=findViewById(R.id.editLocation);
        editNumber1=findViewById(R.id.editNumber1);
        editNumber2=findViewById(R.id.editNumber2);
        editNumber3=findViewById(R.id.editNumber3);

        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);

        Bundle b = getIntent().getExtras();

        if(b!= null){
            ID = b.getString("ID");
            editName.setText(b.getString("Name"));
            editLocation.setText(b.getString("Location"));
            editNumber1.setText(b.getString("Tel1"));
            editNumber2.setText(b.getString("Tel2"));
            editNumber3.setText(b.getString("Tel3"));
        }

        btnDelete.setOnClickListener(view->{
            ExecSQL("DELETE FROM CONTACTS WHERE ID = ?",
                    new Object[]{ID},
                    new OnQuerySuccess() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(UpdateDelete.this,
                                    "Your delete was successful!",
                                    Toast.LENGTH_LONG).show();
                            BackToMain();
                        }
                    },
                    new OnError() {
                        @Override
                        public void OnQueryError(String error) {

                            Toast.makeText(UpdateDelete.this,
                                    error,Toast.LENGTH_LONG).show();
                        }
                    }
            );
        });

        btnUpdate.setOnClickListener(view->{
            ExecSQL("UPDATE CONTACTS SET " +
                            "Name = ?, " +
                            "Location = ?, " +
                            "Tel1 = ?, " +
                            "Tel2 = ?, " +
                            "Tel3 = ? " +
                            "WHERE ID = ?",
                    new Object[]{
                            editName.getText().toString(),
                            editLocation.getText().toString(),
                            editNumber1.getText().toString(),
                            editNumber2.getText().toString(),
                            editNumber3.getText().toString(),
                            ID
                    },
                    new OnQuerySuccess() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(UpdateDelete.this,
                                    "Your update was successful!",
                                    Toast.LENGTH_LONG).show();
                            BackToMain();
                        }
                    },
                    new OnError() {
                        @Override
                        public void OnQueryError(String error) {
                            Toast.makeText(UpdateDelete.this,
                                    error,Toast.LENGTH_LONG).show();
                        }
                    }
            );
        });
    }
}