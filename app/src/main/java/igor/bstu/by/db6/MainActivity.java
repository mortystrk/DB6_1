package igor.bstu.by.db6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String fileName = "Contacts.json";
    private final String publicFileName = "Contacts.json";
    private File file, publicFile;
    EditText name, surname, telefon, birthday, search;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new File(super.getFilesDir(), fileName);
        publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                publicFileName);
        if(!ExistBase(fileName)){
            try {
                CreateFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        name = (EditText) findViewById(R.id.editText2);
        surname = (EditText) findViewById(R.id.editText3);
        telefon = (EditText) findViewById(R.id.editText4);
        birthday = (EditText) findViewById(R.id.editText5);
        search = (EditText) findViewById(R.id.editText6);
        info = (TextView) findViewById(R.id.textView);
    }

    private boolean ExistBase(String filename){
        boolean flag;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
        if(flag = file.exists()){
            Log.d("Log_6", "Файл " + fileName + " существует");
        }
        else{
            Log.d("Log_6", "Файл " + fileName + " не найден");
        }
        return flag;
    }

    private void CreateFile(String filename) throws IOException {
        file.createNewFile();
        publicFile.createNewFile();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject tempObjects = new JSONObject();
        tempObjects.put("", "");
        object.put("Contacts", array);
        try (FileWriter writer = new FileWriter(file)){
            writer.append(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Log.d("Lab_6", "Не удалось записать данные в файл JSON");
        }
        try (FileWriter writer = new FileWriter(publicFile)){
            writer.append(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Log.d("Lab_6", "Не удалось записать данные в файл JSON");
        }
    }

    public void WriteJSON(String name, String surname,
                                 String telefon, String birthday, File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(new FileReader(file)) ;
        //JSONObject object = new JSONObject();
        JSONArray array = (JSONArray) object.get("Contacts");
        JSONObject tempObjects = new JSONObject();
        tempObjects.put("name", name);
        tempObjects.put("surname", surname);
        tempObjects.put("telefon", telefon);
        tempObjects.put("birthday", birthday);
        array.add(tempObjects);
        object.put("Contacts", array);
        try (FileWriter writer = new FileWriter(file)){
            writer.append(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Log.d("Lab_6", "Не удалось записать данные в файл JSON");
        }
    }

    public void SearchByName(String name) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        boolean flag = false;
        JSONObject object = (JSONObject) parser.parse(
                new FileReader(file));
        JSONArray array = (JSONArray) object.get("Contacts");
        for(Object tempObject : array){
            if(((JSONObject) tempObject).get("name").equals(name) ||
                    ((JSONObject) tempObject).get("surname").equals(name)){
                info.setText((((JSONObject) tempObject).get("telefon").toString()));
                flag = true;
            }
        }
        if(!flag){
            info.setText("Значений не найдено");
        }
    }

    public void onSetPublic(View view) throws IOException, ParseException {
        WriteJSON(name.getText().toString(), surname.getText().toString(),
                telefon.getText().toString(), birthday.getText().toString(), publicFile);
        name.setText("");
        surname.setText("");
        telefon.setText("");
        birthday.setText("");
    }

    public void onGet(View view) throws IOException, ParseException {
        SearchByName(search.getText().toString());
        //sPref = getSharedPreferences("Contacts", MODE_PRIVATE);
        //String savedText = sPref.getString(search.getText().toString(), "");
        //info.setText(savedText);
    }

    public void onDelete(View view){
        file.delete();
        publicFile.delete();
    }

    public void onCreate(View view) throws IOException {
        CreateFile(fileName);
    }

    public void onSetPrivate(View view) throws IOException, ParseException {
        WriteJSON(name.getText().toString(), surname.getText().toString(),
                telefon.getText().toString(), birthday.getText().toString(), file);
        name.setText("");
        surname.setText("");
        telefon.setText("");
        birthday.setText("");
    }

    private AlertDialog CreateDialog(int message, int textOnPositiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(textOnPositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        return builder.create();
    }
}
