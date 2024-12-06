package com.example.phonebook;

import androidx.annotation.CallSuper;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;

public class MainActivity extends DBActivity {

    protected EditText editName, editLocation, editNumber1, editNumber2, editNumber3;
    protected Button btnInsert;
    protected ListView simpleList;

    protected void FillListView() throws Exception {
        final ArrayList<String> listResults = new ArrayList<String>();
        SelectSQL("SELECT * FROM CONTACTS ORDER BY ID",
                null,
                new OnSelectSuccess() {
                    @Override
                    public void OnElementSelected(String ID, String Name, String Location, String Tel1, String Tel2, String Tel3) {
                        listResults.add(ID+"\t"+Name+"\t"+ Location+"\t"+ Tel1+"\t"+Tel2+"\t"+Tel3+"\n");
                    }
                }
        );
        simpleList.clearChoices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_contact_list,
                R.id.textView,
                listResults

        );
        simpleList.setAdapter(arrayAdapter);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName=findViewById(R.id.editName);
        editLocation=findViewById(R.id.editLocation);
        editNumber1=findViewById(R.id.editNumber1);
        editNumber2=findViewById(R.id.editNumber2);
        editNumber3=findViewById(R.id.editNumber3);
        btnInsert=findViewById(R.id.btnInsert);
        simpleList=findViewById(R.id.simpleList);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView)(view.findViewById(R.id.textView)))
                        .getText().toString();
                String[] elements=selected.split("\t");

                Intent intent = new Intent(
                        MainActivity.this,
                        UpdateDelete.class
                );
                Bundle b = new Bundle();
                b.putString("ID",elements[0]);
                b.putString("Name",elements[1]);
                b.putString("Location",elements[2]);
                b.putString("Tel1",elements[3]);
                b.putString("Tel2",elements[4]);
                b.putString("Tel3",elements[5].trim());

                intent.putExtras(b);
                startActivityForResult(intent,200,b);
            }
        });

        initDB();
        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnInsert.setOnClickListener((view)->{
            ExecSQL("INSERT INTO CONTACTS(Name, Location, Tel1, Tel2, Tel3)"+
                            "VALUES(?, ?, ?, ?, ?)",
                    new Object[]{
                            editName.getText().toString(),
                            editLocation.getText().toString(),
                            editNumber1.getText().toString(),
                            editNumber2.getText().toString(),
                            editNumber3.getText().toString()
                    },
                    ()-> {
                        Toast.makeText(this,"Saved Successfully!",Toast.LENGTH_LONG).show();
                    },
                    (error)-> {
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    }
            );

            try {
                FillListView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}