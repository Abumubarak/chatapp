package rexzen.sentinelserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ImageView add_room;
    private EditText room_name;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms= new ArrayList<>();
    private String name;
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        add_room =(ImageView)findViewById(R.id.create);
        room_name=(EditText)findViewById(R.id.editText);
        lv=(ListView)findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        lv.setAdapter(arrayAdapter);

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Map<String,Object> map= new HashMap<String,Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);

            }
        });
        request_user_name();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();

                Iterator i =dataSnapshot.getChildren().iterator();
                while(i.hasNext())
                {
                     set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(),Chat_Room.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",name);
                startActivity(intent);

            }
        });


    }

    private void request_user_name()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Enter name:");
        final EditText input_field=new EditText(this);
        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   name=input_field.getText().toString();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                request_user_name();
            }
        });
        builder.show();

    }
}
